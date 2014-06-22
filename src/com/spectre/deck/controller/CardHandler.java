/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.controller;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Spatial;
import com.spectre.controller.character.SpectreAnimationController;
import com.spectre.deck.card.Animation;
import com.spectre.deck.card.Card;

/**
 *
 * @author Kyle Williams
 */
public class CardHandler implements AnimEventListener {

    private final Card card;
    private final String name;
    private final Animation anim;
    private final EffectHandler passiveEffect,
            activeEffect,
            contactEffect;
    private Spatial spat;
    private AnimControl control;
    private AnimChannel mainChannel;
    //Used to Determine if this particular Card Animation is still running
    private boolean cardAnimPlaying;
    private float timer;
    private boolean contact;
    private boolean isDone; //is this card finished updating
    private SpectreAnimationController sac;

    public CardHandler(Card c, Spatial spatial) {
        card = c;
        name = c.getName();
        anim = card.getAnimation();
        spat = spatial;
        isDone = false;
        contact = false;
        passiveEffect = new EffectHandler(card.getPassiveEffect());
        activeEffect = new EffectHandler(card.getActiveEffect());
        contactEffect = new EffectHandler(card.getContactEffect());
        control = spat.getControl(com.jme3.animation.AnimControl.class);
        mainChannel = control.getChannel(0);
        sac = spat.getControl(SpectreAnimationController.class);
        animInit();
        timer = 0;
    }

    public String getName() {
        return name;
    }

    /**
     * Called when button depressed
     *
     * @param card
     */
    public void feedBack() {
    }

    /**
     * Compare card with one within handler and returns true if valid
     *
     * @param c
     * @return
     */
    public boolean compareCard(Card c) {
        return card.equals(c);
    }

    private void animInit() {
        cardAnimPlaying = true;
        //FIX ANIMCNTRL
        //sac.changeAnimation(anim.getAnimationName(), SpectreAnimationController.AnimPriority.Level_2, LoopMode.DontLoop, anim.getSpeed(), anim.getStartTime());
        control.addListener(this);
    }

    @Override
    public void onAnimCycleDone(AnimControl ac, AnimChannel ac1, String string) {
        if (anim.getAnimationName().equals(string) && cardAnimPlaying == true) {
            control.removeListener(this);
            cardAnimPlaying = false;
            //This has to be fixed for interruptions 
            //NO IT DOESN"T THE PROBLEM IS BECAUSE ANIMDONE IS SET UP INCORRECTLY
            animDone();
        }
    }

    @Override
    public void onAnimChange(AnimControl ac, AnimChannel ac1, String string) {
        if (!anim.getAnimationName().equals(string) && cardAnimPlaying == true) {
            control.removeListener(this);
            cardAnimPlaying = false;
            destroy();
            //If a differnt annimation is coming up while this Card's Animation
            //should play then it has been interrupted and should be taken out of update
            isDone = true;
        }
    }

    public void update(float tpf) {
        if (cardAnimPlaying == false) {
            timer += tpf;
        }

        basicEffectUpdate(passiveEffect, true);
        basicEffectUpdate(activeEffect, false);

        if (contact == true) {
            passiveEffect.play(spat);
        }
    }

    private void basicEffectUpdate(EffectHandler eH, boolean destroyOK) {
        if (eH.getEffect() != null) { //Anything looking for values in effect needs to be last as effect is set to null when destroyed
            if (eH.isPlaying() == false && mainChannel.getTime() >= eH.getStartTime()) {
                eH.play(spat);
            } else if (destroyOK == true && eH.isPlaying() == true && mainChannel.getTime() >= eH.getEndTime()) {
                eH.destroy();
            }
        }
    }

    private void animDone() {
        isDone = true;
//        if (!activeEffect.isPlaying()) {
        //           activeEffect.play(spat);
//        }

//        Node bulletg = activeEffect.play(null);
//        if (bulletg == null) {
//            bulletg = activeEffect.play(cmc.getSpatial());
//
//            JOptionPane.showMessageDialog(null, "WTF");
//        }
//        //Quaternion rot = bulletg.getParent().getWorldRotation();
//        //Vector3f dir = rot.getRotationColumn(2);
//        SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(1);
//        RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 1 * 10);
//        //bulletNode.setCcdMotionThreshold(0.001f);
//        bulletNode.setLinearVelocity(new Vector3f(0, 1, 0).mult(80));
//        if (bulletg == null) {
//            JOptionPane.showMessageDialog(null, "Holy CRAP!!!");
//        }
//        bulletg.addControl(bulletNode);
//        Director.getPhysicsSpace().add(bulletNode);
    }

    /**
     * This method is used for the CardManifestController to determine if a new
     * Card can be added to the controller update
     *
     * @param isPlaying
     */
    public boolean isAnimPlaying() {
        return cardAnimPlaying;
    }

    public boolean isDone() {
        return isDone;
    }

    public void destroy() {
        passiveEffect.destroy();
        activeEffect.destroy();
        contactEffect.destroy();
        //cmc.getCardUpdateQueue().remove(this);
    }
}