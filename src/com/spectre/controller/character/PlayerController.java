/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.controller.character;

import com.jme3.input.controls.Trigger;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort; 
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import com.spectre.app.SpectreCamera;
import com.spectre.controller.character.gui.CardGUIController;
import com.spectre.director.Director;
import com.spectre.util.Buttons;
 
import java.util.EnumMap;

/**
 *  TODO: implement SAVABLES
 * @author Kyle Williams
 */
public class PlayerController extends com.jme3.scene.control.AbstractControl{
    //The name of the player
    private String name = null;
    //The physical representation of this player's controlled model
    private CharacterController pSpatial;
    //The Players input buttons
    private EnumMap<Buttons,Trigger[]> inputControls = null;
    private SpectreCamera cam;

    /**
     * Creates a New Player
     * @param name
     */
    public PlayerController(String name){
        this.name=name;
        if(inputControls==null)inputControls=Buttons.getDefaultKeys();
    }

    /*
     * @return name The name of this player
     */
    public String getName(){return name;}

    /**
     * Adds The player to the scene-graph and physical world
     */
    public PlayerController addIntoPlay() {
        Director.getModelNode().attachChild(getModel());
        Director.getPhysicsSpace().add(pSpatial);
        return this;
    }

    /**
     * removes the player from the scene-graph and physical world
     */
    public void removeFromPlay() {
        Director.getModelNode().detachChild(getModel());
        Director.getPhysicsSpace().remove(pSpatial);
    }

    /**
     * Attaches this PlayerController to the specified model
     * @param spatial
     */
    public PlayerController setModel(String name) {
        pSpatial = Director.getModel(name);
        Spatial spat = pSpatial.getSpatial();
        spat.setName(getName());
        //Check To Make sure no one else is in control of this model
        if(spat.getControl(PlayerController.class)!=null){
            spat.getControl(PlayerController.class).removeModel();
            com.spectre.app.SpectreApplication.logger.severe("Reassigning previous model to a new Player");
        }
        
        spat.addControl(this);
        //TODO:Either Create a class whose primary function is to lend out camera's or add to this parameter to include a new camera
        cam = new SpectreCamera(Director.getApp().getCamera(),spatial);
        spat.getControl(InputController.class).set(name, inputControls,getCamera().getCamera());
                
        return this;
    }

    /**
     * Detaches this PlayerController from the controlled model
     * @param spatial
     */
    public void removeModel() {
        spatial.removeControl(this);
        spatial.removeControl(com.spectre.controller.character.EssenceController.class);
    }

    /**
     * Returns the Spatial this Player is in control of
     * @return spatial
     */
    public Spatial getModel(){
        if(spatial==null){
            com.spectre.app.SpectreApplication.logger.severe("This Player does not have control of a character");
            return null;
        }
        return spatial;
    }

    /**
     * Returns the Spatial this Player is in control of
     * Set at characterLoading
     * @return spatial
     */
    public CharacterController getPhysicsModel(){
        if(pSpatial==null){
            com.spectre.app.SpectreApplication.logger.severe("This Player does not have control of a character");
            return null;
        }
        return pSpatial;
    }

    /**
     * A class that handles all inputs for the model
     * Set at characterLoading
     * @return inputHandler
     */
    public InputController getInputController() {
        return spatial.getControl(InputController.class);
    }

    public void setControllerActive(boolean active){
        spatial.getControl(InputController.class).setControllerActive(active);
    }

    /**
     * A class that handles all animation for the model
     * Set at characterLoading
     * @return CharacterAnimationController
     */
    public com.spectre.controller.character.CharacterAnimationController getAnimationController() {
        return spatial.getControl(CharacterAnimationController.class);
    }

    /**
     * A class that handles the GUI of the cards shown in front of the player
     * Set in GameState
     * @return CardGUI
     */
     public com.spectre.controller.character.gui.CardGUIController getCardGUI(){
         return spatial.getControl(CardGUIController.class);
     }
     
     /**
      * A class that handles all life and focus functions of the player
      * Set in GameState
      * @return EssenceController
      */
     public com.spectre.controller.character.EssenceController getEssenceController(){
         return spatial.getControl(EssenceController.class);
     }

    /**
     * TODO: CREATE DESTROYABLE CAMERAS TO GIVE AWAY FOR PLAYERCONTROLLER
     * @param tpf
     */
    public com.spectre.app.SpectreCamera getCamera(){return cam;}

    @Override
    protected void controlUpdate(float tpf) {    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {return null;}
}