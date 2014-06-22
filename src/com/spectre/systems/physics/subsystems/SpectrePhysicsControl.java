/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.physics.subsystems;

import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.PhysicsControl;

/**
 * <code>SpectrePhysicsControl</code> used to identify classes with physics
 * capabilities
 *
 * <br>
 *
 * Convenience interface used to condense Physics implementation classes
 *
 * @author Kyle D. Williams
 */
public interface SpectrePhysicsControl extends PhysicsControl, PhysicsTickListener, PhysicsCollisionListener {
}
