/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Characters movement speed. <br/> 0 indicates normal and a positive or
 * negative value indicates a tenth percentile change
 *
 * @author Kyle D. Williams
 */
public class DexterityPiece implements EntityComponent {

    private int DEX;

    public DexterityPiece() {
        this(0);
    }

    public DexterityPiece(int value) {
        DEX = value;
    }

    public int getDexterity() {
        return DEX;
    }

    @Override
    public String toString() {
        return "Dexterity[" + DEX + "]";
    }
}
