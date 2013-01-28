/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.*;
import com.spectre.controller.scene.SceneController;
import com.spectre.director.Director;
import com.spectre.util.Buttons;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.physics.PhysicsTestHelper;

/**
 *
 * @author Kyle Williams
 */
public class testMain extends SpectreApplication {

    protected BitmapFont guiFont;
    protected BitmapText fpsText;
    protected float secondCounter = 0.0f;
    protected Geometry CameraDebugBox1;
    protected Geometry CameraDebugBox2;

    public static void main(String[] args) {
        testMain app = new testMain();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
        Logger.getLogger("").setLevel(Level.WARNING);
    }

    @Override
    protected void spectreApp() {
        //Director.getPhysicsSpace().enableDebug(assetManager);
        setUp();
        Director.setAnimationsList(new HashMap<String, AnimData>());

//TEMPORARY GAME SETUP **NEEDED TO BE SET AS GAME WAS UPDATING
        getStateManager().attach(new com.spectre.app.SpectreState() {
            @Override
            public void SpectreState(AppStateManager stateManager, Application app) {
                setupScene();

                setupChar();

                SpectrePlayerController spc = Director.getPlayer("KYLO");
                Buttons.setUpRemote(inputManager, 1, "KYLO");
                Buttons.setUp360Remote(inputManager, 1, "KYLO");
                spc.setModel("Sinbad");
                //spc.getPhysicsModel().setLocalTranslation(0,10,0);
                spc.addIntoPlay();

                Director.getApp().addCamera();
                CameraDebugBox1 = setUpDebugCameraBox(renderManager.getMainView("Player 1").getBackgroundColor());
                CameraDebugBox2 = setUpDebugCameraBox(renderManager.getMainView("Player 2").getBackgroundColor());


                //creating the camera Node
                CameraNode camNode = new CameraNode("CamNode", getPlayerCamera(1));
                //Setting the direction to Spatial to camera, this means the camera will copy the movements of the Node
                camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
                //attaching the camNode to the teaNode
                ((Node)Director.getPlayer("KYLO").getSpatial()).attachChild(camNode);
                //setting the local translation of the cam node to move it away from the teanNode a bit
                camNode.setLocalTranslation(new Vector3f(0, 8, -60));
                //setting the camNode to look at the teaNode
                camNode.lookAt(Director.getPlayer("KYLO").getSpatial().getLocalTranslation(), Vector3f.UNIT_Y);

Director.getPhysicsSpace().enableDebug(assetManager);
            }
        });
    }

    public void setupChar() {
        Node character = (Node) assetManager.loadModel("testData/Sinbad.j3o");
        character.scale(.5f);
        character.setLocalTranslation(0, 10, 0);

        character.addControl(new SpectreEssenceController());//gamestate
        SpectrePhysicsController sPc = new SpectrePhysicsController();//character
        character.addControl(sPc);
        HashMap<String, SpectrePhysicsController> cL = new HashMap<String, SpectrePhysicsController>();
        cL.put("Sinbad", sPc);
        Director.setCharacterList(cL);
    }

    public Geometry setUpDebugCameraBox(ColorRGBA color) {
        Box b = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        return geom;
    }

    public void setupScene() {
        SceneController sC = new SceneController(null);
        PhysicsTestHelper.createPhysicsTestWorldSoccer(getSceneNode(), getAssetManager(), Director.getPhysicsSpace());
    }

    public void setUp() {
        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        getRootNode().addLight(ambient);

//        FlyByCamera flyCam = new FlyByCamera(cam);
//        flyCam.setMoveSpeed(20f);
//        flyCam.registerWithInput(inputManager);
        inputManager.setCursorVisible(false);
        StatsView statsView = new StatsView("Statistics View", assetManager, renderer.getStatistics());
//         move it up so it appears above fps text
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        fpsText = new BitmapText(guiFont, false);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        getGuiNode().attachChild(fpsText);

        statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        getGuiNode().attachChild(statsView);
    }

    @Override
    protected void spectreUpdate(float tpf) {
        secondCounter += timer.getTimePerFrame();
        int fps = (int) timer.getFrameRate();
        if (secondCounter >= 1.0f) {
            fpsText.setText("Frames per second: " + fps);
            secondCounter = 0.0f;
        }

        CameraDebugBox1.setLocalTranslation(getPlayerCamera(0).getLocation());
        CameraDebugBox2.setLocalTranslation(getPlayerCamera(1).getLocation());
    }

    @Override
    public void spectreRender(RenderManager rm) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
