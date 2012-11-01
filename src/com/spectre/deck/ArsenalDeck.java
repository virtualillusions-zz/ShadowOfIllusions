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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A ArsenalDeck contains no more than 40 cards and may consist of 1-3 Different Card Series 
 * however the added bonus each series grants is decreased with each additional series; 
 * Also each additional series added to the ArsenalDeck decreases its capacity by 5 
 * (ie: 2 Card Series: Max Capacity 35, 3 Card Series: Max Capacity 30). 
 * 
 * DEPRECATED: A arsenal may contain a maximum of 3 same card or card evolutions per arsenal;
 * however Cards in the RAW Series are not counted and may spawn randomly within the match
 * 
 * When Talking about a ArsenalDeck it is first ordered by Series and then by Trait. 
 * Each Card in the ArsenalDeck is called a flair and grants the user the ability to use what is
 * recorded on it for a specified amount of times.
 *
 * @author Kyle Williams
 */
public final class ArsenalDeck implements Savable {

    private final int CARD_SERIES_LIMIT = 3;
    private String name;
    private final HashMap<String, Integer> arsenal = new HashMap<String, Integer>();
    private final Collection<String> seriesInfo = new HashSet<String>();
    private int maxDeckCount;

    /**
    
    
     * @param isRepository is this Card one of the arsenal decks or the players main arsenal'
     */
    /**
     * By using this constructor this ArsenalDeck is considered a
     * Arsenal and is bound by limitation
     * Arsenal: encompasses cards player can access in play
     * @param repo 
     */
    public ArsenalDeck(String deckName) {
        name = deckName;
        reset();
    }

    public String getDeckName() {
        return name;
    }

    public boolean add(CardInfo c) {
        return add(c.name, c.series);
    }

    public boolean add(Card c) {
        return add(c.getName(), c.getSeries().toString());
    }

    private boolean add(String cardName) {
        return add(cardName, Director.getCard(cardName).getSeries().toString());
    }

    private boolean add(String cardName, String seriesName) {
        if (isOkayToAdd(seriesName)) {
            if (arsenal.containsKey(cardName)) {
                arsenal.put(cardName, 1);
            } else {
                arsenal.put(cardName, arsenal.get(cardName) + 1);
            }

            seriesInfo.add(seriesName);//if going to add a card then update the series
            reCalcMaxDeckCount();//update maxDeck count
            return true;
        }

        return false;
    }

    public boolean remove(CardInfo c) {
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
                seriesInfo.remove(seriesName);//if going to remove a card then update the series
                reCalcMaxDeckCount(); //update max deckCount                    
            }
            return true;
        }
        return false;
    }

    private boolean isOkayToAdd(String series) {
        return isNotAtCapacityLimit() && checkSeriesComply(series);
    }

    /**
     * Check if series has reached its max
     * @param seriesName
     * @return 
     */
    private boolean checkSeriesComply(String seriesName) {
        return seriesInfo.contains(seriesName) || seriesInfo.size() < CARD_SERIES_LIMIT;//if series has card or hasn't reach limit
    }

    /**
     * Check if more can be added
     * @return 
     */
    public boolean isNotAtCapacityLimit() {
        return size() < maxDeckCount;
    }

    /**
     * Checks if its passed its current maximum
     * @return 
     */
    public boolean passedCapacity() {
        return size() > maxDeckCount;
    }

    public void clear() {
        arsenal.clear();
        reset();
    }

    private void reset() {
        maxDeckCount = 40;
        seriesInfo.clear();
    }

    /**
     * Returns the Card Count of all of the cards in the arsenal
     * @return 
     */
    public int size() {
        int size = 0;
        //Requires a loop as a single entry can represent more than one card
        for (Integer i : arsenal.values()) {
            size += i;
        }
        return size;
    }

    /**
     * update max deckCount    
     */
    private void reCalcMaxDeckCount() {
        maxDeckCount = 45 - (5 * seriesInfo.size());
    }

    /**
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

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name, "name", null);

        LinkedList<String> temp = createDeck();
        oc.write(temp.toArray(new String[temp.size()]), "arsenal", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        name = ic.readString("name", null);

        String[] temp = ic.readStringArray("arsenal", new String[1]);
        for (String s : temp) {
            this.add(s);
        }
    }
}
