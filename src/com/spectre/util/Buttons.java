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
import com.spectre.director.Director;

/**
 *
 * @author Kyle Williams
 */
public class Buttons {

    public enum controlInputs {

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
         * Caster Mode:Change To Previous Target
         */
        Action5,
        /**
         * Caster Mode:Change To Next Target
         */
        Action6,
        /**
         * TAP: LOCK ON/OFF
         */
        Action7,
        /**
         * RESET CAMERA VIEW
         */
        Action8,
        /**
         * Caster Mode:GatherFocus(While Still) <br><br> Action Mode:Perform
         * Special Maneuver
         */
        LeftTrigger,
        /**
         * HOLD: Enter Action Mode <br><br> Release:Enter Caster Mode
         */
        RightTrigger,
        /**
         * Show Explanation of Mapped Action 1
         */
        DPadDown,
        /**
         * Show Explanation of Mapped Action 2
         */
        DPadRight,
        /**
         * Show Explanation of Mapped Action 3
         */
        DPadUp,
        /**
         * Show Explanation of Mapped Action 4
         */
        DPadLeft,
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
        LeftThumbstickDown,
        /**
         * Move Character Away From Camera
         */
        LeftThumbstickUp,
        /**
         * Move Character Right Of Camera
         */
        LeftThumbstickRight,
        /**
         * Move Character Left Of Camera
         */
        LeftThumbstickLeft,
        /**
         * Move Camera Down
         */
        RightThumbstickDown,
        /**
         * Move Camera Up
         */
        RightThumbstickUp,
        /**
         * Move Camera Right
         */
        RightThumbstickRight,
        /**
         * Move Camera Left
         */
        RightThumbstickLeft,;
    }

    //SIDE NOTE USE THIS AS PART OF SETUP CALL IN GAMESTATE
// <editor-fold defaultstate="collapsed" desc="keyboardDefaults">   
    public static void setUpRemote(InputManager manager, int playerNum, String playerName) {
        if (playerNum <= 0) {
            return;
        }
        manager.addMapping(playerName + ":" + controlInputs.Action1, new KeyTrigger(KeyInput.KEY_1));
        manager.addMapping(playerName + ":" + controlInputs.Action2, new KeyTrigger(KeyInput.KEY_2));
        manager.addMapping(playerName + ":" + controlInputs.Action3, new KeyTrigger(KeyInput.KEY_3));
        manager.addMapping(playerName + ":" + controlInputs.Action4, new KeyTrigger(KeyInput.KEY_4));
        manager.addMapping(playerName + ":" + controlInputs.Action5, new KeyTrigger(KeyInput.KEY_Q));
        manager.addMapping(playerName + ":" + controlInputs.Action6, new KeyTrigger(KeyInput.KEY_E));
        manager.addMapping(playerName + ":" + controlInputs.Action7, new KeyTrigger(KeyInput.KEY_F));
        manager.addMapping(playerName + ":" + controlInputs.Action8, new KeyTrigger(KeyInput.KEY_R), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        manager.addMapping(playerName + ":" + controlInputs.Back, new KeyTrigger(KeyInput.KEY_BACK));
        manager.addMapping(playerName + ":" + controlInputs.Start, new KeyTrigger(KeyInput.KEY_RETURN));

        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickUp, new KeyTrigger(KeyInput.KEY_W));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickDown, new KeyTrigger(KeyInput.KEY_S));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickLeft, new KeyTrigger(KeyInput.KEY_A));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickRight, new KeyTrigger(KeyInput.KEY_D));

        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickDown, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickUp, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickLeft, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickRight, new MouseAxisTrigger(MouseInput.AXIS_X, false));

        manager.addMapping(playerName + ":" + controlInputs.LeftTrigger, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        manager.addMapping(playerName + ":" + controlInputs.RightTrigger, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        manager.addMapping(playerName + ":" + controlInputs.DPadRight, new KeyTrigger(KeyInput.KEY_RIGHT));
        manager.addMapping(playerName + ":" + controlInputs.DPadLeft, new KeyTrigger(KeyInput.KEY_LEFT));
        manager.addMapping(playerName + ":" + controlInputs.DPadUp, new KeyTrigger(KeyInput.KEY_UP));
        manager.addMapping(playerName + ":" + controlInputs.DPadDown, new KeyTrigger(KeyInput.KEY_DOWN));
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

        private String getID() {
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

        js.getButton(Xbox360.A.getID()).assignButton(playerName + ":" + controlInputs.Action1);
        js.getButton(Xbox360.B.getID()).assignButton(playerName + ":" + controlInputs.Action2);
        js.getButton(Xbox360.Y.getID()).assignButton(playerName + ":" + controlInputs.Action3);
        js.getButton(Xbox360.X.getID()).assignButton(playerName + ":" + controlInputs.Action4);
        js.getButton(Xbox360.LeftShoulder.getID()).assignButton(playerName + ":" + controlInputs.Action5);
        js.getButton(Xbox360.RightShoulder.getID()).assignButton(playerName + ":" + controlInputs.Action6);
        js.getButton(Xbox360.LeftStick.getID()).assignButton(playerName + ":" + controlInputs.Action7);
        js.getButton(Xbox360.RightStick.getID()).assignButton(playerName + ":" + controlInputs.Action8);
        js.getButton(Xbox360.Back.getID()).assignButton(playerName + ":" + controlInputs.Back);
        js.getButton(Xbox360.Start.getID()).assignButton(playerName + ":" + controlInputs.Start);

        js.getAxis(Xbox360.LeftThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftThumbstickDown, playerName + ":" + controlInputs.LeftThumbstickUp);
        js.getAxis(Xbox360.LeftThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftThumbstickRight, playerName + ":" + controlInputs.LeftThumbstickLeft);
        js.getAxis(Xbox360.RightThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.RightThumbstickDown, playerName + ":" + controlInputs.RightThumbstickUp);
        js.getAxis(Xbox360.RightThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + controlInputs.RightThumbstickRight, playerName + ":" + controlInputs.RightThumbstickLeft);
        js.getAxis(Xbox360.LeftRightTriggerAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftTrigger, playerName + ":" + controlInputs.RightTrigger);
        js.getAxis(Xbox360.DPadRightLeftAxis.getID()).assignAxis(playerName + ":" + controlInputs.DPadRight, playerName + ":" + controlInputs.DPadLeft);
        js.getAxis(Xbox360.DPadUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.DPadDown, playerName + ":" + controlInputs.DPadUp);

        manager.setAxisDeadZone(0.2f);
    }
    // </editor-fold>

    /**
     * Returns a full list of Button mappings 
     * @param playerName
     * @return 
     */
    public static String[] getButtons(String playerName) {
        controlInputs[] cI = Buttons.controlInputs.values();
        String[] temp = new String[cI.length];
        for (int i = 0; i < cI.length; i++) {
            temp[i] =  playerName + ":"+cI[i].toString();
        }
        return temp;
    }
}
