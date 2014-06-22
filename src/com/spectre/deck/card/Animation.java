/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.deck.card;

/**
 *
 * @author Kyle D. Williams
 */
public class Animation implements Cloneable {

    private String animation = "";
    private float startTime;
    private float speed;

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
