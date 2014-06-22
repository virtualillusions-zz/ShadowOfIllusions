/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 *
 * @author Kyle Williams
 */
public interface AnimationControl extends SpectreControl, AnimEventListener {

    /**
     * Should be implemented only by enums to indicate common animations such as
     * walk or jump.
     */
    public interface BasicAnimations {

        public String getAnimationName();
    }

    public enum AnimPriority {

        /**
         * Animation can change anytime Applies to: Idle, Basic Movements
         */
        Level_1,
        /**
         * Animation can only change with a call to interrupt Applies to:
         * Special Movements, Cards
         */
        Level_2,
        /**
         * Animation cannot be interrupted Applies to: Debug, Cinematic
         */
        Level_3;
    }

    /**
     * <pre>
     * Set Character Resting Animation
     * IDEALLY Bound from 1-3
     * 1.healthy
     * 2.tired
     * 3.injured
     * </pre>
     *
     * @param state
     */
    public void changeCharState(int characterState);

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName The name of the animation to play
     * @param animPriority The priority of the animation to incoming requests
     * @return boolean indicating if new animation set
     */
    public boolean changeAnimation(String animName, AnimPriority animPriority);

    /**
     * Change currently playing animation if priority is higher
     *
     * <b> Note:</b> For common Animations to avoid possible issues, create a
     * class or enum to define all common animations which implement
     * BasicAnimations
     *
     * @param animName
     * @param animPriority
     * @param blendTime
     * @return boolean indicating if new animation set
     */
    public boolean changeAnimation(String animName, AnimPriority animPriority, float blendTime);

    /**
     * Set additional animation properties for refined control.
     *
     * @param blendTime
     * @param speed
     * @param startTime
     * @param loopMode
     */
    public void setAnimationProperties(LoopMode loopMode, float speed, float startTime);

    /**
     * Intended to initiate special instructions after the end of an animation
     * cycle
     *
     * <b>Note:</b> ideally should only be one call per animation to this method
     * to prevent possible issues
     *
     * @param call Callable called from animation listener at the end of an
     * animation
     */
    public void setFuture(Callable call);

    /**
     * Sets the animation lock preventing the user from changing animation until
     * the currently playing animation is completed
     *
     * Note: Should be called after changeAnimation
     */
    public void setAnimLock();

    /**
     * If current animation priority is Level 2 or lower, interrupts animation
     * and sets lock to false allowing a new animation to be played
     *
     * Note: Should be called before changeAnimation
     */
    public void interrupt();

    /**
     * Check to see if user is locked out from changing animation
     *
     * @return lock
     */
    public boolean isAnimLocked();

    /**
     * A list of available bones
     *
     * @return a list of bones
     */
    public ArrayList<String> getBoneList();
}
