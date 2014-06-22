/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.deck;

import com.google.common.collect.Maps;
import com.jme3.input.InputManager;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.app.SpectreControl;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.scene.visual.components.VisualRepPiece;
import com.spectre.systems.deck.subsystems.DeckController;
import com.spectre.systems.player.components.PlayerPiece;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * <b>Purpose:</b> Default Deck Handler Systems<br/>
 *
 * <b>Description:</b>A defacto Deck Systems that attaches the base DeckControl
 * to a spatial
 *
 * @author Kyle D. Williams
 */
public class DeckSystem extends SpectreAppState {

    private EntityData ed;
    private EntitySet deckSet;
    private InputManager inputManager;
    private HashMap<EntityId, Spatial> modelBindingsList;
    /**
     * forced to keep track of controller to prevent spatial switching issues
     */
    private HashMap<EntityId, SpectreControl> deckContMap;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        this.ed = getEntityData();
        this.deckSet = ed.getEntities(
                PlayerPiece.class,
                VisualRepPiece.class,
                InScenePiece.class);
        this.deckContMap = Maps.newHashMap();
        this.modelBindingsList = sAppState.getModelBindingsList();
    }

    @Override
    protected void cleanUp() {
        this.deckSet.release();
        this.spectreUpdate(0);
        this.deckSet = null;
    }

    @Override
    protected void spectreUpdate(float tpf) {
        if (deckSet.applyChanges()) {
            add(deckSet.getAddedEntities());
            remove(deckSet.getRemovedEntities());
            add(deckSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            Spatial spat = modelBindingsList.get(id);
            SpectreControl cc = deckContMap.get(id);
            if (spat == null) {
                /**
                 * When updated in Visual System this entity will be re-added to
                 * this update
                 */
            } else //ReAttachControl
            if (cc != null) {
                if (cc instanceof DeckController) {
                    cc.replaceSpatial(spat);
                }

            } else {//Add control
                cc = new DeckController(id, ed);
                spat.addControl(cc);
                deckContMap.put(id, cc);
            }
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            SpectreControl sc = deckContMap.get(id);
            sc.destroy();
            deckContMap.remove(id);
        }
    }
}
