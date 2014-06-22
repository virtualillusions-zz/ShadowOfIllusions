/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.controller;

import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.spectre.deck.Effect;
import com.spectre.director.Director;
import java.util.ArrayList;

/**
 *
 * @author Kyle Williams
 */
public class EffectHandler_1 {

    private boolean playing;//check to see if effect is playing, playing is needed to make sure repeat 
    //plays does not occur else would have to do lengthy check
    private Effect effect;
    private Node effectNode;

    public EffectHandler_1(Effect effect) {
        playing = false;
        this.effect = effect;
    }

    private void setUpEffectNode(Node node) {

        AssetManager assets = Director.getAssetManager();

        for (String e : getEmitters()) {
            Spatial eM = assets.loadModel("Deck/Effects/" + e);
            node.attachChild(eM);
        }

        if (getAudioLocation() != null && !getAudioLocation().equals("")) {
            AudioNode aN = new AudioNode(assets, "Deck/Audio/" + getAudioLocation());
            aN.setLooping(false); //set looping to false to prevent issues in current editor
            aN.setPositional(true);
            //aN.setMaxDistance(.002f);
            node.attachChild(aN);
        }

    }

    public void play(Spatial spat) {

        if (playing == false) {
            Node root = spat.getControl(SkeletonControl.class).getAttachmentsNode(getBoneLocation());

            if (effectNode == null) {
                effectNode = new Node("Effect");
                setUpEffectNode(effectNode);
            }

            root.attachChild(effectNode);
            playEffectNode(effectNode);

            playing = true;
        }

    }

    private void playEffectNode(Spatial spat) {
        if (spat != null) {
            if (spat instanceof ParticleEmitter) {
                ((ParticleEmitter) spat).emitAllParticles();
            } else if (spat instanceof AudioNode) {
                ((AudioNode) spat).play();
            } else if (spat instanceof Node) {
                for (Spatial spat1 : ((Node) spat).getChildren()) {
                    playEffectNode(spat1);
                }
            }
        }
    }

    /**
     * Use cleanUp instead when ending the use of the whole module and not only the effect
     */
    public void destroy() {
        if (playing == true) {
            destroyEffectNode(effectNode);
        }
        effectNode = null;
        effect = null;
        playing = false;
    }

    private void destroyEffectNode(Spatial spat) {
        if (spat != null) {
            //If spat is a node go a level down the tree
            if (spat instanceof Node) {
                for (Spatial spat1 : ((Node) spat).getChildren()) {
                    destroyEffectNode(spat1);
                }
            }
            //If spat is a leaf detach it from parent
            if (spat.getParent() != null) {
                spat.getParent().detachChild(spat);
            }
        }
    }

    public Effect getEffect() {
        return effect;
    }

    public String getBoneLocation() {
        return effect.getBoneLocation();
    }

    public String getAudioLocation() {
        return effect.getAudioLocation();
    }

    public float getStartTime() {
        return effect.getStartTime();
    }

    public float getEndTime() {
        return effect.getEndTime();
    }

    public ArrayList<String> getEmitters() {
        return effect.getEmitters();
    }

    public Boolean isPlaying() {
        return playing;
    }
}
