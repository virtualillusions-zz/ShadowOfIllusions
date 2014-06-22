/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.spectre.controller.character.impl.AbstractSpectreController;
import com.spectre.controller.character.impl.InputControl;
import com.spectre.util.Buttons.ControlInputs;

/**
 * A Character Control which handles input handling Attached To Spatial via
 * {@link com.spectre.controller.character.SpectrePlayerController#setModel(String)}
 *
 * @author Kyle Williams
 */
public class SpectreInputController extends AbstractSpectreController implements InputControl {

    private boolean modiferButton = false;

    /**
     * A Character Control used to handle user Input
     */
    public SpectreInputController() {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isEnabled()) {
            return;
        }
        if (name.equals(getPlayerName() + ":" + ControlInputs.Action1)) {//CM: Perform/Remap Action 1 __ AM: Jump
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(0, isPressed);
            } else if (isPressed == true) {
                getPhysCont().jump();
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Action2)) {//CM: Perform/Remap Action 2 __ AM: evade/Slide 
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(1, isPressed);
            } else if (isPressed == true) {
                getPhysCont().evade();
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Action3)) {//CM: Perform/Remap Action 3 __ AM: dash
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(2, isPressed);
            } else if (isPressed == true) {
                getPhysCont().dash();
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Action4)) {//CM: Perform/Remap Action 4 __ AM: Character's Auxillary
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(3, isPressed);
            } else if (isPressed == true) {
                //TODO: AUXILLARY
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.PrevTarget)//CM: Change To Prev Target
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget(false);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.NextTarget)//CM: Change To Next Target
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget(true);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.LockOn)// Lock On/OFF
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget();
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CenterCamera)// Center Camera 
                && isPressed == true) {
            getCameraCont().inputCenterCamera();
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Alt)) {//CM: GatherFocus(whileStill) __ AM: Perform Special Maneuver
            //***Perform Special Manuever is a augmented form of Jump, evade/Slide or dash. It can be used as in chain with these to perform a Double Jump evade/Slide or dash
            if (modiferButton == false) {
                getEssenceCont().gatherFocus(isPressed);
            } else if (isPressed == true) {
                //TODO: Special Movement
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Mode)) {//CM: Hold: Enter Action Mode __ AM: Release: Enter Caster Mode  
            modiferButton = isPressed;
            getPlayerCont().setActionMode(modiferButton);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Info1)) {//Show/Hide Explanation of Mapped Action 1
            getEssenceCont().cardInfo(1, isPressed);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Info2)) {//Show/Hide Explanation of Mapped Action 2
            getEssenceCont().cardInfo(2, isPressed);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Info3)) {//Show/Hide Explanation of Mapped Action 3
            getEssenceCont().cardInfo(3, isPressed);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Info4)) {//Show/Hide Explanation of Mapped Action 4
            getEssenceCont().cardInfo(4, isPressed);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Start)) {//Menu
            if (isPressed == true) {
                //TODO: Open Menu - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.Back)) {//Reshuffle deck at start of battle
            if (isPressed == true) {
                getEssenceCont().reshuffleDeck();
            }
        }
    }

    /**
     * The main AnalogListener
     */
    public void onAnalog(String name, float value, float tpf) {
        if (!isEnabled()) {
            return;
        }
        if (name.equals(getPlayerName() + ":" + ControlInputs.CharacterForward)) {//Move Character Up  
            getPhysCont().moveForward(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CharacterBack)) {//Move Character Back   
            getPhysCont().moveForward(-(value / tpf));
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CharacterLeft)) {//Move Character  Right           
            getPhysCont().moveLeft(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CharacterRight)) {//Move Character Left
            getPhysCont().moveLeft(-(value / tpf));
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CameraUp)) {//Rotate Camera Up
            getCameraCont().inputVRotateCamera(value);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CameraDown)) {//Rotate Camera Down
            getCameraCont().inputVRotateCamera(-value);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CameraRight)) {//Rotate Camera Right
            getCameraCont().inputHRotateCamera(value);
        } else if (name.equals(getPlayerName() + ":" + ControlInputs.CameraLeft)) {//Rotate Camera Left
            getCameraCont().inputHRotateCamera(-value);
        }
    }
}
