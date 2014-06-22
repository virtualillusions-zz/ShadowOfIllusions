/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

import com.jme3.export.Savable;
import com.spectre.deck.card.Card;

/**
 *
 * @author Kyle Williams
 */
public interface ActionControl extends SpectreControl,Savable {

    public void feedBack(String card);

    public boolean isLocked();

    public boolean addToActiveQueue(Card c);

    public void clearQueue();
}
