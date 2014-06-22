/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import com.spectre.deck.card.Card;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>NickName:</b> Master Deck <br/>
 *
 * <b>Purpose:</b> Repository of All Cards In Game <br/>
 *
 * <b>Description:</b> A Concurrent and thus thread-safe
 * <code>HashMap</code> used to store Card Objects in an organized and logical
 * manner. This Class serves as a Master Deck which all other cards reference;
 * as such their should only ever be a single instance of this class at any
 * given time.
 *
 * TODO: PROPERLY LOAD SUPPLYDECK FILE
 *
 * @author Kyle D. Williams
 */
public final class SupplyDeck {

    private static final ConcurrentHashMap<String, Card> supplyDeck = new ConcurrentHashMap<String, Card>();

    private SupplyDeck() {
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
     * card</tt> <br/> <b>Internally used</b>
     * @throws NullPointerException if the specified card is null
     */
    public static Card put(Card card) {
        if (card.getName() == null || card.getName().equals("")) {
            throw new NullPointerException("The Card to be added does not have a proper name."
                    + "\n As such it cannot be mapped to this table");
        }
        return supplyDeck.put(card.getName(), card);
    }

    /**
     * Copies all of the mappings from the specified map to this one. These
     * mappings replace any mappings that this map had for any of the keys
     * currently in the specified map. <br/> <b>Internally used</b>
     *
     * @param Map<String, Card> mappings to be stored in this map
     */
    public static void putAll(Map<String, Card> sD) {
        for (java.util.Map.Entry<String, Card> e : sD.entrySet()) {
            put(e.getValue());
        }
    }

    /**
     * Removes the Card (and its corresponding value) from SupplyDeck map.
     *
     * @param cardName the name of the card
     * @return the previous value associated with <tt>cardName</tt>, or
     * <tt>null</tt> if there was no card by the name <tt>cardName</tt> <br/>
     * <b>Internally used</b>
     * @throws NullPointerException if the specified key is null
     */
    public static Card remove(String cardName) {
        return supplyDeck.remove(cardName);
    }

    /**
     * <br/> <b>Internally used</b>
     *
     * @throws NullPointerException if the specified key is null
     */
    public static boolean remove(String cardName, Card card) {
        return supplyDeck.remove(cardName, card);
    }

    public static boolean containsKey(String ID) {
        return supplyDeck.containsKey(ID);
    }

    public static Card get(String ID) {
        return supplyDeck.get(ID);
    }

    public static Iterable<Entry<String, Card>> entrySet() {
        return supplyDeck.entrySet();
    }

    public static int size() {
        return supplyDeck.size();
    }
}
