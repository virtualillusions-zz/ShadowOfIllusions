/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardCharacteristics;
import java.util.Comparator;

/**
 *
 * @author Kyle
 */
public final class SpectreDeckComparator {

    public static Comparator lexiSortSeries = new Comparator<CardCharacteristics.CardSeries>() {
        public int compare(CardCharacteristics.CardSeries o1, CardCharacteristics.CardSeries o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };
    public static Comparator lexiSortCard = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
