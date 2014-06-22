/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.google.common.collect.Maps;
import com.jme3.animation.Animation;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.spectre.app.debug.DebugState;
import com.spectre.deck.SupplyDeck;
import com.spectre.systems.player.components.PlayerHighScorePiece;
import com.spectre.systems.player.components.PlayerPiece;
import java.util.HashMap;
import org.slf4j.LoggerFactory;

/**
 * The
 * <code>SpectreApplicationState</code> extends the
 * <code>AppState</code> class and acts as a removable
 * <code>AppState</code> version of an
 * <code>SpectreApplicationState</code> class to provide functionality for all
 * base elements of Spectre.
 *
 * <br><br>
 *
 * In Order to properly use
 * <code>SpectreApplicationState</code> please make sure that the base
 * <code>Application</code> class is an instance of
 * <code>SpectreApplication</code> or
 * <code>SimpleApplication</code> to ensure rootNode and guiNode are being
 * updated in the correct order
 *
 *
 * TODO: Change certain methods that should be private but are public to default
 * to block from public view but not package view TODO: Remember methods in this
 * class should be synchronized
 *
 * @author Kyle D. Williams
 */
public class SpectreApplicationState implements AppState {

    public static final org.slf4j.Logger log = LoggerFactory.getLogger(SpectreApplicationState.class.getName());

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        log.trace("initialize():" + this);
        initialized = true;
        this.app = app;
        this.inputManager = app.getInputManager();
        this.inputManager.setAxisDeadZone(2f);//0.5f);//2f);
        this.settings = app.getContext().getSettings();
        this.stateManager = app.getStateManager();
        this.assetManager = app.getAssetManager();
        setUpLogger(isInDebugMode());
        //enable depth test and back-face culling for performance
        app.getRenderer().applyRenderState(com.jme3.material.RenderState.DEFAULT);
        this.guiNode = setUpBaseNode(guiNodeName, app.getGuiViewPort());
        this.rootNode = setUpBaseNode(rootNodeName, app.getViewPort());
        this.modelSubNode = setUpNode(modelNodeName, rootNode);
        this.sceneSubNode = setUpNode(sceneNodeName, rootNode);
        setUpEntitySystem();
        setUpOtherVariables();
        //SET UP DEBUG IF NEEDED
        if (isInDebugMode() && stateManager.getState(DebugState.class) == null) {
            stateManager.attach(new DebugState());
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (edb != null) {
                    edb.close();
                }
            }
        });
    }

    public static void setUpLogger(boolean debug) {
        //programatically configuire logback's log level depending debug mode
        ch.qos.logback.classic.Level rootLevel = debug ? ch.qos.logback.classic.Level.DEBUG : ch.qos.logback.classic.Level.WARN;
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(rootLevel);

        //Configure native logger to be handled by logback
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(java.util.logging.Level.FINEST);
    }

    @Override
    public void cleanup() {
        log.trace("cleanup():" + this);
        initialized = false;
    }

    public boolean isInDebugMode() {
        return settings.getBoolean("DebugMode");
    }

    /**
     * @return the {@link Application application}.
     */
    public Application getApplication() {
        return app;
    }

    /**
     * @return the {@link InputManager input manager}.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * @return The {@link AssetManager asset manager} for this application.
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * A node specifically for the graphical user interface
     *
     * @return guiNode Node
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * The master node that all nodes are added to, should not be called to
     * attach any other node to normally
     *
     * @return rootNode Node
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * A node which Handel all models within the world
     *
     * @return modelNode Node
     */
    public Node getModelNode() {
        return modelSubNode;
    }

    /**
     * A node which Handel all scenes within the world
     *
     * @return sceneNode Node
     */
    public Node getSceneNode() {
        return sceneSubNode;
    }

    /**
     * The main entry point for retrieving entities and components.
     *
     * @return entityData EntityData
     */
    public EntityData getEntityData() {
        return edb.getEntityData();
    }

    /**
     * provides a store of configuration to be used by the application.
     *
     * @return settings AppSettings
     */
    public AppSettings getAppSettings() {
        return settings;
    }

    private Node setUpBaseNode(String nodeName, com.jme3.renderer.ViewPort viewPort) {
        java.util.List<Spatial> scenes = viewPort.getScenes();
        Node node = null;
        for (Spatial spat : scenes) {
            if (spat.getName().equals(nodeName)) {
                node = (Node) spat;
                break;
            }
        }
        if (node == null) {
            throw new NullPointerException(nodeName + " returned null on look up,\n"
                    + "such a node is unable to be created due to complex update statements");
            //CAN'T UPDATE THESE NODE CORRECTLY
            //node = new Node(nodeName);
            //viewPort.attachScene(node);
        }

        if (nodeName.equals(guiNodeName)) {
            node.setQueueBucket(com.jme3.renderer.queue.RenderQueue.Bucket.Gui);
            node.setCullHint(Spatial.CullHint.Never);
        }
        return node;
    }

    private Node setUpNode(String nodeName, Node root) {
        Node node = (Node) root.getChild(nodeName);
        node = com.google.common.base.Objects.firstNonNull(node, new Node(nodeName));
        root.attachChild(node);
        return node;
    }

    private void setUpEntitySystem() {
        EntityData entityData = null;
        if (isInDebugMode()) {
            entityData = new com.simsilica.es.base.DefaultEntityData();
        } else {
            //FINALLY SET UP THE ENTITY SYSTEM AND CALL SUBCLASS INITIALIZE
            if (!settings.containsKey("SqlPath")) {
                settings.putString("SqlPath", "SqlDatabase");
            }

            if (!settings.containsKey("SqlWriteDelay")) {
                settings.put("SqlWriteDelay", Long.valueOf(100L));
            }
            String sqlPath = settings.getString("SqlPath");
            Long writeDelay = (Long) settings.get("SqlWriteDelay");
            try {
                entityData = new com.simsilica.es.sql.SqlEntityData(sqlPath, writeDelay);
            } catch (java.sql.SQLException ex) {
                log.error("Failed to Load EntityData in SpectreApplication.java",
                        ex);
            }
        }
        edb = new EntityDatabaseState(entityData);
        stateManager.attach(edb);
    }

    private void setUpOtherVariables() {
        modelBindingsList = Maps.newHashMap();
        animationsList = Maps.newHashMap();
        playerList = Maps.newHashMap();
        physicsDirector = stateManager.getState(BulletAppState.class);
        if (physicsDirector == null) {
            physicsDirector = new BulletAppState();
            physicsDirector.setThreadingType(com.jme3.bullet.BulletAppState.ThreadingType.PARALLEL);
            stateManager.attach(physicsDirector);
        }
    }

    /**
     * Adds a new player to the game
     *
     * @param player the name of the player
     */
    private void addPlayer(String player) {
        EntityId id = getEntityData().createEntity();
        getEntityData().setComponents(id, new PlayerPiece(player), new PlayerHighScorePiece(0L));
        playerList.put(player, id);
    }

    /**
     * Returns the player if one is not found creates a new instance
     *
     * @param player the name of the player
     * @return PlayerController
     */
    public EntityId getPlayerId(String player) {
        if (!playerList.containsKey(player)) {
            log.trace("Player {0} cannot be found as part of the playerList, a new player shall be made in its place", player);
            addPlayer(player);
        }
        return playerList.get(player);
    }

    /**
     * Controls all of the physics Handling in the Application
     *
     * @see com.jme3.bullet.BulletAppState
     * @return physicsDirector
     */
    public BulletAppState getPhysicsDirector() {
        return physicsDirector;
    }

    /**
     * @see com.jme3.bullet.PhysicsSpace
     * @return physicsSpace
     */
    public com.jme3.bullet.PhysicsSpace getPhysicsSpace() {
        return getPhysicsDirector().getPhysicsSpace();
    }

    /**
     * A HashMap of all Entity Controlled Spatials
     *
     * ie spatials that must be bound to an entity for identification
     *
     * @return modelList HashMap<EntityId, Spatial>
     */
    public HashMap<EntityId, Spatial> getModelBindingsList() {
        return modelBindingsList;
    }

    public Spatial getPlayerModel(String ID) {
        return getModel(getPlayerId(ID));
    }

    /**
     * Returns the characters Spatial Model
     *
     * @param ID the name of the character
     * @return Spatial the characters spatial
     */
    public Spatial getModel(EntityId id) {
        if (!modelBindingsList.containsKey(id)) {
            log.error("Entity ID: " + id + "'s spatial reference cannot be found in the loaded characterList please check id",
                    new java.io.IOException());
        }
        return modelBindingsList.get(id);
    }

    /**
     * Returns all animations saved under a skeleton
     *
     * @param skeletonName
     * @param spat
     * @return
     */
    public AnimData getAnimations(String skeletonName) {
        return animationsList.get(skeletonName);
    }

    /**
     * Returns the first animation with animName Better to do this way so we can
     * ignore naming conventions and can link character animations as well
     *
     * @param animName
     * @param spat
     * @return
     */
    public Animation getAnimation(String animName) {
        for (AnimData animData : animationsList.values()) {
            for (Animation anim : animData.anims) {
                if (anim.getName().equals(animName)) {
                    return anim;
                }
            }
        }
        return null;
    }

    /**
     * Returns the Animation of a specified skeleton
     *
     * @param skeletonName
     * @param animName
     * @param spat
     * @return
     */
    public Animation getAnimation(String skeletonName, String animName) {
        for (Animation anim : animationsList.get(skeletonName).anims) {
            if (anim.getName().equals(animName)) {
                return anim;
            }
        }
        return null;
    }

    /**
     * Returns the Card
     *
     * @param ID the name of the Card
     * @return Card
     */
    public com.spectre.deck.card.Card getCard(String ID) {
        if (!SupplyDeck.containsKey(ID)) {
            log.error("Card " + ID + " cannot be found in the loaded cardList please check spelling",
                    new java.io.IOException());
        }
        return SupplyDeck.get(ID);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        if (stateManager.getState(SpectreApplicationState.class) != null) {
            stateManager.detach(this);
        }
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void render(com.jme3.renderer.RenderManager rm) {
    }

    @Override
    public void postRender() {
    }
    /**
     * <code>initialized</code> is set to true when the method
     * {@link AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) }
     * is called. When {@link AbstractAppState#cleanup() } is called,
     * <code>initialized</code> is set back to false.
     */
    protected boolean initialized = false;
    private boolean enabled = true;
    //SpectreApplicationState Variables
    private Application app;
    private AppSettings settings;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private final String guiNodeName = "Gui Node";
    private final String rootNodeName = "Root Node";
    private final String modelNodeName = "Model Node";
    private final String sceneNodeName = "Scene Node";
    private Node guiNode;
    private Node rootNode;
    private Node modelSubNode;
    private Node sceneSubNode;
    private EntityDatabaseState edb;
///////////Director Variables
    private HashMap<EntityId, Spatial> modelBindingsList;
    private BulletAppState physicsDirector;
    private HashMap<String, EntityId> playerList;
    private HashMap<String, AnimData> animationsList;//use animeData over Animation to allow easier templating
}
