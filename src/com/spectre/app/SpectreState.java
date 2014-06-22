/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.app;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;

/**
 *
 * @author Kyle Williams 
 */
public abstract class SpectreState implements com.jme3.app.state.AppState{
    private boolean initialized = false;
    private boolean isEnabled = true;
    protected AssetManager assets;
    public abstract void SpectreState(AppStateManager stateManager, Application app);
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized=true;
        assets = app.getAssetManager();
        SpectreState(stateManager, app);
    }
    @Override
    public boolean isInitialized() {return initialized;}
    @Override
    public void setEnabled(boolean active) {isEnabled=active;}
    @Override
    public boolean isEnabled() {return isEnabled;}
    @Override
    public void stateAttached(AppStateManager stateManager) {   }
    @Override
    public void stateDetached(AppStateManager stateManager) {   }
    @Override
    public void update(float tpf){  }
    @Override
    public void render(RenderManager rm){   }
    @Override
    public void postRender() {  }
    @Override
    public void cleanup() {initialized=false;}
}