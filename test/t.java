
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kyle Williams
 */
public class t extends SimpleApplication {

    //   private BulletAppState bulletAppState;
    private float modelExtent;
    private Spatial spat;

    public static void main(String[] args) {
        t k = new t();
        k.start();
    }

    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(200f);
        //      bulletAppState = new BulletAppState();
        //      getStateManager().attach(bulletAppState);
        //this.getRootNode().attachChild(getDebugGridBox(ColorRGBA.Cyan));



//        com.jme3.bounding.BoundingBox bb = (com.jme3.bounding.BoundingBox) getModel().getWorldBound();
//        float radius = bb.getXExtent() > bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
//        modelExtent = bb.getYExtent();
//
//        getModel().setLocalTranslation(new Vector3f(0, modelExtent * 2, 0));
//        CharacterControl physicsCharacter = new CharacterControl(new CapsuleCollisionShape(radius, modelExtent), modelExtent / 3);
//
//        getModel().addControl(physicsCharacter);
//        
//        getModel().addControl(new LoadAnimationController());
//        getModel().addControl(new CardManifestController());
//        
//        rootNode.attachChild(getModel());
//
//
//       getPhysicsSpace().add(getModel());
        // Node child = getDebugGridBox(ColorRGBA.Cyan);


        Spatial child = SkyFactory.createSky(assetManager,
                assetManager.loadTexture("Textures/left.jpg"),
                assetManager.loadTexture("Textures/right.jpg"),
                assetManager.loadTexture("Textures/front.jpg"),
                assetManager.loadTexture("Textures/back.jpg"),
                assetManager.loadTexture("Textures/top.jpg"),
                assetManager.loadTexture("Textures/bottom.jpg"));

        BinaryExporter exporter = BinaryExporter.getInstance();
        String userHome = System.getProperty("user.home");
        userHome = "C:/Users/Kyle Williams/Documents/My Dropbox/gameDevelopment/Shadow Of Illusions/ShadowOfIllusions/assets/";
        File file = new File(userHome + "MySkybox.j3o");

        try {
            //exporter.save(getModel(), file);
            exporter.save(child, file);
        } catch (IOException ex) {
            Logger.getLogger(t.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
        }
        rootNode.attachChild(child);
        //stop();
        //  SaveGame.saveGame("assets", "MyModelll.j3o", rootNode);

//        File daveFolder = new File(JmeSystem.getStorageFolder().getAbsolutePath() + File.separator + "assets".replaceAll("/", File.separator));
//        System.out.println(daveFolder.getAbsolutePath());
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);
    }

    private Spatial getModel() {
        if (spat == null) {
            spat = getAssetManager().loadModel("Test.j3o");
            spat.getControl(com.jme3.animation.AnimControl.class).createChannel();
            //loadAllAnimations(spat);
        }
        return spat;
    }

    //   public PhysicsSpace getPhysicsSpace() {
    //       return bulletAppState.getPhysicsSpace();
    //   }
    private Node getDebugGridBox(ColorRGBA color) {
        Node box = new Node("DebugGridBox");

        Material grayMat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        grayMat.setColor("Color", color);
        //Bottom
        createGridPlane(box, grayMat, new Vector3f(-25, 0, -25), Vector3f.ZERO);
//        //Right        
//        createGridPlane(box, grayMat, new Vector3f(23, 0, -25), Vector3f.UNIT_X.negate());
//        //Left        
//        createGridPlane(box, grayMat, new Vector3f(-25, 48, -25), Vector3f.UNIT_X);
//        //back
//        createGridPlane(box, grayMat, new Vector3f(-25, 0, 23), Vector3f.UNIT_Z.negate());
//        //front
//        createGridPlane(box, grayMat, new Vector3f(-25, 48, -25), Vector3f.UNIT_Z);
//        //Top
//        createGridPlane(box, grayMat, new Vector3f(-25, 48, -25), Vector3f.ZERO);

        return box;
    }

    private void createGridPlane(Node root, Material mat, Vector3f translation, Vector3f rotateUpTo) {
        Node grid = new Node("gridNode");
        mat.getAdditionalRenderState().setWireframe(true);

        //Geometry geo1 = new Geometry("grid", new Grid2(10, 10, 5.34f));
        //geo1.setMaterial(mat);
        //grid.attachChild(geo1);
        Box b = new Box(24, 1, 24);
        Geometry geo = new Geometry("Plane", b);

        geo.setMaterial(mat);
        geo.setCullHint(Spatial.CullHint.Dynamic);

        //geo.move(24, -1, 24);
        grid.attachChild(geo);

        //grid.rotateUpTo(rotateUpTo);
        //grid.move(translation);
        //grid.detachChild(geo);//we don't want to see the physics box

        geo.addControl(new RigidBodyControl(0));
        //getPhysicsSpace().add(geo);

        root.attachChild(grid);

    }

    private class Grid2 extends Grid implements Savable {

        public Grid2(int xLines, int yLines, float lineDist) {
            super(xLines, yLines, lineDist);
        }

        @Override
        public void write(JmeExporter ex) throws IOException {
            super.write(ex);
            OutputCapsule out = ex.getCapsule(this);
        }

        @Override
        public void read(JmeImporter im) throws IOException {
            super.read(im);
            InputCapsule in = im.getCapsule(this);
        }
    }
}
