/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;

/**
 *
 * @author Kyle
 */
public class testPhysicsController extends SimpleApplication {
private BulletAppState bulletAppState;
    @Override
    public void simpleInitApp() {
        // activate physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        
        // init a physics test scene
        PhysicsTestHelper.createPhysicsTestWorldDefault(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());
        
    }

    public static void main(String[] args) {
        testPhysicsController app = new testPhysicsController();
        app.start();
    }
}
