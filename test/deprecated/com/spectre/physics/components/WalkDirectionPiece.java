/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics.components;

import com.spectre.util.math.Vector3fPiece;

/**
 *
 * @author Kyle
 */
public class WalkDirectionPiece extends Vector3fPiece {

    public WalkDirectionPiece() {
        this(0f, 0f, 0f);
    }

    public WalkDirectionPiece(float X, float Y, float Z) {
        super(X, Y, Z);
    }

    @Override
    public String toString() {
        return "WalkDirectionPiece[" + super.toString() + "]";
    }
}
