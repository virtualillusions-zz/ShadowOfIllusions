/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.util;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import java.util.EnumMap;

/**
 * specific API for allowed input controls
 * @author Kyle Williams
 */
public enum Buttons {       
        CAMERA_UP ,
        CAMERA_DOWN,
        CAMERA_LEFT,
        CAMERA_RIGHT,
        CENTERCAMERA,
        LOCKON,
        ATTACK1,
        ATTACK2,
        ATTACK3,
        ATTACK4,
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        SPECIALMANEUVER;
    
    /**
     * Returns the default buttonMappings
     * @return
     */
    public static EnumMap <Buttons,Trigger[]> getDefaultKeys(){
        EnumMap <Buttons,Trigger[]> defaultKeys = new EnumMap<Buttons,Trigger[]>(Buttons.class);
        defaultKeys.put(Buttons.CAMERA_UP,new Trigger[]{
             new MouseAxisTrigger(MouseInput.AXIS_Y,false),new JoyAxisTrigger(0, 2, true)});
        defaultKeys.put(Buttons.CAMERA_DOWN,new Trigger[]{
            new MouseAxisTrigger(MouseInput.AXIS_Y,true),new JoyAxisTrigger(0, 2, false)});
        defaultKeys.put(Buttons.CAMERA_LEFT,new Trigger[]{
            new MouseAxisTrigger(MouseInput.AXIS_X,true),new JoyAxisTrigger(0, 3, true)});
        defaultKeys.put(Buttons.CAMERA_RIGHT,new Trigger[]{
            new MouseAxisTrigger(MouseInput.AXIS_X,false),new JoyAxisTrigger(0, 3, false)});
        defaultKeys.put(Buttons.CENTERCAMERA,new Trigger[]{
            new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE),new JoyButtonTrigger(0, 9)});
        defaultKeys.put(Buttons.LOCKON,new Trigger[]{
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT),new JoyButtonTrigger(0, 4)});
        defaultKeys.put(Buttons.ATTACK1,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_1),new JoyButtonTrigger(0, 0)});
        defaultKeys.put(Buttons.ATTACK2,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_2),new JoyButtonTrigger(0, 1)});
        defaultKeys.put(Buttons.ATTACK3,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_3),new JoyButtonTrigger(0, 2)});
        defaultKeys.put(Buttons.ATTACK4,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_4),new JoyButtonTrigger(0, 3)});
        defaultKeys.put(Buttons.FORWARD,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_W),new JoyAxisTrigger(0, 0, true)});
        defaultKeys.put(Buttons.BACKWARD,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_S),new JoyAxisTrigger(0, 0, false)});
        defaultKeys.put(Buttons.LEFT,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_A),new JoyAxisTrigger(0, 1, true)});
        defaultKeys.put(Buttons.RIGHT,new Trigger[]{
            new KeyTrigger(KeyInput.KEY_D),new JoyAxisTrigger(0, 1, false)});
        defaultKeys.put(Buttons.SPECIALMANEUVER,new Trigger[]{
            new MouseButtonTrigger(MouseInput.BUTTON_RIGHT),new JoyButtonTrigger(0, 8)});
        return defaultKeys;
    }
}
