/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics.components;

import com.spectre.util.math.Vector3fPiece;

/**
 * Is a z-forward vector based on the view direction and the current local x/z
 * plane.
 *
 * @author Kyle
 */
public class ViewDirectionPiece extends Vector3fPiece {

    public ViewDirectionPiece() {
        this(0, 0, 1);
    }

    public ViewDirectionPiece(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public String toString() {
        return "ViewDirectionPiece[" + super.toString() + "]";
    }
}
