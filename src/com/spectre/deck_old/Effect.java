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
import java.util.ArrayList;

/**
 * <p/>
 * <p/>
 * @author Kyle Williams
 */
public class Effect implements Savable {

    private String boneLocation;
    private String audioLocation;
    private float startTime;
    private float endTime;
    private ArrayList<String> emitters;

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
            return null;
        }
        return emitters;
    }

    @Override
    public String toString() {
        int i = emitters == null ? 0 : emitters.size();
        return "Effect [ " + boneLocation + ", Emitters: " + i + ", Audio: " + audioLocation + " ]";
    }

    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule capsule = je.getCapsule(this);
        capsule.write(boneLocation, "boneLocation", null);
        capsule.write(audioLocation, "audioLocation", null);
        capsule.write(startTime, "start", 0);
        capsule.write(endTime, "end", 0);
        capsule.writeSavableArrayList(emitters, "emitters", null);
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        InputCapsule ic = ji.getCapsule(this);
        boneLocation = (String) ic.readString("boneLocation", null);
        audioLocation = (String) ic.readString("audioLocation", null);
        startTime = (float) ic.readFloat("start", 0);
        endTime = (float) ic.readFloat("end", 0);
        emitters = (ArrayList<String>) ic.readSavableArrayList("emitters", null);
    }
}
