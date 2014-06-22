/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.app;

import com.spectre.app.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;
import java.util.Iterator;

/**
 *
 * @author Kyle Williams
 */
public abstract class SpectreAppState implements AppState, ActionListener, AnalogListener {

    private boolean initialized = false;
    private boolean isEnabled = true;
    private EntitySet entitySet;

    public abstract void SpectreAppState(AppStateManager stateManager, SpectreApplicationOld app);

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        SpectreAppState(stateManager, (SpectreApplicationOld) app);

        //THIS IS A HORRIBLE CHECK NEED TO CHANGE SOMEHOW
        if (entitySet == null) {
            throw new NullPointerException("Please Initialize entitySet in SpectreAppState using set(EntitySet entitySet)");
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean active) {
        isEnabled = active;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     *
     * @param entitySet
     */
    protected void set(EntitySet entitySet) {
        this.entitySet = entitySet;
    }

    /**
     *
     * @return entityList ArrayList<Entity>
     */
    public EntitySet get() {
        return entitySet;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isEnabled()) {
            return;
        }
        for (Iterator<Entity> it = entitySet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (name.contains(entity.getId() + "")) {
                onAction(entity, name, isPressed, tpf);
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (!isEnabled()) {
            return;
        }
        for (Iterator<Entity> it = entitySet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (name.contains(entity.getId() + "")) {
                onAnalog(entity, name, value, tpf);
            }
        }
    }

    /**
     * Called when an input to which this listener is registered to is invoked.
     *
     * @param id The id of the entity that is initiating the button press
     * @param name The name of the mapping that was invoked
     * @param isPressed True if the action is "pressed", false otherwise
     * @param tpf The time per frame value.
     */
    public abstract void onAction(Entity entity, String name, boolean isPressed, float tpf);

    /**
     * Called to notify the implementation that an analog event has occurred.
     *
     * @param id The id of the entity that is initiating the button press
     * @param name The name of the mapping that was invoked
     * @param value Value of the axis, from 0 to 1.
     * @param tpf The time per frame value.
     */
    public abstract void onAnalog(Entity entity, String name, float value, float tpf);

    @Override
    public void stateAttached(AppStateManager stateManager) {
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void render(RenderManager rm) {
    }

    @Override
    public void postRender() {
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
}