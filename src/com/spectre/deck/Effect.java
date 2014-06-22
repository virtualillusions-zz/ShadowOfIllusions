/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import com.spectre.deck.Effect.EffectType;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p/>
 * <p/>
 * @author Kyle Williams
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({EffectType.class})
public class Effect implements Cloneable {

    @XmlTransient
    public enum EffectType {

        Passive, Active, Contact
    }
    @XmlAttribute
    private String boneLocation;
    @XmlAttribute
    private String audioLocation;
    @XmlAttribute
    private float startTime;
    @XmlAttribute
    private float endTime;
    @XmlAttribute
    private String type;
    @XmlValue
    private ArrayList<String> emitters;

    /**Mainly For JAXB Serialization purposes*/
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
        if (emitters == null) {
            emitters = new ArrayList<String>();
        }
        emitters.add(emitterName);
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
