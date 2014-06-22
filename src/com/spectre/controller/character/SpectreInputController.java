/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author Kyle Williams
 */
public class SpectreInputController extends com.jme3.scene.control.AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    enum Buttons {

        MOVE_FORWARD,//////////////////////////////////////
        MOVE_BACKWARD,//////////////LEFT//////////////////
        MOVE_LEFT,///////////////ANALOG STICK////////////
        MOVE_RIGHT,/////////////////////////////////////

        CAMERA_UP,////////////////////////////////
        CAMERA_DOWN,/////////////////////////////
        CAMERA_LEFT,////////Right Analog Stick//
        CAMERA_RIGHT,//////////////////////////
        CENTERCAMERA,/////////////////////////

        ACTION1,//A,X:Perform/Remap Action 1,Jump
        ACTION2,//B,Circle:Perform/Remap Action 2,Roll Diagonal Left
        ACTION3,//Y,Triangle:Perform/Remap Action 3, Dash
        ACTION4,//X,Square:Perform/Remap Action 4,Roll Diagonal Right
        ACTION5,///LB,L1:Change Target, Strafe Left
        ACTION6,///LT,L2:Lock On Hold:Zoom View,Use Character's Special Weapon
        ACTION7,//LeftStickPressed,L3: Perform Special Manuever
        ACTION8,//RB,R1: Change To Previous Target,Strafe Right
        ACTION9,//RT,R2: Hold:Enter Action Mode, Release: Enter Caster Mode
        ACTION10,//Right Stick Pressed,R3: Reset Camera View

        BACK,//BACK,SELECT: Reshuffle deck at start of battle

        DOWN,//Show Explanation of Mapped Action 1
        RIGHT, //Show Explanation of Mapped Action 2
        UP,//Show Explanation of Mapped Action 3
        LEFT,//Show Explanation of Mapped Action 4

        START;//MENU
//        /**
//         * Returns the default buttonMappings
//         * @return
//         */
//        public static EnumMap<Buttons, Trigger[]> getDefaultKeys() {
//            EnumMap<Buttons, Trigger[]> defaultKeys = new EnumMap<Buttons, Trigger[]>(Buttons.class);
//            defaultKeys.put(Buttons.CAMERA_UP, new Trigger[]{
//                        new MouseAxisTrigger(MouseInput.AXIS_Y, false), new JoyAxisTrigger(0, 2, true)});
//            defaultKeys.put(Buttons.CAMERA_DOWN, new Trigger[]{
//                        new MouseAxisTrigger(MouseInput.AXIS_Y, true), new JoyAxisTrigger(0, 2, false)});
//            defaultKeys.put(Buttons.CAMERA_LEFT, new Trigger[]{
//                        new MouseAxisTrigger(MouseInput.AXIS_X, true), new JoyAxisTrigger(0, 3, true)});
//            defaultKeys.put(Buttons.CAMERA_RIGHT, new Trigger[]{
//                        new MouseAxisTrigger(MouseInput.AXIS_X, false), new JoyAxisTrigger(0, 3, false)});
//            defaultKeys.put(Buttons.CENTERCAMERA, new Trigger[]{
//                        new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE), new JoyButtonTrigger(0, 9)});
//            defaultKeys.put(Buttons.LOCKON, new Trigger[]{
//                        new MouseButtonTrigger(MouseInput.BUTTON_LEFT), new JoyButtonTrigger(0, 4)});
//            defaultKeys.put(Buttons.ATTACK1, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_1), new JoyButtonTrigger(0, 0)});
//            defaultKeys.put(Buttons.ATTACK2, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_2), new JoyButtonTrigger(0, 1)});
//            defaultKeys.put(Buttons.ATTACK3, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_3), new JoyButtonTrigger(0, 2)});
//            defaultKeys.put(Buttons.ATTACK4, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_4), new JoyButtonTrigger(0, 3)});
//            defaultKeys.put(Buttons.FORWARD, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_W), new JoyAxisTrigger(0, 0, true)});
//            defaultKeys.put(Buttons.BACKWARD, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_S), new JoyAxisTrigger(0, 0, false)});
//            defaultKeys.put(Buttons.LEFT, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_A), new JoyAxisTrigger(0, 1, true)});
//            defaultKeys.put(Buttons.RIGHT, new Trigger[]{
//                        new KeyTrigger(KeyInput.KEY_D), new JoyAxisTrigger(0, 1, false)});
//            defaultKeys.put(Buttons.SPECIALMANEUVER, new Trigger[]{
//                        new MouseButtonTrigger(MouseInput.BUTTON_RIGHT), new JoyButtonTrigger(0, 8)});
//            return defaultKeys;
//        }
    }
}
