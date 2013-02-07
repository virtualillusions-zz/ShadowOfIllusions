/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.animation.Skeleton;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.SpectreAbstractController;
import com.spectre.director.Director;
import java.util.ArrayList;
import java.util.Collections;

/**
 * AnimData animation = (AnimData) manager.loadAsset(skelPath); basic moves are
 * idle all weaponANimation to contain attack in name
 *
 * @author Kyle Williams
 */
public class SpectreAnimationController extends SpectreAbstractController implements AnimEventListener {

    public enum AnimPriority {

        DEBUG,
        IDLE,
        MOVEMENT,
        CARD,
        ACTIONMOVEMENT,
        PREJUMP,
        JUMPING,
        FALLING,
        LANDED,
        CINEMATICS
    }
    private AnimControl control;
    private AnimChannel mainChannel;
    private ArrayList<String> boneList;
    private float currentPriority = -1;//Used to compare the priority of the current animation playing with the new one being requested
    // IDEALLY: 0 no priority, 1 low priority, 2 medium priority, 3 high priority, 4 cinematics
    private String idleName;//The name of the animation that should be played when idle. IDEALLY: 3 states Healthy, Tired, Near Death in both Caster and Action modes
    private boolean isActionMode = false;
    private boolean animCompleted = true;//Used to check if current animation completed
    private float idleTimer = 0;//Used to check if its okay to Idle

    /**
     * <pre>
     * Set Character Resting Animation
     * IDEALLY Bound from 1-3
     * 1.healthy
     * 2.tired
     * 3.injured
     * </pre>
     *
     * @param state
     */
    public void changeCharState(int state) {
        int cState = isActionMode == false ? state : 3 + state;
        this.changeIdleState(cState);
    }

    /**
     * <pre>Set modifer button
     * caster = false
     * action = true </pre>
     *
     * @param modiferButton
     */
    public void setMod(boolean modiferButton) {
        isActionMode = modiferButton;
    }

    /**
     * <pre>Used to change the resting state of the character. < br >
     * 1: Mode: Caster - State: Healthy
     * 2: Mode: Caster - State: Tired
     * 3: Mode: Caster - State: Injured
     * 4: Mode: Action - State: Healthy
     * 5: Mode: Action - State: Tired
     * 6: Mode: Action - State: Injured </pre>
     *
     * @param state
     */
    private void changeIdleState(int state) {
        //Check if state exists if it doesn't change until a state is found all characters must have idle as an animation
        switch (state) {
            case 6:
                if (control.getAnim("action_idle_Injured") != null) {
                    idleName = "action_idle_Injured";
                    break;//put break here so it falls through to next case if not found
                }
            case 5:
                if (control.getAnim("action_idle_Tired") != null) {
                    idleName = "action_idle_Tired";
                    break;
                }
            case 4:
                if (control.getAnim("action_idle") != null) {
                    idleName = "action_idle";
                    break;
                }
            case 3:
                if (control.getAnim("idle_Injured") != null) {
                    idleName = "idle_Injured";
                    break;
                }

            case 2:
                if (control.getAnim("idle_Tired") != null) {
                    idleName = "idle_Tired";
                    break;
                }
            case 1:
                if (control.getAnim("idle") != null) {
                    idleName = "idle";
                    break;
                }
            default://IF IT REACHES HERE JUST SEARCH FOR ANY ANIMATION WITH TITLED IDLE AND USE IT
                for (String idle : control.getAnimationNames()) {
                    if (idle.toUpperCase().contains("IDLE")) {
                        idleName = idle;
                        break;
                    }
                }
                break;
        }
    }

    /**
     * Used to copy a basic set of animations such as walking running idle etc
     * from another skeleton
     *
     * @param animSetName
     */
    public void changeBasicAnimSet(String animSetName) {
        for (Animation data : Director.getAnimations(animSetName).anims) {
            control.addAnim(data);
        }
    }

    /**
     * Used to copy a weapon set of animations such as attack1, attack 2 etc
     * from another skeleton
     *
     * @param animSetName
     */
    public void changeWeaponAnimSet(String animSetName) {
        //First to prevent any issue remove all previous attacks
        for (String attk : control.getAnimationNames()) {
            if (attk.toUpperCase().contains("ATTACK")) {
                control.removeAnim(control.getAnim(attk));
            }
        }

        //Then load new set of attacks
        for (Animation data : Director.getAnimations(animSetName).anims) {
            if (data.getName().toUpperCase().contains("ATTACK")) {
                control.addAnim(data);
            }
        }
    }

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName
     * @param priority
     */
    public void changeAnimation(String animName, AnimPriority priority) {
        changeAnimation(animName, priority, 0.15f);//use default blen time
    }

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName
     * @param priority
     * @param loopMode
     */
    public void changeAnimation(String animName, AnimPriority priority, LoopMode loopMode) {
        changeAnimation(animName, priority, 0.15f, loopMode, 1.0f, 0f);//use default blen time
    }

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName
     * @param priority
     * @param blendTime
     */
    public void changeAnimation(String animName, AnimPriority priority, float blendTime) {
        changeAnimation(animName, priority, blendTime, LoopMode.DontLoop, 1.0f, 0f);
    }

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName
     * @param priority
     * @param loopMode
     * @param speed
     * @param startTime
     */
    public void changeAnimation(String animName, AnimPriority priority, LoopMode loopMode, float speed, float startTime) {
        changeAnimation(animName, priority, 0.15f, loopMode, speed, startTime);
    }

    /**
     * Change currently playing animation if priority is higher
     *
     * @param animName
     * @param priority
     * @param blendTime
     */
    public void changeAnimation(String animName, AnimPriority priority, float blendTime, LoopMode loopMode, float speed, float startTime) {
        if (priority.ordinal() > currentPriority || priority.ordinal() == AnimPriority.DEBUG.ordinal()) {
            if (control.getAnim(animName) == null) {//if animation not present try and laod it
                Animation a = Director.getAnimation(animName);
                if (a == null) {
                    com.spectre.app.SpectreApplication.logger.log(
                            java.util.logging.Level.INFO, "Animation {0} cannot be found in the loaded animation list", animName);
                    return; // unable to load animation
                }
                control.addAnim(a);
            }
            mainChannel.setAnim(animName, blendTime);
            mainChannel.setLoopMode(loopMode);
            mainChannel.setSpeed(speed);
            mainChannel.setTime(startTime);
            currentPriority = priority.ordinal();

        }
        idleTimer = 0f;
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (channel.equals(mainChannel) && !animName.equals(idleName)) {
            currentPriority = -1;//Reset priority to -1 so anything else can take advantage
        }
        animCompleted = true;
    }

    @Override
    public void update(float tpf) {
        /*
         * In order to fix a lot of timing issues with automatic idling from other animations
         * set a timer after last animation so it can try to blend naturally
         */
        if (idleTimer >= .25f) {
            idleName = "Dance";
            changeAnimation(idleName, AnimPriority.IDLE, LoopMode.Loop);
        } else {
            idleTimer += tpf;
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        animCompleted = true;
    }

    public boolean getAnimLock() {
        return !animCompleted;
    }

    public void setAnimLock() {
        animCompleted = false;
    }

    @Override
    public void startUp() {
        super.startUp();
        if (control == null) {//To comply with clone for spatial
            control = spatial.getControl(com.jme3.animation.AnimControl.class);
            if (control.getNumChannels() < 1) {
                mainChannel = control.createChannel();

            } else {
                mainChannel = control.getChannel(0);
            }
            changeIdleState(1);
            changeAnimation(idleName, AnimPriority.IDLE);
            control.addListener(this);
            setUpBoneList();
        }
    }

    public ArrayList getBoneList() {
        return boneList;
    }

    //Bone list queries the editor for bones should be put into a class that extends this instead
    private void setUpBoneList() {
        Skeleton skeleton = control.getSkeleton();
        boneList = new ArrayList<String>();
        for (int i = 0; i < skeleton.getBoneCount(); i++) {
            boneList.add(skeleton.getBone(i).getName());
        }
        Collections.sort(boneList);
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        SpectreAnimationController sacTwo = new SpectreAnimationController();
        sacTwo.boneList = this.boneList;
        sacTwo.control = this.control;
        sacTwo.currentPriority = this.currentPriority;
        sacTwo.enabled = this.enabled;
        sacTwo.idleName = this.idleName;
        sacTwo.mainChannel = this.mainChannel;
        sacTwo.setSpatial(spatial);//Most is done here
        return sacTwo;
    }
}
