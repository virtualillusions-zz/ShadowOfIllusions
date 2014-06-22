/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.deck.components;

import com.simsilica.es.EntityComponent;

/**
 * <b>Purpose:</b> In Game/Match Purposes<br/>
 *
 * <b>Description:</b> Used during match in order to to keep track of cards
 * currently in hand It determines typically from [0-3] the position of the card
 * in hand
 *
 * @author Kyle D. Williams
 */
public class HandPiece implements EntityComponent {

    private final int position;

    /**
     * Position typically from [0-3] which determines the position in the card
     * is in hand
     *
     * @param position
     */
    public HandPiece(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "HandPiece[Position=" + getPosition() + "]";
    }
}
