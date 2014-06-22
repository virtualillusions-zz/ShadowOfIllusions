/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.app.state.AbstractAppState;
import com.simsilica.es.EntityData;
import com.simsilica.es.base.DefaultEntityData;

/**
 * The primary Entity Database The main entry point for retrieving entities and
 * components.
 *
 * While This should really be its own AppState it makes more sense for users to
 * access the EntityDatabaseState class through SpectreApplicationState atm
 *
 * @author Kyle D. Williams
 */
public class EntityDatabaseState extends AbstractAppState {

    private EntityData entityData;

    public EntityDatabaseState() {
        this(new DefaultEntityData());
    }

    public EntityDatabaseState(EntityData ed) {
        this.entityData = ed;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    void close() {//avoid use by user by using default-package wide field
        super.cleanup();
        entityData.close();
        entityData = null; // cannot be reused
    }
}
