/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats.CardSeries;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Concurrent Hashmap used to store Card Objects in an organized and logical
 * manner TODO: repurpose and remove extending concurrent map instead initialize
 * it in constructor SUPPLYDECK SHOULD CAN ACTUALLY BECOME STATIC CLASS
 *
 * @author Kyle Williams
 */
public class SupplyDeck {

    private ConcurrentHashMap<String, Card> supplyDeck;

    public SupplyDeck() {
        supplyDeck = new ConcurrentHashMap<String, Card>();
    }

    /**
     * Maps the specified Card to the table. The Card value cannot be be null.
     *
     * <p> The value can be retrieved by calling the <tt>get</tt> method with a
     * key that is equal to the original name of the card.
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
        return supplyDeck.put(card.getName(), card);
    }

    /**
     * Copies all of the mappings from the specified map to this one. These
     * mappings replace any mappings that this map had for any of the keys
     * currently in the specified map.
     *
     * @param m<String, Card> mappings to be stored in this map
     */
    public void putAll(SupplyDeck sD) {
        for (java.util.Map.Entry<String, Card> e : sD.entrySet()) {
            put(e.getValue());
        }
    }

    /**
     * Removes the Card (and its corresponding value) from SupplyDeck map.
     *
     * @param cardName the name of the card
     * @return the previous value associated with <tt>cardName</tt>, or
     * <tt>null</tt> if there was no card by the name <tt>cardName</tt>
     * @throws NullPointerException if the specified key is null
     */
    public Card remove(String cardName) {
        return supplyDeck.remove(cardName);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if the specified key is null
     */
    public boolean remove(String cardName, Card card) {
        return supplyDeck.remove(cardName, card);
    }

    /**
     * Creates and returns a list of cards in a specified series
     *
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<Card> filterCardsBySeries(CardSeries cardSeries, boolean lexiOrder) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : supplyDeck.values()) {
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
     *
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<String> filterCardNamesBySeries(CardSeries cardSeries) {
        ArrayList<String> cards = new ArrayList<String>();
        for (Card card : supplyDeck.values()) {
            if (cardSeries.equals(card.getSeries())) {
                cards.add(card.getName());
            }
        }
        return cards;
    }

    public boolean containsKey(String ID) {
        return supplyDeck.containsKey(ID);
    }

    public Card get(String ID) {
        return supplyDeck.get(ID);
    }

    public Iterable<Entry<String, Card>> entrySet() {
        return supplyDeck.entrySet();
    }

    public int size() {
        return supplyDeck.size();
    }
}
