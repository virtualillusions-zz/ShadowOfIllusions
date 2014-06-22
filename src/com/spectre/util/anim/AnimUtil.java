/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util.anim;

import com.google.common.collect.Lists;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Skeleton;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Kyle D. Williams
 */
public class AnimUtil {

    public static List<String> getBoneList(List boneList, Spatial spat) {
        AnimControl cont = spat.getControl(AnimControl.class);
        Skeleton skel = cont.getSkeleton();
        return getBoneList(boneList, skel);
    }

    /**
     * @param boneList List
     * @param cont AnimCont
     * @return
     */
    public static List<String> getBoneList(List boneList, AnimControl cont) {
        Skeleton skel = cont.getSkeleton();
        return getBoneList(boneList, skel);
    }

    /**
     * Bone list queries the editor for bones should be put into a class that
     * extends this instead
     *
     * @param boneList List
     * @param skeleton Skeleton
     * @return
     */
    public static List<String> getBoneList(List boneList, Skeleton skeleton) {
        //Skeleton skeleton = control.getSkeleton();
        if (boneList == null) {
            boneList = Lists.newArrayList();
        } else {
            boneList.clear();
        }
        for (int i = 0; i < skeleton.getBoneCount(); i++) {
            boneList.add(skeleton.getBone(i).getName());
        }
        Collections.sort(boneList);

        return boneList;
    }

    /**
     * Used to copy a basic set of animations such as walking running idle etc
     * from another skeleton
     *
     * NOTE: USE WITH CAUTION
     *
     * @param animSetName
     */
    public void changeBasicAnimSet(AnimControl control, String animSetName, AnimData aD, boolean replace) {
        for (Animation data : aD.anims) {
            boolean match = control.getAnim(data.getName()) != null;
            if (!replace || (replace && match)) {
                control.addAnim(data);
            }
        }
    }

    /**
     * Used to copy a weapon set of animations such as attack1, attack 2 etc
     * from another skeleton
     *
     * NOTE: USE WITH CAUTION
     *
     * @param animSetName
     */
    public void changeWeaponAnimSet(AnimControl control, String animSetName, AnimData aD) {
        //First to prevent any issue remove all previous attacks
        for (String attk : control.getAnimationNames()) {
            if (attk.toUpperCase().contains("ATTACK")) {
                control.removeAnim(control.getAnim(attk));
            }
        }

        //Then load new set of attacks
        for (Animation data : aD.anims) {
            if (data.getName().toUpperCase().contains("ATTACK")) {
                control.addAnim(data);
            }
        }
    }
}
