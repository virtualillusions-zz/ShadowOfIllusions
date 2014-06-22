/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.visual.components;

import com.simsilica.es.EntityComponent;

/**
 * A common component to represent if the player is currently holding down the
 * modifier button on the controller
 *
 * @author Kyle D. Williams
 */
public class ActionModePiece implements EntityComponent {

    private boolean actionMode;

    public ActionModePiece() {
        this(false);
    }

    public ActionModePiece(boolean actionMode) {
        this.actionMode = actionMode;
    }

    /**
     * Returns the button modifier boolean
     *
     * @return actionMode boolean
     */
    public boolean isActionMode() {
        return actionMode;
    }

    @Override
    public String toString() {
        return "ActionModePiece[actionMode=" + actionMode + "]";
    }
}
