/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.renderer.Camera;

/**
 *
 * @author Kyle Williams
 */
public interface CamControl extends SpectreControl{

    public Camera getCamera();

    public void inputToggleLockOnTarget(boolean b);

    public void inputToggleLockOnTarget();

    public void inputCenterCamera();

    public void inputVRotateCamera(float value);

    public void inputHRotateCamera(float value);
    
}
