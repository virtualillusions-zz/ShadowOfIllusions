/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.system;

import com.google.common.collect.Lists;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SimpleAppState;
import com.spectre.app.input.Buttons;
import com.spectre.scene.camera.CameraSystem;
import com.spectre.scene.camera.components.CameraPiece;
import com.spectre.scene.visual.components.InScenePiece;
import java.util.ArrayList;
import test.physics.PhysicsTestHelper;

/**
 *
 * @author Kyle
 */
public class TestCameraSystem extends SimpleAppState {

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new CameraSystem(),
                new TestCameraSystem()) {
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
    private ArrayList<EntityId> ids = Lists.newArrayList();
    private int idCount = 0;

    @Override
    public void SimpleAppState() {
        PhysicsTestHelper.createPhysicsTestWorldSoccer(getsAppState().getRootNode(), getsAppState().getAssetManager(), getPhysicsSpace());
        //Creates a new EntityId, the id is handled as an object to prevent botching
        EntityId entityId = getEntityData().createEntity();
        ids.add(entityId);
        Buttons.setUpRemote(getInputManager(), 1, entityId + "");
        getEntityData().setComponent(entityId, new CameraPiece(1));


        entityId = getEntityData().createEntity();
        ids.add(entityId);
        getEntityData().setComponent(entityId, new CameraPiece(2));

        entityId = getEntityData().createEntity();
        ids.add(entityId);
        getEntityData().setComponent(entityId, new CameraPiece(3));

        entityId = getEntityData().createEntity();
        ids.add(entityId);
        getEntityData().setComponent(entityId, new CameraPiece(4));

        actionListener.onAction("add", true, 0);
        actionListener.onAction("add", true, 0);
        actionListener.onAction("add", true, 0);
        actionListener.onAction("add", true, 0);
        getInputManager().addMapping("add", new KeyTrigger(KeyInput.KEY_A));
        getInputManager().addMapping("remove", new KeyTrigger(KeyInput.KEY_R));
        getInputManager().addMapping("change", new KeyTrigger(KeyInput.KEY_C));
        getInputManager().addListener(actionListener, "add", "remove", "change");
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if (pressed) {
                System.out.println(name);
                if (name.equals("add")) {
                    if (idCount >= 4) {//remember original entity not in list
                        return;
                    }
                    //Creates a new EntityId, the id is handled as an object to prevent botching
                    EntityId entityId = ids.get(idCount);
                    //Add test Model
                    Spatial spat = getModelBindingsList().get(entityId);
                    if (spat == null) {
                        getModelBindingsList().put(entityId, createBox(new Vector3f(-(idCount + 1) * 4, 1, 0)));
                        spat = getModelBindingsList().get(entityId);
                    }

                    getModelNode().attachChild(spat);
                    //A new TestComponent is added to the Entity
                    getEntityData().setComponent(entityId, new InScenePiece());
                    idCount++;
                } else if (name.equals("remove")) {
                    if (idCount <= 1) {
                        return;
                    }
                    EntityId entityId = ids.get(idCount - 1);

                    Spatial spat = getModelBindingsList().get(entityId);
                    getModelNode().detachChild(spat);
                    getEntityData().removeComponent(entityId, InScenePiece.class);
                    idCount--;
                } else if (name.equals("change")) {
                    for (int i = 0; i > ids.size(); i++) {
                        getEntityData().setComponent(ids.get(i), new InScenePiece());
                    }
                }
            }
        }
    };

    private Spatial createBox(Vector3f location) {
        if (idCount == 0) {
            return PhysicsTestHelper.createControllableNonPhysicsObject(getModelNode(), getAssetManager(), getPhysicsSpace(), getInputManager(), new Vector3f(0, 1, 0));
        }
        Material material = new Material(getAssetManager(), "Common/MatDefs/Misc/ColoredTextured.j3md");//"Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", getAssetManager().loadTexture("Textures/ColoredTex/Monkey.png"));
        material.setColor("Color", getColor());
        Box box = new Box(1, 1, 1);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        final Node node = new Node("Object");
        node.attachChild(boxGeometry);
        node.setLocalTranslation(location);
        return node;
    }

    private ColorRGBA getColor() {
        ColorRGBA colour = null;
        switch (idCount) {
            case 1:
                colour = ColorRGBA.Blue;
                break;
            case 2:
                colour = ColorRGBA.Green;
                break;
            case 3:
                colour = ColorRGBA.Yellow;
                break;
            default:
        }
        return colour;
    }

    @Override
    protected void spectreUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanUp() {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}