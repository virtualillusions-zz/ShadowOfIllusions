/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.camera;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.camera.components.*;
import com.spectre.scene.camera.subsystems.CameraController;
import com.spectre.scene.visual.components.InScenePiece;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <b>NickName:</b>GenericCameraSystem <br/>
 *
 * <b>Purpose:</b> Default Camera System <br/>
 *
 * <b>Description:</b>A Camera System implementation used to implement basic
 * camera controls
 *
 * @author Kyle D. Williams
 */
public class CameraSystem extends SpectreAppState {

    private SpectreApplicationState sAppState;
    private EntitySet camSet;
    private List<Entity> camList;
    private RenderManager renderManager;
    private Node rootNode;
    private ViewPort defaultViewPort;
    /**
     * forced to keep track of controller to prevent spatial switching issues
     */
    private HashMap<EntityId, CameraController> camContMap;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        this.sAppState = sAppState;
        this.sAppState.getInputManager().setCursorVisible(false);
        this.camSet = getEntityData().getEntities(
                CameraPiece.class,
                InScenePiece.class);
        this.camList = Lists.newArrayList();
        this.renderManager = sAppState.getApplication().getRenderManager();
        this.rootNode = sAppState.getRootNode();
        this.defaultViewPort = sAppState.getApplication().getViewPort();
        this.defaultViewPort.detachScene(rootNode);
        this.camContMap = Maps.newHashMap();
    }

    @Override
    public void cleanUp() {
        this.sAppState.getInputManager().setCursorVisible(true);
        this.defaultViewPort.attachScene(rootNode);
        this.sAppState = null;
        camSet.release();
        camSet.applyChanges();
        remove(camSet);
        this.camSet = null;
        camList.clear();
        this.camList = null;
        this.renderManager = null;
        this.rootNode = null;
        this.defaultViewPort = null;
        this.camContMap.clear();
        this.camContMap = null;
    }
//////////////CAMERA MANAGEMENT

    @Override
    public void spectreUpdate(float tpf) {
        if (camSet.applyChanges()) {
            add(camSet.getAddedEntities());
            remove(camSet.getRemovedEntities());
            add(camSet.getChangedEntities());
        }
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            Spatial spat = sAppState.getModelBindingsList().get(id);
            CameraController cc = camContMap.get(id);
            if (spat == null) {
                /**
                 * When updated in Visual System this entity will be re-added to
                 * this update
                 */
                continue;
            } else if (cc != null) {
                cc.replaceSpatial(spat);
            } else {
                //CHECK IF ANOTHER ENTITY IS IN POSESSION OF THE CAMERA NEEDED AND DISCONNECT IF NEEDED
                int num = e.get(CameraPiece.class).getCamNum();

                //CHECK FOR DUPLICATE CAM NUMBERS THROW ERROR IF OVERLAPPING
                for (Entity e2 : camList) {
                    int num2 = e2.get(CameraPiece.class).getCamNum();
                    if (num == num2) {
                        throw new UnsupportedOperationException("The viewport \"" + this.getViewPortName(e)
                                + "\",\nis already in use by another entity and " + e.getId()
                                + " is\nattempting to control the viewport prematurely");
                    }
                }
                //ADD CAMERACONTROLLER
                Camera cam = getViewportCamera(e);
                cc = new CameraController(e, cam,
                        sAppState.getPhysicsSpace(),
                        sAppState.getModelNode(),
                        sAppState.getInputManager());
                spat.addControl(cc);
                camContMap.put(id, cc);
            }
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            CameraController cc = camContMap.get(id);
            cc.destroy();
            destroyCamera(e);
            camContMap.remove(id);
        }
    }

    /**
     * @param camNum int
     * @return the name of the ViewPort
     */
    public String getViewPortName(Entity e) {
        int camNum = e.get(CameraPiece.class).getCamNum();
        return "ViewPort[" + camNum
                + "]";
    }

    /**
     * @return the original Viewport
     */
    public ViewPort getDefaultViewPort() {
        return defaultViewPort;
    }

    /**
     * @return Camera for the players viewport
     */
    public Camera getDefaultCamera() {
        return defaultViewPort.getCamera();
    }

    /**
     * @param camNumber int
     * @return Camera for the players viewport
     */
    public Camera getViewportCamera(Entity e) {
        return getViewPort(e).getCamera();
    }

    /**
     * Destroys a viewport
     *
     * @param camNumber int
     */
    private void destroyCamera(Entity e) {
        camList.remove(e);
        int num = e.get(CameraPiece.class).getCamNum();
        ViewPort view = renderManager.getMainView(getViewPortName(e));
        if (view != null) {
            renderManager.removeMainView(view);
        }
        resetCameraViews();
    }

    /**
     * Generates a new Camera Camera Limit is 4
     *
     * @param camNumber int
     * @return ViewPort a new viewPort
     */
    public ViewPort getViewPort(Entity e) {
        String camName = getViewPortName(e);

        if (camList.size() > 4) {
            throw new IndexOutOfBoundsException("Exceeded maximum Camera Limit");
        } else if (renderManager.getMainView(camName) != null) {
            return renderManager.getMainView(camName);
        }

        Camera cam = this.getDefaultCamera().clone();

        ViewPort viewPort = renderManager.createMainView(camName, cam);

        if (sAppState.isInDebugMode()) {
            viewPort.setBackgroundColor(ColorRGBA.randomColor());
        }

        viewPort.setClearFlags(true, true, true);
        viewPort.attachScene(rootNode);

        camList.add(e);
        resetCameraViews();

        return viewPort;
    }

    /**
     * Organize Camera views in a logical manner
     */
    private void resetCameraViews() {
        Collections.sort(camList, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                int p1 = o1.get(CameraPiece.class).getCamNum();
                int p2 = o2.get(CameraPiece.class).getCamNum();
                return p1 == p2 ? 0 : p1 > p2
                        ? 1 : -1;
            }
        });
        switch (camList.size()) {
            case 0:
                break;
            case 1:
                getViewportCamera(camList.get(0)).setViewPort(0f, 1f, 0f, 1f);
                break;
            case 2:
                getViewportCamera(camList.get(0)).setViewPort(0.01f, 0.495f, 0.01f, 0.99f);
                getViewportCamera(camList.get(1)).setViewPort(0.505f, 0.99f, 0.01f, 0.99f);
                break;
            case 3:
                getViewportCamera(camList.get(0)).setViewPort(0.01f, 0.495f, 0.01f, 0.99f);
                getViewportCamera(camList.get(1)).setViewPort(0.505f, 0.99f, 0.505f, 0.99f);
                getViewportCamera(camList.get(2)).setViewPort(0.505f, 0.99f, 0.01f, 0.495f);
                break;
            case 4:
                getViewportCamera(camList.get(0)).setViewPort(0.01f, 0.495f, 0.505f, 0.99f);
                getViewportCamera(camList.get(1)).setViewPort(0.505f, 0.99f, 0.505f, 0.99f);
                getViewportCamera(camList.get(2)).setViewPort(0.01f, 0.495f, 0.01f, 0.495f);
                getViewportCamera(camList.get(3)).setViewPort(0.505f, 0.99f, 0.01f, 0.495f);
                break;
            default:
                throw new UnsupportedOperationException("TOO MANY/FEW CAMERAS in CameraSystem.java "
                        + camList.size());
        }
    }
}
