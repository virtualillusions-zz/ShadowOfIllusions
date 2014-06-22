/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.test.system;

import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import deprecated.com.spectre.app.SpectreApplication;
import deprecated.com.spectre.removal.EntityRemovalSystem;
import deprecated.com.spectre.removal.components.EntityRemovalPiece;

/**
 *
 * @author Kyle Williams
 */
public class TestEntityRemovalSystem extends SpectreApplication {

    public static void main(String[] args) {
        TestEntityRemovalSystem app = new TestEntityRemovalSystem();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void SpectreApp() {
        getStateManager().attach(new EntityRemovalSystem());
    }

    @Override
    public void spectreUpdate(float tpf) {
        //Creates a new EntityId, the id is handled as an object to prevent botching
        EntityId entityId = getEntityData().createEntity();
        //A new TestComponent is added to the Entity
        getEntityData().setComponent(entityId, new EntityRemovalPiece());
    }

    @Override
    public void spectreRender(RenderManager rm) {
    }
}
