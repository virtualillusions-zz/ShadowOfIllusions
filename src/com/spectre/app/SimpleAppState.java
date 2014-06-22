/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.HashMap;

/**
 *
 * @author Kyle D. Williams
 */
public abstract class SimpleAppState extends SpectreAppState {

    public abstract void SimpleAppState();

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        this.app = sAppState.getApplication();
        this.sAppState = sAppState;
        this.settings = sAppState.getAppSettings();
        this.inputManager = sAppState.getInputManager();
        this.stateManager = sAppState.getApplication().getStateManager();
        this.assetManager = sAppState.getAssetManager();
        this.guiNode = sAppState.getGuiNode();
        this.rootNode = sAppState.getRootNode();
        this.modelSubNode = sAppState.getModelNode();
        this.sceneSubNode = sAppState.getSceneNode();
        this.physicsDirector = sAppState.getPhysicsDirector();
        this.modelBindingsList = sAppState.getModelBindingsList();
        SimpleAppState();
    }

    @Override
    protected void spectreUpdate(float tpf) {
    }

    @Override
    public void cleanUp() {
    }

    /**
     * @return the app
     */
    public Application getApp() {
        return app;
    }

    /**
     * @return the sAppState
     */
    public SpectreApplicationState getsAppState() {
        return sAppState;
    }

    /**
     * @return the settings
     */
    public AppSettings getSettings() {
        return settings;
    }

    /**
     * @return the inputManager
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * @return the stateManager
     */
    public AppStateManager getStateManager() {
        return stateManager;
    }

    /**
     * @return the assetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * @return the guiNode
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * @return the rootNode
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * @return the guiNodeName
     */
    public String getGuiNodeName() {
        return guiNodeName;
    }

    /**
     * @return the rootNodeName
     */
    public String getRootNodeName() {
        return rootNodeName;
    }

    /**
     * @return the modelNodeName
     */
    public String getModelNodeName() {
        return modelNodeName;
    }

    /**
     * @return the sceneNodeName
     */
    public String getSceneNodeName() {
        return sceneNodeName;
    }

    /**
     * @return the modelSubNode
     */
    public Node getModelNode() {
        return modelSubNode;
    }

    /**
     * @return the sceneSubNode
     */
    public Node getSceneNode() {
        return sceneSubNode;
    }

    /**
     * @return the physicsDirector
     */
    public BulletAppState getPhysicsDirector() {
        return physicsDirector;
    }

    public com.jme3.bullet.PhysicsSpace getPhysicsSpace() {
        return getPhysicsDirector().getPhysicsSpace();
    }

    public HashMap<EntityId, Spatial> getModelBindingsList() {
        return modelBindingsList;
    }
    //SpectreApplicationState Variables
    private Application app;
    private SpectreApplicationState sAppState;
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
    private BulletAppState physicsDirector;
    private HashMap<EntityId, Spatial> modelBindingsList;
}
