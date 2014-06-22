/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * The Characters current usable MP
 *
 * @author Kyle D. Williams
 */
public class FocusPiece implements EntityComponent {

    private int FOCUS;

    public FocusPiece() {
        this(0);
    }

    public FocusPiece(int value) {
        FOCUS = value;
    }

    public int getFocus() {
        return FOCUS;
    }

    @Override
    public String toString() {
        return "FOCUS[" + FOCUS + "]";
    }
}
