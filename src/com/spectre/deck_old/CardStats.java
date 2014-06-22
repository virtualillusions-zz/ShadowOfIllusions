/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck_old;

import com.jme3.export.Savable;
import com.jme3.scene.UserData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kyle Williams
 */
public class CardStats{

    /**
     *  This is a list of all Card Series in the game
     * It should be noted that Ryujin is special and instead of its ancestors being both DragonPrime and Dragoon it is only Dragon
     */
   // <editor-fold defaultstate="collapsed" desc="Card Series">                          

    public enum CardSeries {
//                Series 1             Series 2                                 Series 3                                         Series 4
   /*Base*/ Raw(0),
   /*Nature*/      Nature(1)/*, Muse(Nature, 2), Cleric(Muse, 3), BlueLight(Cleric, 4),
                              Dragon(Nature, 2), DragonPrime(Dragon, 3), Bahamut(DragonPrime,4),
                                                 Dragoon(Dragon, 3),Ryujin(Dragoon, 4),
                              NaturalDefender(Nature, 2),
   /*Technology  Technology(1), Nanobot(Technology, 2), Hacker(Nanobot, 3), Virus(Hacker, 4),
                                  Cybernetics(Technology, 2), CyberRaptor(Cybernetics, 3),
   /*Phantom     Phantom(1), Necromancer(Phantom, 2), Devildom(Necromancer, 3), Satonic(Devildom, 4),
                                Shaman(Phantom, 2), Reaper(Shaman, 3),
   /*Celestial   Celestial(1), Zodiac(Celestial, 2), Destiny(Zodiac, 3),
                                    Astro(Celestial, 2), Devine(Astro, 3),
   /*Aether      Creation(1),
                   MatterControl(1)*/;

        private CardSeries(int s) {
            prev = null;
            seriesPos = s;
        }

        private CardSeries(CardSeries k, int s) {
            prev = k;
            seriesPos = s;
        }

        public CardSeries getAncestor() {
            return prev == null ? Raw : prev;
        }

        public int getSeriesPosition() {
            return seriesPos;
        }
        private final CardSeries prev;
        private final int seriesPos;
    }// </editor-fold> 

    /**
     *  This is a list of all Card Traits supported in the game
     */
    // <editor-fold defaultstate="collapsed" desc="Card Trait">                          
    public enum CardTrait {
        Damage, Defense, Enhance, Curse, /*Special, Summon, Transform*/;
    }// </editor-fold> 

    /**
     *  This is a list of all Card Ranges supported in the game
     */
    // <editor-fold defaultstate="collapsed" desc="Card Range">                          
    public enum CardRange {
        Long, Med, Short, Self, Misc;
    }// </editor-fold> 
   
    public final static Comparator lexiSortSeries = new Comparator<CardStats.CardSeries>() {

        public int compare(CardSeries o1, CardSeries o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };
}
