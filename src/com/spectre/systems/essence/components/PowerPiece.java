/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Characters current attack power<br/> 0 indicates normal and a positive or
 * negative value indicates a tenth percentile change
 *
 * @author Kyle D. Williams
 */
public class PowerPiece implements EntityComponent {

    private int POWER;

    public PowerPiece() {
        this(0);
    }

    public PowerPiece(int value) {
        POWER = value;
    }

    public int getPower() {
        return POWER;
    }

    @Override
    public String toString() {
        return "Power[" + POWER + "]";
    }
}
