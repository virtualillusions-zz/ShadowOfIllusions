/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util.math;

import com.simsilica.es.EntityComponent;

/**
 * Component represents a Vector3f
 *
 * @author Kyle D. Williams
 */
public class Vector3fPiece implements EntityComponent {

    protected float X;
    protected float Y;
    protected float Z;

    public Vector3fPiece(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    /**
     * @return the X
     */
    public float getX() {
        return X;
    }

    /**
     * @return the Y
     */
    public float getY() {
        return Y;
    }

    /**
     * @return the Z
     */
    public float getZ() {
        return Z;
    }

    @Override
    public String toString() {
        return "Vector3f[X=" + X + ", Y=" + Y + " Z=" + Z + "]";
    }
}
