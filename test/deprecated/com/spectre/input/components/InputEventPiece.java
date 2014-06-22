/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author Kyle
 */
public class InputEventPiece implements EntityComponent {

    private InputEventType type;

    public InputEventPiece(InputEventType type) {
        this.type = type;
    }

    public InputEventType getType() {
        return type;
    }

    public enum InputEventType {

        CAMERA;
    }
}
