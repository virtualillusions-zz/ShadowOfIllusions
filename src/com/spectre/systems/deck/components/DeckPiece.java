/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.deck.components;

import com.simsilica.es.EntityComponent;

/**
 * <b>Purpose:</b> In Game/Match Purposes<br/>
 *
 * <b>Description:</b> Used during match to keep track of card entities that can
 * be used and that are in the graveyard
 *
 * @author Kyle D. Williams
 */
public class DeckPiece implements EntityComponent {

    private final boolean inGraveyard;

    public DeckPiece() {
        this(false);
    }

    public DeckPiece(boolean inGraveYard) {
        this.inGraveyard = inGraveYard;
    }

    public boolean isInGraveyard() {
        return inGraveyard;
    }

    @Override
    public String toString() {
        return "DeckPiece[InGraveyard=" + isInGraveyard() + "]";
    }
}
