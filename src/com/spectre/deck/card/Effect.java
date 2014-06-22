/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck.card;

import com.spectre.deck.card.Effect.EffectType;
import java.util.ArrayList;

/**
 * <p/>
 * <p/>
 * @author Kyle D. Williams
 */
public class Effect implements Cloneable {

    public enum EffectType {

        Passive, Active, Contact
    }
    private String boneLocation;
    private String audioLocation;
    private float startTime;
    private float endTime;
    private String type;
    private ArrayList<String> emitters;

    /**
     * Mainly For JAXB Serialization purposes
     */
    public Effect() {
    }

    public Effect(EffectType effectType) {
        boneLocation = "pelvis";
        audioLocation = "";
        startTime = 0f;
        endTime = 1f;
        type = effectType.toString();
    }

    public void resetEffect() {
        boneLocation = "pelvis";
        audioLocation = "";
        startTime = 0;
        endTime = 0.01f;
        emitters = new ArrayList<String>();
    }

    public EffectType getEffectType() {
        return EffectType.valueOf(type);
    }

    public void setBoneLocation(String bone) {
        boneLocation = bone;
    }

    public String getBoneLocation() {
        return boneLocation;
    }

    public void setAudioLocation(String bone) {
        audioLocation = bone;
    }

    public String getAudioLocation() {
        return audioLocation;
    }

    public void setStartTime(float time) {
        startTime = time;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setEndTime(float time) {
        endTime = time;
    }

    public float getEndTime() {
        return endTime;
    }

    public void addEmitter(String emitterName) {
        getEmitters().add(emitterName);
    }

    public void removeEmitter(String emitterName) {
        getEmitters().remove(emitterName);
    }

    public ArrayList<String> getEmitters() {
        if (emitters == null) {
            emitters = new ArrayList<String>();
        }
        return emitters;
    }

    @Override
    public String toString() {
        int i = emitters == null ? 0 : emitters.size();
        return "Effect [ " + boneLocation + ", Emitters: " + i + ", Audio: " + audioLocation + " ]";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Effect clone() {
        try {
            Effect effect = (Effect) super.clone();
            effect.type = type;
            effect.endTime = getEndTime();
            effect.startTime = getStartTime();
            effect.boneLocation = getBoneLocation();
            effect.audioLocation = getAudioLocation();
            for (String e : getEmitters()) {
                effect.addEmitter(e);
            }
            return effect;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
