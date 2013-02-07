/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.physics;

import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.font.BitmapText;
import com.jme3.scene.Spatial;

/**
 *
 * @author Kyle
 */
public class PhysicsCharacterHullCollisionTest extends SimpleApplication {

    public PhysicsSpace space;
    public Spatial spat;

    @Override
    public void simpleInitApp() {

        BulletAppState b = new BulletAppState();
        getFlyByCamera().setMoveSpeed(50);
        stateManager.attach(b);
        space = b.getPhysicsSpace();
        space.enableDebug(assetManager);
        PhysicsTestHelper.createPhysicsTestWorldDefault(rootNode, assetManager, space);
        PhysicsTestHelper.createBallShooter(this, rootNode, space);
        /**
         * Load a model. Uses model and texture from jme3-test-data library!
         */
        Spatial teapot = assetManager.loadModel("testData/Sinbad.j3o");
        teapot.setLocalTranslation(0, 5.0f, 0);
        teapot.getControl(AnimControl.class).createChannel().setAnim("Dance");
        //TestCharacterGhostControl ctrl = new TestCharacterGhostControl();
        KinematicRagdollControl ctrl = new KinematicRagdollControl();
        teapot.addControl(ctrl);
        space.add(ctrl);
        rootNode.attachChild(teapot);
        initCrossHairs();
    }

    protected void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    public static void main(String[] main) {
        PhysicsCharacterHullCollisionTest app = new PhysicsCharacterHullCollisionTest();
        app.start();
    }
}
