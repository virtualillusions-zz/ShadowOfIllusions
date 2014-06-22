/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.test.system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SimpleAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.scene.visual.components.VisualRepPiece;
import com.spectre.app.input.Buttons; 
import com.spectre.scene.visual.components.ActionModePiece;
import deprecated.com.spectre.physics.CharacterPhysicsSystem;
import test.physics.PhysicsTestHelper;
 
/**
 *
 * @author Kyle
 */
public class TestCharacterPhysicsSystem extends SimpleAppState {

    public static void main(String[] args) {
        Application app = new SimpleApplication(new SpectreApplicationState(),
                new CharacterPhysicsSystem(),
                new TestCharacterPhysicsSystem()) {
            @Override
            public void simpleInitApp() {
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }
    private ChaseCamera chaseCamera;
    private CameraControl cameraControl;
    private Node spat;
    private Node cameraOffset;
    private boolean change;
    private EntityId id;

    @Override
    public void SimpleAppState() {
        PhysicsTestHelper.createPhysicsTestWorldSoccer(getsAppState().getRootNode(), getsAppState().getAssetManager(), getPhysicsSpace());

        String model = "testData/Jaime/Jaime.j3o";
        id = getEntityData().createEntity();
        Buttons.setUpRemote(getInputManager(), 1, id + "");
        getEntityData().setComponents(id,
                new VisualRepPiece(model),
                new InScenePiece());
        spat = (Node) getApp().getAssetManager().loadModel(model);
        getsAppState().getModelBindingsList().put(id, spat);
        getsAppState().getRootNode().attachChild(spat);
        cameraOffset = new Node("Camera Offset");
        spat.attachChild(cameraOffset);
        cameraOffset.setLocalTranslation(0, 1.5f, -2f);
        cameraControl = new CameraControl(getApp().getCamera());
        chaseCamera = new ChaseCamera(getApp().getCamera(), getInputManager());
        spat.addControl(chaseCamera);
        change = false;

        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        getRootNode().addLight(ambient);

        getInputManager().addMapping("change", new KeyTrigger(KeyInput.KEY_C));
        getInputManager().addMapping("action", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        getInputManager().addListener(actionListener, "change", "action");
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {

            //System.out.println(name);
            if (pressed && name.equals("change")) {
                if (!change) {
                    cameraOffset.addControl(cameraControl);
                    spat.removeControl(chaseCamera);
                } else {
                    cameraOffset.removeControl(cameraControl);
                    spat.addControl(chaseCamera);
                }
                change = !change;
            }
            if (name.equals("action")) {
                getEntityData().setComponent(id, new ActionModePiece(pressed));
            }

        }
    };

    @Override
    protected void spectreUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanUp() {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}
