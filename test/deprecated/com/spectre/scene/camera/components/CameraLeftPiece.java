/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.scene.camera.components;

import com.jme3.renderer.Camera;
import com.spectre.util.math.Vector3fPiece;

/**
 * <code>getLeft</code> retrieves the left axis of the camera.
 *
 * @return the left axis of the camera.
 * @see Camera#getLeft()
 * @author Kyle
 */
public class CameraLeftPiece extends Vector3fPiece {

    public CameraLeftPiece() {
        this(0, 0, 0);
    }

    public CameraLeftPiece(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public String toString() {
        return "CameraLeftPiece[" + super.toString() + "]";
    }
}
