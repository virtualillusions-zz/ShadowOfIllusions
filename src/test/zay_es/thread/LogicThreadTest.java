/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.thread;

import com.jme3.app.SimpleApplication;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.simsilica.es.sql.SqlEntityData;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.zay_es.system.TestDamageComponent;

/**
 *
 * @author Kyle
 */
public class LogicThreadTest extends SimpleApplication {

    private EntityData entitySystem;
    private ScheduledExecutorService exec;

    public static void main(String[] args) {
        new LogicThreadTest().start();
    }

    @Override
    public void simpleInitApp() {
        try {
            //Init the Entity System
            entitySystem = new SqlEntityData("some/path", 100);

            //Init the Game Systems for the visualisation
            //stateManager.attach(new EntityDisplayAppState(rootNode));
            //stateManager.attach(new PlayerInputAppState());

            //create the Thread for the game logic
            exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new GameLogicThread(this, entitySystem), 0, 20, TimeUnit.MILLISECONDS);

        } catch (SQLException ex) {
            Logger.getLogger(LogicThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityData getEntitySystem() {
        return entitySystem;
    }

    @Override
    public void simpleUpdate(float tpf) {

        EntitySet es = getEntitySystem().getEntities(TestDamageComponent.class);
        es.applyChanges(); // not strictly necessary but not a bad idea
        System.out.println(es);

    }

    @Override
    public void destroy() {
        super.destroy();
        //Shutdown the thread when the game is closed
        exec.shutdown();
    }
}
