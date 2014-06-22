/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input.subsystems;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.spectre.app.input.Buttons.ControlInputs;
import deprecated.com.spectre.input.components.ModifierButtonPiece;

/**
 *
 * @author Kyle Williams
 */
public class BasicInputHandler extends InputHandler {

    @Override
    public void onAction(Entity entity, String name, boolean isPressed, float tpf) {
        EntityId id = entity.getId();
        if (name.equals(id + ":" + ControlInputs.Action1)) {//CM: Perform/Remap Action 1 __ AM: Jump
//            if (modiferButton == false) {
//                //isPressed card pressed/released
//                getEssenceCont().cardPressed(0, isPressed);
//            } else if (isPressed == true) {
//                getPhysCont().jump();
//            }
        } else if (name.equals(id + ":" + ControlInputs.Action2)) {//CM: Perform/Remap Action 2 __ AM: evade/Slide 
//            if (modiferButton == false) {
//                //isPressed card pressed/released
//                getEssenceCont().cardPressed(1, isPressed);
//            } else if (isPressed == true) {
//                getPhysCont().evade();
//            }
        } else if (name.equals(id + ":" + ControlInputs.Action3)) {//CM: Perform/Remap Action 3 __ AM: dash
//            if (modiferButton == false) {
//                //isPressed card pressed/released
//                getEssenceCont().cardPressed(2, isPressed);
//            } else if (isPressed == true) {
//                getPhysCont().dash();
//            }
        } else if (name.equals(id + ":" + ControlInputs.Action4)) {//CM: Perform/Remap Action 4 __ AM: Character's Auxillary
//            if (modiferButton == false) {
//                //isPressed card pressed/released
//                getEssenceCont().cardPressed(3, isPressed);
//            } else if (isPressed == true) {
//                //TODO: AUXILLARY
//            }
        } else if (name.equals(id + ":" + ControlInputs.PrevTarget)//CM: Change To Prev Target
                && isPressed == true) {
//            getCameraCont().inputToggleLockOnTarget(false);
        } else if (name.equals(id + ":" + ControlInputs.NextTarget)//CM: Change To Next Target
                && isPressed == true) {
//            getCameraCont().inputToggleLockOnTarget(true);
        } else if (name.equals(id + ":" + ControlInputs.LockOn)// Lock On/OFF
                && isPressed == true) {
//            getCameraCont().inputToggleLockOnTarget();
        } else if (name.equals(id + ":" + ControlInputs.CenterCamera)// Center Camera 
                && isPressed == true) {
//            getCameraCont().inputCenterCamera();
        } else if (name.equals(id + ":" + ControlInputs.Alt)) {//CM: GatherFocus(whileStill) __ AM: Perform Special Maneuver
//            //***Perform Special Manuever is a augmented form of Jump, evade/Slide or dash. It can be used as in chain with these to perform a Double Jump evade/Slide or dash
//            if (modiferButton == false) {
//                getEssenceCont().gatherFocus(isPressed);
//            } else if (isPressed == true) {
//                //TODO: Special Movement
//            }
        } else if (name.equals(id + ":" + ControlInputs.Mode)) {//CM: Hold: Enter Action Mode __ AM: Release: Enter Caster Mode 
            entity.set(new ModifierButtonPiece(isPressed));
        } else if (name.equals(id + ":" + ControlInputs.Info1)) {//Show/Hide Explanation of Mapped Action 1
//            getEssenceCont().cardInfo(1, isPressed);
        } else if (name.equals(id + ":" + ControlInputs.Info2)) {//Show/Hide Explanation of Mapped Action 2
//            getEssenceCont().cardInfo(2, isPressed);
        } else if (name.equals(id + ":" + ControlInputs.Info3)) {//Show/Hide Explanation of Mapped Action 3
//            getEssenceCont().cardInfo(3, isPressed);
        } else if (name.equals(id + ":" + ControlInputs.Info4)) {//Show/Hide Explanation of Mapped Action 4
//            getEssenceCont().cardInfo(4, isPressed);
        } else if (name.equals(id + ":" + ControlInputs.Start)) {//Menu
//            if (isPressed == true) {
//                //TODO: Open Menu - req Nifty
//            }
        } else if (name.equals(id + ":" + ControlInputs.Back)) {//Reshuffle deck at start of battle
//            if (isPressed == true) {
//                getEssenceCont().reshuffleDeck();
//            }
        }
    }

    @Override
    public void onAnalog(Entity entity, String name, float value, float tpf) {
        EntityId id = entity.getId();
        if (name.equals(id + ":" + ControlInputs.CharacterForward)) {//Move Character Up  
            // getPhysCont().moveForward(value / tpf);
        } else if (name.equals(id + ":" + ControlInputs.CharacterBack)) {//Move Character Back   
            // getPhysCont().moveForward(-(value / tpf));
        } else if (name.equals(id + ":" + ControlInputs.CharacterLeft)) {//Move Character  Right           
            // getPhysCont().moveLeft(value / tpf);
        } else if (name.equals(id + ":" + ControlInputs.CharacterRight)) {//Move Character Left
            // getPhysCont().moveLeft(-(value / tpf));
        } else if (name.equals(id + ":" + ControlInputs.CameraUp)) {//Rotate Camera Up
            // getCameraCont().inputVRotateCamera(value);
        } else if (name.equals(id + ":" + ControlInputs.CameraDown)) {//Rotate Camera Down
            //  getCameraCont().inputVRotateCamera(-value);
        } else if (name.equals(id + ":" + ControlInputs.CameraRight)) {//Rotate Camera Right
            //  getCameraCont().inputHRotateCamera(value);
        } else if (name.equals(id + ":" + ControlInputs.CameraLeft)) {//Rotate Camera Left
            // getCameraCont().inputHRotateCamera(-value);
        }
    }
}
