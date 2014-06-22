/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck_old;

import com.spectre.deck_old.CardStats.CardSeries;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is a Deck That should encompass every card in the game
 * @author Kyle Williams
 */
public class MasterDeck extends java.util.concurrent.ConcurrentHashMap<String,Card>{
//    HashSet<CardSeries> series = new HashSet<CardSeries>();
//    @Override
//    public Card put(String key, Card value) {
//        series.add(value.series);
//        return super.put(key, value);
//    }
//    /**
//     * Returns the series Types held by this deck
//     * @return 
//     */
//    public ArrayList<CardSeries> getSeriesList(){return new ArrayList(series);}
    
    public Card put(Card card){      
        return super.put(card.getName(),card);
    }
    /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<Card> filterCardsBySeries(CardSeries cardSeries,boolean lexiOrder){
        ArrayList<Card> cards = new ArrayList<Card>();
        for(Card card:values()){
            if(cardSeries.equals(card.getSeries())){
                cards.add(card);
            }
        } 
        if(lexiOrder)
            Collections.sort(cards, new Comparator<Card>(){ 
                    @Override
                    public int compare(Card o1, Card o2){ 
                        return o1.getName().compareTo(o2.getName()); 
                    } 
                }
            ); 
        return cards;
    }
        /**
     * Creates and returns a list of cards in a specified series
     * @param cardSeries
     * @return Cards in the specified Series
     */
    public ArrayList<String> filterCardNamesBySeries(CardSeries cardSeries){
        ArrayList<String> cards = new ArrayList<String>();
        for(Card card:values()){
            if(cardSeries.equals(card.getSeries())){
                cards.add(card.getName());
            }
        }        
        return cards;
    }
}
