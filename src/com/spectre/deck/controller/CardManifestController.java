package com.spectre.deck.controller;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.deck.Card;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.spectre.deck;
//
///**
// * Esper Flair Identification Card
// * TODO: SET UP THIS FIELD LOOK AT BOMBCONTROL FOR ASSISANCE IN jme3test.bullet;
// * IDEA: Attack and Defense power independant of effects Effect attack only take effect after anim is completed defense and others take effect instantly
// * TODO: when loading effects use bonePosition and then offset to get the proper position of the character
// * Remember: remember the call in Skeleton Control getAttachmentsNode
// * Remember: Search and replace all locations that calls Card
// * @author Kyle Williams 
//
//
//
//LET IT BE KNOWN CARD MANIFEST CONTROLLER WILL BECOME ANIMATIONMANIFESTCONTROLLER AND WILL CONTROL ALL ANIMATION OF SPATIAL CHARACTERS
// */
public class CardManifestController extends com.jme3.scene.control.AbstractControl implements Savable {

    private LinkedList<CardHandler> cardUpdateQueue;
    private boolean isPlayingAnim = false;

    /**
     * Activates a Card Object
     * This Constructor adds a card to the queue if and only if a card Animation is not currently playing
     * @param card 
     */
    public void addToActiveQueue(Card card) {
        if (isPlaying() == false) {
            CardHandler cardNode = new CardHandler(card, getSpatial());
            cardUpdateQueue.add(cardNode);
        }
    }

    public void setPlayingAnim(boolean isPlaying) {
        this.isPlayingAnim = isPlaying;
    }

    public boolean isPlaying() {
        return isPlayingAnim;
    }

    public boolean contains(Card card) {
        for (CardHandler c : cardUpdateQueue) {
            if (c.compareCard(card)) {
                return true;
            }
        }
        return false;
    }

    public void clearQueue() {
        for (CardHandler c : cardUpdateQueue) {
            c.destroy();
        }
        cardUpdateQueue.clear();
    }

    /***
     * needs work only initial
     * Checks for isPlayingAnim boolean if true other cards will not be allowed to enter the update queue until a uneventful update loop
     * Checks for isDone if it is done then the card can be destroyed and removed from the update Queue;
     * @param f 
     */
    @Override
    protected void controlUpdate(float tpf) {
        isPlayingAnim = false;

        for (CardHandler cn : cardUpdateQueue) {
            if (cn.isDone() == false) {
                cn.update(tpf);
                if (cn.isAnimPlaying() == true) {
                    isPlayingAnim = true;
                }
            } else {
                cn.destroy();
                cardUpdateQueue.remove(cn);
            }
        }
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (cardUpdateQueue == null) {
            cardUpdateQueue = new LinkedList<CardHandler>();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial sptl) {
        CardManifestController cont = new CardManifestController();
        cont.cardUpdateQueue.addAll(this.cardUpdateQueue);
        cont.setEnabled(enabled);
        cont.setPlayingAnim(isPlayingAnim);
        cont.setSpatial(sptl);
        return cont;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(isPlayingAnim, "isPlayingAnim", false);
        oc.write(spatial, "spatial", null);
        Savable[] savables = new Savable[cardUpdateQueue.size()];
        cardUpdateQueue.toArray(savables);
        oc.write(savables, "cardUpdateQueue", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        JOptionPane.showMessageDialog(null, "TEST");
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        isPlayingAnim = ic.readBoolean("isPlayingAnim", false);
        spatial = (Spatial) ic.readSavable("spatial", null);
        Savable[] savables = (Savable[]) ic.readSavableArray("cardUpdateQueue", null);
        cardUpdateQueue = new LinkedList<CardHandler>();
        for (int i = 0; i > savables.length; i++) {
            cardUpdateQueue.add((CardHandler) savables[i]);
        }
    }
}