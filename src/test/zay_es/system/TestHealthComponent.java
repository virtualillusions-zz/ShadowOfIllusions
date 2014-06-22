/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.system;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author Kyle
 */
public class TestHealthComponent implements EntityComponent {

    public float health;

    public TestHealthComponent(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }
}
