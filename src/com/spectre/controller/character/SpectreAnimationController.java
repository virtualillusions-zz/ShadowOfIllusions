/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.animation.Skeleton;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.impl.ActionModeControl;
import com.spectre.controller.character.impl.AnimationControl;
import com.spectre.controller.character.impl.CharControl;
import com.spectre.director.Director;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * @author Kyle Williams
 */
public class SpectreAnimationController extends AbstractControl implements AnimationControl, ActionModeControl {

    public enum SpectreBasicAnimations implements BasicAnimations {

        WALKING("Walk"),
        JOGING("Walk"),//("Jog"),
        RUNNING("Run"),
        DASHING("SideKick"),//("Dash"),
        EVADING("Punch"),//("Evade"),
        JUMPING("Jumping"),//("Jump"),
        FALLING("Taunt"),//("Fall"),
        PRE_ACTION("JumpStart"),//("PreAction"),
        POST_ACTION("JumpEnd");//("PostAction");
        private String animationName;

        private SpectreBasicAnimations(String animationName) {
            this.animationName = animationName;
        }

        public String getAnimationName() {
            return animationName;
        }
    }
    /**
     * Determines if the control is in an action state or caster state
     */
    public boolean actionMode = false;
    private AnimControl control;
    private AnimChannel mainChannel;
    private ArrayList<String> boneList;
    private Callable futureCall = null;
    private CharControl charCont;
    /**
     * Character current state healthy,tired, or injured used to prevent
     * reseting of idle
     */
    private int idleState = -1;
    /**
     * The name of the animation that should be played when idle. IDEALLY: 3
     * states Healthy, Tired, Near Death in both Caster and Action modes
     */
    private String idleName;
    private String currentAnimation;
    /**
     * Boolean which allows an animation to be fully completed before changing
     */
    private boolean lock;
    /**
     * Used to compare the priority of the current animation playing with the
     * new one being requested
     */
    private AnimPriority currentPriority;

    /**
     * <pre>Used to change the resting state of the character. < br >
     * 1: Mode: Caster - State: Healthy
     * 2: Mode: Caster - State: Tired
     * 3: Mode: Caster - State: Injured
     * 4: Mode: Action - State: Healthy
     * 5: Mode: Action - State: Tired
     * 6: Mode: Action - State: Injured </pre>
     *
     * @param characterState
     */
    @SuppressWarnings("fallthrough")
    public void changeCharState(int characterState) {
        int cState = actionMode == false ? characterState : 3 + characterState;

        if (cState != idleState) {
            //Check if state exists if it doesn't change until a state is found all characters must have idle as an animation
            //NOTE: FALLING THROUGH IS INTENDED
            switch (characterState) {
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
            this.idleState = characterState;
        }
    }

    /**
     * Used to copy a basic set of animations such as walking running idle etc
     * from another skeleton
     *
     * NOTE: USE WITH CAUTION
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
     * NOTE: USE WITH CAUTION
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

    public boolean changeAnimation(String animName, AnimPriority animPriority) {
        return changeAnimation(animName, animPriority, 0.15f);
    }

    public boolean changeAnimation(String animName, AnimPriority animPriority, float blendTime) {
        if (currentPriority.equals(AnimPriority.Level_3)
                || (currentPriority.equals(AnimPriority.Level_2) && lock == true)
                || currentAnimation.equals(animName)) {
            return false;
        }
        //if animation not present try and load it
        if (control.getAnim(animName) == null) {
            Animation a = Director.getAnimation(animName);
            if (a == null) {
                com.spectre.app.SpectreApplication.logger.log(
                        java.util.logging.Level.SEVERE, "Animation {0} cannot be found in the loaded animation list", animName);
                return false; // unable to load animation
            }
            control.addAnim(a);
        }
        mainChannel.setAnim(animName, blendTime);
        currentPriority = animPriority;
        currentAnimation = animName;
        if (!animPriority.equals(AnimPriority.Level_1)) {
            lock = true;
        }
        return true;
    }

    public void setAnimationProperties(LoopMode loopMode, float speed, float startTime) {
        mainChannel.setLoopMode(loopMode);
        mainChannel.setSpeed(speed);
        mainChannel.setTime(startTime);
    }

    public void setFuture(Callable cA) {
        this.futureCall = cA;
    }

    public void setAnimLock() {
        lock = true;
    }

    public void interrupt() {
        lock = false;
    }

    public boolean isAnimLocked() {
        return lock;
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        resetPriority();

        if (futureCall != null) {
            try {
                futureCall.call();
            } catch (Exception ex) {
                SpectreApplication.logger.log(Level.SEVERE, null, ex);
            } finally {
                futureCall = null;
            }
        }
    }

    public void setActionMode(boolean modiferButton) {
        actionMode = modiferButton;
        changeCharState(idleState);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (getCharacterControl().isFalling()) {
            changeAnimation(SpectreBasicAnimations.FALLING.getAnimationName(),
                    AnimationControl.AnimPriority.Level_2);
        } else if (getCharacterControl().isJumping()) {
            changeAnimation(SpectreBasicAnimations.JUMPING.getAnimationName(),
                    AnimationControl.AnimPriority.Level_2);
        } else {
            changeAnimation(idleName, AnimationControl.AnimPriority.Level_1, 0);
        }
    }

    public void startUp() {
        resetPriority();
        currentAnimation = "";
        if (control == null) {//To comply with clone for spatial
            control = spatial.getControl(com.jme3.animation.AnimControl.class);
            if (control.getNumChannels() < 1) {
                mainChannel = control.createChannel();

            } else {
                mainChannel = control.getChannel(0);
            }
            changeCharState(1);
            changeAnimation(idleName, AnimPriority.Level_1);
            control.addListener(this);
        }
        setUpBoneList();
    }

    public void cleanUp() {
        control.removeListener(this);
        currentPriority = AnimPriority.Level_1;
    }

    /**
     * Quick way to set up controller for next animation
     */
    private void resetPriority() {
        currentPriority = AnimPriority.Level_1;//Reset priority to -1 so anything else can take advantage        
        lock = false;

    }

    /**
     * A references to the character's current character controller
     *
     * @return
     */
    private CharControl getCharacterControl() {
        if (charCont == null) {
            charCont = getSpatial().getControl(CharControl.class);
        }
        return charCont;
    }

    /**
     * Bone list queries the editor for bones should be put into a class that
     * extends this instead
     */
    private void setUpBoneList() {
        Skeleton skeleton = control.getSkeleton();
        if (boneList == null) {
            boneList = new ArrayList<String>();
        } else {
            boneList.clear();
        }
        for (int i = 0; i < skeleton.getBoneCount(); i++) {
            boneList.add(skeleton.getBone(i).getName());
        }
        Collections.sort(boneList);
    }

    public ArrayList getBoneList() {
        return boneList;
    }

    public void ControlChanged() {
        charCont = null;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //not needed
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //not used change implies animation changed not changing
    }
}
