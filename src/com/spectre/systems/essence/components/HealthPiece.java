/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * The Characters current Life Point value
 *
 * @author Kyle D. Williams
 */
public class HealthPiece implements EntityComponent {

    private int HP;

    public HealthPiece() {
        this(40);
    }

    public HealthPiece(int value) {
        this.HP = value;
    }

    public int getHealth() {
        return HP;
    }

    @Override
    public String toString() {
        return "Health[" + HP + "]";
    }
}
