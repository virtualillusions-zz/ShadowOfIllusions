/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.system;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author Kyle
 */
public class TestDamageComponent implements EntityComponent {

    private float damage;
    private EntityId target;

    public TestDamageComponent(EntityId target, float damage) {
        this.damage = damage;
        this.target = target;
    }

    public float getDamage() {
        return damage;
    }

    public EntityId getTarget() {
        return target;
    }
}
