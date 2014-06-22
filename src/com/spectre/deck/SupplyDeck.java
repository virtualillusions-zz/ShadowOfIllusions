/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import com.spectre.deck.CardStats.CardSeries;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A Concurrent Hashmap used to store Card Objects in an organized and logical manner
 * @author Kyle Williams
 */
public class SupplyDeck extends java.util.concurrent.ConcurrentHashMap<String, Card> {

    /**
     * Maps the specified Card to the table. The Card value cannot be be null.
     *   
     * <p> The value can be retrieved by calling the <tt>get</tt> method with
     * a key that is equal to the original name of the card.
     *
     * @param card value to associate with the table
     * @return the previous value associated with the <tt>name of the card</tt>,
     * or <tt>null</tt> if there was no mapping for the <tt>name of the
     * card</tt>
     * @throws NullPointerException if the specified card is null
     */
    public Card put(Card card) {
        if (card.getName() == null || card.getName().equals("")) {
            throw new NullPointerException("The Card to be added does not have a proper name."
                    + "\n As such it cannot be mapped to this table");
        }
        return super.put(card.getName(), card);
    }

    /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<Card> filterCardsBySeries(CardSeries cardSeries, boolean lexiOrder) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : values()) {
            if (cardSeries.equals(card.getSeries())) {
                cards.add(card);
            }
        }
        if (lexiOrder) {
            Collections.sort(cards, new Comparator<Card>() {

                @Override
                public int compare(Card o1, Card o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        return cards;
    }

    /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<String> filterCardNamesBySeries(CardSeries cardSeries) {
        ArrayList<String> cards = new ArrayList<String>();
        for (Card card : values()) {
            if (cardSeries.equals(card.getSeries())) {
                cards.add(card.getName());
            }
        }
        return cards;
    }

    /**
     * @deprecated Please use put(com.spectre.deck.Card) 
     * @see SupplyDeck#put(com.spectre.deck.Card) 
     */
    @Override
    @Deprecated
    public Card put(String key, Card value) {
        return super.put(key, value);
    }
}
