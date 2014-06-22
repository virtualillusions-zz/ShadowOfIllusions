/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Indicates the speed at which the character replenishes MP
 *
 * @author Kyle D. Williams
 */
public class ReplenishRatePiece implements EntityComponent {

    private float REPLENISH_RATE;
    /**
     * current Focus Increment Time is the value used to check against
     */
    private float cfiTime;

    public ReplenishRatePiece() {
        this(4f, 0f);
    }

    public ReplenishRatePiece(float value) {
        this.REPLENISH_RATE = value;
    }

    public ReplenishRatePiece(float replenishRate, float currentFocusIncrementTime) {
        this.REPLENISH_RATE = replenishRate;
        this.cfiTime = currentFocusIncrementTime;
    }

    public float getReplenishRate() {
        return REPLENISH_RATE;
    }

    public float getCurrentReplenishRate() {
        return cfiTime;
    }

    @Override
    public String toString() {
        return "ReplenishRate[ReplenishRate=" + REPLENISH_RATE + ", CurrentFocusIncrementTime=" + cfiTime + "]";
    }
}
