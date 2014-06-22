/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author Kyle
 */
public class EvadePiece implements EntityComponent {

    private boolean evade;

    public EvadePiece() {
        this(false);
    }

    public EvadePiece(boolean evade) {
        this.evade = evade;
    }

    public boolean getIsEvade() {
        return evade;
    }

    @Override
    public String toString() {
        return "EvadePiece[evade=" + evade + "]";
    }
}
