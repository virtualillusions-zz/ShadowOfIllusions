/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.app;

import com.google.common.collect.Maps;
import com.jme3.animation.Animation;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferUtils;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.sql.SqlEntityData;
import com.spectre.systems.player.components.PlayerHighScorePiece;
import com.spectre.systems.player.components.PlayerPiece;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>SpectreApplicationOld</code> extends the
 * <code>Application</code> class to provide functionality for all base elements
 * of Spectre, and an accessible root node that is updated and rendered
 * regularly.
 *
 * @author Kyle Williams
 */
public abstract class SpectreApplicationOld extends Application {

    private EntityData entityData;
    private final Node guiNode = new Node("GUI Node");
    private final Node rootNode = new Node("Root Node");
    private final Node modelSubNode = new Node("Model Node");
    private final Node sceneSubNode = new Node("Scene Node");
    public static final Logger logger = Logger.getLogger("com.spectre");
///////////Director Variables
    private HashMap<EntityId, Spatial> modelBindingList;
    public final float gravity = -9.81f;
    private BulletAppState physicsDirector;
    private HashMap<String, EntityId> playerList;
    private HashMap<String, AnimData> animationsList;//use animeData over Animation to allow easier templating
    //private FilterSubDirector filterDirector;
    //private SupplyDeck cardList;

    public boolean isInDebugMode() {
        return settings.getBoolean("DebugMode");
    }

    @Override
    public void start() {
//        //Creates settings inCase this
//        //is the first time application has been started
//        if (settings == null) {
//            setSettings(new AppSettings(true));
//        }
//            
//        //re-setting settings they can have been merged from the registry.        
//        setSettings(settings);
//        super.start();
        // set some default settings in-case
        // settings dialog is not shown
        boolean loadSettings = false;
        if (settings == null) {
            setSettings(new AppSettings(true));
            loadSettings = true;
        }

        // show settings dialog
        if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
            return;
        }

        settings.setUseJoysticks(true);
        //re-setting settings they can have been merged from the registry.
        setSettings(settings);
        //CHECK IF IN DEBUG MODE OR NOT
        if (settings.getBoolean("DebugMode") == true) {
            Logger.getLogger("").setLevel(Level.FINE);
        } else {
            Logger.getLogger("").setLevel(Level.SEVERE);
        }
        super.start();
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
        return entityData;
    }

    /**
     * provides a store of configuration to be used by the application.
     *
     * @return settings AppSettings
     */
    public AppSettings getAppSettings() {
        return settings;
    }

    @Override
    public void initialize() {
        super.initialize();

        //enable depth test and back-face culling for performance
        renderer.applyRenderState(RenderState.DEFAULT);

        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        rootNode.attachChild(modelSubNode);
        rootNode.attachChild(sceneSubNode);
        viewPort.attachScene(rootNode);
        //This remains intact remember
        //YOU HAVE TWO CHOICES
        //the idea of putting all meaningful information into a panel 
        //and then shrinking that panel for the four players must create
        //a system to take all parts of the xml file turn them into entities and 
        //allow them to be duplicated
        // OR simply loading the main pannel and duplicate it if needed
        //CREATE start menu in pop-up to avoid caring about anything
        guiViewPort.attachScene(guiNode);

        if (settings.getBoolean("DebugMode") == true) {
            if (context.getType() == Type.Display) {
                inputManager.addMapping("Exit_Vezla", new KeyTrigger(KeyInput.KEY_ESCAPE));
            }

            inputManager.addMapping("Memory_Vezla", new KeyTrigger(KeyInput.KEY_F1));

            //This Is the Primary Way to set Up registered KeyBindings
            inputManager.addListener(new ActionListener() {
                @Override
                public void onAction(String binding, boolean value, float tpf) {
                    if (binding.equals("Exit_Vezla")) {
                        stop();
                    } else if (binding.equals("Memory_Vezla")) {
                        BufferUtils.printCurrentDirectMemory(null);
                    }
                }
            }, "Exit_Vezla", "Memory_Vezla");
        }

        //Add joystick support
        //settings.setUseJoysticks(true);
        inputManager.setAxisDeadZone(0.2f);

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
            entityData = new SqlEntityData(sqlPath, writeDelay);
        } catch (SQLException ex) {
            SpectreApplicationOld.logger.log(Level.SEVERE,
                    "Failed to Load EntityData in SpectreApplication.java",
                    ex);
        }
////////////////////Initialize Director Variables
        modelBindingList = Maps.newHashMap();
        animationsList = Maps.newHashMap();
        playerList = new HashMap<String, EntityId>();
        if (getStateManager().getState(BulletAppState.class) != null) {
            physicsDirector = getStateManager().getState(BulletAppState.class);
        } else {
            physicsDirector = new BulletAppState();
            physicsDirector.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
            stateManager.attach(physicsDirector);
            physicsDirector.getPhysicsSpace().setGravity(new Vector3f(0f, gravity, 0f));
        }

        //Call Game Code
        SpectreApp();
    }

    @Override
    public void update() {
        super.update();// makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;

        // update states        
        stateManager.update(tpf);

        // spectre Update and root node
        spectreUpdate(tpf);

        rootNode.updateLogicalState(tpf);//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION
        guiNode.updateLogicalState(tpf);//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION

        rootNode.updateGeometricState();//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION
        guiNode.updateGeometricState();//IGNORE WARNING NOT EXTENDING SIMPLEAPPLICATION

        // render states
        stateManager.render(renderManager);
        renderManager.render(tpf, context.isRenderable());
        spectreRender(renderManager);
        stateManager.postRender();
    }

    /**
     * This function is used mainly for quick testing purposes
     */
    public abstract void SpectreApp();

    /**
     * This updated function is used mainly for quick testing purposes
     */
    public abstract void spectreUpdate(float tpf);

    /**
     * This rendering function is used mainly for quick testing purposes
     */
    public abstract void spectreRender(RenderManager rm);

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
            deprecated.com.spectre.app.SpectreApplication.logger.log(Level.FINE, "Player {0} cannot be found as part of the playerList, a new player shall be made in its place", player);
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
        return modelBindingList;
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
}
