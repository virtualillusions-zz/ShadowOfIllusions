package com.spectre;

import com.spectre.director.GameState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.spectre.director.Director;
import com.spectre.director.FXDirector;
import com.spectre.director.FXDirector.ALBUMS;

/**
 * Start Up Should be Instant
 * Everything Should not be loaded by default instead
 * References Should be created and loaded as Needed: Characters, Scenes
 * Their should be one master Rig called MasterRig all card animations should be loaded to it and animations should only be 
   added to other character rigs as needed
 * References Loaded Should not be quickly removed should keep references to use later. Create Hashmap lookup for all References
 * Attempt High Level of Modularity no controls actively depend on other controls.
 * 
 */ 
public class Main extends com.spectre.app.SpectreApplication {
 
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        //settings.setUseJoysticks(true);
        app.setSettings(settings);
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
        app.start();
    }

    @Override
    protected void spectreApp() {
        //getAppStateManager().attach(new com.spectre.util.FlyCamState());
        getAppStateManager().attach(new com.spectre.util.ProfilerState());
        
        final GameState gS = new GameState();
        getAppStateManager().attach(gS);
         
       //TEMPORARY GAME SETUP **NEEDED TO BE SET AS GAME WAS UPDATING
       getAppStateManager().attach(new com.spectre.app.SpectreState() {

            @Override
            public void SpectreState(AppStateManager stateManager, Application app) {
               //FXDirector.playSong(ALBUMS.Ambient, "Girls");
               FXDirector.playSong(ALBUMS.Ambient, "Run Run Run");
               getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
               //MODEL FOR PLAYER TO BE SET WITHIN GUI
               Director.getPlayer("Gajeel").setModel("Reina"); 
               //Director.getPlayer("Gajeel").getInputController().setEnabled(false);
               //Director.getPlayer("Natsu").setModel("Alex");
               //gS.setUp("CyberPlane","Gajeel","Natsu");
               gS.setUp("CyberPlane","Gajeel");
               gS.start(); 
               //Director.getPhysicsSpace().enableDebug(assetManager);
            }            
        });
    }

    @Override
    protected void spectreUpdate(float tpf) {}

    @Override
    public void spectreRender(RenderManager rm) {}
}
