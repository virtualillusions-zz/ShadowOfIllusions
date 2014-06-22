/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.test.system;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import deprecated.com.spectre.app.SpectreApplication;
import com.spectre.app.input.Buttons;
import deprecated.com.spectre.input.InputSystem;
import deprecated.com.spectre.input.components.ControllerPiece;

/**
 *
 * @author Kyle Williams
 */
public class TestInputSystem extends SpectreApplication {

    private EntityId entityId;

    public static void main(String[] args) {
        TestInputSystem app = new TestInputSystem();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void SpectreApp() {
        getStateManager().attach(new InputSystem());


        getStateManager().attach(new AbstractAppState() {
            @Override
            public void initialize(AppStateManager stateManager, Application app) {
                super.initialize(stateManager, app);

                //Creates a new EntityId, the id is handled as an object to prevent botching
                entityId = getEntityData().createEntity();
                //A new TestComponent is added to the Entity
                getEntityData().setComponent(entityId, new ControllerPiece(ControllerPiece.ControllerType.DEBUG, 1));
                Buttons.setUpRemote(inputManager, 1, entityId + "");
                //XBOX CONTROLLER NOT WORKING FIX LATER
                Buttons.setUp360Remote(inputManager, 1, entityId + "");
            }
        });
    }

    @Override
    public void spectreUpdate(float tpf) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void spectreRender(RenderManager rm) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
