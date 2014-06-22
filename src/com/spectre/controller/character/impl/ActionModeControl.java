/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

/**
 * A control used to identify controls that require knowledge of the state of
 * the character either caster or action mode
 *
 * @author Kyle
 */
public interface ActionModeControl {

    /**
     * Changes the Current Mode of the controller for the character
     * CasterMode=False<br/> ActionMode=true
     *
     *
     * @param modiferButton
     */
    public void setActionMode(boolean modiferButton);
}
