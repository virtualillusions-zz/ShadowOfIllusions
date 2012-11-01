/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.director;

import com.jme3.animation.Animation;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.spectre.deck.card.Card;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.SpectrePhysicsController;
import com.spectre.controller.character.SpectrePlayerController;
import com.spectre.deck.SupplyDeck;
import java.io.IOException;
import java.util.HashMap;

/**
 *  TODO: whenever you load characters check for xml values basicSet, type and weapon
 *        SET ALL IN USERDATA OF SPATIAL. IDEALLY IT SHOULD BE ALREADY SET WITH SPATIAL.
 *  This Class holds all valuable information to allow the game to run properly
 *  @Note physics appState is found in this class called physicsDirector
 * @author Kyle Williams 
 */
public final class Director extends com.spectre.app.SpectreState implements Savable {

    private static AppStateManager stateManager;
    private static FilterSubDirector filterDirector;
    private static SpectreApplication application;
    private static BulletAppState physicsDirector;
    private static HashMap<String, SpectrePhysicsController> characterList; //A loaded list of physics characters
    private static HashMap<String, Spatial> sceneList; //A loaded list of scenes
    //Player initializations Done Here to prevent any issues if savable isn't called
    private static HashMap<String, SpectrePlayerController> playerList = new HashMap<String, SpectrePlayerController>();
    private static HashMap<String, AnimData> animationsList;
    private static SupplyDeck cardList;

    @Override
    public void SpectreState(AppStateManager StateManager, Application app) {
        synchronized (this) {
            //Basic initializations
            application = (SpectreApplication) app;
            stateManager = StateManager;
            //Physics initializations
            physicsDirector = new BulletAppState();
            physicsDirector.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
            stateManager.attach(physicsDirector);
        }
    }

    /**
     * Adds a new player to the game
     * @param player the name of the player
     */
    private static void addPlayer(String player) {
        playerList.put(player, new SpectrePlayerController(player));
    }

    /**
     * Returns the player if one is not found creates a new instance
     * @param player the name of the player
     * @return PlayerController
     */
    public static SpectrePlayerController getPlayer(String player) {
        if (!playerList.containsKey(player)) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.INFO,
                    "Player " + player + " cannot be found as part of the playerList, a new player shall be made in its place",
                    new java.io.IOException());
            addPlayer(player);
        }
        return playerList.get(player);
    }

    /**
     * Returns VezlaApplication in order to make system related calls
     * @return application
     */
    public static SpectreApplication getApp() {
        return application;
    }

    /**
     * Returns the VezlaApplications Asset Manager
     * @return application
     */
    public static AssetManager getAssetManager() {
        return application.getAssetManager();
    }

    /**
     * Controls all of the physics Handling in the Application
     * @see com.jme3.bullet.BulletAppState
     * @return physicsDirector
     */
    public static BulletAppState getPhysicsDirector() {
        return physicsDirector;
    }

    /**
     * @see com.jme3.bullet.PhysicsSpace
     * @return physicsSpace
     */
    public static com.jme3.bullet.PhysicsSpace getPhysicsSpace() {
        return physicsDirector.getPhysicsSpace();
    }

    /**
     * Returns if the models have been loaded or not
     */
    public static boolean modelsAreLoaded() {
        return characterList != null;
    }

    /**
     * A Loaded List of all models
     * @param cL
     */
    public static void setCharacterList(HashMap<String, SpectrePhysicsController> cL) {
        characterList = cL;
    }

    /**
     * Returns the characters Spatial Model
     * @param ID the name of the character
     * @return Spatial the characters spatial
     */
    public static SpectrePhysicsController getModel(String ID) {
        if (!characterList.containsKey(ID)) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    "Character " + ID + " cannot be found in the loaded characterList please check spelling",
                    new java.io.IOException());
        }
        return characterList.get(ID);
    }

    /**
     * Sets the postProcessingManager for the game
     * @param filterDirector
     */
    public static void setFilterDirector(FilterSubDirector fD) {
        filterDirector = fD;
    }

    /**
     * Returns the post Processing Manager for the game
     * @return FilterDirector
     */
    public static FilterSubDirector getFilterDirector() {
        if (filterDirector == null) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    "The FilterDirector has not yet been initiated",
                    new java.io.IOException());
        }
        return filterDirector;
    }

    /**
     * Returns the Card
     * @param ID the name of the Card 
     * @return Card
     */
    public static Card getCard(String ID) {
        if (!cardList.containsKey(ID)) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    "Card " + ID + " cannot be found in the loaded cardList please check spelling",
                    new java.io.IOException());
        }
        return cardList.get(ID);
    }

    /**
     * Returns if the models have been loaded or not
     */
    public static boolean scenesAreLoaded() {
        return sceneList != null;
    }

    /**
     * A Loaded List of all models
     * @param cL
     */
    public static void setSceneList(HashMap<String, Spatial> sL) {
        sceneList = sL;
    }

    /**
     * A Loaded List of all Cards
     * @param deck 
     */
    public static void setCardList(SupplyDeck deck) {
        cardList = deck;
    }

    /**
     * A Loaded List of all Cards
     * @param deck 
     */
    public static SupplyDeck getCardList(HashMap<String, Card> deck) {
        return cardList;
    }

    /**
     * A Loaded List of all skeleton animations
     * @param aL
     */
    public static void setAnimationsList(HashMap<String, AnimData> aL) {
        animationsList = aL;
    }

    /**
     * Returns the first animation with animName
     * Better to do this way so their can be duplicate idles and can link character animations as well
     * @param animName
     * @param spat
     * @return 
     */
    public static AnimData getAnimations(String skeletonName) {
        return animationsList.get(skeletonName);
    }

    /**
     * Returns the first animation with animName
     * Better to do this way so their can be duplicate idles and can link character animations as well
     * @param animName
     * @param spat
     * @return 
     */
    public static Animation getAnimation(String animName) {
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
     * Returns the Animation of a specified skeleton with animName
     * Better to do this way so their can be duplicate idles and can link character animations as well
     * @param skeletonName
     * @param animName
     * @param spat
     * @return 
     */
    public static Animation getAnimation(String skeletonName, String animName) {
        for (Animation anim : animationsList.get(skeletonName).anims) {
            if (anim.getName().equals(animName)) {
                return anim;
            }
        }
        return null;
    }

    /**
     * Returns the Scene's sceneController
     * @param ID the name of the Scene
     * @return ID the scenes controller
     */
    public static com.spectre.controller.scene.SceneController getScene(String ID) {
        if (!sceneList.containsKey(ID) || sceneList.get(ID).getControl(com.spectre.controller.scene.SceneController.class) == null) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    "Scene: " + ID + " cannot be found in the loaded sceneList please check spelling",
                    new java.io.IOException());
        }
        return sceneList.get(ID).getControl(com.spectre.controller.scene.SceneController.class);
    }

    /**
     * Returns the Totems's totemController
     * @param ID the name of the totem
     * @return ID the totem controller
     */
    public static com.spectre.controller.scene.TotemController getTotem(String ID) {
        if (!sceneList.containsKey(ID) || sceneList.get(ID).getControl(com.spectre.controller.scene.TotemController.class) == null) {
            com.spectre.app.SpectreApplication.logger.log(
                    java.util.logging.Level.SEVERE,
                    "Scene: " + ID + " cannot be found in the loaded sceneList please check spelling",
                    new java.io.IOException());
        }
        return sceneList.get(ID).getControl(com.spectre.controller.scene.TotemController.class);
    }

    /**@see com.spectre.app.SpectreApplication#getModelNode()*/
    public static com.jme3.scene.Node getModelNode() {
        return application.getModelNode();
    }

    /**@see com.spectre.app.SpectreApplication#getSceneNode()*/
    public static com.jme3.scene.Node getSceneNode() {
        return application.getSceneNode();
    }

    /**
     * @param <T> AppState Class
     * @param stateClass the AppStates .class
     * @return the desired app state if it is attached
     */
    public static <C extends com.jme3.app.state.AppState> C getAppState(Class<C> stateClass) {
        return stateManager.getState(stateClass);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: ADD GAMESAVE HERE 
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.writeStringSavableMap(playerList, "playerList", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        playerList = (HashMap<String, SpectrePlayerController>) ic.readStringSavableMap("playerList", null);
    }
}
