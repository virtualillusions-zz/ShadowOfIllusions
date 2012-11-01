/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.RenderState;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.util.BufferUtils;

/**
 * <code>SpectreApplication</code> extends the <code>Application</code> class
 * to provide functionality for all base elements of Spectre, and an accessible
 * root node that is updated and rendered regularly.
 * @author Kyle Williams
 */
public abstract class SpectreApplication extends Application {

    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("GUI Node");
    protected Node modelSubNode = new Node("Model Node");
    protected Node sceneSubNode = new Node("Scene Node");
    public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.jme3");

    @Override
    public void start() {
        //Creates settings inCase this
        //is the first time application has been started
        if (settings == null) {
            setSettings(new AppSettings(true));
        }

        setSettings(settings);
        super.start();
    }

    /**@return guiNode a node specifically for the graphical user interface*/
    public Node getGuiNode() {
        return guiNode;
    }

    /**Should not be called to attach any other node to
     * @return rootNode is the master node that all nodes are added to*/
    public Node getRootNode() {
        return rootNode;
    }

    /**@return modelNode a node which Handel all models within the world*/
    public Node getModelNode() {
        return modelSubNode;
    }

    /**@return sceneNode a node which Handel all scenes within the world*/
    public Node getSceneNode() {
        return sceneSubNode;
    }

    public AppSettings getAppSettings() {
        return settings;
    }

    public float getSpeed() {
        return speed;
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
        guiViewPort.attachScene(guiNode);

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
        //Add joystick support
        //settings.setUseJoysticks(true);
        inputManager.setAxisDeadZone(0.2f);
        //Must be initialzied before other states to avoid issue
        getStateManager().attach(new com.spectre.director.Director());
        //Handles and organzies music and effects
        getStateManager().attach(new com.spectre.director.FXDirector());



        //Class used to load all assets that will be used during the course of the application runtime
//        SpectreAssetLoader spectreLoader = new SpectreAssetLoader(getAssetManager(),getAudioRenderer());
//        //Checks to see if all models where loaded
//        if(!com.spectre.director.Director.modelsAreLoaded()||!com.spectre.director.Director.scenesAreLoaded()){            
//            //Finds and loads all characters and animations if not already done so
//            if(!com.spectre.director.Director.modelsAreLoaded()){
//                spectreLoader.findAndLoadAllCharacters();
//            }
//           //Finds and loads all Scenes if not already done so
//            if(!com.spectre.director.Director.scenesAreLoaded()){
//                spectreLoader.findAndLoadAllScenes();
//            }
//        }
//        //Finds and loads all music
//        spectreLoader.findAndLoadAllMusic();
//        //Finds and loads all Cards
//        spectreLoader.findAndLoadAllCards();




        //This SETS UP THE CAMERA DIRECTOR USED TO SET DYNAMIC ANGLES AND SUCH
        //this.getAppStateManager().attach(new com.vza.director.CameraDirector());
        //This SETS UP THE GUI FOR OUR GAME AND SERVES AS A HUB TO OTHER GAME TYPES
        //this.getAppStateManager().attach(new com.vza.director.GuiDirector());
        //This SETS UP THE COLLISION FOR THE GAME
        //this.getAppStateManager().attach(new com.vza.director.PhysicsDirector());
        //SetsUp Post Filters for the whole game
        com.spectre.director.FilterSubDirector filterDirector = new com.spectre.director.FilterSubDirector(getAssetManager());
        filterDirector.setupFilters(renderer, viewPort);
        com.spectre.director.Director.setFilterDirector(filterDirector);
        //Call Game Code
        spectreApp();

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
        rootNode.updateLogicalState(tpf);//TEMP FIX TO PHYSICS STUTTERING
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
    protected abstract void spectreApp();

    /**
     * This updated function is used mainly for quick testing purposes
     */
    protected abstract void spectreUpdate(float tpf);

    /**
     * This rendering function is used mainly for quick testing purposes
     */
    public abstract void spectreRender(RenderManager rm);
}