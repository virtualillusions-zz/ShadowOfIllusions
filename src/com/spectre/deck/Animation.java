/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Kyle Williams
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Animation implements Cloneable {

    @XmlAttribute
    private String animation = "";
    @XmlAttribute
    private float startTime;
    @XmlAttribute
    private float speed;

    /**Mainly For JAXB Serialization purposes*/
    public Animation() {
        startTime = 0;
        speed = 1;
    }

    public void setAnimationName(String anim) {
        animation = anim;
    }

    public String getAnimationName() {
        return animation;
    }

    public void setStartTime(float time) {
        startTime = time;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        if (animation.equals("")) {
            return "N/A" + '|' + startTime + '|' + speed;
        }
        return animation + '|' + startTime + '|' + speed;
    }

    @Override
    public Animation clone() {
        try {
            Animation anim = (Animation) super.clone();
            anim.animation = getAnimationName();
            anim.speed = getSpeed();
            anim.startTime = getStartTime();
            return anim;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
