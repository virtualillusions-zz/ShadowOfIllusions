/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck;

import java.util.LinkedList;

/**
 *
 * @author Kyle Williams
 */
public final class Hand {

    /**
     * The Cards that the player can use
     */
    private final String[] cards;
    /**
     * The amount of times those cards have been used
     */
    private final Integer[] uses;

    public Hand() {
        cards = new String[4];
        uses = new Integer[4];
        emptyHand();
    }

    public String getCard(int cardNum) {
        return cards[cardNum];
    }

    /**
     * Used to increase use counter, which is used to check against max use to
     * determine if the card should be removed from hand
     *
     * @param cardNum
     * @return
     */
    public int increaseUses(int cardNum) {
        uses[cardNum] = uses[cardNum] + 1;
        return uses[cardNum];
    }

    public void setCard(int cardNum, String newCard) {
        cards[cardNum] = newCard;
        uses[cardNum] = 0;
    }

    /**
     * Shuffles The Deck of cards including the contents of the hand
     *
     * @param deck
     */
    public void shuffleDeck(LinkedList<String> deck) {
        //don't push null elements
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null && !cards[i].equals("")) {
                deck.push(cards[i]);
            }
        }

        java.util.Collections.shuffle(deck);//shuffle so elements are random

        cards[0] = deck.pop();
        cards[1] = deck.pop();
        cards[2] = deck.pop();
        cards[3] = deck.pop();
    }

    /**
     * Quick check to see if the player has a particular card in hand
     *
     * @param s
     * @return
     */
    public boolean hasCard(String cardName) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].equals(cardName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Return card  position, if the card isn't in hand return -1
     * @param s
     * @return
     */
    public int getCardPosition(String cardName) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].equals(cardName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes all cards from hand and reset use counter
     */
    public void emptyHand() {
        for (int i = 0; i < cards.length; i++) {
            cards[i] = "";
            uses[i] = 0;
        }
    }
}
