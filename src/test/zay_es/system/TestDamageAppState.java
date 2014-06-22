/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.system;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Kyle
 */
public class TestDamageAppState extends AbstractAppState {

    EntityData entitySystem;
    EntitySet damageSet;
    EntitySet healthSet;

    public TestDamageAppState(EntityData entitySystem) {
        this.entitySystem = entitySystem;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        damageSet = entitySystem.getEntities(TestDamageComponent.class);
        healthSet = entitySystem.getEntities(TestHealthComponent.class);
    }

    @Override
    public void update(float tpf) {

        Map<EntityId, Float> damageSummary = new HashMap<EntityId, Float>();

        damageSet.applyChanges();
        healthSet.applyChanges();

        Iterator<Entity> iterator = damageSet.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            TestDamageComponent dc = entity.get(TestDamageComponent.class);

            float f = 0;
            if (damageSummary.containsKey(dc.getTarget())) {
                f = damageSummary.get(dc.getTarget());
            }

            f += dc.getDamage();
            damageSummary.put(dc.getTarget(), f);

            //entity.destroy();
        }

        Iterator<EntityId> keyIterator = damageSummary.keySet().iterator();
        while (keyIterator.hasNext()) {
            EntityId entityId = keyIterator.next();
            float damage = damageSummary.get(entityId);

            Entity entity = healthSet.getEntity(entityId);
            if (entity != null) {
                TestHealthComponent hc = entity.get(TestHealthComponent.class);
                float newLife = hc.getHealth() - damage;

                if (newLife < 0) {
                    entitySystem.removeComponent(entityId, TestHealthComponent.class);
                } else {
                    entity.set(new TestHealthComponent(newLife));
                }
            }
        }
    }
}