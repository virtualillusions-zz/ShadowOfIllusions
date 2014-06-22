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
public class DashPiece implements EntityComponent {

    private boolean dash;

    public DashPiece() {
        this(false);
    }

    public DashPiece(boolean dash) {
        this.dash = dash;
    }

    public boolean getIsDash() {
        return dash;
    }

    @Override
    public String toString() {
        return "DashPiece[dash=" + dash + "]";
    }
}
