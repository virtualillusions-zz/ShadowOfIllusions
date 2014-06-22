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
public class DirectionLeftPiece implements EntityComponent {

    private float directionLeft;

    public DirectionLeftPiece() {
        this(0.0f);
    }

    public DirectionLeftPiece(float directionLeft) {
        this.directionLeft = directionLeft;
    }

    public float getDirectionLeft() {
        return directionLeft;
    }

    @Override
    public String toString() {
        return "DirectionLeftPiece[directionLeft=" + directionLeft + "]";
    }
}
