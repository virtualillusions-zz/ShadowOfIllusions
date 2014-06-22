/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Characters Life Points at the beginning of a match
 *
 * @author Kyle D. Williams
 */
public class BeginingHealthPiece implements EntityComponent {

    private int BEGINING_HP;

    public BeginingHealthPiece() {
        this(40);
    }

    public BeginingHealthPiece(int value) {
        this.BEGINING_HP = value;
    }

    public int getBeginingHealth() {
        return BEGINING_HP;
    }

    @Override
    public String toString() {
        return "BeginingHealth[" + BEGINING_HP + "]";
    }
}
