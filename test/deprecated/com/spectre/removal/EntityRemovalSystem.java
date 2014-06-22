/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.removal;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import deprecated.com.spectre.app.SpectreApplication;
import deprecated.com.spectre.removal.components.EntityRemovalPiece;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * This System Gracefully Removes Entities from entityData in the next iteration
 * after the EntityRemovalComponent is added to the entity in the system
 *
 *
 * @deprecated NEVER USE THIS REMOVING ENTITIES FROM AN ACTIVE SYSTEM IS A
 * HORRIBLE IDEA
 *
 * @author Kyle
 */
public class EntityRemovalSystem extends AbstractAppState {

    private EntityData ed;
    private EntitySet removalSet;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        ed = ((SpectreApplication)app).getEntityData();
        removalSet = ed.getEntities(EntityRemovalPiece.class);
    }

    @Override
    public void update(float tpf) {
        if (removalSet.applyChanges()) {
            add(removalSet.getAddedEntities());
            remove(removalSet.getRemovedEntities());
            change(removalSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            ed.removeEntity(e.getId());
            SpectreApplication.logger.log(Level.INFO, "Removed Entity:{0}", e.getId());
        }
    }

    private void remove(Set<Entity> removedEntities) {
    }

    private void change(Set<Entity> changedEntities) {
    }
}
