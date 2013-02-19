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
}
