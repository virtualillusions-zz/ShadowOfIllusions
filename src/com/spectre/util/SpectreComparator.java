/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats;
import java.util.Comparator;

/**
 *
 * @author Kyle
 */
public class SpectreComparator {

    public final static Comparator lexiSortSeries = new Comparator<CardStats.CardSeries>() {
        public int compare(CardStats.CardSeries o1, CardStats.CardSeries o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };
    public final static Comparator lexiSortCard = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
