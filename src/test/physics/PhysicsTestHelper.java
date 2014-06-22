/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.physics;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 * @author normenhansen
 */
public class PhysicsTestHelper {

    /**
     * creates a simple physics test world with a floor, an obstacle and some
     * test boxes
     *
     * @param rootNode
     * @param assetManager
     * @param space
     */
    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        floorGeometry.addControl(new RigidBodyControl(0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        //movable boxes
        for (int i = 0; i < 12; i++) {
            Box box = new Box(0.25f, 0.25f, 0.25f);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(material);
            boxGeometry.setLocalTranslation(i, 5, -3);
            //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
            boxGeometry.addControl(new RigidBodyControl(2));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }

        //immovable sphere with mesh collision shape
        Sphere sphere = new Sphere(8, 8, 1);
        Geometry sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setMaterial(material);
        sphereGeometry.setLocalTranslation(4, -4, 2);
        sphereGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(sphere), 0));
        rootNode.attachChild(sphereGeometry);
        space.add(sphereGeometry);

    }

    public static void createPhysicsTestWorldSoccer(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box floorBox = new Box(20, 0.25f, 20);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        floorGeometry.addControl(new RigidBodyControl(0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        //movable spheres
        for (int i = 0; i < 5; i++) {
            Sphere sphere = new Sphere(16, 16, .5f);
            Geometry ballGeometry = new Geometry("Soccer ball", sphere);
            ballGeometry.setMaterial(material);
            ballGeometry.setLocalTranslation(i, 2, -3);
            //RigidBodyControl automatically uses Sphere collision shapes when attached to single geometry with sphere mesh
            ballGeometry.addControl(new RigidBodyControl(.001f));
            ballGeometry.getControl(RigidBodyControl.class).setRestitution(1);
            rootNode.attachChild(ballGeometry);
            space.add(ballGeometry);
        }
        {
            //immovable Box with mesh collision shape
            Box box = new Box(1, 1, 1);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(material);
            boxGeometry.setLocalTranslation(4, 1, 2);
            boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }
        {
            //immovable Box with mesh collision shape
            Box box = new Box(1, 1, 1);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(material);
            boxGeometry.setLocalTranslation(4, 3, 4);
            boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }
    }

    /**
     * creates a box geometry with a RigidBodyControl
     *
     * @param assetManager
     * @return
     */
    public static Geometry createPhysicsTestBox(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box box = new Box(0.25f, 0.25f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
        boxGeometry.addControl(new RigidBodyControl(2));
        return boxGeometry;
    }

    /**
     * creates a sphere geometry with a RigidBodyControl
     *
     * @param assetManager
     * @return
     */
    public static Geometry createPhysicsTestSphere(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Sphere sphere = new Sphere(8, 8, 0.25f);
        Geometry boxGeometry = new Geometry("Sphere", sphere);
        boxGeometry.setMaterial(material);
        //RigidBodyControl automatically uses sphere collision shapes when attached to single geometry with sphere mesh
        boxGeometry.addControl(new RigidBodyControl(2));
        return boxGeometry;
    }

    /**
     * creates an empty node with a RigidBodyControl
     *
     * @param manager
     * @param shape
     * @param mass
     * @return
     */
    public static Node createPhysicsTestNode(AssetManager manager, CollisionShape shape, float mass) {
        Node node = new Node("PhysicsNode");
        RigidBodyControl control = new RigidBodyControl(shape, mass);
        node.addControl(control);
        return node;
    }

    /**
     * creates the necessary inputlistener and action to shoot balls from teh
     * camera
     *
     * @param app
     * @param rootNode
     * @param space
     */
    public static void createBallShooter(final Application app, final Node rootNode, final PhysicsSpace space) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                Sphere bullet = new Sphere(32, 32, 0.4f, true, false);
                bullet.setTextureMode(Sphere.TextureMode.Projected);
                Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
                key2.setGenerateMips(true);
                Texture tex2 = app.getAssetManager().loadTexture(key2);
                mat2.setTexture("ColorMap", tex2);
                if (name.equals("shoot") && !keyPressed) {
                    Geometry bulletg = new Geometry("bullet", bullet);
                    bulletg.setMaterial(mat2);
                    bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    bulletg.setLocalTranslation(app.getCamera().getLocation());
                    RigidBodyControl bulletControl = new RigidBodyControl(10);
                    bulletg.addControl(bulletControl);
                    bulletControl.setLinearVelocity(app.getCamera().getDirection().mult(25));
                    bulletg.addControl(bulletControl);
                    rootNode.attachChild(bulletg);
                    space.add(bulletControl);
                }
            }
        };
        app.getInputManager().addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(actionListener, "shoot");
    }

    public static void createPhysicsGhostObject(Node rootNode, Vector3f location, PhysicsSpace space) {
        Vector3f halfExtents = new Vector3f(1, 1, 1);
        final GhostControl ghostControl = new GhostControl(new BoxCollisionShape(halfExtents));
        Node node = new Node("Ghost Object");
        node.addControl(ghostControl);
        node.setLocalTranslation(location);
        rootNode.attachChild(node);
        space.add(ghostControl);
        space.addCollisionListener(new PhysicsCollisionListener() {
            @Override
            public void collision(PhysicsCollisionEvent event) {
                System.out.println("Collided with Debug GhostObject[" + ghostControl.getOverlappingObjects().size() + "]");
            }
        });
    }

    public static Node createControllableNonPhysicsObject(Node rootNode, AssetManager assetManager, PhysicsSpace space, InputManager inputManager,Vector3f location) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box box = new Box(1, 1, 1);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        final Node node = new Node("Object");
        node.attachChild(boxGeometry);
        node.setLocalTranslation(location);
        rootNode.attachChild(node);
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("CharBack", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_M));
        AnalogListener analogListener = new AnalogListener() {
            @Override
            public void onAnalog(String binding, float value, float tpf) {
                if (binding.equals("CharLeft")) {
                    node.move(-10 * tpf, 0, 0);
                } else if (binding.equals("CharRight")) {
                    node.move(10 * tpf, 0, 0);
                } else if (binding.equals("CharForward")) {
                    node.move(0, 0, -10 * tpf);
                } else if (binding.equals("CharBack")) {
                    node.move(0, 0, 10 * tpf);
                } else if (binding.equals("CharUp")) {
                    node.move(0, 10 * tpf, 0);
                } else if (binding.equals("CharDown")) {
                    node.move(0, -10 * tpf, 0);
                }
            }
        };

        inputManager.addListener(analogListener, "CharLeft", "CharRight", "CharForward", "CharBack", "CharUp", "CharDown");

        return node;
    }
    
      public static void createPhysicsTown(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);
        Node floorGeometry = (Node) assetManager.loadModel("testData/town/main.scene");
        floorGeometry.setLocalTranslation(0, -5, 0);
        floorGeometry.scale(3f);
        floorGeometry.addControl(new RigidBodyControl(0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);
    }

    public static void createPhysicsWildHouse(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);
        Node floorGeometry = (Node) assetManager.loadModel("testData/wildhouse/main.scene");
        floorGeometry.setLocalTranslation(0, -5, 0);
        floorGeometry.scale(3f);
        floorGeometry.addControl(new RigidBodyControl(0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);
    }
}
