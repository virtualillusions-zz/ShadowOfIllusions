/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.camera.components;

import com.jme3.renderer.Camera;
import com.spectre.util.math.Vector3fPiece;

/**
 * <code>getDirection</code> retrieves the direction vector the camera is
 * facing.
 *
 * @return the direction the camera is facing.
 * @see Camera#getDirection()
 * @author Kyle D. Williams
 */
public class CameraDirectionPiece extends Vector3fPiece {

    public CameraDirectionPiece() {
        this(0, 0, 0);
    }

    public CameraDirectionPiece(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public String toString() {
        return "CameraDirectionPiece[" + super.toString() + "]";
    }
}
