/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * A component to represent if the player is currently holding down the modifier
 * button on the controller
 *
 * @author Kyle D. Williams
 */
public class ModifierButtonPiece implements EntityComponent {

    private boolean modiferButton;

    public ModifierButtonPiece() {
        this(false);
    }

    public ModifierButtonPiece(boolean modiferButton) {
        this.modiferButton = modiferButton;
    }

    /**
     * Returns the button modifier boolean
     *
     * @return modiferButton boolean
     */
    public boolean getModifierButton() {
        return modiferButton;
    }
}
