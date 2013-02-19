/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.spectre.deck.card.Card;
import com.spectre.director.Director;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * <pre>
 * <b>NickName:</b> Arsenal
 *
 * <b>Purpose:</b> Players Deck when in a duel
 *
 * <b>Description:</b> An ArsenalDeck contains no more than 40 cards and may
 * consist of 1-3 Different Card Series however the added bonus each series
 * grants is decreased with each additional series; Also each additional series
 * added to the ArsenalDeck decreases its capacity by 5 (ie: 2 Card Series: Max
 * Capacity 35, 3 Card Series: Max Capacity 30).
 *
 * A arsenal may contain a maximum of 3 same card or card evolutions per
 * arsenal; however Cards in the RAW Series are not counted and may spawn
 * randomly within the match
 *
 * When Talking about a ArsenalDeck it is first ordered by Series and then
 * by Trait. Each Card in the ArsenalDeck is called a flair and grants the user
 * the ability to use what is recorded on it for a specified amount of times. *
 *
 * <b>This Classes main function is
 * <code> createDeck </code></b>
 *
 * Consider: Readding a Dynamic Deck Remember: The Connection to Repository is
 * Implied when creating handler for the two their must be an implicit check
 * between the amount of cards available in Repository Deck and the Amount of
 * Cards Used in ArsenalDeck
 * </pre>
 *
 * @author Kyle Williams
 */
public final class ArsenalDeck implements Savable {

    private final static int CARD_LIMIT = 3;//An Arsenal Is only allowed to have 3 of the same cards
    private int CARD_SERIES_LIMIT;//meant to be final however must be readable   
    private String name;
    private final HashMap<String, Integer> arsenal;
    private final HashMap<String, Integer> seriesInfo;//Used to keep track of all series added to deck
    private int maxDeckCount;

    /**
     * By using this constructor this ArsenalDeck is considered a Arsenal and is
     * bound by limitation Arsenal: encompasses cards player can access in play
     *
     * @param repo
     */
    public ArsenalDeck(String deckName, DeckType type) {
        name = deckName;
        CARD_SERIES_LIMIT = type.ordinal() + 1;
        calcMaxDeckCount();
        arsenal = new HashMap<String, Integer>();
        seriesInfo = new HashMap<String, Integer>();
        clear();
    }

    /**
     * Creates The Final Deck to be used In Game
     *
     * @return
     */
    public LinkedList<String> createDeck() {
        LinkedList<String> deck = new LinkedList<String>();
        for (java.util.Map.Entry<String, Integer> entry : arsenal.entrySet()) {//Loops through map
            String key = entry.getKey();
            for (int i = 0; i < entry.getValue(); i++) {//Adds duplicate cards to list.
                deck.add(key);
            }
        }
        return deck;
    }

    public String getName() {
        return name;
    }

    public boolean add(CardInfo c) {
        if (c.cardsUsed>=c.cardCount) {
            return false;
        } else {
            c.cardsUsed++;
            return add(c.name, c.series);
        }
    }

    public boolean add(Card c) {
        return add(c.getName(), c.getSeries().toString());
    }

    private boolean add(String cardName) {
        return add(cardName, Director.getCard(cardName).getSeries().toString());
    }

    private boolean add(String cardName, String seriesName) {
        if (isOkayToAdd(cardName, seriesName)) {
            if (!arsenal.containsKey(cardName)) {
                arsenal.put(cardName, 1);//must not start at 0 for checking purposes
            } else {
                arsenal.put(cardName, arsenal.get(cardName) + 1);
            }

            reCalcSeriesInfo(seriesName, true);//if going to add a card then update the series
            //reCalcMaxDeckCount();//update maxDeck count
            return true;
        }

        return false;
    }

    public boolean remove(CardInfo c) {
        c.cardsUsed--;
        return remove(c.name, c.series);
    }

    public boolean remove(Card c) {
        return remove(c.getName(), c.getSeries().toString());
    }

    private boolean remove(String cardName, String seriesName) {
        if (arsenal.containsKey(cardName)) {
            arsenal.put(cardName, arsenal.get(cardName) - 1);
            if (arsenal.get(cardName) <= 0) {//if the count is 0 simply remove its instance in arsenal
                arsenal.remove(cardName);
                reCalcSeriesInfo(seriesName, false);
                //reCalcMaxDeckCount(); //update max deckCount                    
            }
            return true;
        }
        return false;
    }

    private void reCalcSeriesInfo(String seriesName, boolean addOne) {

        if (!seriesInfo.containsKey(seriesName)) {
            seriesInfo.put(seriesName, 1);
            return;
        }

        int i = seriesInfo.get(seriesName);
        i = addOne == true ? i++ : i--;

        if (i <= 0) {
            seriesInfo.remove(seriesName);
        } else {
            seriesInfo.put(seriesName, i);
        }

    }

    private boolean isOkayToAdd(String cardName, String series) {
        return !isAtMaxCapacity() && checkCardLimit(cardName) && checkSeriesComply(series);
    }

    /**
     * Checks if its passed its current maximum
     *
     * @return
     */
    public boolean isAtMaxCapacity() {
        return size() >= maxDeckCount;
    }

    public boolean checkCardLimit(String cardName) {
        return arsenal.get(cardName)==null || arsenal.get(cardName) <= CARD_LIMIT;
    }

    /**
     * Check if series has reached its max
     *
     * @param seriesName
     * @return
     */
    private boolean checkSeriesComply(String seriesName) {
        return seriesInfo.containsKey(seriesName) || seriesInfo.size() <= CARD_SERIES_LIMIT;
    }

    /**
     * update max deckCount
     *
     * @deprecated Arsenal Deck is no longer Dynamic. This is Temporary 4th Deck
     * type will allow for a dynamic Deck
     * @see calcMaxDeckCount
     */
    private void reCalcMaxDeckCount() {
        maxDeckCount = 45 - (5 * seriesInfo.size());
    }

    private void calcMaxDeckCount() {
        maxDeckCount = 45 - (5 * CARD_SERIES_LIMIT);
    }
/////////////////////////////////
    public void clear() {
        arsenal.clear();        
        seriesInfo.clear();        
        calcMaxDeckCount();
    }
///////////////////////
    /**
     * Returns the Card Count of all of the cards in the arsenal
     *
     * @return
     */
    public int size() {
        int size = 0;
        //Requires a loop as a single entry can represent more than one card
        for (Integer i : arsenal.values()) {
            size += i;//i represents number of same cards in Deck
        }
        return size;
    }

    public DeckType getDeckType() {
        return DeckType.values()[CARD_SERIES_LIMIT - 1];
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name, "name", null);
        oc.write(CARD_SERIES_LIMIT, "CARD_SERIES_LIMIT", -1);
        LinkedList<String> temp = createDeck();
        oc.write(temp.toArray(new String[temp.size()]), "arsenal", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        name = ic.readString("name", null);
        CARD_SERIES_LIMIT = ic.readInt("CARD_SERIES_LIMIT", -1);


        String[] temp = ic.readStringArray("arsenal", new String[1]);
        for (String s : temp) {
            this.add(s);
        }

        if (CARD_SERIES_LIMIT == -1) {
            CARD_SERIES_LIMIT = seriesInfo.size();//This is valid if all Cards were added correctly
        }

        calcMaxDeckCount();
    }

    /**
     * The enum DeckType is used to describe the type of deck being used
     */
    public enum DeckType {

        /**
         * <pre>
         * <b>SINGLE_CAPACITY</b>
         *      Series Limit: 1
         *      Card Capacity: 40 Card
         *      Spawn Speed: Fast
         * </pre>
         */
        SINGLE_CAPACITY,
        /**
         * <pre>
         * <b>DOUBLE_CAPACITY</b>
         *      Series Limit: 2
         *      Card Capacity: 35 Card Spawn
         *      Speed: Fast
         * </pre>
         */
        DOUBLE_CAPACITY,
        /**
         * <pre>
         * <b>TRIPLE_CAPACITY</b>
         *      Series Limit: 3
         *      Card Capacity: 30 Card Spawn
         *      Speed: Fast
         * </pre>
         */
        TRIPLE_CAPACITY;
    }
}
