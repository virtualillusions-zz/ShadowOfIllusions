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
import java.io.IOException;

/**
 * * A utility class to help organize and store card information 
 *
 * @author Kyle Williams
 */
public class CardInfo implements Savable {

    /**
     * The name of the card
     */
    public String name;
    /**
     * The name of the card Series used only to prevent an external call to
     * director
     */
    public String series;
    /**
     * The amount of exp player has build up for this card //SHOULD BE ONLY USED
     * IN REPO TODO: figure how to reward exp at the end of match for successful
     * use
     */
    public float exp;
    /**
     * The amount of cards of the same kind in the arsenal make note that
     */
    public int cardCount;
    /**
     * The amount of cards being used. This number cannot be greater than
     * <code>cardCount</code>
     */
    public int cardsUsed;

    public CardInfo(String cardName, String cardSeries) {
        name = cardName;
        series = cardSeries;
        exp = 0;
        cardCount = 1;
        cardsUsed = 0;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name, "name", null);
        oc.write(series, "series", null);
        oc.write(exp, "exp", 0);
        oc.write(cardCount, "cardCount", 0);
        oc.write(cardsUsed, "cardsUsed", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        name = ic.readString("name", null);
        series = ic.readString("series", null);
        exp = ic.readInt("exp", 0);
        cardCount = ic.readInt("cardCount", 0);
        cardsUsed = ic.readInt("cardsUsed", 0);
    }
    
    @Override
    public String toString(){
        return "CardInfo["+name+", "+series+", EXP("+exp+"), Card Count("+cardCount+"), Cards Used("+cardsUsed+")]";
    }
}
