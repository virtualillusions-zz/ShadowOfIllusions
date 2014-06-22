/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.util.TempVars;

/**
 *
 * @author Kyle Williams
 */
public class spcTempArchive {

    private void activePhysicsTick(TempVars vars) {
        //because of some slight issues with view direction use this boolean to 
        //determine if it should be set or not
        boolean setViewDirection = false;
        //Used later on to create smooth transitions speeding up or slowing down
        float interpolateScalar = 0.01f;
        Vector3f tempWlkDir = vars.vect1.zero();
//LANDED
        if (onGround && isInAir()) {
            interpolateScalar = 1f;//by doing this will be set to 0 to simulate since just fell
//IN AIR
        } else if (isInAir()) {//is character jumping or falling
            tempWlkDir.set(walkDirection);
//DASH OR EVADE
        } else if (!auxillary.equals(Vector3f.ZERO)) {
            //check if character dash or roll
            auxillary.interpolateLocal(Vector3f.ZERO, 0.1f);

            //put after set walk direction so don't interrput viewDirection
            if (FastMath.abs(auxillary.getX()) < 0.1f && FastMath.abs(auxillary.getZ()) < 0.1f) {
                auxillary.zero();
                setDucked(false);
            }
            tempWlkDir.set(auxillary);
            interpolateScalar = 1f;

//WALKING
        } else if (directionForward != 0 || directionLeft != 0) {

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
            setViewDirection = true;
        }
        //This is added to allow scalar to remain at 0.01f to help with turning
        //and set here to prevent prolonged stop
        if (tempWlkDir.equals(Vector3f.ZERO)) {
            interpolateScalar = 0.1f;
        }
        //Interpolate current walk direction to new one to prevent sparatic movement 
        walkDirection.interpolateLocal(tempWlkDir, interpolateScalar);

        tempWlkDir.setY(0);
        if (setViewDirection == true) {
            setViewDirection(walkDirection);
            viewDirection.normalizeLocal();
        }
        //Changes Characters view and walk direction
        //walkDirection.set(tempWlkDir);
        //In Order to prevent some hiccups after walkDirection should be zero. Zero out this vector if it reaches below a threshold
        if (FastMath.abs(walkDirection.getX()) < .001f && FastMath.abs(walkDirection.getZ()) < .001f) {
            walkDirection.zero();
        }

    }

    private void passivePhysicsTick(TempVars vars) {
        // dampen existing x/z forces
        float existingLeftVelocity = velocity.dot(localLeft);
        float existingForwardVelocity = velocity.dot(localForward);
        Vector3f counter = vars.vect1;
        existingLeftVelocity = existingLeftVelocity * physicsDamping;
        existingForwardVelocity = existingForwardVelocity * physicsDamping;
        counter.set(-existingLeftVelocity, 0, -existingForwardVelocity);
        localForwardRotation.multLocal(counter);
        velocity.addLocal(counter);

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
            //add resulting vector to existing velocity
            velocity.addLocal(localWalkDirection);
        }
        rigidBody.setLinearVelocity(velocity);
//CHECK JUMP
        if (jump) {
            //TODO: precalculate jump force
            Vector3f rotatedJumpForce = vars.vect1;
            rotatedJumpForce.set(0, mass * 6.5f, 0);
            rigidBody.applyImpulse(localForwardRotation.multLocal(rotatedJumpForce), Vector3f.ZERO);
        } else if (auxillary.equals(Vector3f.ZERO)) {
//CHECK DASH
            if (dash) {
                //TODO: precalculate Dash force
                Vector3f dir = vars.vect2;
                dir.set(viewDirection);
                auxillary.addLocal(localForwardRotation.multLocal(dir));
                auxillary.multLocal(4 * BASIC_MAX_SPEED);
                auxillary.setY(0);
//CHECK EVADE            
            } else if (evade) {
                //TODO: precalculate evade force
                //must move both back and to a side
                if (directionLeft == 0) {
                    directionLeft = FastMath.nextRandomInt(-1, 1);
                }
                Vector3f dir = vars.vect2;
                //Rotate 90 degrees of current rotation
                dir.set(rotation.mult(localLeft));
                if (directionLeft < 0) {
                    dir.negateLocal();
                }
                auxillary.addLocal(localForwardRotation.multLocal(dir));

                //evasion never moves forward
                //Vector3f dir = vars.vect2;
                dir.set(viewDirection);
                dir.negateLocal();
                auxillary.addLocal(localForwardRotation.multLocal(dir));
                //FINALALY MULTIPLY BY THE ACTUAL MOVING FACTOR
                auxillary.multLocal(BASIC_MAX_SPEED);
                auxillary.setY(0);
                setDucked(true);
            }
        }
        //this prevents prolonged execution
        jump = false;
        dash = false;
        evade = false;
        directionForward = 0;
        directionLeft = 0;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Nothing">   
    /**
     * Determines if the control is in an action state or caster state
     */
    public boolean actionMode = false;
    private static final float gravity = 0f;//Director.gravity;
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
    //private AnimationControl animCont;
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
    private CollisionShape[] shapes;

    private void setViewDirection(Vector3f walkDirection) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setDucked(boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void walkDirection(int directionForward, Vector3f direction, Vector3f vect2, Vector3f tempWlkDir) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isInAir() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    // </editor-fold> 
}
