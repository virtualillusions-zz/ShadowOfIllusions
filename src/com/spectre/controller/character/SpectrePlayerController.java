/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.input.InputManager;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.SpectreAbstractController;

import com.spectre.controller.character.gui.CardGUIController;
import com.spectre.director.Director;
import com.spectre.util.Buttons;
import java.io.IOException;

/**
 * This Controller is used to handle all player information
 * TODO: UPDATE 
 * @author Kyle Williams
 */
public class SpectrePlayerController extends SpectreAbstractController implements Savable {
    //The name of the player

    private String playerName = null;
    private SpectreInputController sic;
    private SpectrePhysicsController sphc;
    private int playerhighScore = 0;//This is to keep track of players highest total Score

    /**
     * The Highest Score the player has
     * @return 
     */
    public int getPlayerHighScore() {
        return playerhighScore;
    }

    /**
     * Sets a new high score if higher than the current
     * @param newHighScore 
     */
    public void setPlayerNewHighScore(int newHighScore) {
        playerhighScore = newHighScore > playerhighScore ? newHighScore : playerhighScore;
    }

    /**
     * Creates a New Player
     * @param name
     */
    public SpectrePlayerController(String name) {
        this.playerName = name;
    }

    /**
     * @return name The name of this player
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Adds The player to the scene-graph and physical world
     */
    public SpectrePlayerController addIntoPlay() {
        Director.getModelNode().attachChild(getModel());
        //Director.getPhysicsSpace().add(spc);

        enableInput(true);


        for (int i = 0; i < getModel().getNumControls(); i++) {
            Control cont = getModel().getControl(i);
            if (cont != this && cont instanceof SpectreAbstractController) {
                ((SpectreAbstractController) cont).startUp();
            }
        }

        return this;
    }

    /**
     * removes the player from the scene-graph and physical world
     */
    public void removeFromPlay() {
        Director.getModelNode().detachChild(getModel());
        //Director.getPhysicsSpace().remove(spc);
        enableInput(false);

        for (int i = 0; i < getModel().getNumControls(); i++) {
            Control cont = getModel().getControl(i);
            if (cont != this && cont instanceof SpectreAbstractController) {
                ((SpectreAbstractController) cont).cleanUp();
            }
        }

    }

    /**
     * Attaches this PlayerController to the specified model
     * @param spatial
     */
    public SpectrePlayerController setModel(String name) {
        sphc = Director.getModel(name);
        Spatial spat = sphc.getSpatial();//Done because currently any spatial attached to this control is irrelevent 
        //spat.setName(getPlayerName());//should i do this to help identify later?
        spat.setUserData("playerName", playerName);
        //Check To Make sure no one else is in control of this model
        if (spat.getControl(SpectrePlayerController.class) != null) {
            spat.getControl(SpectrePlayerController.class).removeModel();
            com.spectre.app.SpectreApplication.logger.severe("Reassigning previous model to a new Player");
        }

        spat.addControl(this);
        spat.addControl(getInputCont());
        //TODO:Either Create a class whose primary function is to lend out camera's or add to this parameter to include a new camera
        //cam = new SpectreCameraController(Director.getApp().getCamera(), spatial);
        //spat.getControl(InputController.class).set(name, inputControls, getCamera().getCamera());

        return this;
    }

    /**
     * Detaches this PlayerController from the controlled model
     * @param spatial
     */
    public void removeModel() {
        spatial.removeControl(getInputCont());
        spatial.setUserData("playerName", "");
        spatial.removeControl(this);
        sphc = null;
    }

    /**
     * Returns the Spatial this Player is in control of
     * @return spatial
     */
    public Spatial getModel() {
        if (spatial == null) {
            com.spectre.app.SpectreApplication.logger.severe("This Player does not have control of a character");
            return null;
        }
        return spatial;
    }

    public void enableInput(boolean enabled) {
        if (enabled == true) {
            InputManager iM = Director.getApp().getInputManager();
            iM.addListener(getInputCont(), Buttons.getBoth(getPlayerName()));
        } else {
            InputManager iM = Director.getApp().getInputManager();
            for (String s : Buttons.getBoth(getPlayerName())) {
                iM.deleteMapping(s);
            }
            iM.removeListener(getInputCont());
        }
    }

    /**
     * A class that handles the GUI of the cards shown in front of the player
     * Set in GameState
     * @return CardGUI
     */
    public com.spectre.controller.character.gui.CardGUIController getCardGUI() {
        return spatial.getControl(CardGUIController.class);
    }

    @Override
    public SpectreInputController getInputCont() {
        if (sic == null) {
            sic = new SpectreInputController();
        }
        return sic;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;//AT NO TIME SHOULD THIS CONTROLLER BE CLONED IT IS UNIQUE TO EACH USER
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(playerName, "name", null);
        oc.write(playerhighScore, "playerhighScore", 0);
        oc.write(sic, "spectreInputController", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        playerName = ic.readString("name", null);
        playerhighScore = ic.readInt("playerhighScore", 0);
        sic = (SpectreInputController) ic.readSavable("spectreInputController", null);
    }
}