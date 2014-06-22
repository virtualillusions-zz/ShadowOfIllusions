/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app.input;

import com.google.common.base.Preconditions;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import java.util.Iterator;

/**
 *
 * @author Kyle D. Williams
 */
public abstract class InputSpectreAppState extends SpectreAppState implements SpectreInputListener {

    protected EntitySet mainSet;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        Preconditions.checkNotNull(isInitialized() && mainSet == null,
                "Please set the main EntrySet \"mainSet\", it currently returns null");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        String[] s = name.split(":");
        for (Iterator<Entity> it = mainSet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (s[0].equals(entity.getId().toString())) {
                onAction(entity, s[1], isPressed, tpf);
                break;
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        String[] s = name.split(":");
        for (Iterator<Entity> it = mainSet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (s[0].equals(entity.getId().toString())) {
                onAnalog(entity, s[1], value, tpf);
                break;
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
}
