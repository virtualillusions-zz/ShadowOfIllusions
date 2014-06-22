/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Character state healthy tired or injured used to prevent reseting of idle
 *
 * @author Kyle D. Williams
 */
public class CharacterStatePiece implements EntityComponent {

    private int type;

    public CharacterStatePiece() {
        this(CharacterState.Healthy);
    }

    public CharacterStatePiece(CharacterState type) {
        this.type = type.ordinal();
    }

    public CharacterState getType() {
        return CharacterState.values()[type];
    }

    @Override
    public String toString() {
        return "CharacterStatePiece[type=" + getType() + "]";
    }

    public enum CharacterState {

        Healthy, Tired, Injured;
    }
}
