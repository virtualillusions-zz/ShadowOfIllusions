/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;
import deprecated.com.spectre.app.SpectreApplication;
import com.spectre.app.input.Buttons;
import deprecated.com.spectre.input.components.ControllerPiece;
import deprecated.com.spectre.input.components.ControllerPiece.ControllerType;
import deprecated.com.spectre.input.subsystems.BasicInputHandler;
import deprecated.com.spectre.input.subsystems.DummyInputHandler;
import deprecated.com.spectre.input.subsystems.InputHandler;
import java.util.Iterator;
import java.util.Set;

/**
 * Create a map link listener to the map and remove listener from inputmanager
 * when needed
 *
 * @author Kyle
 */
public class InputSystem extends AbstractAppState {

    private EntitySet inputSet;
    private InputManager inputManager;
    private DummyInputHandler dih;
    private BasicInputHandler bih;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.inputSet = ((SpectreApplication) app).getEntityData().getEntities(ControllerPiece.class);
        this.inputManager = app.getInputManager();
        dih = new DummyInputHandler();
        bih = new BasicInputHandler();
    }

    @Override
    public void update(float tpf) {
        if (inputSet.applyChanges()) {
            add(inputSet.getAddedEntities());
            remove(inputSet.getRemovedEntities());
            change(inputSet.getChangedEntities());
        }
    }

    private InputHandler getInputHandler(ControllerType type) {
        InputHandler iL = null;
        switch (type) {
            case DEBUG:
                iL = dih;
                break;
            case BASIC:
                iL = bih;
                break;
            default:
                throw new IndexOutOfBoundsException("Cannot find correct InputHandler");
        }
        return iL;
    }

    private void add(Entity e) {
        ControllerType type = e.get(ControllerPiece.class).getControllerType();
        InputHandler iL = getInputHandler(type);

        inputManager.addListener(iL, Buttons.getButtons(e.getId()));
        iL.add(e);
    }

    private void remove(Entity e) {
        ControllerType type = e.get(ControllerPiece.class).getControllerType();
        InputHandler iL = getInputHandler(type);

        iL.remove(e);
        String[] mappings = Buttons.getButtons(e.getId());
        for (String mappingName : mappings) {
            //deleting all mappings will remove listener
            inputManager.deleteMapping(mappingName);
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            add(it.next());
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            remove(it.next());
        }
    }

    private void change(Set<Entity> changedEntities) {
        for (Iterator<Entity> it = changedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            remove(e);
            add(e);
        }
    }
}
