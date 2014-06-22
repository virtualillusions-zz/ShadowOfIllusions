/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app.debug;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.system.JmeContext;
import com.jme3.util.BufferUtils;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;

/**
 *
 * @author Kyle D. Williams
 */
public class DebugState extends SpectreAppState {

    private final String PHYS = "Physics";
    private final String STATS = "Stats";
    private final String MEM = "Memory";
    private final String EXIT = "Exit";
    private final String NODE = "Node";
    private final String GUI_NODE = "GUI_Node";
    private AppStateManager sm;
    private static Application app;
    private static PhysicsDebugAppState dps;
    private StatsAppState sAs;
    private ScreenshotAppState sap;

    @Override
    public void SpectreAppState(final SpectreApplicationState sAppState) {
        app = sAppState.getApplication();
        sm = app.getStateManager();
        final InputManager inputManager = sAppState.getInputManager();
        final PhysicsSpace pSpace = sAppState.getPhysicsDirector().getPhysicsSpace();

        inputManager.addMapping(MEM, new KeyTrigger(KeyInput.KEY_F1));
        if (app.getContext().getType() == JmeContext.Type.Display) {
            inputManager.addMapping(EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            inputManager.addMapping(NODE, new KeyTrigger(KeyInput.KEY_F4));
            inputManager.addMapping(GUI_NODE, new KeyTrigger(KeyInput.KEY_F5));
        }
        //ATTACH STATS VIEW
        final Node guiNode = sAppState.getGuiNode();
        final BitmapFont guiFont = sAppState.getAssetManager().loadFont("Interface/Fonts/TimesNewRoman12Bold.fnt");
        sAs = new StatsAppState(guiNode, guiFont);
        sm.attach(sAs);
        inputManager.addMapping(STATS, new KeyTrigger(KeyInput.KEY_F2));
        //ATTACH PHYSICS DEBUG
        if (pSpace != null) {
            dps = new PhysicsDebugAppState(pSpace);
            sm.attach(dps);
            dps.setEnabled(false);
            inputManager.addMapping(PHYS, new KeyTrigger(KeyInput.KEY_F3));
        }

        //AttachScreenShotAppState
        sap = new ScreenshotAppState("screenshots/");
        sm.attach(sap);
        
        
        final Node rootNode = sAppState.getRootNode();
        //This Is the Primary Way to set Up registered KeyBindings
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String binding, boolean value, float tpf) {
                if (value) {
                    if (binding.equals(EXIT)) {
                        app.stop();
                    } else if (binding.equals(MEM)) {
                        BufferUtils.printCurrentDirectMemory(null);
                    } else if (binding.equals(STATS)) {
                        sAs.toggleStats();
                    } else if (binding.equals(NODE)) {
                        PrintNodeUtil.printNode(rootNode, true);
                    } else if (binding.equals(GUI_NODE)) {
                        PrintNodeUtil.printNode(guiNode);
                    } else if (binding.equals(PHYS)) {
                        dps.setEnabled(!dps.isEnabled());
                    }
                }
            }
        }, EXIT, MEM, STATS, PHYS, NODE, GUI_NODE);
    }

    @Override
    protected void spectreUpdate(float tpf) {
    }

    @Override
    public void cleanUp() {
        sm.detach(sAs);
        sm.detach(dps);
        sm.detach(sap);
    }
}
