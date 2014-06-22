/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;
import com.spectre.director.Director;

/**
 *
 * @author Kyle Williams
 */
public class CharacterController extends com.jme3.bullet.control.CharacterControl implements com.jme3.bullet.collision.PhysicsCollisionListener{
    public CharacterController(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
    } 

    public Spatial getSpatial() {return spatial;}

    public void collision(PhysicsCollisionEvent event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
