/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 *
 * @author Kyle Williams
 */
public class Buttons {

    public enum controlInputs {
        //Controls                 Caster Mode           Action Mode

        Action1,//            Perform/Remap Action 1        Jump
        Action2,//            Perform/Remap Action 2     Roll/Slide
        Action3,//            Perform/Remap Action 3        Dash
        Action4,//            Perform/Remap Action 4     Use Character Weapon
        Action5,//            Change To Prev Target       Dash/Roll Left
        Action6,//            Change To Next Target       Dash/Roll Right
        Action7,//            GatherFocus(While Still)   Perform Special Manuever
        Action8,//                          RESET CAMERA VIEW
        Back,//                       Reshuffle Deck at start of battle
        Start,//                                    Menu
        LeftThumbstickDown,//                 Move Character
        LeftThumbstickUp,//                         ^
        LeftThumbstickRight,//                      ^
        LeftThumbstickLeft,//                       ^
        RightThumbstickDown,//                Move Camera
        RightThumbstickUp,//                        ^
        RightThumbstickRight,//                     ^
        RightThumbstickLeft,//                      ^
        LeftTrigger,//                      LOCK ON HOLD:Zoom View
        RightTrigger,//      HOLD: Enter Action Mode      Release:Enter Caster Mode
        DPadRight,//                    Show Explanation of Mapped Action 2
        DPadLeft,//                     Show Explanation of Mapped Action 4
        DPadUp,//                       Show Explanation of Mapped Action 3
        DPadDown;//                     Show Explanation of Mapped Action 1
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
        manager.addMapping(playerName + ":" + controlInputs.Action8, new KeyTrigger(KeyInput.KEY_R));
        manager.addMapping(playerName + ":" + controlInputs.Back, new KeyTrigger(KeyInput.KEY_BACK));
        manager.addMapping(playerName + ":" + controlInputs.Start, new KeyTrigger(KeyInput.KEY_RETURN));

        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickDown, new KeyTrigger(KeyInput.KEY_S));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickUp, new KeyTrigger(KeyInput.KEY_W));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickLeft, new KeyTrigger(KeyInput.KEY_A));
        manager.addMapping(playerName + ":" + controlInputs.LeftThumbstickRight, new KeyTrigger(KeyInput.KEY_D));

        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickDown, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickUp, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickLeft, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        manager.addMapping(playerName + ":" + controlInputs.RightThumbstickRight, new MouseAxisTrigger(MouseInput.AXIS_X, true));

        manager.addMapping(playerName + ":" + controlInputs.LeftTrigger, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        manager.addMapping(playerName + ":" + controlInputs.RightTrigger, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        manager.addMapping(playerName + ":" + controlInputs.DPadRight, new KeyTrigger(KeyInput.KEY_7));
        manager.addMapping(playerName + ":" + controlInputs.DPadLeft, new KeyTrigger(KeyInput.KEY_8));
        manager.addMapping(playerName + ":" + controlInputs.DPadUp, new KeyTrigger(KeyInput.KEY_9));
        manager.addMapping(playerName + ":" + controlInputs.DPadDown, new KeyTrigger(KeyInput.KEY_0));
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
            return;//@TODO remember to add some sort of exception or logger here
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

        js.getAxis(Xbox360.LeftThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftThumbstickUp, playerName + ":" + controlInputs.LeftThumbstickDown);
        js.getAxis(Xbox360.LeftThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftThumbstickRight, playerName + ":" + controlInputs.LeftThumbstickLeft);
        js.getAxis(Xbox360.RightThumbstickUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.RightThumbstickDown, playerName + ":" + controlInputs.RightThumbstickUp);
        js.getAxis(Xbox360.RightThumbstickLeftRightAxis.getID()).assignAxis(playerName + ":" + controlInputs.RightThumbstickLeft, playerName + ":" + controlInputs.RightThumbstickRight);
        js.getAxis(Xbox360.LeftRightTriggerAxis.getID()).assignAxis(playerName + ":" + controlInputs.LeftTrigger, playerName + ":" + controlInputs.RightTrigger);
        js.getAxis(Xbox360.DPadRightLeftAxis.getID()).assignAxis(playerName + ":" + controlInputs.DPadRight, playerName + ":" + controlInputs.DPadLeft);
        js.getAxis(Xbox360.DPadUpDownAxis.getID()).assignAxis(playerName + ":" + controlInputs.DPadUp, playerName + ":" + controlInputs.DPadDown);

        manager.setAxisDeadZone(0.2f);
    }
    // </editor-fold>

    public static String[] getAction(String playerName) {
        return new String[]{playerName + ":Action1", playerName + ":Action2", playerName + ":Action3", playerName + ":Action4", playerName + ":Action5",
                    playerName + ":Action6", playerName + ":Action7", playerName + ":Action8", playerName + ":Back", playerName + ":Start"};
    }

    public static String[] getAnalog(String playerName) {
        return new String[]{playerName + ":LeftThumbstickDown", playerName + ":LeftThumbstickUp",
                    playerName + ":LeftThumbstickRight", playerName + ":LeftThumbstickLeft",
                    playerName + ":RightThumbstickDown", playerName + ":RightThumbstickUp",
                    playerName + ":RightThumbstickRight", playerName + ":RightThumbstickLeft",
                    playerName + ":LeftTrigger", playerName + ":RightTrigger",
                    playerName + ":DPadRight", playerName + ":DPadLeft",
                    playerName + ":DPadUp", playerName + ":DPadDown"};
    }

    public static String[] getBoth(String playerName) {
        String[] ac = getAction(playerName);
        String[] an = getAnalog(playerName);
        String[] both = new String[ac.length + an.length];
        System.arraycopy(ac, 0, both, 0, ac.length);
        System.arraycopy(an, 0, both, ac.length, an.length);
        return both;
    }
}
