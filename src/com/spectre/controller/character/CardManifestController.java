/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.spectre.controller.character.impl.ActionControl;
import com.spectre.deck.card.Card;
import com.spectre.deck.controller.CardHandler;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Esper Flair Identification Card TODO: SET UP THIS FIELD LOOK AT BOMBCONTROL
 * FOR ASSISANCE IN jme3test.bullet; IDEA: Attack and Defense power independant
 * of effects Effect attack only take effect after anim is completed defense and
 * others take effect instantly TODO: when loading effects use bonePosition and
 * then offset to get the proper position of the character Remember: remember
 * the call in Skeleton Control getAttachmentsNode Remember: Search and replace
 * all locations that calls Card
 *
 *
 * ANIMATION LOCK look into
 *
 *
 * @author Kyle Williams LET IT BE KNOWN CARD MANIFEST CONTROLLER WILL BECOME
 * ANIMATIONMANIFESTCONTROLLER AND WILL CONTROL ALL ANIMATION OF SPATIAL
 * CHARACTERS attached in SpectreCharacterController should be attached in
 * GameState
 */
public class CardManifestController extends AbstractControl implements ActionControl {

    private LinkedList<CardHandler> cardUpdateQueue;
    private boolean lock = false;//prevents adding cards to the queue if true

    /**
     * Activates a Card Object This function pushes a card onto the card queue
     * if and only if its access is not currently locked
     *
     * @param card
     */
    @Override
    public boolean addToActiveQueue(Card card) {
//        if (isLocked() == false) {
//            CardHandler cardNode = new CardHandler(card, getSpatial());
//            cardUpdateQueue.push(cardNode);//use push instead of add so feedback can find the card eaiser
//            return true;
//        }
        return true;
    } 

    /**
     * Called when button depressed
     *
     * @param card
     */
    @Override
    public void feedBack(String card) {
        for (CardHandler c : cardUpdateQueue) {
            if (c.getName().equals(card)) {
                c.feedBack();
                return;
            }
        }
    }

    /**
     * Checks to see if cardUpdateQueue is locked or free to add new cards
     *
     * @return lock
     */
    @Override
    public boolean isLocked() {
        return lock;
    }

    /**
     * Checks to see if Card is already in Queue <br/><b>Their can be multiple
     * instances</b>
     *
     * @param card
     * @return
     */
    public boolean contains(Card card) {
        for (CardHandler c : cardUpdateQueue) {
            if (c.compareCard(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permanently removes all cards from queue
     */
    @Override
    public void clearQueue() {
        for (CardHandler c : cardUpdateQueue) {
            c.destroy();
        }
        cardUpdateQueue.clear();
    }

    /**
     * Updates the cards until removed from Queue <br/><b>Only Initial checks to
     * lock which is intended.</b>
     *
     * @param tpf
     */
    @Override
    protected void controlUpdate(float tpf) {
        lock = false;

        for (CardHandler cn : cardUpdateQueue) {
            if (cn.isDone() == false) {
                cn.update(tpf);
                if (cn.isAnimPlaying() == true) {
                    lock = true;
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
        } else {
            clearQueue();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial sptl) {
        CardManifestController cont = new CardManifestController();
        cont.setSpatial(sptl);
        cont.cardUpdateQueue.addAll(this.cardUpdateQueue);
        cont.setEnabled(enabled);
        cont.lock = lock;
        return cont;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(lock, "isPlayingAnim", false);
        oc.write(spatial, "spatial", null);
        Savable[] savables = new Savable[cardUpdateQueue.size()];
        cardUpdateQueue.toArray(savables);
        oc.write(savables, "cardUpdateQueue", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        lock = ic.readBoolean("isPlayingAnim", false);
        spatial = (Spatial) ic.readSavable("spatial", null);
        Savable[] savables = (Savable[]) ic.readSavableArray("cardUpdateQueue", null);
        cardUpdateQueue = new LinkedList<CardHandler>();
        for (int i = 0; i > savables.length; i++) {
            cardUpdateQueue.add((CardHandler) savables[i]);
        }
    }

    public void startUp() {
    }

    public void cleanUp() {
    }

    public void ControlChanged() {
    }
}