/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.character;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.AbstractPhysicsControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;
import com.spectre.app.SpectreApplication;
import com.spectre.controller.character.impl.ActionModeControl;
import com.spectre.controller.character.impl.AnimationControl;
import com.spectre.controller.character.impl.CamControl;
import com.spectre.controller.character.impl.CharControl;
import com.spectre.director.Director;
import java.util.List;
import java.util.logging.Level;

/**
 * This class controls all physics aspect of the character THE KEYWORD IS DAMAGE
 * Due to stability issues with the KinematicRagdollControl this class will also
 * make use of CharacterControl to perform normal operations not yet supported
 * by KinematicRagdollControl
 *
 * @author Kyle Williams Esper Flair Identification Card TODO: SET UP THIS FIELD
 * LOOK AT BOMBCONTROL FOR ASSISANCE IN jme3test.bullet; IDEA: Attack and
 * Defense power independent of effects Effect attack only take effect after
 * anim is completed defense and others take effect instantly TODO: when loading
 * effects use bonePosition and then offset to get the proper position of the
 * character Remember: remember the call in Skeleton Control getAttachmentsNode
 * Remember: Search and replace all locations that calls Card
 *
 * @author Kyle Williams This is intended to be a replacement for the internal
 * bullet character class. A RigidBody with cylinder collision shape is used and
 * its velocity is set continuously, a ray test is used to check if the
 * character is on the ground.
 *
 * The character keeps his own local coordinate system which adapts based on the
 * gravity working on the character so the character will always stand upright.
 *
 * Forces in the local x/z plane are dampened while those in the local y
 * direction are applied fully (e.g. jumping, falling).
 *
 * @author normenhansen
 */
public final class SpectrePhysicsController extends AbstractPhysicsControl implements CharControl, ActionModeControl {

    /**
     * Determines if the control is in an action state or caster state
     */
    public boolean actionMode = false;
    private static final float gravity = Director.gravity;
    private final Vector3f walkDirection = new Vector3f();
    private float physicsDamping = 0.9f;
    private final int BASIC_MAX_SPEED = 50;
    private final float runningSpeed = 1.00f;
    private final float joggingSpeed = 0.50f;
    private final float walkingSpeed = 0.25f;
    private float airTime = 0.0f;
    private float altitude = 0.0f;//calculate altitute, used specifically to check if jumping or falling
    private int directionForward = 0;
    private int directionLeft = 0;
    private float walkSpeed = 0.0f;
    private Vector3f auxillary = new Vector3f();
    private Camera cam;
    private boolean jump = false;
    private boolean dash = false;
    private boolean evade = false;
    private AnimationControl animCont;

    /**
     * Only used for serialization, do not use this constructor.
     */
    public SpectrePhysicsController() {
        shapes = new CollisionShape[2];
    }

    public void setActionMode(boolean modiferButton) {
        actionMode = modiferButton;
    }

    public void moveForward(float value) {
        directionForward = bindMoveVal(value);
    }

    public void moveLeft(float value) {
        directionLeft = bindMoveVal(value);
    }

    /**
     * Used to bind the input directional value between 0.5f and 1f
     *
     * @param value
     * @return
     */
    private int bindMoveVal(float value) {
        int sign = (int) FastMath.sign(value);
        value = FastMath.abs(value);
        value = value >= .5f ? joggingSpeed : walkingSpeed;
        walkSpeed = walkSpeed > value ? walkSpeed : value;
        return sign;
    }

    public void jump() {
        //TODO: debounce over some frames
        if (!onGround) {
            return;
        }
        jump = true;
    }

    public void evade() {
        if (!onGround) {
            return;
        }
        evade = true;
    }

    public void dash() {
        if (!onGround) {
            return;
        }
        dash = true;
    }

    public boolean isInAir() {
        return airTime > .03f;
    }

    public boolean isFalling() {
        boolean isFalling = rigidBody_location.getY() < altitude;
        return isInAir() && isFalling;
    }

    public boolean isJumping() {
        boolean isJumping = rigidBody_location.getY() > altitude;
        return isInAir() && isJumping;
    }

    /**
     * A Function to aid in the walking vector takes the direction desired and
     * if the integer value is negative the vector is negated
     *
     * @param direction
     * @param cameraDirection The camera vector that should be used
     * @param temp A temporary Vector used to quickly store information. Please
     * use TempVars
     * @param store A vector to store the result
     */
    private void walkDirection(int direction, Vector3f cameraDirection, Vector3f temp, Vector3f store) {
        Vector3f camDir = temp;
        camDir.set(cameraDirection);
        if (direction < 0) {//since using "camLeft" only negate if pos
            camDir.negateLocal();
        }
        camDir.normalizeLocal();
        store.addLocal(camDir);
    }

    /**
     * Toggle character ducking. When ducked the characters capsule collision
     * shape height will be multiplied by duckedFactor to make the capsule
     * smaller. When unducking, the character will check with a ray test if it
     * can in fact unduck and only do so when its possible. You can check the
     * state of the unducking by checking isDucked().
     *
     * @param enabled
     */
    private void setDucked(boolean enabled) {
        if (enabled) {
            wantToUnDuck = false;
            updateCollisionShape(true);
        } else {
            if (!checkCanUnDuck()) {
                updateCollisionShape(false);
            } else {
                wantToUnDuck = true;
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        rigidBody.getPhysicsLocation(rigidBody_location);
        //rotation has been set through viewDirection
        applyPhysicsTransform(rigidBody_location, rotation);
    }

    /**
     * Used internally, don't call manually
     *
     * @param space
     * @param tpf
     */
    public void physicsTick(PhysicsSpace space, float tpf) {
        rigidBody.getLinearVelocity(velocity);
    }

    /**
     * Used internally, don't call manually
     *
     * @param space
     * @param tpf
     */
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        onGround = isOnGround();

        if (wantToUnDuck && checkCanUnDuck()) {
            wantToUnDuck = false;
            updateCollisionShape(false);
        }

        activePhysicsTick();
        passivePhysicsTick();
        //TODO: THIS CAUSES ISSUE WHEN CALLING ISJUMPING/ISFALLING 
        //POSSIBILITY TO RETURN FALSE NEGATIVES DUE TO INCONSISTENT UPDATE
        //final cached values        
        //Calculate new Airtime here to avoid issue
        airTime = onGround ? 0 : airTime + tpf;
        //calculate altitute, used specifically to check if jumping or falling
        altitude = onGround ? 0 : rigidBody_location.getY();
    }

    private void activePhysicsTick() {
        TempVars vars = TempVars.get();

        Vector3f tempWlkDir = vars.vect1.zero();//make seperate vector to allow interpolation
        //Used later on to create smooth transitions speeding up or slowing down
        float interpolateScalar = 0.3f;

        //BASIC_MOVEMENT
        if (directionForward != 0 || directionLeft != 0) {

            if (directionForward != 0) {
                walkDirection(directionForward, cam.getDirection(), vars.vect2, tempWlkDir);
            }
            if (directionLeft != 0) {
                walkDirection(directionLeft, cam.getLeft(), vars.vect2, tempWlkDir);
            }

            float tempVal = actionMode == true ? runningSpeed : walkSpeed;
            tempWlkDir.multLocal(BASIC_MAX_SPEED * tempVal);
            walkSpeed = 0;
            directionForward = 0;
            directionLeft = 0;
        }


        //This is added to allow scalar to remain at 0.01f to help with turning
        //and set here to prevent prolonged stop
        if (tempWlkDir.equals(Vector3f.ZERO)) {
            interpolateScalar = 1f;
        }
        //Interpolate current walk direction to new one to prevent sparatic movement 
        walkDirection.interpolateLocal(tempWlkDir, interpolateScalar);
        vars.release();
    }

    private void passivePhysicsTick() {
        TempVars vars = TempVars.get();
        // dampen existing x/z forces
        float existingLeftVelocity = velocity.dot(localLeft);
        float existingForwardVelocity = velocity.dot(localForward);
        Vector3f counter = vars.vect1;
        existingLeftVelocity = existingLeftVelocity * physicsDamping;
        existingForwardVelocity = existingForwardVelocity * physicsDamping;
        counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        localForwardRotation.multLocal(counter);
        velocity.addLocal(counter);

        walkDirection.setY(0);
        float designatedVelocity = walkDirection.length();
        if (designatedVelocity > 0) {
            Vector3f localWalkDirection = vars.vect1;
            //normalize walkdirection
            localWalkDirection.set(walkDirection).normalizeLocal();
            //check for the existing velocity in the desired direction
            float existingVelocity = velocity.dot(localWalkDirection);
            //calculate the final velocity in the desired direction
            float finalVelocity = designatedVelocity - existingVelocity;
            localWalkDirection.multLocal(finalVelocity);
            //instananeously set view direction
            this.setViewDirection(localWalkDirection);
            //add resulting vector to existing velocity
            velocity.addLocal(localWalkDirection);
        }
        rigidBody.setLinearVelocity(velocity);

//        if (jump) {
//            //TODO: precalculate jump force
//            Vector3f rotatedJumpForce = vars.vect1;
//            float distance = -gravity + mass * 50f;  //must negate gravity and add mass
//            rotatedJumpForce.set(0, distance, 0);
//            rigidBody.applyImpulse(localForwardRotation.multLocal(rotatedJumpForce), Vector3f.ZERO);
//            jump = false;
//
//        }
        vars.release();
    }

    public void collision(PhysicsCollisionEvent event) {
    }

    /**
     * Sets the view direction for the character. Note this only defines the
     * rotation of the spatial in the local x/z plane of the character.
     *
     * @param vec
     */
    public void setViewDirection(Vector3f vec) {
        viewDirection.set(vec);
        updateLocalViewDirection();
    }

    /**
     * This actually sets a new collision shape to the character to change the
     * height of the capsule.
     *
     * @param isDucked
     */
    protected void updateCollisionShape(boolean isDucked) {
        ducked = isDucked;
        CollisionShape shape = isDucked == false ? shapes[0] : shapes[1];
        rigidBody.setCollisionShape(shape);
    }

    /**
     * This returns the current positive Y extent of the capsule shape
     *
     * @return
     */
    private float getCurrentHeight() {
        return ducked == true ? this.height * duckedFactor : this.height;
    }

    /**
     * Check if the character is on the ground. This is determined by a ray test
     * in the center of the character and might return false even if the
     * character is not falling yet.
     *
     * @return
     */
    public boolean isOnGround() {
        TempVars vars = TempVars.get();
        Vector3f location = vars.vect1;
        Vector3f rayVector = vars.vect2;
        location.set(localUp).multLocal(getCurrentHeight()).addLocal(this.rigidBody_location);
        //values chosen after testing
        //rayVector.set(localUp).multLocal(-getCurrentHeight() - FastMath.ZERO_TOLERANCE).addLocal(location);
        rayVector.set(localUp).multLocal(-1.1f * getCurrentHeight() - FastMath.ZERO_TOLERANCE);
        rayVector.addLocal(location);

        List<PhysicsRayTestResult> results = space.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This checks if the character can go from ducked to unducked state by
     * doing a ray test.
     */
    protected boolean checkCanUnDuck() {
        TempVars vars = TempVars.get();
        Vector3f location = vars.vect1;
        Vector3f rayVector = vars.vect2;
        location.set(localUp).multLocal(FastMath.ZERO_TOLERANCE).addLocal(this.rigidBody_location);
        rayVector.set(localUp).multLocal(height + FastMath.ZERO_TOLERANCE).addLocal(location);
        List<PhysicsRayTestResult> results = space.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Realign the local forward vector to given direction vector, if null is
     * supplied Vector3f.UNIT_Z is used. Input vector has to be perpendicular to
     * current gravity vector. This normally only needs to be called when the
     * gravity direction changed continuously and the local forward vector is
     * off due to drift. E.g. after walking around on a sphere "planet" for a
     * while and then going back to a y-up coordinate system the local z-forward
     * might not be 100% aligned with Z axis.
     *
     * @param vec The new forward vector, has to be perpendicular to the current
     * gravity vector!
     */
    public void resetForward(Vector3f vec) {
        if (vec == null) {
            vec = Vector3f.UNIT_Z;
        }
        localForward.set(vec);
        updateLocalCoordinateSystem();
    }

    /**
     * Get the current linear velocity along the three axes of the character.
     * This is prepresented in world coordinates, parent coordinates when the
     * control is set to applyLocalPhysics.
     *
     * @return The current linear velocity of the character
     */
    public Vector3f getVelocity() {
        return velocity;
    }

    /**
     * Set the gravity for this character. Note that this also realigns the
     * local coordinate system of the character so that continuous changes in
     * gravity direction are possible while maintaining a sensible control over
     * the character.
     *
     * @param gravity
     */
    public void setGravity(Vector3f gravity) {
        rigidBody.setGravity(gravity);
        localUp.set(gravity).normalizeLocal().negateLocal();
        updateLocalCoordinateSystem();
    }

    /**
     * Sets how much the physics forces in the local x/z plane should be
     * dampened.
     *
     * @param physicsDamping The dampening value, 0 = no dampening, 1 = no
     * external force, default = 0.9
     */
    public void setPhysicsDamping(float physicsDamping) {
        this.physicsDamping = physicsDamping;
    }

    /**
     * Gets how much the physics forces in the local x/z plane should be
     * dampened.
     */
    public float getPhysicsDamping() {
        return physicsDamping;
    }

    /**
     * Updates the local coordinate system from the localForward and localUp
     * vectors, adapts localForward, sets localForwardRotation quaternion to
     * local z-forward rotation.
     */
    private void updateLocalCoordinateSystem() {
        //gravity vector has possibly changed, calculate new world forward (UNIT_Z)
        calculateNewForward(localForwardRotation, localForward, localUp);
        localLeft.set(localUp).crossLocal(localForward);
        rigidBody.setPhysicsRotation(localForwardRotation);
        updateLocalViewDirection();
    }

    /**
     * Updates the local x/z-flattened view direction and the corresponding
     * rotation quaternion for the spatial.
     */
    private void updateLocalViewDirection() {
        //update local rotation quaternion to use for view rotation
        localForwardRotation.multLocal(rotatedViewDirection.set(viewDirection));
        calculateNewForward(rotation, rotatedViewDirection, localUp);
    }

    /**
     * This method works similar to Camera.lookAt but where lookAt sets the
     * priority on the direction, this method sets the priority on the up vector
     * so that the result direction vector and rotation is guaranteed to be
     * perpendicular to the up vector.
     *
     * @param rotation The rotation to set the result on or null to create a new
     * Quaternion, this will be set to the new "z-forward" rotation if not null
     * @param direction The direction to base the new look direction on, will be
     * set to the new direction
     * @param worldUpVector The up vector to use, the result direction will be
     * perpendicular to this
     * @return
     */
    private void calculateNewForward(Quaternion rotation, Vector3f direction, Vector3f worldUpVector) {
        if (direction == null) {
            return;
        }
        TempVars vars = TempVars.get();
        Vector3f newLeft = vars.vect1;
        Vector3f newLeftNegate = vars.vect2;

        newLeft.set(worldUpVector).crossLocal(direction).normalizeLocal();
        if (newLeft.equals(Vector3f.ZERO)) {
            if (direction.x != 0) {
                newLeft.set(direction.y, -direction.x, 0f).normalizeLocal();
            } else {
                newLeft.set(0f, direction.z, -direction.y).normalizeLocal();
            }
            SpectreApplication.logger.log(Level.INFO, "Zero left for direction {0}, up {1}",
                    new Object[]{direction, worldUpVector});
        }
        newLeftNegate.set(newLeft).negateLocal();
        direction.set(worldUpVector).crossLocal(newLeftNegate).normalizeLocal();
        if (direction.equals(Vector3f.ZERO)) {
            direction.set(Vector3f.UNIT_Z);
            SpectreApplication.logger.log(Level.INFO, "Zero left for left {0}, up {1}",
                    new Object[]{newLeft, worldUpVector});
        }
        if (rotation != null) {
            rotation.fromAxes(newLeft, worldUpVector, direction);
        }
        vars.release();
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * spatial is attached for example.
     *
     * @param vec
     */
    @Override
    public void setPhysicsLocation(Vector3f vec) {
        rigidBody.setPhysicsLocation(vec);
        rigidBody_location.set(vec);
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * spatial is attached for example. We don't set the actual physics rotation
     * but the view rotation here. It might actually be altered by the
     * calculateNewForward method.
     *
     * @param quat
     */
    @Override
    public void setPhysicsRotation(Quaternion quat) {
        rotation.set(quat);
        rotation.multLocal(rotatedViewDirection.set(viewDirection));
        updateLocalViewDirection();
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * control is supposed to add all objects to the physics space.
     *
     * @param space
     */
    @Override
    public void addPhysics(PhysicsSpace space) {
        space.getGravity(localUp).normalizeLocal().negateLocal();
        updateLocalCoordinateSystem();

        space.addCollisionObject(rigidBody);
        space.addTickListener(this);
        space.addCollisionListener(this);
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * control is supposed to remove all objects from the physics space.
     *
     * @param space
     */
    @Override
    public void removePhysics(PhysicsSpace space) {
        space.removeCollisionObject(rigidBody);
        space.removeTickListener(this);
        space.removeCollisionListener(this);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (this.spatial != null && this.spatial != spatial) {
            removeSpatialData(this.spatial);
        } else if (this.spatial == spatial) {
            return;
        }
        this.spatial = spatial;
        if (spatial == null) {
            return;
        }

        recalculateShapes();
        rigidBody = new PhysicsRigidBody(shapes[0], mass);
        rigidBody.setAngularFactor(0);

        //super call
        createSpatialData(this.spatial);
        setPhysicsLocation(getSpatialTranslation());
        setPhysicsRotation(getSpatialRotation());

        SpectreApplication.logger.log(Level.INFO, "Created Collision Object/s for {0}", spatial.getName());
    }


    /*
     * Create new character collision shapes with parameters based on boundingbox
     * Note: ideally their should be a more advanced compoundshape similar to KinemeticRagDoll
     */
    private void recalculateShapes() {
        BoundingBox bb = (BoundingBox) spatial.getWorldBound();
        //use smaller side extent over larger side to create a more narrow hitbox 
        //and because A-Pose and T-Pose difference greatly impacts value
        radius = bb.getXExtent() < bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        radius /= 2;
        height = bb.getYExtent() * 2;
        mass = 1 * bb.getVolume();//random

        //     create collision shapes and wrap them in compound shape to help with offset
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        shapes[0] = new CapsuleCollisionShape(radius, height - (2 * radius));//standing     
        ccs.addChildShape(shapes[0], new Vector3f(0, height / 2.0f, 0));
        shapes[0] = ccs;

        ccs = new CompoundCollisionShape();
        shapes[1] = new CapsuleCollisionShape(radius, ((height * duckedFactor) - (2 * radius)));//ducked    
        ccs.addChildShape(shapes[1], new Vector3f(0, (height * duckedFactor) / 2.0f, 0));
        shapes[1] = ccs;
    }

    @Override
    protected void createSpatialData(Spatial spat) {
        rigidBody.setUserObject(spatial);
    }

    @Override
    public void removeSpatialData(Spatial spat) {
        rigidBody.setUserObject(null);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        super.render(rm, vp);
    }

    public Control cloneForSpatial(Spatial spatial) {
        SpectrePhysicsController control = new SpectrePhysicsController();
        control.setSpatial(spatial);
        return control;
    }

    public Spatial getSpatial() {
        return this.spatial;
    }

    /**
     * A references to the character's current animation controller
     *
     * @return
     */
    private AnimationControl getAnimationControl() {
        if (animCont == null) {
            animCont = getSpatial().getControl(AnimationControl.class);
        }
        return animCont;
    }

    public void startUp() {
        Director.getPhysicsSpace().add(this);
        cam = spatial.getControl(CamControl.class).getCamera();
    }

    public void cleanUp() {
        Director.getPhysicsSpace().remove(this);
        cam = null;
    }
    private PhysicsRigidBody rigidBody;
    private float height;
    private float radius;
    private float mass;
    private final float duckedFactor = 0.4f;
    /**
     * Local up direction, derived from gravity.
     */
    private final Vector3f localUp = new Vector3f(0, 1, 0);
    /**
     * Local absolute z-forward direction, derived from gravity and UNIT_Z,
     * updated continuously when gravity changes.
     */
    private final Vector3f localForward = new Vector3f(0, 0, 1);
    /**
     * Local left direction, derived from up and forward.
     */
    private final Vector3f localLeft = new Vector3f(1, 0, 0);
    /**
     * Local z-forward quaternion for the "local absolute" z-forward direction.
     */
    private final Quaternion localForwardRotation = new Quaternion(Quaternion.DIRECTION_Z);
    /**
     * Is a z-forward vector based on the view direction and the current local
     * x/z plane.
     */
    private final Vector3f viewDirection = new Vector3f(0, 0, 1);
    /**
     * Stores final spatial location, corresponds to RigidBody location.
     */
    private final Vector3f rigidBody_location = new Vector3f();
    /**
     * Stores final spatial rotation, is a z-forward rotation based on the view
     * direction and the current local x/z plane. See also rotatedViewDirection.
     */
    private final Quaternion rotation = new Quaternion(Quaternion.DIRECTION_Z);
    private final Vector3f rotatedViewDirection = new Vector3f(0, 0, 1);
    private final Vector3f velocity = new Vector3f();
    private boolean ducked = false;
    private boolean onGround = false;
    private boolean wantToUnDuck = false;
    private final CollisionShape[] shapes;

    public void ControlChanged() {
        animCont = null;
    }
}
