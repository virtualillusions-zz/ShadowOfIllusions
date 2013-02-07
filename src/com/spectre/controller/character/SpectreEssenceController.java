/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.SpectreAbstractController;
import com.spectre.deck.card.Card;
import java.util.LinkedList;

/**
 * TODO: WTF WAS I THINKING WITH THIS CONTROLLER!!
 * This Controller 
 *  This Controller handles Life And Focus meters as well as its GUI Representation
 *  TODO: Focus should equal 0 and be augmented over time
 * when focus passed threshold state is tired
 * when health pass threshold state is injured and takes presedence over focus
 * @author Kyle Williams
 */
//TODO:Implement All stats in this class this includes speed attack speed as well as cardgeneration time
public class SpectreEssenceController extends SpectreAbstractController {

    private final static int MAXIMUM_FOCUS = 20;
    private final static int STARTING_LIFE = 40;
    private int Life = STARTING_LIFE;
    private int maxFocus = MAXIMUM_FOCUS;
    private int Focus = 0;
    private float focusTimeInterval = .5f;//The higher the slower
    private float Time = 0;
    private float lfiTime = 0;  //last Focus Increment Time is the time since focus last set
    //private GuiEssenceController gui;
    private float totemSpawnInterval = 10;//TODO: nah totem spawn interval is based on val from deck
    private boolean isPressed = false;
    private boolean overTotem = false;
    private final Card[] cards = new Card[4];
    private LinkedList<String> deck;

    public int getLife() {
        return Life;
    }

    public int getFocus() {
        return Focus;
    }

    public int getTotalFocus() {
        return maxFocus;
    }

    public void incrementLife(int amount) {
        Life += amount;
        //gui.setLife(Life);
        checkCharacterState();
    }

    public void incrementFocus(int amount) {
        Focus += amount;
        lfiTime = Time;//Add here so if focus added by card will reset lfi
        //gui.setFocus(CurrentFocus, Focus);
        checkCharacterState();
    }

    public void incrementMaxFocus(int amount) {
        maxFocus += amount;
    }

    public void setFocusTimeInterval(float newTimeInterval) {
        focusTimeInterval = newTimeInterval;
    }

    public void setActiveDeck(LinkedList<String> newDeck) {
        deck = newDeck;
        java.util.Collections.shuffle(newDeck);//shuffle so elements are random
    }

    public String getNextCard() {
        return deck.poll();
    }

    public void cardPressed(int cardNum, boolean isPressed) {
//        Card c = null;
//        if (overTotem) {
//            //TODO: Somehow get card player is standing over then remove it from scene.
//            cards[cardNum - 1] = c;
//            return c;
//        }
//        this.isPressed = isPressed;
//        if (isPressed == true) {
//            return null;
//        }
//        //TODO:check card requirements else return null;
//        c = cards[cardNum - 1];
//        return c;
    }

    private void checkCharacterState() {
        int characterState = 1; //is the character 1.healthy, 2.tired or 3.injured

        if (Life <= STARTING_LIFE / 2) {
            characterState = 2;
        } else if (Focus <= maxFocus / 2) {
            characterState = 3;
        }
        getAnimCont().changeCharState(characterState);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (Focus < maxFocus) {
            if ((Time - lfiTime) > focusTimeInterval) {
                incrementFocus(1);
            } else {
                Time += tpf;
            }
        } else {
            lfiTime = Time;
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        SpectreEssenceController sec = new SpectreEssenceController();
        sec.Life = Life;
        sec.maxFocus = maxFocus;
        sec.Focus = Focus;
        sec.focusTimeInterval = focusTimeInterval;
        sec.Time = Time;
        sec.lfiTime = lfiTime;
        sec.totemSpawnInterval = totemSpawnInterval;
        sec.isPressed = isPressed;
        sec.overTotem = overTotem;
        sec.cards[0] = cards[0];
        sec.cards[1] = cards[1];
        sec.cards[2] = cards[2];
        sec.cards[3] = cards[3];
        sec.deck = deck;
        sec.setSpatial(spatial);
        return sec;
    }

    public float getTotemSpawnInterval() {
        return totemSpawnInterval;
    }
    //public void setGuiController(GuiEssenceController GUI) {
    //this.gui = GUI;
    //gui.start(spatial);
    //}

    void cardInfo(int i, boolean pressed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void gatherFocus(boolean pressed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void reshuffleDeck() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}