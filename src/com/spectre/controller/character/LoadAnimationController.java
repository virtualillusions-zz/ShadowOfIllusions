package com.spectre.controller.character;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Skeleton;
import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.plugins.ogre.AnimData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.spectre.deck;
//
///**
// * Esper Flair Identification Card
// * TODO: SET UP THIS FIELD LOOK AT BOMBCONTROL FOR ASSISANCE IN jme3test.bullet;
// * IDEA: Attack and Defense power independant of effects Effect attack only take effect after anim is completed defense and others take effect instantly
// * TODO: when loading effects use bonePosition and then offset to get the proper position of the character
// * Remember: remember the call in Skeleton Control getAttachmentsNode
// * Remember: Search and replace all locations that calls Card
// * @author Kyle Williams 
// */
public class LoadAnimationController extends com.jme3.scene.control.AbstractControl implements Savable {

    private AnimControl ctrl;
    private ArrayList<String> boneList;

    public void loadAllAnimations(String skelPath, AssetManager manager) {
        AnimData animation = (AnimData) manager.loadAsset(skelPath);
        for (Animation anim : animation.anims) {
            ctrl.addAnim(anim);
        }
    }

    public ArrayList getBoneList() {
        return boneList;
    }

    private void setUpBoneList() {
        Skeleton skeleton = ctrl.getSkeleton();
        boneList = new ArrayList<String>();
        for (int i = 0; i < skeleton.getBoneCount(); i++) {
            boneList.add(skeleton.getBone(i).getName());
        }
        Collections.sort(boneList);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        ctrl = spatial.getControl(com.jme3.animation.AnimControl.class);
        setUpBoneList();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    public Control cloneForSpatial(Spatial sptl) {
        LoadAnimationController cont = new LoadAnimationController();
        cont.setEnabled(enabled);
        cont.setSpatial(sptl);
        return cont;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
    }
}