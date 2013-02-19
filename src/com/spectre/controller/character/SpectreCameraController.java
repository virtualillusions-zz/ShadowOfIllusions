 /*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.spectre.controller.character;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.input.FlyByCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.controller.character.impl.AbstractSpectreController;
import com.spectre.director.Director;
import java.util.LinkedList;

/**
 * This Camera is a spatial Controller used to give players view into the world.
 * It pays special attention to the world and reacts to collision accordingly as
 * such it is a modified version of the ChaseCamera for a Physics enabled
 * environment. Attached To Spatial
 * via{@link com.spectre.controller.character.SpectrePlayerController#setModel(String)}
 *
 * @author Kyle Williams [MODIFIED FROM com.jme3.input.ChaseCamera by
 * @author nehon ]
 */
public class SpectreCameraController extends AbstractSpectreController {

    /**
     * A Character Control Used to handle Character Camera
     */
    public SpectreCameraController() {
    }

    /**
     * Sets the spacial for the camera control, should only be used internally
     *
     * @param spatial
     */
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        //computePosition();
        prevPos = new Vector3f(spatial.getWorldTranslation());
        //cam.setLocation(pos);
        //
        setMaxVerticalRotation(FastMath.PI / 3.5f);
        setMinVerticalRotation(-FastMath.PI / 4f);
        inputCenterCamera();
        setMinDistance(6.0f);
        setMaxDistance(12.0f);
        setDefaultDistance(getMaxDistance());//Forces the Camera to a specific distance
        //computePosition();
    }

    @Override
    public void startUp() {
        if (cam == null) {
            setCamera(Director.getApp().addCamera());
        }
    }

    @Override
    public void cleanUp() {
        cam = null;
    }

    /**
     * Determines camera Location and Rotation
     */
    private void computePosition() {
        zoomCamera(getMaxDistance());//Forces the Camera to a specific distance

        if (lockOn == true) {
            float[] angles = new float[3];
            Quaternion camRotation = getCamera().getRotation();
            camRotation.toAngles(angles);
            float t = FastMath.HALF_PI - (angles[1] + FastMath.PI);
            setDefaultHorizontalRotation(t);
            float min = getMinVerticalRotation();
            float max = getMaxVerticalRotation();
            t = FastMath.clamp(angles[0], min, max);
            setDefaultVerticalRotation(t);
        }

        float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - vRotation);
        pos.set(hDistance * FastMath.cos(rotation), (distance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        pos.addLocal(spatial.getWorldTranslation());

        collide();
    }

    /**
     * Collisions calculations to prevent character from being obscured
     */
    private void collide() {
        //compute position
        float hDistance = (targetDistance) * FastMath.sin((FastMath.PI / 2) - vRotation);
        maxPos = new Vector3f(hDistance * FastMath.cos(rotation), (targetDistance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        maxPos = maxPos.add(spatial.getWorldTranslation());
        //collide
        if (pSpace == null) {
            pSpace = Director.getPhysicsSpace();
            zoomCamera(getMaxDistance());
        }
        //Makes sure physicsSpace is set and when it is set and zooms the camera out
        @SuppressWarnings("unchecked")
        LinkedList<PhysicsRayTestResult> testResults = (LinkedList) pSpace.rayTest(spatial.getWorldTranslation(), maxPos);
        float hf = 1f;//hitFraction
        if (testResults != null && testResults.size() > 0) {
            hf = testResults.getFirst().getHitFraction();
        }
        targetDistance = hf * targetDistance;//((float)((int)(hf*100)))/100 * targetDistance;
    }

    /**
     * rotate the camera around the target on the horizontal plane
     *
     * @param value DO NOT AUGMENT VALUE WITH TPF
     */
    public void inputHRotateCamera(float value) {
        if (lockOn == true) {
            return;
        }
        value = sensitivity * value;
        if (!canRotate || !enabled) {
            return;
        }
        rotating = true;
        targetRotation += value * rotationSpeed;
    }

    /**
     * rotate the camera around the target on the vertical plane
     *
     * @param value DO NOT AUGMENT VALUE WITH TPF
     */
    public void inputVRotateCamera(float value) {
        if (lockOn == true) {
            return;
        }
        value = sensitivity * value;
        if (!canRotate || !enabled) {
            return;
        }
        vRotating = true;
        float lastGoodRot = targetVRotation;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = lastGoodRot;
        }
        if (veryCloseRotation) {
            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
                targetVRotation = minVerticalRotation;
            } else if (targetVRotation < -FastMath.DEG_TO_RAD * 90) {
                targetVRotation = lastGoodRot;
            }
        } else {
            if ((targetVRotation < minVerticalRotation)) {
                targetVRotation = lastGoodRot;
            }
        }
    }

    /**
     * @deprecated This method will not work due to proper collision
     * calculations. Regardless Method is un-needed
     * @param value
     * @param zoomIn
     */
    public void inputZoomCamera(float value, boolean zoomIn) {
        if (lockOn == true) {
            return;
        }
        value = zoomIn == true ? -value : +value;//zoom in means value should be decremental
        zoomCamera(value);
        if (zoomin == true) {
            distanceLerpFactor = 0;
        }
        this.zoomin = zoomIn;
    }

    /**
     * Centers the Camera squarely behind target spatial
     */
    public void inputCenterCamera() {
        lockOn = false;
        float[] angles = new float[3];
        spatial.getWorldRotation().toAngles(angles);
        float t = FastMath.HALF_PI - (angles[1] + FastMath.PI);
        setDefaultHorizontalRotation(t);//places camera directly behind character
        t = FastMath.QUARTER_PI / 4;
        setDefaultVerticalRotation(t);//random vertical position of camera values are irrelevant
    }

    /**
     * Locks onto closest Target in view of camera or if none closest target to
     * player
     */
    public void inputToggleLockOnTarget() {
        if (lockOn == true) {
            lockOn = false;
            lockOnTarget = null;
        } else {
            getLookAtTarget(true);
            if (lockOnTarget != null) {
                lockOn = true;
            }
        }
    }

    /**
     * Gets Next or previous Target
     *
     * @param next
     */
    public void inputToggleLockOnTarget(boolean next) {
        if (lockOnTarget == null) {
            getLookAtTarget(false);
        } else {
            int i = getRootNode().getChildIndex(lockOnTarget);
            i += next == true ? 1 : -1;
            i = modulo(i, getRootNode().getChildren().size());
            if (getRootNode().getChild(i).equals(spatial)) {
                i += next == true ? 1 : -1;
                i = modulo(i, getRootNode().getChildren().size());
            }
            lockOnTarget = getRootNode().getChild(i);
        }
        if (lockOnTarget != null) {
            lockOn = true;
        }

    }

    /**
     * Locks onto a target either by field of view or distance from target
     *
     * @param targetInView
     */
    private void getLookAtTarget(boolean targetInView) {
        float lookAtDistance = -1;
        for (Spatial spat : getRootNode().getChildren()) {
            if (!spat.equals(spatial)) {
                float d = spatial.getLocalTranslation().distance(spat.getLocalTranslation());
                int ordinal = getCamera().contains(spat.getWorldBound()).ordinal();
                if (targetInView == false || ordinal != FrustumIntersect.Outside.ordinal()) {
                    if (lookAtDistance == -1 || d < lookAtDistance) {
                        lockOnTarget = spat;
                        lookAtDistance = d;
                    }
                }
            }
        }
    }

    /**
     * Returns Node which contains all playable characters
     *
     * @return
     */
    private Node getRootNode() {
        if (rootNode == null) {
            rootNode = spatial.getParent();
        }
        return rootNode;
    }

    /**
     * Modulo Fix for negative numbers
     *
     * @param dividend the number that is being divided by. Appears on Left
     * @param divisor the number the dividend is being divided by. Appears on
     * Right
     * @return
     */
    private int modulo(int dividend, int divisor) {
        return (dividend % divisor + divisor) % divisor;
    }

    /**
     * sets the current zoom distance for the chase camera
     *
     * @param new distance
     */
    protected void zoomCamera(float value) {
        if (!enabled) {
            return;
        }

        zooming = true;
        targetDistance += value * zoomSpeed;
        if (targetDistance > maxDistance) {
            targetDistance = maxDistance;
        }
        if (targetDistance < minDistance) {
            targetDistance = minDistance;
        }
        if (veryCloseRotation) {
            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
                targetVRotation = minVerticalRotation;
            }
        }
    }

    /**
     * update the camera control
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        if (enabled) {

            targetLocation.set(spatial.getWorldTranslation()).addLocal(lookAtOffset);

            if (smoothMotion) {

                //computation of target direction
                targetDir.set(targetLocation).subtractLocal(prevPos);
                float dist = targetDir.length();

                //Low pass filtering on the target postition to avoid shaking when physics are enabled.
                if (offsetDistance < dist) {
                    //target moves, start chasing.
                    chasing = true;
                    //target moves, start trailing if it has to.
                    if (trailingEnabled) {
                        trailing = true;
                    }
                    //target moves...
                    targetMoves = true;
                } else {
                    //if target was moving, we compute a slight offset in rotation to avoid a rought stop of the cam
                    //We do not if the player is rotationg the cam
                    if (targetMoves && !canRotate) {
                        if (targetRotation - rotation > trailingRotationInertia) {
                            targetRotation = rotation + trailingRotationInertia;
                        } else if (targetRotation - rotation < -trailingRotationInertia) {
                            targetRotation = rotation - trailingRotationInertia;
                        }
                    }
                    //Target stops
                    targetMoves = false;
                }

                //the user is rotating the cam by dragging the mouse
                if (canRotate) {
                    //reseting the trailing lerp factor
                    trailingLerpFactor = 0;
                    //stop trailing user has the control
                    trailing = false;
                }


                if (trailingEnabled && trailing) {
                    if (targetMoves) {
                        //computation if the inverted direction of the target
                        Vector3f a = targetDir.negate().normalizeLocal();
                        //the x unit vector
                        Vector3f b = Vector3f.UNIT_X;
                        //2d is good enough
                        a.y = 0;
                        //computation of the rotation angle between the x axis and the trail
                        if (targetDir.z > 0) {
                            targetRotation = FastMath.TWO_PI - FastMath.acos(a.dot(b));
                        } else {
                            targetRotation = FastMath.acos(a.dot(b));
                        }
                        if (targetRotation - rotation > FastMath.PI || targetRotation - rotation < -FastMath.PI) {
                            targetRotation -= FastMath.TWO_PI;
                        }

                        //if there is an important change in the direction while trailing reset of the lerp factor to avoid jumpy movements
                        if (targetRotation != previousTargetRotation && FastMath.abs(targetRotation - previousTargetRotation) > FastMath.PI / 8) {
                            trailingLerpFactor = 0;
                        }
                        previousTargetRotation = targetRotation;
                    }
                    //computing lerp factor
                    trailingLerpFactor = Math.min(trailingLerpFactor + tpf * tpf * trailingSensitivity, 1);
                    //computing rotation by linear interpolation
                    rotation = FastMath.interpolateLinear(trailingLerpFactor, rotation, targetRotation);

                    //if the rotation is near the target rotation we're good, that's over
                    if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
                        trailing = false;
                        trailingLerpFactor = 0;
                    }
                }

                //linear interpolation of the distance while chasing
                if (chasing) {
                    distance = temp.set(targetLocation).subtractLocal(cam.getLocation()).length();
                    distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * chasingSensitivity * 0.05f), 1);
                    distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                    if (targetDistance + 0.01f >= distance && targetDistance - 0.01f <= distance) {
                        distanceLerpFactor = 0;
                        chasing = false;
                    }
                }

                //linear interpolation of the distance while zooming
                if (zooming) {
                    distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * zoomSensitivity), 1);
                    distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                    if (targetDistance + 0.1f >= distance && targetDistance - 0.1f <= distance) {
                        zooming = false;
                        distanceLerpFactor = 0;
                    }
                }

                //linear interpolation of the rotation while rotating horizontally
                if (rotating) {
                    rotationLerpFactor = Math.min(rotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                    rotation = FastMath.interpolateLinear(rotationLerpFactor, rotation, targetRotation);
                    if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
                        rotating = false;
                        rotationLerpFactor = 0;
                    }
                }

                //linear interpolation of the rotation while rotating vertically
                if (vRotating) {
                    vRotationLerpFactor = Math.min(vRotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                    vRotation = FastMath.interpolateLinear(vRotationLerpFactor, vRotation, targetVRotation);
                    if (targetVRotation + 0.01f >= vRotation && targetVRotation - 0.01f <= vRotation) {
                        vRotating = false;
                        vRotationLerpFactor = 0;
                    }
                }
                //computing the position
                computePosition();
                //setting the position at last
                cam.setLocation(pos.addLocal(lookAtOffset));
            } else {
                //easy no smooth motion
                vRotation = targetVRotation;
                rotation = targetRotation;
                distance = targetDistance;
                computePosition();
                cam.setLocation(pos.addLocal(lookAtOffset));
            }


            Spatial s = lockOn == true ? lockOnTarget : spatial;
            targetLocation.set(s.getWorldTranslation()).addLocal(lookAtOffset);

            //keeping track on the previous position of the target
            prevPos.set(targetLocation);

            //the cam looks at the target
            cam.lookAt(targetLocation, initialUpVec);

        }
    }

    /**
     * Set Camera Sensitivity to input
     *
     * @param sensitivity
     */
    public void setCameraSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    /**
     * Enable or disable the camera
     *
     * @param enabled true to enable
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        canRotate = enabled;
    }

    /**
     * Returns the max zoom distance of the camera (default is 40)
     *
     * @return maxDistance
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets the max zoom distance of the camera (default is 40)
     *
     * @param maxDistance
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
        if (maxDistance < distance) {
            zoomCamera(maxDistance - distance);
        }
    }

    /**
     * Returns the min zoom distance of the camera (default is 1)
     *
     * @return minDistance
     */
    public float getMinDistance() {
        return minDistance;
    }

    /**
     * Sets the min zoom distance of the camera (default is 1)
     *
     * @return minDistance
     */
    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
        if (minDistance > distance) {
            zoomCamera(distance - minDistance);
        }
    }

    /**
     * clone this camera for a spatial
     *
     * @param spatial
     * @return
     */
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        SpectreCameraController cc = new SpectreCameraController();
        cc.setCamera(getCamera());
        cc.setMaxDistance(getMaxDistance());
        cc.setMinDistance(getMinDistance());
        return cc;
    }

    /**
     * @return The maximal vertical rotation angle in radian of the camera
     * around the target
     */
    public float getMaxVerticalRotation() {
        return maxVerticalRotation;
    }

    /**
     * Sets the maximal vertical rotation angle in radian of the camera around
     * the target. Default is Pi/2;
     *
     * @param maxVerticalRotation
     */
    public void setMaxVerticalRotation(float maxVerticalRotation) {
        this.maxVerticalRotation = maxVerticalRotation;
    }

    /**
     *
     * @return The minimal vertical rotation angle in radian of the camera
     * around the target
     */
    public float getMinVerticalRotation() {
        return minVerticalRotation;
    }

    /**
     * Sets the minimal vertical rotation angle in radian of the camera around
     * the target default is 0;
     *
     * @param minHeight
     */
    public void setMinVerticalRotation(float minHeight) {
        this.minVerticalRotation = minHeight;
    }

    /**
     * @return True is smooth motion is enabled for this chase camera
     */
    public boolean isSmoothMotion() {
        return smoothMotion;
    }

    /**
     * Enables smooth motion for this chase camera
     *
     * @param smoothMotion
     */
    public void setSmoothMotion(boolean smoothMotion) {
        this.smoothMotion = smoothMotion;
    }

    /**
     * returns the chasing sensitivity
     *
     * @return
     */
    public float getChasingSensitivity() {
        return chasingSensitivity;
    }

    /**
     *
     * Sets the chasing sensitivity, the lower the value the slower the camera
     * will follow the target when it moves default is 5 Only has an effect if
     * smoothMotion is set to true and trailing is enabled
     *
     * @param chasingSensitivity
     */
    public void setChasingSensitivity(float chasingSensitivity) {
        this.chasingSensitivity = chasingSensitivity;
    }

    /**
     * Returns the rotation sensitivity
     *
     * @return
     */
    public float getRotationSensitivity() {
        return rotationSensitivity;
    }

    /**
     * Sets the rotation sensitivity, the lower the value the slower the camera
     * will rotates around the target when draging with the mouse default is 5,
     * values over 5 should have no effect. If you want a significant slow down
     * try values below 1. Only has an effect if smoothMotion is set to true
     *
     * @param rotationSensitivity
     */
    public void setRotationSensitivity(float rotationSensitivity) {
        this.rotationSensitivity = rotationSensitivity;
    }

    /**
     * returns true if the trailing is enabled
     *
     * @return
     */
    public boolean isTrailingEnabled() {
        return trailingEnabled;
    }

    /**
     * Enable the camera trailing : The camera smoothly go in the targets trail
     * when it moves. Only has an effect if smoothMotion is set to true
     *
     * @param trailingEnabled
     */
    public void setTrailingEnabled(boolean trailingEnabled) {
        this.trailingEnabled = trailingEnabled;
    }

    /**
     *
     * returns the trailing rotation inertia
     *
     * @return
     */
    public float getTrailingRotationInertia() {
        return trailingRotationInertia;
    }

    /**
     * Sets the trailing rotation inertia : default is 0.1. This prevent the
     * camera to roughtly stop when the target stops moving before the camera
     * reached the trail position. Only has an effect if smoothMotion is set to
     * true and trailing is enabled
     *
     * @param trailingRotationInertia
     */
    public void setTrailingRotationInertia(float trailingRotationInertia) {
        this.trailingRotationInertia = trailingRotationInertia;
    }

    /**
     * returns the trailing sensitivity
     *
     * @return
     */
    public float getTrailingSensitivity() {
        return trailingSensitivity;
    }

    /**
     * Only has an effect if smoothMotion is set to true and trailing is enabled
     * Sets the trailing sensitivity, the lower the value, the slower the camera
     * will go in the target trail when it moves. default is 0.5;
     *
     * @param trailingSensitivity
     */
    public void setTrailingSensitivity(float trailingSensitivity) {
        this.trailingSensitivity = trailingSensitivity;
    }

    /**
     * returns the zoom sensitivity
     *
     * @return
     */
    public float getZoomSensitivity() {
        return zoomSensitivity;
    }

    /**
     * Sets the zoom sensitivity, the lower the value, the slower the camera
     * will zoom in and out. default is 5.
     *
     * @param zoomSensitivity
     */
    public void setZoomSensitivity(float zoomSensitivity) {
        this.zoomSensitivity = zoomSensitivity;
    }

    /**
     * Sets the default distance at start of applicaiton
     *
     * @param defaultDistance
     */
    public void setDefaultDistance(float defaultDistance) {
        distance = defaultDistance;
        targetDistance = distance;
    }

    /**
     * sets the default horizontal rotation in radian of the camera at start of
     * the application
     *
     * @param angleInRad
     */
    public void setDefaultHorizontalRotation(float angleInRad) {
        rotation = angleInRad;
        targetRotation = angleInRad;
    }

    /**
     * sets the default vertical rotation in radian of the camera at start of
     * the application
     *
     * @param angleInRad
     */
    public void setDefaultVerticalRotation(float angleInRad) {
        vRotation = angleInRad;
        targetVRotation = angleInRad;
    }

    /**
     * @return If drag to rotate feature is enabled.
     *
     * @see FlyByCamera#setDragToRotate(boolean)
     */
    public boolean isDragToRotate() {
        return dragToRotate;
    }

    /**
     * @param rotateOnlyWhenClose When this flag is set to false the chase
     * camera will always rotate around its spatial independently of their
     * distance to one another. If set to true, the chase camera will only be
     * allowed to rotated below the "horizon" when the distance is smaller than
     * minDistance + 1.0f (when fully zoomed-in).
     */
    public void setDownRotateOnCloseViewOnly(boolean rotateOnlyWhenClose) {
        veryCloseRotation = rotateOnlyWhenClose;
    }

    /**
     * @return True if rotation below the vertical plane of the spatial tied to
     * the camera is allowed only when zoomed in at minDistance + 1.0f. False if
     * vertical rotation is always allowed.
     */
    public boolean getDownRotateOnCloseViewOnly() {
        return veryCloseRotation;
    }

    /**
     * return the current distance from the camera to the target
     *
     * @return
     */
    public float getDistanceToTarget() {
        return distance;
    }

    /**
     * returns the current horizontal rotation around the target in radians
     *
     * @return
     */
    public float getHorizontalRotation() {
        return rotation;
    }

    /**
     * returns the current vertical rotation around the target in radians.
     *
     * @return
     */
    public float getVerticalRotation() {
        return vRotation;
    }

    /**
     * returns the offset from the target's position where the camera looks at
     *
     * @return
     */
    public Vector3f getLookAtOffset() {
        return lookAtOffset;
    }

    /**
     * Sets the offset from the target's position where the camera looks at
     *
     * @param lookAtOffset
     */
    public void setLookAtOffset(Vector3f lookAtOffset) {
        this.lookAtOffset = lookAtOffset;
    }

    /**
     * Sets the up vector of the camera used for the lookAt on the target
     *
     * @param up
     */
    public void setUpVector(Vector3f up) {
        initialUpVec = up;
    }

    /**
     * Returns the up vector of the camera used for the lookAt on the target
     *
     * @return
     */
    public Vector3f getUpVector() {
        return initialUpVec;
    }

    @Override
    public Camera getCamera() {
        return cam;
    }

    public void setCamera(Camera camera) {
        cam = camera;
        initialUpVec = cam.getUp().clone();
    }
    private float minVerticalRotation = 0.00f;
    private float maxVerticalRotation = FastMath.PI / 2;
    private float minDistance = 1.0f;
    private float maxDistance = 40.0f;
    private float distance = 20;
    private float zoomSpeed = 2f;
    private float rotationSpeed = 1.0f;
    private float rotation = 0;
    private float trailingRotationInertia = 0.05f;
    private float zoomSensitivity = 5f;
    private float rotationSensitivity = 5f;
    private float chasingSensitivity = 5f;
    private float trailingSensitivity = 0.5f;
    private float vRotation = FastMath.PI / 6;
    private boolean smoothMotion = true;
    private boolean trailingEnabled = true;
    private float rotationLerpFactor = 0;
    private float trailingLerpFactor = 0;
    private boolean rotating = false;
    private boolean vRotating = false;
    private float targetRotation = rotation;
    private Vector3f initialUpVec;
    private float targetVRotation = vRotation;
    private float vRotationLerpFactor = 0;
    private float targetDistance = distance;
    private float distanceLerpFactor = 0;
    private boolean zooming = false;
    private boolean trailing = false;
    private boolean chasing = false;
    private boolean veryCloseRotation = true;
    private boolean canRotate = true;
    private float offsetDistance = 0.002f;
    private Vector3f prevPos;
    private boolean targetMoves = false;
    private final Vector3f targetDir = new Vector3f();
    private float previousTargetRotation;
    private final Vector3f pos = new Vector3f();
    private Vector3f targetLocation = new Vector3f(0, 0, 0);
    private boolean dragToRotate = false;
    private Vector3f lookAtOffset = new Vector3f(0, 0, 0);
    private Vector3f temp = new Vector3f(0, 0, 0);
    private boolean zoomin;
    private PhysicsSpace pSpace = null;
    private Vector3f maxPos = new Vector3f();
    private float sensitivity = 2.5f;
    private Node rootNode;
    private Spatial lockOnTarget;
    private boolean lockOn;
}
