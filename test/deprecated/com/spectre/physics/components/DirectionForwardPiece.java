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
public class DirectionForwardPiece implements EntityComponent {

    private float directionForward;

    public DirectionForwardPiece() {
        this(0.0f);
    }

    public DirectionForwardPiece(float directionForward) {
        this.directionForward = directionForward;
    }

    public float getDirectionForward() {
        return directionForward;
    }

    @Override
    public String toString() {
        return "DirectionForwardPiece[directionForward=" + directionForward + "]";
    }
}
