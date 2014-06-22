/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.spectre.app.SpectreApplication;

/**
 *
 * @author Kyle Williams
 */
public class Buttons {

    public enum ControlInputs {

        /**
         * Caster Mode:Perform/Remap Action 1 <br><br> Action Mode:Jump
         */
        Action1,
        /**
         * Caster Mode:Perform/Remap Action 2 <br><br> Action Mode:Roll/Slide
         */
        Action2,
        /**
         * Caster Mode:Perform/Remap Action 3 <br><br> Action Mode:Dash
         */
        Action3,
        /**
         * Caster Mode:Perform/Remap Action 4 <br><br> Action Mode:Character
         * Auxiliary <br> Characters Secondary Attack ie shield, sword, Power-up
         */
        Action4,
        /**
         * Change To Previous Target
         */
        PrevTarget,
        /**
         * Change To Next Target
         */
        NextTarget,
        /**
         * TAP: LOCK ON/OFF
         */
        LockOn,
        /**
         * RESET CAMERA VIEW
         */
        CenterCamera,
        /**
         * Caster Mode:GatherFocus(While Still) <br><br> Action Mode:Perform
         * Special Maneuver
         */
        Alt,
        /**
         * HOLD: Enter Action Mode <br><br> Release:Enter Caster Mode
         */
        Mode,
        /**
         * Show Explanation of Mapped Action 1
         */
        Info1,
        /**
         * Show Explanation of Mapped Action 2
         */
        Info2,
        /**
         * Show Explanation of Mapped Action 3
         */
        Info3,
        /**
         * Show Explanation of Mapped Action 4
         */
        Info4,
        /**
         * Menu
         */
        Start,
        /**
         * Reshuffle Deck at start of battle
         */
        Back,
        /**
         * Move Character Towards Camera
         */
        CharacterBack,
        /**
         * Move Character Away From Camera
         */
        CharacterForward,
        /**
         * Move Character Right Of Camera
         */
        CharacterRight,
        /**
         * Move Character Left Of Camera
         */
        CharacterLeft,
        /**
         * Move Camera Down
         */
        CameraDown,
        /**
         * Move Camera Up
         */
        CameraUp,
        /**
         * Move Camera Right
         */
        CameraRight,
        /**
         * Move Camera Left
         */
        CameraLeft,;
    }

    //SIDE NOTE USE THIS AS PART OF SETUP CALL IN GAMESTATE
// <editor-fold defaultstate="collapsed" desc="keyboardDefaults">   
    public static void setUpRemote(InputManager manager, int playerNum, String playerName) {
        if (playerNum <= 0) {
            return;
        }
        manager.addMapping(playerName + ":" + ControlInputs.Action1, new KeyTrigger(KeyInput.KEY_1));
        manager.addMapping(playerName + ":" + ControlInputs.Action2, new KeyTrigger(KeyInput.KEY_2));
        manager.addMapping(playerName + ":" + ControlInputs.Action3, new KeyTrigger(KeyInput.KEY_3));
        manager.addMapping(playerName + ":" + ControlInputs.Action4, new KeyTrigger(KeyInput.KEY_4));
        manager.addMapping(playerName + ":" + ControlInputs.PrevTarget, new KeyTrigger(KeyInput.KEY_Q));
        manager.addMapping(playerName + ":" + ControlInputs.NextTarget, new KeyTrigger(KeyInput.KEY_E));
        manager.addMapping(playerName + ":" + ControlInputs.LockOn, new KeyTrigger(KeyInput.KEY_F));
        manager.addMapping(playerName + ":" + ControlInputs.CenterCamera, new KeyTrigger(KeyInput.KEY_R), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        manager.addMapping(playerName + ":" + ControlInputs.Back, new KeyTrigger(KeyInput.KEY_BACK));
        manager.addMapping(playerName + ":" + ControlInputs.Start, new KeyTrigger(KeyInput.KEY_RETURN));

        manager.addMapping(playerName + ":" + ControlInputs.CharacterForward, new KeyTrigger(KeyInput.KEY_W));
        manager.addMapping(playerName + ":" + ControlInputs.CharacterBack, new KeyTrigger(KeyInput.KEY_S));
        manager.addMapping(playerName + ":" + ControlInputs.CharacterLeft, new KeyTrigger(KeyInput.KEY_A));
        manager.addMapping(playerName + ":" + ControlInputs.CharacterRight, new KeyTrigger(KeyInput.KEY_D));

        manager.addMapping(playerName + ":" + ControlInputs.CameraDown, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        manager.addMapping(playerName + ":" + ControlInputs.CameraUp, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        manager.addMapping(playerName + ":" + ControlInputs.CameraLeft, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        manager.addMapping(playerName + ":" + ControlInputs.CameraRight, new MouseAxisTrigger(MouseInput.AXIS_X, false));

        manager.addMapping(playerName + ":" + ControlInputs.Alt, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        manager.addMapping(playerName + ":" + ControlInputs.Mode, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        manager.addMapping(playerName + ":" + ControlInputs.Info2, new KeyTrigger(KeyInput.KEY_RIGHT));
        manager.addMapping(playerName + ":" + ControlInputs.Info4, new KeyTrigger(KeyInput.KEY_LEFT));
        manager.addMapping(playerName + ":" + ControlInputs.Info3, new KeyTrigger(KeyInput.KEY_UP));
        manager.addMapping(playerName + ":" + ControlInputs.Info1, new KeyTrigger(KeyInput.KEY_DOWN));
    }
    // </editor-fold> 

// <editor-fold defaultstate="collapsed" desc="xbox360RemoteDefaults">
    public enum Xbox360 {

        A("0"),
        B("1"),
        X("2"),
        Y("3"),
        LeftShoulder("4"),
        RightShoulder("5"),
        Back("6"),
        Start("7"),
        LeftStick("8"),
        RightStick("9"),
        LeftThumbstickUpDownAxis("y"),
        LeftThumbstickLeftRightAxis("x"),
        RightThumbstickUpDownAxis("ry"),
        RightThumbstickLeftRightAxis("rx"),
        LeftRightTriggerAxis("z"),
        DPadRightLeftAxis("pov_x"),
        DPadUpDownAxis("pov_y");
        private final String i;

        Xbox360(String i) {
            this.i = i;

        }

        public String getID() {
            return i;
        }
    }

    public static void setUp360Remote(InputManager manager, int playerNum, String playerName) {
        if (playerNum <= 0) {
            return;
        } else if (manager.getJoysticks().length <= 0) {
            SpectreApplication.logger.severe("No Input Device Found");
            return;
        }

        Joystick js = manager.getJoysticks()[playerNum - 1];

        js.getButton(Xbox360.A.getID()).assignButton(playerName + ":" + ControlInputs.Action1);
        js.getButton(Xbox360.B.getID()).assignButton(playerName + ":" + ControlInputs.Action2);
        js.getButton(Xbox360.Y.getID()).assignButton(playerName + ":" + ControlInputs.Action3);
        js.getButton(Xbox360.X.getID()).assignButton(playerName + ":" + ControlInputs.Action4);
        js.getButton(Xbox360.LeftShoulder.getID()).assignButton(playerName + ":" + ControlInputs.PrevTarget);
        js.getButton(Xbox360.RightShoulder.getID()).assignButton(playerName + ":" + ControlInputs.NextTarget);
        js.getButton(Xbox360.LeftStick.getID()).assignButton(playerName + ":" + ControlInputs.LockOn);
        js.getButton(Xbox360.RightStick.getID()).assignButton(playerName + ":" + ControlInputs.CenterCamera);
        js.getButton(Xbox360.Back.getID()).assignButton(playerName + ":" + ControlInputs.Back);
        js.getButton(Xbox360.Start.getID()).assignButton(playerName + ":" + ControlInputs.Start);

        js.getAxis(Xbox360.LeftThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + ControlInputs.CharacterBack, playerName + ":" + ControlInputs.CharacterForward);
        js.getAxis(Xbox360.LeftThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + ControlInputs.CharacterRight, playerName + ":" + ControlInputs.CharacterLeft);
        js.getAxis(Xbox360.RightThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + ControlInputs.CameraDown, playerName + ":" + ControlInputs.CameraUp);
        js.getAxis(Xbox360.RightThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + ControlInputs.CameraRight, playerName + ":" + ControlInputs.CameraLeft);
        js.getAxis(Xbox360.LeftRightTriggerAxis.getID()).assignAxis(playerName + ":" + ControlInputs.Alt, playerName + ":" + ControlInputs.Mode);
        js.getAxis(Xbox360.DPadRightLeftAxis.getID()).assignAxis(playerName + ":" + ControlInputs.Info2, playerName + ":" + ControlInputs.Info4);
        js.getAxis(Xbox360.DPadUpDownAxis.getID()).assignAxis(playerName + ":" + ControlInputs.Info1, playerName + ":" + ControlInputs.Info3);

        manager.setAxisDeadZone(0.2f);
    }
    // </editor-fold>

    /**
     * Returns a full list of Button mappings
     *
     * @param playerName
     * @return
     */
    public static String[] getButtons(String playerName) {
        ControlInputs[] cI = Buttons.ControlInputs.values();
        String[] temp = new String[cI.length];
        for (int i = 0; i < cI.length; i++) {
            temp[i] = playerName + ":" + cI[i].toString();
        }
        return temp;
    }
}
