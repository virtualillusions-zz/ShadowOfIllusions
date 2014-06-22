/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * The Characters current focus level which indicates the current maximum MP the
 * character can gather at one time
 *
 * @author Kyle D. Williams
 */
public class FocusLevelPiece implements EntityComponent {

    private int FOCUS_LEVEL;

    public FocusLevelPiece() {
        this(20);
    }

    public FocusLevelPiece(int value) {
        FOCUS_LEVEL = value;
    }

    public int getFocusLevel() {
        return FOCUS_LEVEL;
    }

    @Override
    public String toString() {
        return "FocusLevel[" + FOCUS_LEVEL + "]";
    }
}
