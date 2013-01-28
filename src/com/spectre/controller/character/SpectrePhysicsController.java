/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.animation.Bone;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.RagdollCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.KinematicRagdollControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;
import com.spectre.app.SpectreAbstractController;
import com.spectre.controller.character.SpectreAnimationController.AnimPriority;
import com.spectre.director.Director;

/**
 * This class controls all physics aspect of the character THE KEYWORD IS DAMAGE
 * Due to stability issues with the KinematicRagdollControl this class will also
 * make use of CharacterControl to perform normal operations not yet supported
 * by KinematicRagdollControl
 *
 * @author Kyle Williams
 */
///**
// * Esper Flair Identification Card
// * TODO: SET UP THIS FIELD LOOK AT BOMBCONTROL FOR ASSISANCE IN jme3test.bullet;
// * IDEA: Attack and Defense power independant of effects Effect attack only take effect after anim is completed defense and others take effect instantly
// * TODO: when loading effects use bonePosition and then offset to get the proper position of the character
// * Remember: remember the call in Skeleton Control getAttachmentsNode
// * Remember: Search and replace all locations that calls Card
// * @author Kyle Williams 
// */
public class SpectrePhysicsController extends SpectreAbstractController implements RagdollCollisionListener, PhysicsCollisionListener {

    private KinematicRagdollControl ragdoll;
    private CharacterControl character;
    private boolean actionMode = false;
    private float airTime = 0.0f;
    private float altitude = 0.0f;//calculate altitute, used specifically to check if jumping or falling
    private boolean isMoving = false;
    private Vector3f special = new Vector3f();
    private float DirectionFD = 0;
    private float DirectionLR = 0;
    private final float joggingSpeed = 0.75f;
    private final float walkingSpeed = 0.25f;
    private float highestSpeed; //used to keep track of previous speeds
    private boolean jump = false;//use jump boolean to help with animation locking

    /**
     * <pre>
     * A convient method to change animation
     * 1)Basic Movement
     * 2)Start of Jump
     * 3)Falling or jumping
     * 4)End of jump
     * 5)Dash
     * 6)Rolling
     * </pre>
     *
     * @param state
     */
    protected void animUpdate(int state) {
        switch (state) {
            case 1:
                if (actionMode) {
                    getAnimCont().changeAnimation("RunTop", AnimPriority.MOVEMENT);//Running
                } else if (highestSpeed == joggingSpeed) {//some issues here not working like it should
                    getAnimCont().changeAnimation("RunBase", AnimPriority.MOVEMENT);//Joggin
                } else {
                    getAnimCont().changeAnimation("SliceHorizontal", AnimPriority.MOVEMENT);//Walking
                    //System.out.println("SHOULD BE");
                }
                break;
            case 2:
                getAnimCont().changeAnimation("JumpStart", AnimPriority.PREJUMP);
                getAnimCont().setAnimLock();//lock it here so start of jump depends on animation length
                break;
            case 3:
                if (isFalling()) {
                    getAnimCont().changeAnimation("JumpLoop", AnimPriority.FALLING);
                } else if (isJumping()) {
                    getAnimCont().changeAnimation("SliceVertical", AnimPriority.JUMPING);
                }
                break;
            case 4:
                getAnimCont().changeAnimation("JumpEnd", AnimPriority.LANDED);
                getAnimCont().setAnimLock();//lock out movement until anim is done            
                break;
            case 5:
                getAnimCont().changeAnimation("HandsRelaxed", AnimPriority.ACTIONMOVEMENT);
                getAnimCont().setAnimLock();//lock out movement until anim is done            
                break;
            case 6:
                getAnimCont().changeAnimation("HandsClosed", AnimPriority.ACTIONMOVEMENT);
                getAnimCont().setAnimLock();//lock out movement until anim is done            
                break;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (getAnimCont().getAnimLock()) {//check if update bound by animation
            return;

        } else if (jump == true) {
            character.jump();
            jump = false;
        }


        TempVars var = TempVars.get();//Create temporary values
        Vector3f walkDirection = var.vect1.zero();//Zero out walk Direction
        float interpolateScalar = 0.01f;//Used later on to create smooth transitions speeding up or slowing down
        boolean setViewDirection = false;//because of some slight issues with view direction use this boolean to determine if it should be set or not

        if (character.onGround() && airTime > 0.3f) {

            this.animUpdate(4);
            interpolateScalar = 1f;//by doing this will be set to 0 to simulate since just fell
        } else if (getIsInAir()) {//is character jumpting or falling

            animUpdate(3);
            walkDirection.set(character.getWalkDirection());//if jumping keep going in same direction
        } else if (!special.equals(Vector3f.ZERO)) {//check if character dash or roll

            special.interpolate(Vector3f.ZERO, 0.01f);

            walkDirection.set(special);
            interpolateScalar = 1f;
            setViewDirection = true;

            if (FastMath.abs(special.getX()) < 0.1f && FastMath.abs(special.getZ()) < 0.1f) {//put after set walk direction so don't interrput viewDirection
                special.zero();
            }
        } else if (DirectionFD != 0 || DirectionLR != 0) {//If no special vector then look at player movement input

            animUpdate(1);
            //ANIM FITST TO PREVENT ANY ISSUES
            if (DirectionFD != 0) {
                Vector3f camDir = var.vect3;
                camDir.set(getCamera().getDirection());
                if (DirectionFD < 0) {
                    camDir.negateLocal();
                }
                camDir.setY(0f);
                float tempVal = actionMode == true ? 1.2f : FastMath.abs(DirectionFD);
                camDir.multLocal(tempVal);
                walkDirection.addLocal(camDir);
                DirectionFD = 0;
            }
            if (DirectionLR != 0) {
                Vector3f camLeft = var.vect4;
                camLeft.set(getCamera().getLeft());
                if (DirectionLR > 0) {//since using "camLeft" only negate if pos
                    camLeft.negateLocal();
                }
                camLeft.setY(0f);
                float tempVal = actionMode == true ? 1.2f : FastMath.abs(DirectionLR);
                camLeft.multLocal(tempVal);
                walkDirection.addLocal(camLeft);
                DirectionLR = 0;
            }
            highestSpeed = 0;//reset LR or FD high for anim purpose
            setViewDirection = true;
        }

        //Interpolate current walk direction to new one to prevent sparatic movement
        walkDirection.set(character.getWalkDirection().interpolate(walkDirection, interpolateScalar));
        if (setViewDirection == true) {
            character.setViewDirection(walkDirection.setY(0));
        }
        //Changes Characters view and walk direction
        character.setWalkDirection(walkDirection);
        //In Order to prevent some hiccups after walkDirection should be zero. Zero out this vector if it reaches below a threshold
        if (FastMath.abs(character.getWalkDirection().getX()) < .001f && FastMath.abs(character.getWalkDirection().getZ()) < .001f) {
            character.getWalkDirection().zero();
        }

        var.release();//release vector info
        //CALCULATE EVERYTHING FOR AIR
        airTime = character.onGround() ? 0 : airTime + tpf;//Calculate new Airtime here to avoid issue
        altitude = character.getPhysicsLocation().getY();//calculate altitute, used specifically to check if jumping or falling
    }

    public void setMod(boolean modiferButton) {//Remember this method changes anim and characters speed can increase by 1.2%
        getAnimCont().setMod(modiferButton);
        actionMode = modiferButton;
    }

    /**
     * Returns the amount of time in Air Largely used for animation
     *
     * @return
     */
    public boolean getIsInAir() {
        return airTime > .03f;
    }

    /**
     * Checks to see if this character isMoving
     */
    public boolean getIsMoving() {
        return isMoving;
    }

    /**
     * Checks if the character is falling
     *
     * @return
     */
    private boolean isFalling() {
        return character.getPhysicsLocation().getY() < altitude;
    }

    /**
     * Checks if the characters is jumping
     *
     * @return
     */
    private boolean isJumping() {
        return character.getPhysicsLocation().getY() > altitude;
    }

    /**
     * makes the character jump
     */
    public void Jump() {
        animUpdate(2);
        jump = true;
    }

    /**
     * Directional value used to determine characters movement Anything above 2
     * is normal moving speed pos_Value: Forward neg_Value: Backward
     *
     * @param value
     */
    public void moveCharacterUD(float value) {
        DirectionFD = bindMoveVal(value);
    }

    /**
     * Directional value used to determine characters movement Anything above 2
     * is normal moving speed pos_Value: Left neg_Value: Right
     *
     * @param value
     */
    public void moveCharacterLR(float value) {
        DirectionLR = bindMoveVal(value);
    }

    /**
     * Used to bind the input directional value between 0.5f and 1f
     *
     * @param value
     * @return
     */
    private float bindMoveVal(float value) {
        float sign = FastMath.sign(value);
        value = FastMath.abs(value);
        value = value >= .5f ? joggingSpeed : walkingSpeed;
        highestSpeed = highestSpeed < value ? value : highestSpeed;//if LR or UD at .75 then switch to that one instead 
       
        return value * sign;
    }

    public void Dash() {
        setSpecial(true);
    }

    public void Roll() {
        setSpecial(false);
    }

    /**
     * 0 Dash 1 Left 2 Right
     *
     * @param vec
     * @param multScalar
     */
    private void setSpecial(boolean Dash) {
        if (special.equals(Vector3f.ZERO)) {//ONLY IF PREV WAS DONE
            TempVars var = TempVars.get();
            Vector3f temp = var.vect1;
            if (Dash == true) {
                temp.set(character.getViewDirection());
                temp.multLocal(20);
                //animUpdate(6);
            } else {
                temp.set(character.getViewDirection());
                temp.multLocal(15);
                //animUpdate(5);
            }

            special.set(temp);
            var.release();

        }
    }

    public void collide(Bone bone, PhysicsCollisionObject object, PhysicsCollisionEvent event) {//ragdoll
//        if (object.getUserObject() != null && object.getUserObject() instanceof Geometry) {
//            Geometry geom = (Geometry) object.getUserObject();
//            if (geom.getName().contains("DAMAGE")) {
//               
//
//            }
//        }
        ///////////////////////
//         Vector3f v = new Vector3f();
//                    v.set(model.getLocalTranslation());
//                    v.y = 0;
//                    model.setLocalTranslation(v);
//                    Quaternion q = new Quaternion();
//                    float[] angles = new float[3];
//                    model.getLocalRotation().toAngles(angles);
//                    q.fromAngleAxis(angles[1], Vector3f.UNIT_Y);
//                    model.setLocalRotation(q);
//                    if (angles[0] < 0) {
//                        animChannel.setAnim("StandUpBack");
//                        ragdoll.blendToKinematicMode(0.5f);
//                    } else {
//                        animChannel.setAnim("StandUpFront");
//                        ragdoll.blendToKinematicMode(0.5f);
//                    }
    }

    //Used to collect collision information
    public void collision(PhysicsCollisionEvent event) {//character
        if (event.getNodeA().getName().contains("DAMAGE") || event.getNodeB().getName().contains("DAMAGE")) {
            if (event.getNodeA().getUserData("playerName").equals(getPlayerName()) || event.getNodeB().getUserData("playerName").equals(getPlayerName())) {
                //DEPENDING ON DAMAGE TAKEN GET 
                character.setEnabled(false);
                ragdoll.setEnabled(true);
                ragdoll.setRagdollMode();
            }
        }
    }

    @Override
    public void startUp() {
        special.zero();

        //SETUP RAGDOLLCONTROL
        if (spatial.getControl(KinematicRagdollControl.class) != null) {
            ragdoll = spatial.getControl(KinematicRagdollControl.class);
        } else {
            ragdoll = new KinematicRagdollControl(0.5f);
            //ragdoll.addCollisionListener(this);
            spatial.addControl(ragdoll);
        }
        Director.getPhysicsSpace().add(ragdoll);
        ragdoll.setEnabled(false);//by default set to false

        //SETUP CHARACTERCONTROL
        com.jme3.bounding.BoundingBox bb = (com.jme3.bounding.BoundingBox) spatial.getWorldBound();
        float diameter = bb.getXExtent() > bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        float height = bb.getYExtent() / 2 - diameter / 4;

        if (spatial.getControl(CharacterControl.class) != null) {
            character = spatial.getControl(CharacterControl.class);
        } else {
            character = new CharacterControl(new CapsuleCollisionShape(diameter, height), bb.getYExtent() );
            spatial.addControl(character);
        }
        //character.setGravity(character.getGravity() * .1f);
        //character.setJumpSpeed();//height * 15f * 3);
        character.setFallSpeed(height * 15f * 3);

        //Director.getPhysicsSpace().addCollisionListener(this);
        Director.getPhysicsSpace().add(character);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        Director.getPhysicsSpace().remove(ragdoll);
        Director.getPhysicsSpace().remove(character);
        //Director.getPhysicsSpace().removeCollisionListener(this);
    }

    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }
}
