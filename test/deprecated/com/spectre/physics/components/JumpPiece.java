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
public class JumpPiece implements EntityComponent {

    private boolean jump;

    public JumpPiece() {
        this(false);
    }

    public JumpPiece(boolean jump) {
        this.jump = jump;
    }

    public boolean getIsJump() {
        return jump;
    }

    @Override
    public String toString() {
        return "JumpPiece[jump=" + jump + "]";
    }
}
