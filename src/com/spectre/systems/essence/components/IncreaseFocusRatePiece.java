/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Boolean used to check if focus rate should be increased and by how much
 *
 * @author Kyle D. Williams
 */
public class IncreaseFocusRatePiece implements EntityComponent {

    private boolean isIncreaseFocusRate;
    private float increasedRate;
 
    public IncreaseFocusRatePiece() {
        this(false, 1.005f);
    }

    public IncreaseFocusRatePiece(boolean increaseFocusRate) {
        this(increaseFocusRate, 1.005f);
    }

    public IncreaseFocusRatePiece(float increasedRate) {
        this(true, increasedRate);
    }

    public IncreaseFocusRatePiece(boolean increaseFocusRate, float increasedRate) {
        this.isIncreaseFocusRate = increaseFocusRate;
        this.increasedRate = increasedRate;
    }

    public boolean isIncreasedFocusRate() {
        return isIncreaseFocusRate;
    }

    public float getIncreasedFocusRate() {
        return increasedRate;
    }

    @Override
    public String toString() {
        return "IncreaseFocusRatePiece[increaseFocusRate=" + isIncreaseFocusRate + ", increasedRate=" + increasedRate + "]";
    }
}
