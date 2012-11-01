/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.system.AppSettings;
import com.spectre.app.SpectreApplication;
import com.spectre.app.SpectreCameraController;
import com.spectre.controller.character.*;
import com.spectre.controller.scene.SceneController;
import com.spectre.director.Director;
import com.spectre.director.FilterSubDirector;
import com.spectre.util.Buttons;
import java.util.HashMap;
import test.physics.PhysicsTestHelper;

/**
 *
 * @author Kyle Williams
 */
public class testMain extends SpectreApplication {

    protected BitmapFont guiFont;
    protected BitmapText fpsText;
    protected float secondCounter = 0.0f;

    public static void main(String[] args) {
        testMain app = new testMain();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    protected void spectreApp() {
        setUp();
        inputManager.addMapping("KYLO:LeftThumbstickUp",
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("KYLO:LeftThumbstickDown",
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("KYLO:LeftThumbstickLeft",
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("KYLO:LeftThumbstickRight",
                new KeyTrigger(KeyInput.KEY_RIGHT));
        Director.setAnimationsList(new HashMap<String, AnimData>());
        
//TEMPORARY GAME SETUP **NEEDED TO BE SET AS GAME WAS UPDATING
        getStateManager().attach(new com.spectre.app.SpectreState() {

            @Override
            public void SpectreState(AppStateManager stateManager, Application app) {
                setupScene();

                setupChar();

                SpectrePlayerController spc = new SpectrePlayerController("KYLO");
                Buttons.setUpRemote(inputManager, 1, "KYLO");
                Buttons.setUp360Remote(inputManager, 1, "KYLO");
                spc.setModel("Sinbad");
                //spc.getPhysicsModel().setLocalTranslation(0,10,0);
                spc.addIntoPlay();
//Director.getPhysicsSpace().enableDebug(assetManager);
            }
        });
    }

    public void setupChar() {
        Node character = (Node) assetManager.loadModel("testData/Sinbad.j3o");
        character.scale(.5f);
        character.setLocalTranslation(0, 10, 0);
        character.addControl(new SpectreAnimationController());//character
        character.addControl(new SpectreCameraController(cam));//gamestate
        character.addControl(new SpectreEssenceController());//gamestate
        SpectrePhysicsController sPc = new SpectrePhysicsController();//character
        character.addControl(sPc);
        HashMap<String, SpectrePhysicsController> cL = new HashMap<String, SpectrePhysicsController>();
        cL.put("Sinbad", sPc);
        Director.setCharacterList(cL);
    }

    public void setupScene() {
        SceneController sC = new SceneController(null);
        /** Load a model. Uses model and texture from jme3-test-data library! */
//        Geometry geomScene = (Geometry) assetManager.loadModel("testData/Sponza.j3o");
//        Box b = new Box(Vector3f.ZERO, 100, 10, 100); // create cube shape at the origin
//        Geometry geomScene = new Geometry("Box", b);  // create cube geometry from the shape
//        Material mat = new Material(assetManager,
//                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
//        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
//        geomScene.setMaterial(mat);                   // set the cube's material
//        geomScene.setCullHint(CullHint.Never);
//        geomScene.setBatchHint(BatchHint.Always);
        //Node scene = new Node("scene");
        //scene.attachChild(geomScene);
        //scene.addControl(sC);
        //sC.attachScene();
        PhysicsTestHelper.createPhysicsTestWorld(getSceneNode(), getAssetManager(), Director.getPhysicsSpace());
    }

    public void setUp() {
        /** A white ambient light source. */
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
    }

    @Override
    public void spectreRender(RenderManager rm) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
