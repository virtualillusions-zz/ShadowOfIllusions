/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.thread;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.EntityData;
import test.zay_es.system.TestDamageAppState;

/**
 *
 * @author Kyle
 */
public class GameLogicThread implements Runnable {

    private final float tpf = 0.02f;
    private AppStateManager stateManager;

    public GameLogicThread(Application app, EntityData entitySystem) {
        stateManager = new AppStateManager(app);

        //add the logic AppStates to this thread
        stateManager.attach(new TestDamageAppState(entitySystem));
    }

    public void run() {
        stateManager.update(tpf);
    }
}
