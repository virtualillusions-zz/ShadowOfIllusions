/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
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
                if (isPressed == true) {
                    //card pressed button
                    getEssenceCont().cardPressed(1, true);
                } else {
                    //cardReleased button
                    getEssenceCont().cardPressed(1, false);
                }
            } else if (isPressed == true) {
                getPhysCont().Jump();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action2)) {//CM: Perform/Remap Action 2 __ AM: Roll/Slide 
            if (modiferButton == false) {
                if (isPressed == true) {
                    //card pressed button
                    getEssenceCont().cardPressed(2, true);
                } else {
                    //cardReleased button
                    getEssenceCont().cardPressed(2, false);
                }
            } else if (isPressed == true) {
                getPhysCont().Roll();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action3)) {//CM: Perform/Remap Action 3 __ AM: Dash
            if (modiferButton == false) {
                if (isPressed == true) {
                    //card pressed button
                    getEssenceCont().cardPressed(3, true);
                } else {
                    //cardReleased button
                    getEssenceCont().cardPressed(3, false);
                }
            } else if (isPressed == true) {
                getPhysCont().Dash();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action4)) {//CM: Perform/Remap Action 4 __ AM: Use Character's Weapon
            if (modiferButton == false) {
                if (isPressed == true) {
                    //card pressed button
                    getEssenceCont().cardPressed(4, true);
                } else {
                    //cardReleased button
                    getEssenceCont().cardPressed(4, false);
                }
            } else if (isPressed == true) {
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action5)//CM: Change To Prev Target__AM:Dash/Roll Left
                && isPressed == true) {
            if (modiferButton == false) {
                getCameraCont().inputToggleLockOnTarget(false);
            } else {
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action6)//CM: Change To Next Target__AM:Dash/Roll Right
                && isPressed == true) {
            if (modiferButton == false) {
                getCameraCont().inputToggleLockOnTarget(true);
            } else {
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action7)) {// Lock On Hold:Zoom View
            if (isPressed == true) {
                getCameraCont().inputToggleLockOnTarget();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Action8)) {// Center Camera 
            if (isPressed == true) {
                getCameraCont().inputCenterCamera();
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.LeftTrigger)) {//CM: GatherFocus(whileStill) __ AM: Perform Special Maneuver
            //***Perform Special Manuever is a augmented form of Jump, Roll/Slide or Dash. It can be used as in chain with these to perform a Double Jump Roll/Slide or Dash
            if (modiferButton == false) {
                if (isPressed == true) {
                } else {
                }
            } else if (isPressed == true) {
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightTrigger)) {//CM: Hold: Enter Action Mode __ AM: Release: Enter Caster Mode  
            modiferButton = isPressed;//THIS IS AN IMPORTANT BUTTON IT DETERMINES IF 
            getPhysCont().setMod(modiferButton);
            System.out.println("SpectreInputController:" + name + " : " + isPressed);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadDown)) {//Show Explanation of Mapped Action 1
            if (isPressed == true) {
                //Show Action 1 Explanation - req Nifty
            } else {
                //Hide Action 1 Explanation - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadRight)) {//Show Explanation of Mapped Action 2
            if (isPressed == true) {
                //Show Action 2 Explanation - req Nifty
            } else {
                //Hide Action 2 Explanation - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadUp)) {//Show Explanation of Mapped Action 3
            if (isPressed == true) {
                //Show Action 3 Explanation - req Nifty
            } else {
                //Hide Action 3 Explanation - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.DPadLeft)) {//Show Explanation of Mapped Action 4
            if (isPressed == true) {
                //Show Action 4 Explanation - req Nifty
            } else {
                //Hide Action 4 Explanation - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Start)) {//Menu
            if (isPressed == true) {
                //Open Menu - req Nifty
            }
        } else if (name.equals(getPlayerName() + ":" + controlInputs.Back)) {//Reshuffle deck at start of battle
            if (isPressed == true) {
                //Some Special Operation
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
            getCameraCont().inputVRotateCamera(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickDown)) {//Rotate Camera Down
            getCameraCont().inputVRotateCamera(-value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickRight)) {//Rotate Camera Right
            getCameraCont().inputHRotateCamera(value / tpf);
        } else if (name.equals(getPlayerName() + ":" + controlInputs.RightThumbstickLeft)) {//Rotate Camera Left
            getCameraCont().inputHRotateCamera(-value / tpf);
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        SpectreInputController sic = new SpectreInputController();
        sic.setSpatial(spatial);
        return sic;
    }
}
