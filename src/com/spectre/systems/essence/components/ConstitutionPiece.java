/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence.components;

import com.simsilica.es.EntityComponent;

/**
 * Characters Defense. <br/> 0 indicates normal and a positive or negative value
 * indicates a tenth percentile change
 *
 * @author Kyle D. Williams
 */
public class ConstitutionPiece implements EntityComponent {

    private int CON;

    public ConstitutionPiece() {
        this(0);
    }

    public ConstitutionPiece(int value) {
        CON = value;
    }

    public int getConstitution() {
        return CON;
    }

    @Override
    public String toString() {
        return "Constitution[" + CON + "]";
    }
}
