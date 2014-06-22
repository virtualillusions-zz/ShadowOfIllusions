/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.components;

import com.simsilica.es.PersistentComponent;

/**
 * <b>NickName:</b> Experience <br/>
 *
 * <b>Purpose:</b> Track excessive card use<br/>
 *
 * <b>Description:</b> A CardExperiencePiece The amount of exp player has build
 * up for this card
 *
 * @author Kyle D. Williams
 */
public class CardExperiencePiece implements PersistentComponent{

    public final float exp;

    public CardExperiencePiece(float experiencePoints) {
        exp = experiencePoints;
    }

    public float getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "CardExperiencePiece[XP=" + getExp() + "]";
    }
}
