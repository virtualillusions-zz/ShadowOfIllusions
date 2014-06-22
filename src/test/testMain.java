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
import com.jme3.input.Joystick;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.ogre.AnimData;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.*;
import com.spectre.controller.scene.SceneController;
import com.spectre.deck.ArsenalDeck;
import com.spectre.deck.ArsenalDeck.DeckType;
import com.spectre.deck.RepositoryDeck;
import com.spectre.deck.SupplyDeck;
import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardCharacteristics;
import com.spectre.director.Director;
import com.spectre.util.AbstractCard;
import com.spectre.util.Buttons;
import java.util.HashMap;
import java.util.LinkedList;
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
    protected SpectreDuelController sdc;
    //QUICK VISUAL TESTING MOSTLY FOR RAYS
    private static Geometry testBox, testBox2;
    public static Vector3f vectestBox = new Vector3f(), vectestBox2 = new Vector3f();

    public static void main(String[] args) {
        testMain app = new testMain();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        app.setSettings(settings);
        app.start();
        Logger.getLogger("").setLevel(Level.FINE);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("GOOD BYE");
            }
        });
    }

    @Override
    protected void spectreApp() {
        vectestBox.set(Vector3f.POSITIVE_INFINITY);
        vectestBox2.set(Vector3f.POSITIVE_INFINITY);
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
                if (context.getType() == JmeContext.Type.Display
                        && inputManager.getJoysticks().length > 0) {
                    Joystick js = inputManager.getJoysticks()[0];
                    js.getButton(Buttons.Xbox360.Start.getID()).assignButton("Exit_Vezla");
                    js.getButton(Buttons.Xbox360.Back.getID()).assignButton("Exit_Vezla");
                }
                spc.setModel("Sinbad");
                Node spat = ((Node) Director.getPlayer("KYLO").getSpatial());
                getViewPort().attachScene(rootNode);
                getViewPort().setBackgroundColor(ColorRGBA.randomColor());
                spc.getCameraCont().setCamera(getCamera());
                spc.addIntoPlay();


                //Director.getPhysicsDirector().setDebugEnabled(true);

                //spc.getSpatial().addControl(new ChaseCamera(getCamera(), spc.getSpatial(), inputManager));
//                CameraDebugBox1 = setUpDebugCameraBox(renderManager.getMainView("Player 1").getBackgroundColor());


//                Director.getApp().addCamera();
//                CameraDebugBox2 = setUpDebugCameraBox(renderManager.getMainView("Player 2").getBackgroundColor());
//                //creating the camera Node
//                CameraNode camNode = new CameraNode("CamNode", getPlayerCamera(1));
//                //Setting the direction to Spatial to camera, this means the camera will copy the movements of the Node
//                camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
//                //attaching the camNode to the teaNode
//                spat.attachChild(camNode);
//                //setting the local translation of the cam node to move it away from the teanNode a bit
//                camNode.setLocalTranslation(new Vector3f(0, 8, -60));
//                //setting the camNode to look at the teaNode
//                camNode.lookAt(Director.getPlayer("KYLO").getSpatial().getLocalTranslation(), Vector3f.UNIT_Y);

                sdc = spat.getControl(SpectreDuelController.class);


                SupplyDeck sD = new SupplyDeck();
                RepositoryDeck rD = Director.getPlayer("KYLO").getRepo();
                ArsenalDeck aD = Director.getPlayer("KYLO").getRepo().createArsenalDeck("Arsenal", DeckType.TRIPLE_CAPACITY);
                for (int i = 0; i < 500; i++) {
                    Card c = AbstractCard.createNewCard("Card: " + i, CardCharacteristics.CardSeries.NATURE);
                    sD.put(c);
                    rD.add(c);
                    aD.add(c);
                }

                System.out.println(sD.size());
                System.out.println(rD.size());
                System.out.println(aD.size());
                Director.setCardList(sD);
                LinkedList<String> d = aD.createDeck();
                sdc.setActiveDeck(d);

                testBox = setUpDebugCameraBox(ColorRGBA.Black);
                testBox2 = setUpDebugCameraBox(ColorRGBA.White);
            }
        });
    }

    public void setupChar() {
        Node character = (Node) assetManager.loadModel("testData/Jaime/Jaime.j3o");
        character.scale(4f);
        character.setLocalTranslation(0, 10, 0);

        SpectrePhysicsController sPc = new SpectrePhysicsController();//character
        character.addControl(sPc);

        HashMap<String, SpectrePhysicsController> cL = new HashMap<String, SpectrePhysicsController>();
        cL.put("Sinbad", sPc);
        Director.setCharacterList(cL);
        // Director.getPhysicsSpace().add(sPc);

        //PhysicsTestHelper.createPhysicsGhostObject(rootNode, new Vector3f(0,5,0), Director.getPhysicsSpace());

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
        //PhysicsTestHelper.createPhysicsWildHouse(getSceneNode(), getAssetManager(), Director.getPhysicsSpace());
        PhysicsTestHelper.createPhysicsTown(getSceneNode(), getAssetManager(), Director.getPhysicsSpace());
        //PhysicsTestHelper.createPhysicsTestWorldSoccer(getSceneNode(), getAssetManager(), Director.getPhysicsSpace());
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
    private int fps = 0;

    @Override
    protected void spectreUpdate(float tpf) {
        testBox.setLocalTranslation(vectestBox);
        testBox2.setLocalTranslation(vectestBox2);

        secondCounter += timer.getTimePerFrame();
        //int fps = (int) timer.getFrameRate();
        if (secondCounter >= 1.0f) {
            fps = (int) timer.getFrameRate();//diff
            //fpsText.setText("Frames per second: " + fps);
            secondCounter = 0.0f;
        }
        ////////////////////////
        int hp = sdc.getAttributes().getHP();
        int focus = sdc.getAttributes().getFOCUS();
        String s = String.format("Frames per second: %4d \t HP:%2d \t Focus: %2d", fps, hp, focus);
        fpsText.setText(s);
        ///////////////////////
        if (getPlayerCamera(0) != null) {

            CameraDebugBox1.setLocalTranslation(getPlayerCamera(0).getLocation());
        }
        if (getPlayerCamera(1) != null) {

            CameraDebugBox2.setLocalTranslation(getPlayerCamera(1).getLocation());
        }
    }

    @Override
    public void spectreRender(RenderManager rm) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
