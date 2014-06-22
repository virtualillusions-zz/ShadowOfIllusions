/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SimpleAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.camera.CameraSystem;
import com.spectre.scene.camera.components.CameraPiece;
import com.spectre.scene.visual.VisualSystem;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.scene.visual.components.VisualRepPiece;
import com.spectre.app.input.Buttons;
import com.spectre.systems.physics.PhysicsSystem;
import test.physics.PhysicsTestHelper;

/**
 *
 * @author Kyle
 */
public class TestPhysicsSystem extends SimpleAppState {
    
    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new SpectreApplicationState(),
                new VisualSystem(),
                new CameraSystem(),
                new PhysicsSystem(),
                new TestPhysicsSystem()) {
            @Override
            public void simpleInitApp() {
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        //settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }
    private EntityId id;
    
    @Override
    public void SimpleAppState() {
        //create structure
        PhysicsTestHelper.createPhysicsTown(
                getsAppState().getRootNode(),
                getsAppState().getAssetManager(),
                getPhysicsSpace());
        
        id = getEntityData().createEntity();
        Buttons.setUpRemote(getInputManager(), 1, id + "");
        Buttons.setUp360Remote(getInputManager(), 1, id + "");
        getEntityData().setComponents(id,
                new VisualRepPiece("testData/Jaime/Jaime.j3o"),
                new InScenePiece(), new CameraPiece(1));
        Spatial spat = getAssetManager().loadModel("testData/Jaime/Jaime.j3o");
        spat.setLocalScale(5f);
        getModelBindingsList().put(id, spat);
        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        getRootNode().addLight(ambient);
    }
}
