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
import java.io.IOException;

/**
 *
 * @author Kyle Williams
 */
public class Animation implements Savable {

    private String animation = "N/A";
    private float startTime;
    private float endTime;
    private float speed;

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

    public void setEndTime(float time) {
        endTime = time;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return animation+'|'+startTime+':'+endTime+'|'+speed;
    }
    
    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        capsule.write(animation, "animationName", null);
        capsule.write(startTime, "start", 0);
        capsule.write(endTime, "end", 0);
        capsule.write(speed, "speed", 1.0f);
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        InputCapsule ic = ji.getCapsule(this);
        animation = (String) ic.readString("animationName", null);
        startTime = (float) ic.readFloat("start", 0);
        endTime = (float) ic.readFloat("end", 0);
        speed = (float) ic.readFloat("speed", 1.0f);
    }
}
