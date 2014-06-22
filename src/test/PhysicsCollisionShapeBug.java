/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Kyle Williams
 */
public class PhysicsCollisionShapeBug extends SimpleApplication {

    public static void main(String[] args) {
        PhysicsCollisionShapeBug app = new PhysicsCollisionShapeBug();
        app.start();
    }
    private BulletAppState bulletAppState;
    private CollisionShape[] shapes;
    private RigidBodyControl rigidBody;
    private boolean firstShape;

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        //box with changing collisionShape
        Spatial spat = setUpModel(new Vector3f(0, 5, 0), 1, 1, 1, ColorRGBA.Green);
        recalculateShapes(spat);
        rigidBody = setUpPhysics(spat, shapes[0]);
        //floor
        Spatial floor = setUpModel(Vector3f.ZERO, 10, 1, 10, ColorRGBA.Gray);
        setUpPhysics(floor, null);


        setUpKeys();

        this.getFlyByCamera().setMoveSpeed(6f);
        this.getCamera().setLocation(new Vector3f(4, 5, 4));
    }

    public Spatial setUpModel(Vector3f location, float x, float y, float z, ColorRGBA color) {
        Geometry cube_leak = new Geometry("Cube", new Box(x, y, z));
        Material mat_tl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tl.setColor("Color", color); // purple
        cube_leak.setMaterial(mat_tl);
        cube_leak.setLocalTranslation(location);
        rootNode.attachChild(cube_leak);
        return cube_leak;
    }

    public RigidBodyControl setUpPhysics(Spatial spat, CollisionShape shape) {
        RigidBodyControl body;
        if (shape == null) {
            body = new RigidBodyControl(0);
        } else {
            body = new RigidBodyControl(shape, 8);
        }
        spat.addControl(body);
        bulletAppState.getPhysicsSpace().add(body);
        return body;
    }

    private void recalculateShapes(Spatial spatial) {
        shapes = new CollisionShape[2];
        firstShape = false;
        BoundingBox bb = (BoundingBox) spatial.getWorldBound();
        //use smaller side extent over larger side to create a more narrow hitbox 
        //and because A-Pose and T-Pose difference greatly impacts value
        float diameter = bb.getXExtent() < bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        float height = bb.getYExtent();

        //create collision shapes and wrap them in compound shape to help with offset
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        shapes[0] = new CapsuleCollisionShape(diameter, height);//standing            
        ccs.addChildShape(shapes[0], new Vector3f(0, -height * 0.1f, 0));
        shapes[0] = ccs;

        ccs = new CompoundCollisionShape();
        shapes[1] = new CapsuleCollisionShape(diameter, height * 0.6f);//ducked        
        ccs.addChildShape(shapes[1], new Vector3f(0, -(height * 0.6f), 0));
        shapes[1] = ccs;
    }

    public void setUpKeys() {
        inputManager.addMapping("Switch Shape", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(new ActionListener() {
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("Switch Shape") && keyPressed) {
                    CollisionShape shape = firstShape ? shapes[0] : shapes[1];
                    firstShape = !firstShape;
                    rigidBody.setCollisionShape(shape);
                }
            }
        }, "Switch Shape");
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        getCamera().lookAt(rigidBody.getPhysicsLocation(), Vector3f.UNIT_Y);
    }
}
