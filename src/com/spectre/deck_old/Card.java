/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck_old;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.UserData;
import com.spectre.deck_old.CardStats.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Remember: if self kill particles at end animation if particles 0 if duration
 * lon and particles not 0 kill at end of duration else kill particles when make
 * contact <p/>
 * <p/>
 * @author Kyle Williams
 */
public class Card implements Savable {

    private ConcurrentHashMap<String, Savable> stats;
    private Animation animation;
    private ArrayList<Effect> passiveEffects;
    private ArrayList<Effect> activeEffects;

    public String getName() {
        return getData("cardName");
    }

    public CardSeries getSeries() {
        return CardSeries.valueOf((String) getData("cardSeries"));
    }

    public CardTrait getTrait() {
        return CardTrait.valueOf((String) getData("cardTrait"));
    }

    public CardRange getRange() {
        return CardRange.valueOf((String) getData("cardRange"));
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

    public ArrayList<Effect> getPassiveEffects() {
        if (passiveEffects == null) {
            passiveEffects = new ArrayList<Effect>();
        }
        return passiveEffects;
    }

    public ArrayList<Effect> getActiveEffects() {
        if (activeEffects == null) {
            activeEffects = new ArrayList<Effect>();
        }
        return activeEffects;
    }

    public final void setData(String key, Object data) {
        if (stats == null) {
            stats = new ConcurrentHashMap<String, Savable>();
        }
        try {
            if (data instanceof Savable) {
                stats.put(key, (Savable) data);
            } else {
                stats.put(key, new UserData(UserData.getObjectType(data), data));
            }
        } catch (IllegalArgumentException e) {
            if (data instanceof Enum) {
                stats.put(key, new UserData(UserData.getObjectType(data.toString()), data.toString()));

            } else {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final <T> T getData(String key) {
        if (stats == null) {
            return null;
        }

        Savable s = stats.get(key);
        if (s instanceof UserData) {
            return (T) ((UserData) s).getValue();
        } else {
            return (T) s;
        }
    }

    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        Long time = new Date().getTime();
        stats.put("dateModified", new UserData(UserData.getObjectType(time), time));
        capsule.writeStringSavableMap(stats, "card_stats", null);
        capsule.write(animation, "anim", null);
        capsule.writeSavableArrayList(activeEffects, "active_Effects", null);
        capsule.writeSavableArrayList(passiveEffects, "passive_Effects", null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter ji) throws IOException {
        InputCapsule ic = ji.getCapsule(this);
        stats = (ConcurrentHashMap<String, Savable>) ic.readStringSavableMap("card_stats", null);
        animation = (Animation) ic.readSavable("animation", null);
        activeEffects = (ArrayList<Effect>) ic.readSavableArrayList("active_Effects", null);
        passiveEffects = (ArrayList<Effect>) ic.readSavableArrayList("passive_Effects", null);
    }
//    protected String predecessor = "n/a";
//    protected ArrayList<String> successor = new ArrayList<String>();
}