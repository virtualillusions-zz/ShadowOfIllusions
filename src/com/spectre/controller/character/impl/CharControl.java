/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;

/**
 *
 * @author Kyle Williams
 */
public interface CharControl extends SpectreControl, PhysicsTickListener, PhysicsCollisionListener {

    /**
     * Directional value used to determine characters movement Anything above 2
     * is normal moving speed pos_Value: Forward neg_Value: Backward
     *
     * @param value
     */
    public void moveForward(float value);

    /**
     * Directional value used to determine characters movement Anything above 2
     * is normal moving speed pos_Value: Left neg_Value: Right
     *
     * @param value
     */
    public void moveLeft(float value);

    /**
     * Makes the character jump with the set jump force.
     */
    public void jump();

    /**
     * Quickly evade roll/slide in a specific direction
     */
    public void evade();

    /**
     * Returns if the character is in air
     */
    public boolean isInAir();

    /**
     * returns if the character is falling
     */
    public boolean isFalling();

    /**
     * returns if the character is jumping
     */
    public boolean isJumping();

    /**
     * Quickly rush in a specific direction
     */
    public void dash();

    /**
     * getSpatial retrieves the spatial object of the entity.
     *
     * @return
     */
    public Spatial getSpatial();
}
