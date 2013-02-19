/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardCharacteristics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Kyle Williams
 */
public final class SpectreDeckFilters {

    /**
     * Creates and returns a list of cards in a specified series
     * <br/><b>Internally used</b>
     *
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public static ArrayList<Card> filterCardsBySeries(Collection<Card> deck, CardCharacteristics.CardSeries cardSeries, boolean lexiOrder) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : deck) {
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
     * Creates and returns a list of cards in a specified series <br/>
     * <b>Internally used</b>
     *
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public static ArrayList<String> filterCardNamesBySeries(Collection<Card> deck, CardCharacteristics.CardSeries cardSeries) {
        ArrayList<String> cards = new ArrayList<String>();
        for (Card card : deck) {
            if (cardSeries.equals(card.getSeries())) {
                cards.add(card.getName());
            }
        }
        return cards;
    }
}
