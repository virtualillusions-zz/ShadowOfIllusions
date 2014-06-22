/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.util.TempVars;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.spectre.app.SpectreApplicationState;
import com.spectre.app.input.Buttons.ControlInputs;
import java.util.List;
import deprecated.test.system.TestCharacterPhysicsSystem;

/**
 * @Normen Physics should definitely be implemented with the base
 * PhysicsRigidBody etc. objects instead of the PhysicsControl extensions and
 * have the physics thread run completely separate, only setting the results on
 * objects that are in “free fall” or otherwise physics enabled.
 * @author Kyle Williams
 */
public class CharacterPhysicsSystem extends AbstractCharacterPhysicsSystem {

    public static void main(String[] args) {
        Application app = new SimpleApplication(new SpectreApplicationState(),
                new CharacterPhysicsSystem(),
                new TestCharacterPhysicsSystem()) {
            @Override
            public void simpleInitApp() {
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        super.SpectreAppState(sAppState);
        this.physicsSpace.addCollisionListener(this);
        this.physicsSpace.addTickListener(this);
    }

    @Override
    protected void spectreUpdate(EntityId id, TempVars vars, float tpf) {
        Quaternion rotation = get(id, ROTATION_Q);
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);

        rigidBody.getPhysicsLocation(rigidBody_location);
        //rotation has been set through viewDirection
        applyPhysicsTransform(id, rigidBody_location, rotation);
    }

    @Override
    protected void physicsTick(EntityId id, TempVars vars, float tpf) {
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f velocity = get(id, VELOCITY_V);
        rigidBody.getLinearVelocity(velocity);
    }

    @Override
    protected void prePhysicsTick(EntityId id, TempVars vars, float tpf) {
        set(id, ON_GROUND_B, isOnGround(id));
        if (((Boolean) get(id, WANT_TO_UNDUCK_B)) && checkCanUnDuck(id)) {
            set(id, WANT_TO_UNDUCK_B, false);
            updateCollisionShape(id, false);
        }

        activePhysicsTick(id, vars);
        passivePhysicsTick(id, vars);
        //TODO: THIS CAUSES ISSUE WHEN CALLING ISJUMPING/ISFALLING 
        //POSSIBILITY TO RETURN FALSE NEGATIVES DUE TO INCONSISTENT UPDATE
        //final cached values        
        float airTime = get(id, AIR_TIME_F);
        boolean onGround = get(id, ON_GROUND_B);
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);
        //Calculate new Airtime here to avoid issue            
        set(id, AIR_TIME_F, onGround ? 0 : airTime + tpf);
        //calculate altitute, used specifically to check if jumping or falling
        set(id, ALTITUDE_F, onGround ? 0 : rigidBody_location.getY());
    }

    private void activePhysicsTick(EntityId id, TempVars vars) {
        float directionForward = get(id, DIRECTION_FORWARD_F);
        float directionLeft = get(id, DIRECTION_LEFT_F);
        Vector3f tempWlkDir = vars.vect1.zero();//make seperate vector to allow interpolation
        Vector3f walkDirection = get(id, WALK_DIRECTION_V);
        //Used later on to create smooth transitions speeding up or slowing down
        float interpolateScalar = 0.3f;

        //BASIC_MOVEMENT
        if (directionForward != 0 || directionLeft != 0) {
            if (directionForward != 0) {
                Vector3f camDirection = getCameraDirection(id, vars.vect2);
                walkDirection(directionForward, camDirection, vars.vect3, tempWlkDir);
            }
            if (directionLeft != 0) {
                Vector3f camLeft = getCameraLeft(id, vars.vect2);
                walkDirection(directionLeft, camLeft, vars.vect3, tempWlkDir);
            }
            directionForward = FastMath.abs(directionForward);
            directionLeft = FastMath.abs(directionLeft);
            float walkSpeed = directionForward > directionLeft ? directionForward : directionLeft;
            float tempVal = getActionMode(id) == true ? runningSpeed : walkSpeed;
            tempWlkDir.multLocal(BASIC_MAX_SPEED * tempVal);
            set(id, DIRECTION_FORWARD_F, 0);
            set(id, DIRECTION_LEFT_F, 0);
        }


        //This is added to allow scalar to remain at 0.01f to help with turning
        //and set here to prevent prolonged stop
        if (tempWlkDir.equals(Vector3f.ZERO)) {
            interpolateScalar = 1f;
        }
        //Interpolate current walk direction to new one to prevent sparatic movement
        walkDirection.interpolateLocal(tempWlkDir, interpolateScalar);
    }

    private void passivePhysicsTick(EntityId id, TempVars vars) {
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f velocity = get(id, VELOCITY_V);
        Vector3f localLeft = get(id, LOCAL_LEFT_V);
        Vector3f localForward = get(id, LOCAL_FORWARD_V);
        Quaternion localForwardRotation = get(id, LOCAL_FORWARD_ROTATION_Q);
        float physicsDamping = get(id, PHYSICS_DAMPING_F);
        Vector3f walkDirection = get(id, WALK_DIRECTION_V);
        // dampen existing x/z forces
        float existingLeftVelocity = velocity.dot(localLeft);
        float existingForwardVelocity = velocity.dot(localForward);
        Vector3f counter = vars.vect2;
        existingLeftVelocity = existingLeftVelocity * physicsDamping;
        existingForwardVelocity = existingForwardVelocity * physicsDamping;
        counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        localForwardRotation.multLocal(counter);
        velocity.addLocal(counter);

        walkDirection.setY(0);

//System.out.println(walkDirection);
        float designatedVelocity = walkDirection.length();
        if (designatedVelocity > 0) {
            Vector3f localWalkDirection = vars.vect2;
            //normalize walkdirection
            localWalkDirection.set(walkDirection).normalizeLocal();
            //check for the existing velocity in the desired direction
            float existingVelocity = velocity.dot(localWalkDirection);
            //calculate the final velocity in the desired direction
            float finalVelocity = designatedVelocity - existingVelocity;
            localWalkDirection.multLocal(finalVelocity);
            //instananeously set view direction
            this.setViewDirection(id, localWalkDirection);
            //add resulting vector to existing velocity
            velocity.addLocal(localWalkDirection);
        }
        rigidBody.setLinearVelocity(velocity);
        set(id, WALK_DIRECTION_V, walkDirection);
//        if (jump) {
//            //TODO: precalculate jump force
//            Vector3f rotatedJumpForce = vars.vect1;
//            float distance = -gravity + mass * 50f;  //must negate gravity and add mass
//            rotatedJumpForce.set(0, distance, 0);
//            rigidBody.applyImpulse(localForwardRotation.multLocal(rotatedJumpForce), Vector3f.ZERO);
//            jump = false;
//
//        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
    }

    public void moveForward(EntityId id, float value) {
        set(id, DIRECTION_FORWARD_F, bindMoveVal(value));
    }

    public void moveLeft(EntityId id, float value) {
        set(id, DIRECTION_LEFT_F, bindMoveVal(value));
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

    public void jump(EntityId id) {
        //TODO: debounce over some frames
        if (!((Boolean) get(id, ON_GROUND_B))) {
            return;
        }
        set(id, JUMP_B, true);
    }

    public void evade(EntityId id) {
        if (!((Boolean) get(id, ON_GROUND_B))) {
            return;
        }
        set(id, EVADE_B, true);
    }

    public void dash(EntityId id) {
        if (!((Boolean) get(id, ON_GROUND_B))) {
            return;
        }
        set(id, DASH_B, true);
    }

    public boolean isInAir(EntityId id) {
        float airTime = get(id, AIR_TIME_F);
        return airTime > .03f;
    }

    public boolean isFalling(EntityId id) {
        TempVars vars = TempVars.get();
        float altitude = get(id, ALTITUDE_F);
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);
        boolean isFalling = rigidBody_location.getY() < altitude;
        vars.release();
        return isInAir(id) && isFalling;
    }

    public boolean isJumping(EntityId id) {
        TempVars vars = TempVars.get();
        float altitude = get(id, ALTITUDE_F);
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);
        boolean isJumping = rigidBody_location.getY() > altitude;
        vars.release();
        return isInAir(id) && isJumping;
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
    private void walkDirection(float direction, Vector3f cameraDirection,
            Vector3f temp, Vector3f store) {
        Vector3f camDir = temp;
        camDir.set(cameraDirection);
        if (direction < 0.0f) {//since using "camLeft" only negate if pos
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
    private void setDucked(EntityId id, boolean enabled) {
        if (enabled) {
            set(id, WANT_TO_UNDUCK_B, false);
            updateCollisionShape(id, true);
        } else {
            if (!checkCanUnDuck(id)) {
                updateCollisionShape(id, false);
            } else {
                set(id, WANT_TO_UNDUCK_B, true);
            }
        }
    }

    /**
     * Sets the view direction for the character. Note this only defines the
     * rotation of the spatial in the local x/z plane of the character.
     *
     * @param vec
     */
    public void setViewDirection(EntityId id, Vector3f vec) {
        set(id, VIEW_DIRECTION_V, vec);
        updateLocalViewDirection(id);
    }

    /**
     * This actually sets a new collision shape to the character to change the
     * height of the capsule.
     *
     * @param isDucked
     */
    protected void updateCollisionShape(EntityId id, boolean isDucked) {
        set(id, DUCKED_B, isDucked);
        CollisionShape normShape = get(id, NORM_SHAPE);
        CollisionShape duckShape = get(id, DUCK_SHAPE);
        //GOTCHA!!! GENERICS IN TURNARY NOT ALLOWED
        CollisionShape shape = isDucked == false ? normShape : duckShape;
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        rigidBody.setCollisionShape(shape);
    }

    /**
     * Check if the character is on the ground. This is determined by a ray test
     * in the center of the character and might return false even if the
     * character is not falling yet.
     *
     * @return
     */
    public boolean isOnGround(EntityId id) {
        //This returns the current positive Y extent of the capsule shape        

        TempVars vars = TempVars.get();
        float height = get(id, HEIGHT_F);
        boolean ducked = get(id, DUCKED_B);
        float currentHeight = ducked == true ? height * duckedFactor : height;
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f location = vars.vect2;
        Vector3f rayVector = vars.vect3;
        Vector3f localUp = get(id, LOCAL_UP_V);
        location.set(localUp).multLocal(currentHeight).addLocal(rigidBody_location);
        //values chosen after testing
        //rayVector.set(localUp).multLocal(-getCurrentHeight() - FastMath.ZERO_TOLERANCE).addLocal(location);
        rayVector.set(localUp).multLocal(-1.1f * currentHeight - FastMath.ZERO_TOLERANCE);
        rayVector.addLocal(location);

        List<PhysicsRayTestResult> results = physicsSpace.rayTest(location, rayVector);
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
    protected boolean checkCanUnDuck(EntityId id) {
        TempVars vars = TempVars.get();
        int height = get(id, HEIGHT_F);
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f rigidBody_location = get(id, RIGID_BODY_LOCATION_V);
        Vector3f location = vars.vect2;
        Vector3f rayVector = vars.vect3;
        Vector3f localUp = get(id, LOCAL_UP_V);
        location.set(localUp).multLocal(FastMath.ZERO_TOLERANCE).addLocal(rigidBody_location);
        rayVector.set(localUp).multLocal(height + FastMath.ZERO_TOLERANCE).addLocal(location);
        List<PhysicsRayTestResult> results = physicsSpace.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
//////INPUT

    @Override
    public void onAction(Entity entity, String name, boolean isPressed, float tpf) {
        EntityId id = entity.getId();
        if (isPressed && getActionMode(id)) {
            if (name.equals(ControlInputs.Action1.toString())) {// Jump                
                jump(id);
            } else if (name.equals(ControlInputs.Action2.toString())) {// evade/Slide 
                evade(id);
            } else if (name.equals(ControlInputs.Action3.toString())) {// dash
                dash(id);
            }
        }
    }

    @Override
    public void onAnalog(Entity entity, String name, float value, float tpf) {
        EntityId id = entity.getId();
        if (name.equals(ControlInputs.CharacterForward.toString())) {//Move Character Up  
            moveForward(id, value / tpf);
        } else if (name.equals(ControlInputs.CharacterBack.toString())) {//Move Character Back   
            moveForward(id, -(value / tpf));
        } else if (name.equals(ControlInputs.CharacterLeft.toString())) {//Move Character  Right           
            moveLeft(id, value / tpf);
        } else if (name.equals(ControlInputs.CharacterRight.toString())) {//Move Character Left
            moveLeft(id, -(value / tpf));
        }
    }
}
