/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app.debug;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.debug.BulletDebugAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Kyle D. Williams
 */
public class PhysicsDebugAppState extends BulletDebugAppState {

    private ViewPort defualtViewPort;

    public PhysicsDebugAppState(PhysicsSpace space) {
        super(space);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        defualtViewPort = app.getViewPort();
        rm.removeMainView(viewPort);
    }

    @Override
    public void render(RenderManager rm) {
        if (isEnabled()) {
            for (ViewPort vm : rm.getMainViews()) {
                if (!vm.equals(defualtViewPort) || !vm.equals(viewPort)) {
                    rm.renderScene(physicsDebugRootNode, vm);
                }
            }
        }
    }
}
