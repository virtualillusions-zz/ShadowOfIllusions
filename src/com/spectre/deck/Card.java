/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import com.spectre.deck.CardStats.*;
import com.spectre.deck.Effect.EffectType;
import java.util.Date;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Remember: if self kill particles at end animation if particles 0 if duration
 * lon and particles not 0 kill at end of duration else kill particles when make
 * contact <p/>
 * <p/>
 * @author Kyle Williams
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CardTrait.class, CardRange.class, CardSeries.class})
@XmlRootElement
public class Card implements Cloneable {

    private Animation animation;
    private Effect passiveEffect;
    private Effect activeEffect;
    private Effect contactEffect;
    private HashMap<String, Object> stats;
    private HashMap<String, String> extra;
    @XmlTransient
    private Boolean lock;

    /**Mainly For JAXB Serialization purposes*/
    public Card() {
        lock = true;
    }

    @XmlAttribute
    public String getName() {
        return getData("cardName");
    }

    public CardSeries getSeries() {
        return getData("cardSeries");
    }

    public CardTrait getTrait() {
        return getData("cardTrait");
    }

    public CardRange getRange() {
        return getData("cardRange");
    }

    public Date getDateModified() {
        Long m = getData("dateModified");
        return new Date(m);
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

    public final boolean setData(String key, Object data) {
        if (lock == true) {
            return false;
        }
        if (stats == null) {
            stats = new HashMap<String, Object>(50);
        }
        if (getData(key) == null || !getData(key).equals(data)) {
            stats.put(key, data);
            stats.put("dateModified", new Date().getTime());
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getData(String key) {
        if (stats == null) {
            return null;
        }
        return (T) stats.get(key);
    }

    public final boolean setExtraData(String key, String extraData) {
        if (lock == true) {
            return false;
        }
        if (extra == null) {
            extra = new HashMap<String, String>(10);
        }
        if (extraData == null || extraData.equals("")) {
            extra.remove(key);
        } else {
            extra.put(key, extraData);
        }
        stats.put("dateModified", new Date().getTime());
        return true;
    }

    @SuppressWarnings("unchecked")
    public final String getExtraData(String key) {
        if (extra == null) {
            return null;
        }
        return extra.get(key);
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
                for (java.util.Map.Entry<String, Object> e : stats.entrySet()) {
                    card.setData(e.getKey(), e.getValue());
                }
            }
            if (extra != null) {
                for (java.util.Map.Entry<String, String> e : extra.entrySet()) {
                    card.setExtraData(e.getKey(), e.getValue());
                }
            }
            return card;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}