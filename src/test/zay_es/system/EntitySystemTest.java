/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.system;

import com.jme3.app.SimpleApplication;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.sql.SqlEntityData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class EntitySystemTest extends SimpleApplication {

    private EntityData es;

    public static void main(String[] args) throws SQLException {
        EntitySystemTest app = new EntitySystemTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try {
            //Create a new Entity System
            es = new SqlEntityData("some/path", 100);

            //Add the DamageAppState to the Application
            stateManager.attach(new TestDamageAppState(es));

            //Create a new Entity with 100 HP
            EntityId healthEntity = es.createEntity();
            es.setComponents(healthEntity, new TestHealthComponent(100));

            //Create a new Damage Entity who deals 5 damage to the health entity
            es.setComponents(healthEntity, new TestDamageComponent(healthEntity, 5));
        } catch (SQLException ex) {
            Logger.getLogger(EntitySystemTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityData getEntitySystem() {
        return es;
    }

    @Override
    public void simpleUpdate(float tpf) {

        EntitySet es = getEntitySystem().getEntities(TestHealthComponent.class);
        es.applyChanges(); // not strictly necessary but not a bad idea
        for (Iterator i = es.iterator(); i.hasNext();) {
            Entity ent = (Entity) i.next();

            System.out.println(ent.getId() + ", " + ent.get(TestHealthComponent.class).getHealth());
        }
    }
}
