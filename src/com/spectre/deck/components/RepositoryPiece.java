/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.components;

import com.simsilica.es.PersistentComponent;

/**
 * <b>NickName:</b> Repository <br/>
 *
 * <b>Purpose:</b> Organizational Purposes<br/>
 *
 * <b>Description:</b> A RepositoryDeck is a class to store all cards owned by
 * the player. It tracks the amount of cards availble to use and that are
 * currently in use
 *
 * @author Kyle D. Williams
 */
public class RepositoryPiece implements PersistentComponent {

    private final int cardCount;
    private final int cardsUsed;

    public RepositoryPiece() {
        this(0, 0);
    }

    /**
     *
     * @param maxAmount the maximum amount of cards allowed to be used
     * @param amountLeft the amount of cards left to use
     */
    public RepositoryPiece(int maxAmount, int amountInUse) {
        cardCount = maxAmount;
        cardsUsed = amountInUse;
    }

    /**
     * The maximum amount of cards that can be used
     *
     * @return
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * The amount of cards currently in use
     *
     * @return
     */
    public int getCardsUsed() {
        return cardsUsed;
    }

    @Override
    public String toString() {
        return "RepositoryPiece[MaxAmount=" + getCardCount() + ", AmountInUse=" + getCardsUsed() + "]";
    }
}
