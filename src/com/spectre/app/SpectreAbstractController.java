/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.spectre.controller.character.SpectreAnimationController;
import com.spectre.controller.character.SpectreEssenceController;
import com.spectre.controller.character.SpectreInputController;
import com.spectre.controller.character.SpectrePhysicsController;
import com.spectre.controller.character.SpectrePlayerController;

/**
 *
 * @author Kyle Williams
 */
public abstract class SpectreAbstractController extends AbstractControl {

    protected SpectreAnimationController sac;//set in SpectrePlayerController
    protected SpectreEssenceController sec;
    protected SpectreInputController sic;//set in SpectrePlayerController
    protected SpectrePhysicsController sphc;//pre-loaded
    protected SpectrePlayerController spc;//Set or created by player
    protected String playerName;
    protected SpectreCameraController scc;//set in SpectrePlayerController
    protected Camera cam;

    public void startUp() {
    }

    public void cleanUp() {
        sac = null;
        sec = null;
        sic = null;
        sphc = null;
        spc = null;
        playerName = null;
        scc = null;
        cam = null;
    }

    /**
     * A class that handles all animation for the model
     * Set at characterLoading
     * @return CharacterAnimationController
     */
    public SpectreAnimationController getAnimCont() {
        if (sac == null) {
            sac = spatial.getControl(SpectreAnimationController.class);
        }
        return sac;
    }

    /**
     * A class that handles all life and focus functions of the player
     * Set in GameState
     * @return SpectreEssenceController
     */
    public SpectreEssenceController getEssenceCont() {
        if (sec == null) {
            sec = spatial.getControl(SpectreEssenceController.class);
        }
        return sec;
    }

    /**
     * A class that handles all inputs for the model
     * Set at characterLoading
     * @return inputHandler
     */
    public SpectreInputController getInputCont() {
        if (sic == null) {
            sic = spatial.getControl(SpectreInputController.class);
        }
        return sic;
    }

    /**
     * Returns the Spatial this Player is in control of
     * Set at characterLoading
     * @return spatial
     */
    public SpectrePhysicsController getPhysCont() {
        if (sphc == null) {
            sphc = spatial.getControl(SpectrePhysicsController.class);
        }
        return sphc;
    }

    public SpectrePlayerController getPlayerCont() {
        if (spc == null) {
            spc = spatial.getControl(SpectrePlayerController.class);
        }
        return spc;
    }

    /**
     * Returns the current Players Name 
     * @return 
     */
    public String getPlayerName() {
        if (playerName == null) {
            playerName = spatial.getControl(SpectrePlayerController.class).getPlayerName();
        }
        return playerName;
    }

    public SpectreCameraController getCameraCont() {
        if (scc == null) {
            scc = spatial.getControl(SpectreCameraController.class);
        }
        return scc;
    }

    /**
     * TODO: CREATE DESTROYABLE CAMERAS TO GIVE AWAY FOR PLAYERCONTROLLER
     * @param tpf
     */
    public Camera getCamera() {
        if (cam == null) {
            cam = spatial.getControl(SpectreCameraController.class).getCamera();
        }
        return cam;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}