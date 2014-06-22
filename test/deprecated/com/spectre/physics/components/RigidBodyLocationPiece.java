/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics.components;

import com.spectre.util.math.Vector3fPiece;

/**
 * Stores final spatial location, corresponds to RigidBody location.
 *
 * @author Kyle
 */
public class RigidBodyLocationPiece extends Vector3fPiece {

    public RigidBodyLocationPiece() {
        this(0f, 0f, 0f);
    }

    public RigidBodyLocationPiece(float X, float Y, float Z) {
        super(X, Y, Z);
    }

    @Override
    public String toString() {
        return "RigidBodyLocationPiece[" + super.toString() + "]";
    }
}
