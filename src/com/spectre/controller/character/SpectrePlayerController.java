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
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.impl.ActionModeControl;
import com.spectre.controller.character.impl.SpectreControl;
import com.spectre.deck.RepositoryDeck;
import com.spectre.director.Director;
import com.spectre.util.Buttons;
import java.io.IOException;
import java.util.logging.Level;

/**
 * This Controller is used to handle all player information TODO needs to be
 * updated to call interface controls instead of it directly
 *
 * @author Kyle Williams
 */
public class SpectrePlayerController implements SpectreControl, ActionModeControl, Savable {

    /*This is to keep track of players highest total Score*/
    private int playerhighScore = 0;
    private RepositoryDeck repo;
    protected boolean enabled = true;
    protected Spatial spatial;
    private SpectreAnimationController sac;
    private SpectreDuelController sec;
    private SpectreInputController sic;
    private SpectrePhysicsController sphc;
    private SpectreCameraController scc;
    private CardManifestController cmc;
    protected String playerName;

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
        Director.getModelNode().attachChild(getSpatial());

        enableInput(true);

        setSpectreControllers(true);

        return this;
    }

    /**
     * removes the player from the scene-graph and physical world
     */
    public SpectrePlayerController removeFromPlay() {
        Director.getModelNode().detachChild(getSpatial());

        enableInput(false);

        setSpectreControllers(false);

        return this;
    }

    /**
     * Used to Start or Destroy attached Spectre Controls applied to the spatial
     *
     * @param isStartUp
     */
    private void setSpectreControllers(boolean isStartUp) {
        for (int i = 0; i < getSpatial().getNumControls(); i++) {
            Control cont = getSpatial().getControl(i);
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
        spat.addControl(getEssenceCont());
        spat.addControl(getManifestCont());
        return this;
    }

    /**
     * Detaches this PlayerController from the controlled model
     *
     * @param spatial
     */
    public void removeModel() {
        spatial.removeControl(getManifestCont());
        spatial.removeControl(getEssenceCont());
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
    public Spatial getSpatial() {
        if (spatial == null) {
            com.spectre.app.SpectreApplication.logger.severe("This Player does not have control of a character");
            return null;
        }
        return spatial;
    }

    public void setActionMode(boolean modiferButton) {
        for (int i = 0; i < getSpatial().getNumControls(); i++) {
            Control cont = getSpatial().getControl(i);
            if (cont != this && cont instanceof ActionModeControl) {
                ((ActionModeControl) cont).setActionMode(modiferButton);

            }
        }
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

    public CardManifestController getManifestCont() {
        if (cmc == null) {
            cmc = new CardManifestController();
        }
        return cmc;
    }

    public SpectreDuelController getEssenceCont() {
        if (sec == null) {
            sec = new SpectreDuelController();
        }
        return sec;
    }

    public SpectreAnimationController getAnimCont() {
        if (sac == null) {
            sac = new SpectreAnimationController();
        }
        return sac;
    }

    public SpectreInputController getInputCont() {
        if (sic == null) {
            sic = new SpectreInputController();
        }
        return sic;
    }

    public SpectreCameraController getCameraCont() {
        if (scc == null) {
            scc = new SpectreCameraController();
        }
        return scc;
    }

    public void ControlChanged() {
        sac = null;
        sec = null;
        sic = null;
        scc = null;
        cmc = null;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("/AT NO TIME SHOULD THIS CONTROLLER BE CLONED IT IS UNIQUE TO EACH USER");
    }

    @Override
    public void startUp() {
        SpectreApplication.logger.log(Level.INFO, "Adding {0} to the field", playerName);
    }

    @Override
    public void cleanUp() {
        SpectreApplication.logger.log(Level.INFO, "Removing {0} to the field", playerName);
    }

    public void setSpatial(Spatial spatial) {
        if (this.spatial != null && spatial != null && spatial != this.spatial) {
            throw new IllegalStateException("This control has already been added to a Spatial");
        }
        this.spatial = spatial;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void update(float tpf) {
    }

    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(spatial, "spatial", null);
        oc.write(playerName, "name", null);
        oc.write(playerhighScore, "playerhighScore", 0);
        oc.write(repo, "repo", new RepositoryDeck());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
        playerName = ic.readString("name", null);
        playerhighScore = ic.readInt("playerhighScore", 0);
        repo = (RepositoryDeck) ic.readSavable("repo", new RepositoryDeck());
    }
}