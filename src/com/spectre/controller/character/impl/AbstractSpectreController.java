/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.spectre.controller.character.SpectrePlayerController;

/**
 * An abstract AbstractControl implementation of the SpectreControl interface.
 * Used for character Management
 *
 * Note: Since their will be different controls in place it seems better to find
 * the control in spatial and return each time instead of save an instance
 *
 * @author Kyle Williams
 */
public abstract class AbstractSpectreController extends AbstractControl implements SpectreControl {

    private SpectrePlayerController spc;
    private CharControl sphc;//pre-loaded
    private AnimationControl sac;
    private ActionControl cmc;
    private InputControl sic;
    private CamControl scc;
    private DuelControl sec;
    protected String playerName;

    public void cleanUp() {
        sac = null;
        sec = null;
        sic = null;
        sphc = null;
        spc = null;
        playerName = null;
        scc = null;
    }

    public void ControlChanged() {        
        sac = null;
        sec = null;
        sic = null;
        sphc = null;
        spc = null;
        playerName = null;
        scc = null;
    }

    /**
     * A class that handles all animation for the model Set at characterLoading
     *
     * @return CharacterAnimationController
     */
    public AnimationControl getAnimCont() {
        if (sac == null) {
            sac = spatial.getControl(AnimationControl.class);
        }
        return sac;
    }

    /**
     * A class that handles all life and focus functions of the player Set in
     * GameState
     *
     * @return SpectreEssenceController
     */
    public DuelControl getEssenceCont() {
        if (sec == null) {
            sec = spatial.getControl(DuelControl.class);
        }
        return sec;
    }

    /**
     * A class that handles Card implementation
     *
     * @return
     */
    public ActionControl getManifestCont() {
        if (cmc == null) {
            cmc = spatial.getControl(ActionControl.class);
        }
        return cmc;
    }

    /**
     * A class that handles all inputs for the model Set at characterLoading
     *
     * @return inputHandler
     */
    public InputControl getInputCont() {
        if (sic == null) {
            sic = spatial.getControl(InputControl.class);
        }
        return sic;
    }

    /**
     * Returns the Spatial this Player is in control of Set at characterLoading
     *
     * @return spatial
     */
    public CharControl getPhysCont() {
        if (sphc == null) {
            sphc = spatial.getControl(CharControl.class);
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
     *
     * @return
     */
    public String getPlayerName() {
        if (playerName == null) {
            playerName = spatial.getControl(SpectrePlayerController.class).getPlayerName();
        }
        return playerName;
    }

    public CamControl getCameraCont() {
        if (scc == null) {
            scc = spatial.getControl(CamControl.class);
        }
        return scc;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void startUp() {
    }
}