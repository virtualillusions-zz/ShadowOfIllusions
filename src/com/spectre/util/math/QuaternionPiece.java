/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util.math;

import com.simsilica.es.EntityComponent;

/**
 * Component represents a Quaternion
 *
 * @author Kyle D. Williams
 */
public class QuaternionPiece implements EntityComponent {

    protected float X;
    protected float Y;
    protected float Z;
    protected float W;

    public QuaternionPiece(float X, float Y, float Z, float W) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.W = W;
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

    /**
     * @return the W
     */
    public float getW() {
        return W;
    }

    @Override
    public String toString() {
        return "Quaternion[X=" + X + ", Y=" + Y + ", Z=" + Z + ", W" + W + "]";
    }
}
