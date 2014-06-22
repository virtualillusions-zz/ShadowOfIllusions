/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.physics.subsystems;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.TempVars;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.spectre.app.SpectreApplicationState;
import com.spectre.app.SpectreControl;
import com.spectre.scene.camera.CameraSystem;
import com.spectre.scene.camera.components.CameraDirectionPiece;
import com.spectre.scene.visual.VisualSystem;
import com.spectre.app.input.Buttons;
import com.spectre.app.input.Buttons.ControlInputs;
import com.spectre.app.input.SpectreInputListener;
import com.spectre.scene.visual.components.ActionModePiece;
import com.spectre.systems.physics.PhysicsSystem;
import com.spectre.util.math.MathUtil;
import com.spectre.util.math.Vector3fPiece;
import java.io.IOException;
import java.util.List;
import test.system.TestPhysicsSystem;

/**
 * AbstractPhysicsControl manages the life cycle of a physics object that is
 * attached to a spatial in the SceneGraph.
 *
 * TODO: re-implement with ragdoll physics
 *
 * @author Kyle
 */
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
 * @author Kyle D. Williams
 */
public class CharacterPhysicsController extends SpectreControl implements SpectrePhysicsControl, SpectreInputListener {

    public static void main(String[] args) {
        Application app = new SimpleApplication(
                new SpectreApplicationState(),
                new VisualSystem(),
                new CameraSystem(),
                new PhysicsSystem(),
                new TestPhysicsSystem()) {
            @Override
            public void simpleInitApp() {
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        //settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }

    public CharacterPhysicsController(EntityId id,
            EntityData entityData,
            InputManager inputManager,
            PhysicsSpace physicsSpace) {
        this.id = id;
        this.ed = entityData;
        this.inputManager = inputManager;
        this.physicsSpace = physicsSpace;
        this.shapes = new CollisionShape[2];
    }
////////////////////Spatial.setSpatial(Spatial spat)/////////////////////////

    @Override
    public void SpectreControl() {
        recalculateShapes();
        rigidBody = new PhysicsRigidBody(shapes[0], mass);
        rigidBody.setAngularFactor(0);

        //Set Final Position and Orientation
        rigidBody.setUserObject(spatial);
        setPhysicsLocation(getSpatialTranslation());
        setPhysicsRotation(getSpatialRotation());
        //Finally register input
        inputManager.addListener(this, Buttons.getPhysicsButtons(id));

        //Attach to physicsSpace
        physicsSpace.add(this);

        log.trace("Created Collision Object/s for {0}", spatial.getName());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        physicsSpace.remove(this);
        physicsSpace = null;
        rigidBody.setUserObject(null);
        inputManager.removeListener(this);
        inputManager = null;
        ed = null;
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

        //create collision shapes and wrap them
        //in compound shape to help with offset
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        shapes[0] = new CapsuleCollisionShape(radius, height - (2 * radius));//standing     
        ccs.addChildShape(shapes[0], new Vector3f(0, height / 2.0f, 0));
        shapes[0] = ccs;

        ccs = new CompoundCollisionShape();
        shapes[1] = new CapsuleCollisionShape(radius, ((height * duckedFactor) - (2 * radius)));//ducked    
        ccs.addChildShape(shapes[1], new Vector3f(0, (height * duckedFactor) / 2.0f, 0));
        shapes[1] = ccs;
    }
/////////////////////////PhysicsSpace.add(Object o)/////////////////////////

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return space;
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        if (space == null) {
            if (this.space != null) {
                removePhysics(this.space);
                added = false;
            }
        } else {
            if (this.space == space) {
                return;
            } else if (this.space != null) {
                removePhysics(this.space);
            }
            addPhysics(space);
            added = true;
        }
        this.space = space;
    }

    /**
     * Called when the physics object is supposed to add all objects it needs to
     * manage to the physics space.
     *
     * @param space
     */
    public void addPhysics(PhysicsSpace space) {
        space.getGravity(localUp).normalizeLocal().negateLocal();
        updateLocalCoordinateSystem();

        space.addCollisionObject(rigidBody);
        space.addTickListener(this);
        space.addCollisionListener(this);
    }

    /**
     * Called when the physics object is supposed to remove all objects added to
     * the physics space.
     *
     * @param space
     */
    public void removePhysics(PhysicsSpace space) {
        space.removeCollisionObject(rigidBody);
        space.removeTickListener(this);
        space.removeCollisionListener(this);
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
//////////////////////////////////////INPUT/////////////////////////////

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed == true && getActionMode()) {
            if (name.equals(id + ":" + ControlInputs.Action1)) {// Jump                
                jump();
            } else if (name.equals(id + ":" + ControlInputs.Action2)) {// evade/Slide 
                evade();
            } else if (name.equals(id + ":" + ControlInputs.Action3)) {// dash
                dash();
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(id + ":" + ControlInputs.CharacterForward)) {//Move Character Up  
            moveForward(value / tpf);
        } else if (name.equals(id + ":" + ControlInputs.CharacterBack)) {//Move Character Back   
            moveForward(-(value / tpf));
        } else if (name.equals(id + ":" + ControlInputs.CharacterLeft)) {//Move Character  Right           
            moveLeft(value / tpf);
        } else if (name.equals(id + ":" + ControlInputs.CharacterRight)) {//Move Character Left
            moveLeft(-(value / tpf));
        }
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
    private float bindMoveVal(float value) {
        int sign = (int) FastMath.sign(value);
        value = FastMath.abs(value);
        value = value >= .5f ? joggingSpeed : walkingSpeed;
        return sign * value;
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

//////////////////////////////////////COMPUTATION/////////////////////////////    
    @Override
    protected void spectreUpdate(float tpf) {
        super.spectreUpdate(tpf);
        //update on local thread

        actionMode = ed.getComponent(id, ActionModePiece.class);
        cameraDirection = ed.getComponent(id, CameraDirectionPiece.class);

        rigidBody.getPhysicsLocation(rigidBody_location);
        //rotation has been set through viewDirection
        applyPhysicsTransform(rigidBody_location, rotation);
    }

    /**
     * Updates Spatial transform to coincide with the parameters
     *
     * @param worldLocation Vector3f
     * @param worldRotation Quaternion
     */
    protected void applyPhysicsTransform(Vector3f worldLocation, Quaternion worldRotation) {
        if (enabled && spatial != null) {
            Vector3f localLocation = spatial.getLocalTranslation();
            Quaternion localRotationQuat = spatial.getLocalRotation();
            if (!applyLocal && spatial.getParent() != null) {
                localLocation.set(worldLocation).subtractLocal(spatial.getParent().getWorldTranslation());
                localLocation.divideLocal(spatial.getParent().getWorldScale());
                localRotationQuat.set(worldRotation);

                spatial.setLocalTranslation(localLocation);
                spatial.setLocalRotation(localRotationQuat);
            } else {
                spatial.setLocalTranslation(worldLocation);
                spatial.setLocalRotation(worldRotation);
            }
        }

    }

    /**
     * Called after the physics has been stepped, use to check for forces etc.
     *
     * @param space the physics space
     * @param tpf the time per frame in seconds
     */
    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        rigidBody.getLinearVelocity(velocity);
    }

    /**
     * Called before the physics is actually stepped, use to apply forces etc.
     *
     * @param space the physics space
     * @param tpf the time per frame in seconds
     */
    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        onGround = isOnGround();

        if (wantToUnDuck && checkCanUnDuck()) {
            wantToUnDuck = false;
            updateCollisionShape(false);
        }
        TempVars vars = TempVars.get();
        activePhysicsTick(vars);
        passivePhysicsTick(vars);
        //TODO: THIS CAUSES ISSUE WHEN CALLING ISFALLING 
        //POSSIBILITY TO RETURN FALSE NEGATIVES DUE TO INCONSISTENT UPDATE
        //final cached values        
        //Calculate new Airtime here to avoid issue
        airTime = onGround ? 0 : airTime + tpf;
        isInAir = airTime > .03f;
        //determine if object is falling using its current and previous position       
        Vector3f tempVec1 = vars.vect1.set(localUp).multLocal(rigidBody_location);
        float length = tempVec1.length();
        isFalling = isInAir && (length < (altitude));
        isRising = isInAir && (length > (altitude));
        //calculate new altitute, used specifically to check if jumping or falling
        altitude = onGround ? 0 : length;
        vars.release();
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
        float multBy = -getCurrentHeight() - FastMath.ZERO_TOLERANCE;
        // multBy *= stickToFloor ? 1.1f : 1f;
        rayVector.set(localUp).multLocal(multBy).addLocal(location);
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
     * Perform computations based on user input
     */
    private void activePhysicsTick(TempVars vars) {
        Vector3f tempWlkDir = vars.vect1.zero();//make seperate vector to allow interpolation
        //Used later on to create smooth transitions speeding up or slowing down
        interolationScalar = 0.1f;//0.09f;

        //BASIC_MOVEMENT
        if (directionForward != 0 || directionLeft != 0) {
            Vector3f camDirection = getCameraDirection(vars.vect2).normalizeLocal();
            if (directionForward != 0) {
                Vector3f tempCamDirection = vars.vect3.set(camDirection);
                calculateWalkDirection(directionForward, tempCamDirection,
                        tempWlkDir, localUp);
            }
            if (directionLeft != 0) {
                Vector3f tempCamLeft = vars.vect3.set(localUp);
                tempCamLeft.crossLocal(camDirection).normalizeLocal();
                calculateWalkDirection(directionLeft, tempCamLeft,
                        tempWlkDir, localUp);
            }
            directionForward = FastMath.abs(directionForward);
            directionLeft = FastMath.abs(directionLeft);
            float walkSpeed = directionForward > directionLeft ? directionForward : directionLeft;
            float tempVal = getActionMode() == true ? runningSpeed : walkSpeed;
            tempWlkDir.multLocal(BASIC_MAX_SPEED * tempVal);
            directionForward = 0;
            directionLeft = 0;
        }


        //This is added to allow scalar to remain at 0.01f to help with turning
        //and set here to prevent prolonged stop
        if (tempWlkDir.equals(Vector3f.ZERO)) {
            interolationScalar = 1f;
        }
        //Interpolate current walk direction to new one to prevent sparatic movement 
        walkDirection.interpolateLocal(tempWlkDir, interolationScalar);
    }

    /**
     * This method works similar to Camera.lookAt but where lookAt sets the
     * priority on the direction, this method sets the priority on the up vector
     * so that the result direction vector and rotation is guaranteed to be
     * perpendicular to the up vector.
     *
     * A function to aid in the walking vector takes the direction desired and
     * if the integer value is negative the vector is negated
     *
     * @param direction float
     * @param directionVector The direction to base the new look direction on,
     * will be set to the new direction
     * @param store A vector to store the result
     */
    private void calculateWalkDirection(float direction,
            Vector3f directionVector, Vector3f store, Vector3f worldUpVector) {
        calculateNewForward(null, directionVector, worldUpVector);
        //negate if direction negative indicating opposite direction
        if (direction < 0.0f) {
            directionVector.negateLocal();
        }
        //finally add to store value...adding allows diagonal
        store.addLocal(directionVector);
    }

    /**
     * Perform basic computation needed to correctly update spatial physics
     */
    private void passivePhysicsTick(TempVars vars) {
        // dampen existing left/forward forces
        float existingLeftVelocity = velocity.dot(localLeft);
        float existingForwardVelocity = velocity.dot(localForward);
        Vector3f counter = vars.vect1;
        existingLeftVelocity = existingLeftVelocity * physicsDamping;
        existingForwardVelocity = existingForwardVelocity * physicsDamping;
        //counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        //NO ABOVE ASSUMES X/Z BE SAFE IN CASE LOCAL LEFT CAN CHANGE
        Vector3f counterLocalLeft = vars.vect2.set(localLeft);
        counterLocalLeft.multLocal(-existingLeftVelocity);
        Vector3f counterLocalForward = vars.vect3.set(localForward);
        counterLocalForward.multLocal(-existingForwardVelocity);
        counter.set(counterLocalLeft).addLocal(counterLocalForward);
        ////FINALLY
        localForwardRotation.multLocal(counter);
        velocity.addLocal(counter);

        //CALCULATE WALK DIRECTION/////////
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
            Vector3f localViewDirection = vars.vect2;
            localViewDirection.set(viewDirection);
            localViewDirection.interpolateLocal(localWalkDirection, 2 * interolationScalar);
            setViewDirection(localViewDirection);
            //add resulting vector to existing velocity
            velocity.addLocal(localWalkDirection);
        }


        if (onGround) {
            //APPLY VELOCITY WHICH ALLOWS THE CHARACTER TO WALK
            rigidBody.setLinearVelocity(velocity);
            stickToFloor = true;
        }
//        else if(isFalling()){
//             rigidBody.applyImpulse(rigidBody.getGravity().divide(4), Vector3f.ZERO);
//        }
        if (jump) {
            if (onGround) {
                //TODO: precalculate jump force
                Vector3f rotatedJumpForce = vars.vect1.set(localUp);
                rotatedJumpForce.multLocal(rigidBody.getGravity());
                rotatedJumpForce.divideLocal(-2f);
                rigidBody.applyImpulse(localForwardRotation.multLocal(rotatedJumpForce), Vector3f.ZERO);
                stickToFloor = false;
            } else if (isFalling) {
                jump = false;
            }
        } else if (stickToFloor && isRising) {
            //  rigidBody.applyImpulse(rigidBody.getGravity(), Vector3f.ZERO);
            //System.out.println("offGround: " + i++);
        }
    }
    int i = 0;

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
            log.trace("Zero left for direction {0}, up {1}",
                    new Object[]{direction, worldUpVector});
        }
        newLeftNegate.set(newLeft).negateLocal();
        direction.set(worldUpVector).crossLocal(newLeftNegate).normalizeLocal();
        if (direction.equals(Vector3f.ZERO)) {
            direction.set(Vector3f.UNIT_Z);
            log.trace("Zero left for left {0}, up {1}",
                    new Object[]{newLeft, worldUpVector});
        }
        if (rotation != null) {
            rotation.fromAxes(newLeft, worldUpVector, direction);
        }
        vars.release();
    }

    public boolean isInAir() {
        return isInAir;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean isRising() {
        return isRising;
    }
////////////////////////////////MISC    

    @Override
    public void collision(PhysicsCollisionEvent event) {
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
     * Called when the physics object is supposed to move to the spatial
     * position.
     *
     * @param vec
     */
    protected void setPhysicsLocation(Vector3f vec) {
        rigidBody.setPhysicsLocation(vec);
        rigidBody_location.set(vec);
    }

    /**
     * Called when the physics object is supposed to move to the spatial
     * rotation.
     *
     *
     * We don't set the actual physics rotation but the view rotation here. It
     * might actually be altered by the calculateNewForward method.
     *
     * @param quat
     */
    protected void setPhysicsRotation(Quaternion quat) {
        rotation.set(quat);
        rotation.multLocal(rotatedViewDirection.set(viewDirection));
        updateLocalViewDirection();
    }

    public boolean isApplyPhysicsLocal() {
        return applyLocal;
    }

    /**
     * When set to true, the physics coordinates will be applied to the local
     * translation of the Spatial
     *
     * @param applyPhysicsLocal
     */
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        applyLocal = applyPhysicsLocal;
    }

    protected Vector3f getSpatialTranslation() {
        if (applyLocal) {
            return spatial.getLocalTranslation();
        }
        return spatial.getWorldTranslation();
    }

    protected Quaternion getSpatialRotation() {
        if (applyLocal) {
            return spatial.getLocalRotation();
        }
        return spatial.getWorldRotation();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (space != null) {
            if (enabled && !added) {
                if (spatial != null) {
                    setPhysicsLocation(getSpatialTranslation());
                    setPhysicsRotation(getSpatialRotation());
                }
                addPhysics(space);
                added = true;
            } else if (!enabled && added) {
                removePhysics(space);
                added = false;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean getActionMode() {
        return actionMode != null ? actionMode.isActionMode() : false;
    }

    public Vector3f getCameraDirection(Vector3f tempVec) {
        if (cameraDirection != null) {
            return MathUtil.pieceToVec(tempVec, cameraDirection);
        }
        //If no cam direction then go forward according 
        //to character orientation
        tempVec.set(localForward);
        return tempVec;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(applyLocal, "applyLocalPhysics", false);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
        applyLocal = ic.readBoolean("applyLocalPhysics", false);
    }
    private EntityId id;
    private EntityData ed;
    private ActionModePiece actionMode;
    private Vector3fPiece cameraDirection;
    private PhysicsSpace physicsSpace;
    private InputManager inputManager;
    private boolean added = false;
    private PhysicsSpace space = null;
    private boolean applyLocal = false;
    private final Vector3f walkDirection = new Vector3f();
    private float physicsDamping = 0.9f;
    private final int BASIC_MAX_SPEED = 25;
    private final float runningSpeed = 2.00f;
    private final float joggingSpeed = 1.0f;
    private final float walkingSpeed = 0.25f;
    private float airTime = 0.0f;
    private float altitude;//calculate altitute, used specifically to check if jumping or falling
    private boolean isInAir = false;
    private boolean isFalling = false;
    private boolean isRising = false;
    private float directionForward = 0;
    private float directionLeft = 0;
    private Vector3f auxillary = new Vector3f();
    private boolean jump = false;
    private boolean dash = false;
    private boolean evade = false;
    private PhysicsRigidBody rigidBody;
    private float height;
    private float radius;
    private float mass;
    private final float duckedFactor = 0.4f;
    private boolean stickToFloor = true;
    /**
     * Used to interpolate and coordinate viewDirection and walkDirection
     * vectors
     */
    private float interolationScalar = 0.3f;
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
    /**
     * Used for view direction computation
     */
    private final Vector3f rotatedViewDirection = new Vector3f(0, 0, 1);
    private final Vector3f velocity = new Vector3f();
    private boolean ducked = false;
    private boolean onGround = false;
    private boolean wantToUnDuck = false;
    private CollisionShape[] shapes;
}
