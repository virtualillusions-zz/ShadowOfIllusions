/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input.components;

import com.simsilica.es.EntityComponent;

/**
 * A component used to determine if the player is bound to a controller
 *
 * @author Kyle
 */
public class ControllerPiece implements EntityComponent {

    private ControllerType contType;
    private int playerNum;

    public ControllerPiece(ControllerType type, int playerNum) {
        this.contType = type;
        this.playerNum = playerNum;
    }

    /**
     * Returns the InputHandler Type
     *
     * @return contType ControllerType
     */
    public ControllerType getControllerType() {
        return contType;
    }

    /**
     * Returns the player number
     *
     * @return playerNum Integer
     */
    public int getPlayerNumber() {
        return playerNum;
    }

    public enum ControllerType {

        DEBUG,
        BASIC;
    }
}
