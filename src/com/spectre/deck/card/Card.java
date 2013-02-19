/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck.card;

import com.spectre.controller.character.SpectreDuelController.SpectreCharacterAttributes;
import com.spectre.deck.card.CardCharacteristics.CardRange;
import com.spectre.deck.card.CardCharacteristics.CardSeries;
import com.spectre.deck.card.CardCharacteristics.CardStats;
import com.spectre.deck.card.CardCharacteristics.CardTrait;
import com.spectre.deck.card.Effect.EffectType;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Kyle Williams
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Animation.class, Effect.class, CardCharacteristics.class})
@XmlRootElement
public class Card implements Cloneable {

    private Animation animation;
    private Effect passiveEffect;
    private Effect activeEffect;
    private Effect contactEffect;
    private HashMap<CardStats, Object> stats;
    @XmlTransient
    private Boolean lock;

    /**
     * Mainly For JAXB Serialization purposes
     */
    public Card() {
        lock = true;
    }

    @XmlAttribute
    public String getName() {
        return getData(CardStats.CARD_NAME);
    }

    public CardSeries getSeries() {
        return getData(CardStats.CARD_SERIES);
    }

    public CardTrait getTrait() {
        return getData(CardStats.CARD_TRAIT);
    }

    public CardRange getRange() {
        return getData(CardStats.CARD_RANGE);
    }

    public Timestamp getDateModified() {
        Long m = getData(CardStats.DATE_MODIFIED);
        return new Timestamp(m);
    }

    public Animation getAnimation() {
        if (animation == null) {
            animation = new Animation();
        }
        return animation;
    }

    public Effect getPassiveEffect() {
        if (passiveEffect == null) {
            passiveEffect = new Effect(EffectType.Passive);
        }
        return passiveEffect;
    }

    public Effect getActiveEffect() {
        if (activeEffect == null) {
            activeEffect = new Effect(EffectType.Active);
        }
        return activeEffect;
    }

    public Effect getContactEffect() {
        if (contactEffect == null) {
            contactEffect = new Effect(EffectType.Contact);
        }
        return contactEffect;
    }

    public final boolean setData(CardStats key, Object data) {
        if (lock == true) {
            return false;
        }
        if (stats == null) {
            stats = new HashMap<CardStats, Object>(50);
        }

        if (key.sameClass(data)
                && (getData(key) == null || !getData(key).equals(data))) {
            stats.put(key, data);
            stats.put(CardStats.DATE_MODIFIED, new Date().getTime());
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getData(CardStats key) {
        if (stats == null) {
            return null;
        }
        return (T) stats.get(key);
    }

    public void lockData(boolean isLock) {
        lock = isLock;
    }

    public Boolean isDataLocked() {
        return lock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Card clone() {
        try {
            Card card = (Card) super.clone();
            card.animation = getAnimation().clone();
            card.passiveEffect = getPassiveEffect().clone();
            card.activeEffect = getActiveEffect().clone();
            card.contactEffect = getContactEffect().clone();
            if (stats != null) {
                for (java.util.Map.Entry<CardStats, Object> e : stats.entrySet()) {
                    card.setData(e.getKey(), e.getValue());
                }
            }
            return card;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}