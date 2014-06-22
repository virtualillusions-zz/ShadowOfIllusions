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
public class PhysicsDampingPiece implements EntityComponent {

    private float physicsDamping;

    public PhysicsDampingPiece() {
        this(0.9f);
    }

    public PhysicsDampingPiece(float physicsDamping) {
        this.physicsDamping = physicsDamping;
    }

    public float getPhysicsDamping() {
        return physicsDamping;
    }

    @Override
    public String toString() {
        return "PhysicsDampingPiece[physicsDamping=" + physicsDamping + "]";
    }
}
