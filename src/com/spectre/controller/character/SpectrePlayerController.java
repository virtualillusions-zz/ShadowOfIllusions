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
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.gui.CardGUIController;
import com.spectre.controller.character.impl.AbstractSpectreController;
import com.spectre.controller.character.impl.SpectreControl;
import com.spectre.deck.RepositoryDeck;
import com.spectre.director.Director;
import com.spectre.util.Buttons;
import java.io.IOException;
import java.util.logging.Level;

/**
 * This Controller is used to handle all player information
 *
 * @author Kyle Williams
 */
public class SpectrePlayerController extends AbstractSpectreController implements Savable {

    /*This is to keep track of players highest total Score*/
    private int playerhighScore = 0;
    private RepositoryDeck repo;

    /**
     * The Highest Score the player has
     *
     * @return
     */
    public int getPlayerHighScore() {
        return playerhighScore;
    }

    /**
     * Sets a new high score if higher than the current
     *
     * @param newHighScore
     */
    public void setPlayerNewHighScore(int newHighScore) {
        playerhighScore = newHighScore > playerhighScore ? newHighScore : playerhighScore;
    }

    /**
     * Creates a New Player
     *
     * @param name
     */
    public SpectrePlayerController(String name) {
        this.playerName = name;
        repo = new RepositoryDeck();
    }

    /**
     * @return name The name of this player
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    public RepositoryDeck getRepo() {
        return repo;
    }

    /**
     * Adds The player to the scene-graph and physical world
     */
    public SpectrePlayerController addIntoPlay() {
        Director.getModelNode().attachChild(getModel());

        enableInput(true);

        setSpectreControllers(true);

        return this;
    }

    /**
     * removes the player from the scene-graph and physical world
     */
    public void removeFromPlay() {
        Director.getModelNode().detachChild(getModel());

        enableInput(false);

        setSpectreControllers(false);
    }

    /**
     * Used to Start or Destroy attached Spectre Controls applied to the spatial
     *
     * @param isStartUp
     */
    public void setSpectreControllers(boolean isStartUp) {
        for (int i = 0; i < getModel().getNumControls(); i++) {
            Control cont = getModel().getControl(i);
            if (cont != this && cont instanceof SpectreControl) {
                if (isStartUp == true) {
                    ((SpectreControl) cont).startUp();
                } else {
                    ((SpectreControl) cont).cleanUp();
                }
            }
        }
    }

    /**
     * Attaches this PlayerController to the specified model
     *
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
        spat.addControl(getCameraCont());//gamestate
        spat.addControl(getAnimCont());//character
        spat.addControl(getInputCont());
        return this;
    }

    /**
     * Detaches this PlayerController from the controlled model
     *
     * @param spatial
     */
    public void removeModel() {
        spatial.removeControl(getInputCont());
        spatial.removeControl(getAnimCont());
        spatial.removeControl(getCameraCont());
        spatial.setUserData("playerName", null);
        spatial.removeControl(this);
        sphc = null;
    }

    /**
     * Returns the Spatial this Player is in control of
     *
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
            iM.addListener(getInputCont(), Buttons.getButtons(getPlayerName()));
        } else {
            InputManager iM = Director.getApp().getInputManager();
            for (String s : Buttons.getButtons(getPlayerName())) {
                iM.deleteMapping(s);
            }
            iM.removeListener(getInputCont());
        }
    }

    /**
     * A class that handles the GUI of the cards shown in front of the player
     * Set in GameState
     *
     * @return CardGUI
     */
    public CardGUIController getCardGUI() {
        return spatial.getControl(CardGUIController.class);
    }

    @Override
    public SpectreAnimationController getAnimCont() {
        if (sac == null) {
            sac = new SpectreAnimationController();
        }
        return sac;
    }

    @Override
    public SpectreInputController getInputCont() {
        if (sic == null) {
            sic = new SpectreInputController();
        }
        return sic;
    }

    @Override
    public SpectreCameraController getCameraCont() {
        if (scc == null) {
            scc = new SpectreCameraController();
        }
        return scc;
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
        oc.write(repo, "repo", new RepositoryDeck());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        playerName = ic.readString("name", null);
        playerhighScore = ic.readInt("playerhighScore", 0);
        repo = (RepositoryDeck) ic.readSavable("repo", new RepositoryDeck());
    }

    @Override
    public void startUp() {
        SpectreApplication.logger.log(Level.INFO, "Adding {0} to the field", playerName);
    }

    @Override
    public void cleanUp() {
        SpectreApplication.logger.log(Level.INFO, "Removing {0} to the field", playerName);
    }
}