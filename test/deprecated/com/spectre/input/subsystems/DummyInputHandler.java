/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input.subsystems;

import com.simsilica.es.Entity;
import deprecated.com.spectre.app.SpectreApplication;
import java.util.logging.Level;

/**
 *
 * @author Kyle Williams
 */
public class DummyInputHandler extends InputHandler {

    @Override
    public void onAction(Entity entity, String name, boolean isPressed, float tpf) {
        SpectreApplication.logger.log(Level.INFO, "Entity[{0}] pressed {1}({2},{3})",
                new Object[]{entity.getId().getId(), name, isPressed, tpf});
    }

    @Override
    public void onAnalog(Entity entity, String name, float value, float tpf) {
        SpectreApplication.logger.log(Level.INFO, "Entity[{0}] pressed {1}({2},{3})",
                new Object[]{entity.getId().getId(), name, value, tpf});
    }
}
