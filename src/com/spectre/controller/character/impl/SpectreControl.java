/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 * An interface for character controls. <p>
 * <code>SpectreControl</code>s are used to specify certain update and render
 * logic for a {@link Spatial}.
 *
 * @author Kyle Williams
 */
public interface SpectreControl extends Control {

    /**
     * Final Stage before the Game starts. All Controls are added at this point
     */
    public void startUp();

    /**
     * Called after the end of a Game
     */
    public void cleanUp();
    
    /**
     * This is a required call when changing controls. The idea behind creating
     * interfaces for each class allows subclasses of various controls not just
     * the basic ones which makes the system more robust
     * Note: Consider hijacking update statement and actively checking spatial for changes
     */
    public void ControlChanged();
}
