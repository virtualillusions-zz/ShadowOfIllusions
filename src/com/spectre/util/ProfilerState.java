/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.util;
 
import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.system.Timer;
import com.spectre.app.SpectreApplication;
import com.spectre.app.SpectreState;
 
/**
 *
 * @author Kyle Williams
 */
public class ProfilerState extends SpectreState{

    private Node profNode = new Node("Profiler Node");
    private Node guiNode;
    private boolean initialized = false;
    private boolean active = true;
    private float secondCounter = 0.0f;
    private BitmapText fpsText,defualtText;
    private BitmapFont guiFont;
    private StatsView statsView;
    private Timer timer;

    public void SpectreState(AppStateManager stateManager, Application app) {
        initialized=true;
        guiNode=((SpectreApplication)app).getGuiNode();
        //Gets the Timer for the secondCounter
        timer = app.getContext().getTimer();
        //The font to be used
        guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        //Sets up the fps and places it on the bottom left of the screen
        fpsText = new BitmapText(guiFont,false);
        fpsText.setSize(guiFont.getCharSet().getRenderedSize());
        fpsText.setLocalTranslation(0,fpsText.getLineHeight(),0);
        fpsText.setText("Frames per second");
        profNode.attachChild(fpsText);
        //sets up the statistical view and places it above the fps text
        statsView = new StatsView("Statistics View",app.getAssetManager(), app.getRenderer().getStatistics());
        statsView.setLocalTranslation(0,fpsText.getLineHeight(),0);
        profNode.attachChild(statsView);
        //A palce Holder Text
        defualtText = new BitmapText(guiFont,false);
        defualtText.setSize(guiFont.getCharSet().getRenderedSize());
        defualtText.setLocalTranslation(0,fpsText.getLineHeight(),0);
        defualtText.setText("Press TAB to Activate Profiler");
        guiNode.attachChild(defualtText);


        if(app.getInputManager()!=null){
            app.getInputManager().addMapping("VEZLA_Profiler", new KeyTrigger(KeyInput.KEY_TAB));

            app.getInputManager().addListener(new ActionListener(){
                @Override
                public void onAction(String name, boolean value, float tpf){
                    if(name.equals("VEZLA_Profiler")&&value==true){
                        if(active==false){
                           guiNode.detachChild(defualtText);
                           guiNode.attachChild(profNode);
                           active=true;
                        }else{
                           guiNode.detachChild(profNode);
                           guiNode.attachChild(defualtText);
                           active=false;
                        }
                    }
                }
            },"VEZLA_Profiler");
        }
    }

    @Override
    public void update(float tpf) {
        secondCounter += timer.getTimePerFrame();
        int fps = (int) timer.getFrameRate();
        if(secondCounter>=1.0f){
            fpsText.setText("Frames per second: "+fps);
            secondCounter=0.0f;
        }
    }
}
