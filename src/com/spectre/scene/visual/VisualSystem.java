/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.visual;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.app.input.Buttons;
import com.spectre.scene.visual.components.*;
import com.spectre.scene.visual.components.VisualRepPiece.VisualType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * <b>NickName:</b> GenericVisualSystem<br/>
 *
 * <b>Purpose:</b> DefactoVisualSystem<br/>
 *
 * <b>Description: Defacto Visual System used in almost all cases</b>
 *
 * @author Kyle D. Williams
 */
public class VisualSystem extends SpectreAppState implements ActionListener {

    private Node modelNode;
    private Node sceneNode;
    private EntitySet visualRepSet;
    private InputManager inputManager;
    //prevent heirarchal nodal issues later on no way around this so ignore
    private LoadingCache<String, Spatial> loadedList;
    //used to keep track of what entites control which spatials
    private HashMap<EntityId, Spatial> modelBindingList;

    @Override
    public void SpectreAppState(final SpectreApplicationState sAppState) {
        this.modelBindingList = sAppState.getModelBindingsList();
        this.modelNode = sAppState.getModelNode();
        this.sceneNode = sAppState.getSceneNode();
        this.visualRepSet = getEntityData().getEntities(
                VisualRepPiece.class,
                InScenePiece.class);
        this.inputManager = sAppState.getInputManager();
        this.loadedList = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String, Spatial>() {
            @Override
            public Spatial load(String key) throws Exception {
                return sAppState.getAssetManager().loadModel(key);
            }
        });
    }

    @Override
    public void cleanUp() {
        visualRepSet.release();
        spectreUpdate(0);
        this.visualRepSet = null;
        this.modelNode = null;
        this.sceneNode = null;
        this.inputManager.removeListener(this);
        this.inputManager = null;
        this.loadedList = null;
        this.modelBindingList = null;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        for (Iterator<Entity> it = visualRepSet.iterator(); it.hasNext();) {
            Entity entity = it.next();
            if (name.contains(entity.getId() + ":" + Buttons.ControlInputs.Mode)) {
                entity.set(new ActionModePiece(isPressed));
                break;
            }
        }
    }

    @Override
    public void spectreUpdate(float tpf) {
        if (visualRepSet.applyChanges()) {
            add(visualRepSet.getAddedEntities());
            remove(visualRepSet.getRemovedEntities());
            add(visualRepSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            VisualRepPiece vrp = e.get(VisualRepPiece.class);
            Node root = vrp.getVisualType() == VisualType.Character ? modelNode : sceneNode;
            String assetPath = vrp.getAssetName();
            try {
                Spatial model = modelBindingList.get(id);
                if (model != null && !model.getKey().getName().equals(assetPath)) {
                    model.removeFromParent();//must be used this way in case a
                    //sceneNode for some reason becomes a modelNode
                    //Now reload new asset
                    model = null;
                }
                if (model == null) {
                    model = loadedList.get(vrp.getAssetName()).clone();
                    //Set Spatial mapping
                    modelBindingList.put(e.getId(), model);
                    ///SECTION MUST BE PLACED INSIDE TO PREVENT LOOP
                    //Alert All systems of a change
                    e.set(new InScenePiece());
                }
                //Prevent changing mesh resetting actionModePiece
                if (getEntityData().getComponent(id, ActionModePiece.class) == null) {
                    //Set Stance Piece
                    e.set(new ActionModePiece());
                }
                //FINISH UP 
                //attaching outside allows for debugging possibilities like preloading and changing scale
                //TODO: this should be added to model==null block and their should be a different way to change scale,etc
                root.attachChild(model);
                inputManager.addListener(this, Buttons.getActionButton(e.getId()));
            } catch (ExecutionException ex) {
                log.error("Unable to correctly load Asset: " + vrp.getAssetName(), ex);
            }
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            VisualRepPiece vrp = e.get(VisualRepPiece.class);
            Node root = vrp.getVisualType() == VisualType.Character ? modelNode : sceneNode;
            Spatial model = modelBindingList.remove(e.getId());
            root.detachChild(model);
            //Generally frowned on but required to update classes in case of issue
            getEntityData().removeComponent(e.getId(), InScenePiece.class);
        }
    }
}