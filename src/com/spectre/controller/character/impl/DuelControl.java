/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character.impl;

/**
 *
 * @author Kyle Williams
 */
public interface DuelControl extends SpectreControl{

    public void cardPressed(int i, boolean pressed);

    public void gatherFocus(boolean pressed);

    public void cardInfo(int i, boolean pressed);

    public void reshuffleDeck();
    
}
