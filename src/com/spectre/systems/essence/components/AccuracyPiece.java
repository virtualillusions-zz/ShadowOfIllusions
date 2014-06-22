/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Characters current projectile accuracy 0 indicates normal and a positive or
 * negative value indicates a tenth percentile change
 *
 * @author Kyle D. Williams
 */
public class AccuracyPiece implements EntityComponent {

    private int ACC;

    public AccuracyPiece() {
        this(0);
    }

    public AccuracyPiece(int value) {
        ACC = value;
    }

    public int getAccuracy() {
        return ACC;
    }

    @Override
    public String toString() {
        return "Accuracy[" + ACC + "]";
    }
}
