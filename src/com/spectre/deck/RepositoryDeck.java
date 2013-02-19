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
import com.spectre.deck.ArsenalDeck.DeckType;
import com.spectre.deck.card.Card;
import com.spectre.director.Director;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <b>NickName:</b> Repo <br/>
 *
 * <b>Purpose:</b> Repository of All Cards Player Owns <br/>
 *
 * <b>Description:</b> A RepositoryDeck is a class to store all cards owned by
 * the player
 *
 * @author Kyle Williams
 */
public final class RepositoryDeck implements Savable {

    private final Collection<CardInfo> repository;
    private final ArrayList<ArsenalDeck> arsenalList;

    /**
     * By using this constructor this ArsenalDeck is considered a Repository and
     * is bound by limitation Repository: encompasses all cards player owns
     *
     * @param repo
     */
    public RepositoryDeck() {
        repository = new HashSet<CardInfo>();
        arsenalList = new ArrayList<ArsenalDeck>();
    }

    /**
     * Creates a brand new Arsenal Deck
     *
     * @param name
     * @return
     */
    public ArsenalDeck createArsenalDeck(String name, DeckType type) {
        ArsenalDeck aD = new ArsenalDeck(name, type);
        arsenalList.add(aD);
        return aD;
    }

    public ArsenalDeck getArsenalDeck(String deckName) {
        for (ArsenalDeck a : arsenalList) {
            if (a.getName().equals(deckName)) {
                return a;
            }
        }
        return null;
    }

    public boolean deleteArsenalDeck(String name) {
        for (Iterator<ArsenalDeck> i = arsenalList.iterator(); i.hasNext();) {
            ArsenalDeck aD = i.next();
            if (aD.getName().equals(name)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    public boolean deleteArsenalDeck(ArsenalDeck deck) {
        return arsenalList.remove(deck);
    }

    /**
     * Returns the Card Info Specified
     *
     * @param cardName
     * @return
     */
    private CardInfo getCardInfo(String cardName) {
        for (CardInfo c : repository) {
            if (c.name.equals(cardName)) {
                return c;
            }
        }
        return null;
    }

    public boolean add(String cardName) {
        return add(cardName, null);
    }

    public boolean add(Card c) {
        return add(c.getName(), c.getSeries().toString());
    }

    public boolean add(String cardName, String seriesName) {
        CardInfo c = getCardInfo(cardName);
        if (c != null) {
            c.cardCount++;
            return true;
        }

        if (seriesName == null) {
            seriesName = Director.getCard(cardName).getSeries().toString();
        }
        return repository.add(new CardInfo(cardName, seriesName));
    }

    public boolean remove(Card c) {
        return remove(c.getName());
    }

    public boolean remove(String cardName) {
        for (Iterator<CardInfo> i = repository.iterator(); i.hasNext();) {
            CardInfo cI = i.next();
            if (cI.name.equals(cardName)) {
                cI.cardCount--;
                if (cI.cardCount <= 0) {
                    i.remove();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the Card Count of all of the cards in the arsenal
     *
     * @return
     */
    public int size() {
        int size = 0;
        //Requires a loop as a single entry can represent more than one card
        for (CardInfo cI : repository) {
            size += cI.cardCount;
        }
        return size;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(repository.toArray(new CardInfo[repository.size()]), "repository", null);
        oc.writeSavableArrayList(arsenalList, "arsenalList", null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        CardInfo[] cI = (CardInfo[]) ic.readSavableArray("repository", new CardInfo[1]);
        repository.addAll(Arrays.asList(cI));
        Collection<ArsenalDeck> aD = ic.readSavableArrayList("arsenalList", new ArrayList<ArsenalDeck>());
        arsenalList.addAll(aD);
    }
}
