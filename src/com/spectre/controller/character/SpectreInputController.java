/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.spectre.app.SpectreAbstractController;
import com.spectre.util.Buttons.controlInputs;

/**
 *
 * @author Kyle Williams
 */
public class SpectreInputController extends SpectreAbstractController implements ActionListener, AnalogListener {

    private boolean modiferButton = false;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isEnabled()) {
            return;
        }
        if (name.equals(getPlayerName() + ":" + controlInputs.Action1)) {//CM: Perform/Remap Action 1 __ AM: Jump
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(1, isPressed);
            } else if (isPressed == true) {
                getPhysCont().Jump();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action2)) {//CM: Perform/Remap Action 2 __ AM: Roll/Slide 
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(2, isPressed);
            } else if (isPressed == true) {
                getPhysCont().Roll();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action3)) {//CM: Perform/Remap Action 3 __ AM: Dash
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(3, isPressed);
            } else if (isPressed == true) {
                getPhysCont().Dash();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action4)) {//CM: Perform/Remap Action 4 __ AM: Character's Auxillary
            if (modiferButton == false) {
                //isPressed card pressed/released
                getEssenceCont().cardPressed(4, isPressed);
            } else if (isPressed == true) {
                //TODO: AUXILLARY
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action5)//CM: Change To Prev Target
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget(false);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action6)//CM: Change To Next Target
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget(true);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action7)// Lock On/OFF
                && isPressed == true) {
            getCameraCont().inputToggleLockOnTarget();
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action8)// Center Camera 
                && isPressed == true) {
            getCameraCont().inputCenterCamera();
        } else if (name.equals(getPlayerName() + ":" + controlInputs.LeftTrigger)) {//CM: GatherFocus(whileStill) __ AM: Perform Special Maneuver
            //***Perform Special Manuever is a augmented form of Jump, Roll/Slide or Dash. It can be used as in chain with these to perform a Double Jump Roll/Slide or Dash
            if (modiferButton == false) {
                getEssenceCont().gatherFocus(isPressed);
            } else if (isPressed == true) {
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightTrigger)) {//CM: Hold: Enter Action Mode __ AM: Release: Enter Caster Mode  
            modiferButton = isPressed;
            getPhysCont().setMod(modiferButton);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadDown)) {//Show/Hide Explanation of Mapped Action 1
            getEssenceCont().cardInfo(1, isPressed);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadRight)) {//Show/Hide Explanation of Mapped Action 2
            getEssenceCont().cardInfo(2, isPressed);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadUp)) {//Show/Hide Explanation of Mapped Action 3
            getEssenceCont().cardInfo(3, isPressed);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadLeft)) {//Show/Hide Explanation of Mapped Action 4
            getEssenceCont().cardInfo(4, isPressed);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Start)) {//Menu
            if (isPressed == true) {
                //TODO: Open Menu - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Back)) {//Reshuffle deck at start of battle
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
        if (name.equals(getPlayerName() + ":" + controlInputs.LeftThumbstickUp)) {//Move Character Up  
            getPhysCont().moveCharacterUD(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.LeftThumbstickDown)) {//Move Character Back   
            getPhysCont().moveCharacterUD(-value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.LeftThumbstickRight)) {//Move Character  Right           
            getPhysCont().moveCharacterLR(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.LeftThumbstickLeft)) {//Move Character Left
            getPhysCont().moveCharacterLR(-value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickUp)) {//Rotate Camera Up
            getCameraCont().inputVRotateCamera(value);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickDown)) {//Rotate Camera Down
            getCameraCont().inputVRotateCamera(-value);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickRight)) {//Rotate Camera Right
            getCameraCont().inputHRotateCamera(value);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickLeft)) {//Rotate Camera Left
            getCameraCont().inputHRotateCamera(-value);
        }
    }
}
