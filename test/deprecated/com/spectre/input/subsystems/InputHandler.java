/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input.subsystems;

import com.google.common.collect.Lists;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.simsilica.es.Entity;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kyle Williams
 */
public abstract class InputHandler implements ActionListener, AnalogListener {

    private ArrayList<Entity> entityList;

    public InputHandler() {
        entityList = Lists.newArrayList();
    }

    /**
     *
     * @return entityList ArrayList<Entity>
     */
    public ArrayList<Entity> get() {
        return entityList;
    }

    /**
     * Adds entity to input handler
     *
     * @param e Entity
     */
    public void add(Entity e) {
        entityList.add(e);
    }

    /**
     * Removes entity from input handler
     *
     * @param e Entity
     */
    public void remove(Entity e) {
        entityList.remove(e);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        for (Iterator<Entity> it = entityList.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (name.contains(entity.getId() + "")) {
                onAction(entity, name, isPressed, tpf);
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        for (Iterator<Entity> it = entityList.iterator(); it.hasNext();) {
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
}
