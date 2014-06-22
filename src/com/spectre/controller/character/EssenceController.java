/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.controller.character;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import com.spectre.controller.character.gui.GuiEssenceController;


/**
 *  This Controller handles Life And Focus meters as well as its GUI Representation
 *  TODO: Focus should equal 0 and be augmented over time
 * @author Kyle Williams
 */
//TODO:Implement All stats in this class this includes speed attack speed as well as cardgeneration time
public class EssenceController extends com.jme3.scene.control.AbstractControl{
    private int Life = 100;
    private int Focus = 20;
    private int CurrentFocus = 20;
    private float reFocusTime =0;
    private float Time =0;
    private float timeInterval = 1;
    private GuiEssenceController gui;
    private float totemSpawnInterval = 10;

    @Override
    public void setSpatial(Spatial spat){
        super.setSpatial(spat);
    }

    public float getCurrentFocus(){return CurrentFocus;}
    public float getTotalFocus(){return Focus;}
    public float getLife(){return Life;}
    public float getReFocusTime(){return reFocusTime;}

    public void setFocus(int newFocus){
        Focus = newFocus;
        gui.setFocus(CurrentFocus, Focus);
    }

    public void setCurrentFocus(int newFocus){
        CurrentFocus = newFocus;
        gui.setFocus(CurrentFocus, Focus);
    }

    public void addFocus(int difference){
        Focus+=difference;
        gui.setFocus(CurrentFocus, Focus);
    }

    public void useFocus(int used){
        CurrentFocus-=used;
        gui.setFocus(CurrentFocus, Focus);
        reFocusTime=Time+timeInterval;
    }

    public void setLife(int newLife){
        Life=newLife;
        gui.setLife(Life);
    }

    public void takeLife(int damage){
        Life-=damage;
        gui.setLife(Life);
    }

    public void setFocusTimeInterval(float newTime){timeInterval=newTime;}
    
    @Override
    protected void controlUpdate(float tpf) {
        if(CurrentFocus<Focus){
            if(reFocusTime>Time){
                CurrentFocus++;
                System.out.println(CurrentFocus);
            }else{
                reFocusTime+=timeInterval;
            }
            Time+=tpf;
        }        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getTotemSpawnInterval() {
        return totemSpawnInterval;
    }
    
    public void setGuiController(GuiEssenceController GUI){
        this.gui=GUI;
        //gui.start(spatial);
    }

}