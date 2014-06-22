/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.input.InputManager;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.spectre.director.Director;
import java.util.LinkedList;

/**
 *
 * @author Kyle Williams
 */
public class SpectreCamera extends ChaseCamera{
     private PhysicsSpace pSpace = null;
     private Vector3f maxPos = new Vector3f();
     private float sensitivity = 2.5f;
     
     /**
     * Constructs the chase camera
     * @param cam the application camera
     * @param target the spatial to follow
     */
    public SpectreCamera(Camera cam, final Spatial target) {
        super(cam,target);        
        setUp();
        setDragToRotate(false);
    }
    
    private void setUp(){        
        inputManager = Director.getApp().getInputManager();        
        setMaxVerticalRotation(FastMath.PI /3.5f);
        setMinVerticalRotation(-FastMath.PI /4f);
        setMaxDistance(6.0f);        
        setMinDistance(6.0f);
        computePosition();
        registerInput(inputManager);
        registerWithInput(inputManager);
    }
   
     private void collide() {
        //compute position
        float hDistance = (targetDistance) * FastMath.sin((FastMath.PI / 2) - vRotation);
        maxPos = new Vector3f(hDistance * FastMath.cos(rotation), (targetDistance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation));
        maxPos = maxPos.add(target.getWorldTranslation());
        //collide
        if(pSpace==null){pSpace = Director.getPhysicsSpace();zoomCamera(6f);}//Makes sure physicsSpace is set and when it is set and zooms the camera out
        LinkedList<PhysicsRayTestResult> testResults = (LinkedList) pSpace.rayTest(target.getWorldTranslation(), maxPos);
        float hf = 1f;//hitFraction
        if(testResults != null && testResults.size() > 0) {
            hf = testResults.getFirst().getHitFraction();
        } 
        //targetDistance = ((float)((int)(hitFraction*100)))/100 * targetDistance;
        targetDistance = hf * targetDistance;
     }
     
     /**
     * sets the current zoom distance for the chase camera
     * @param new distance
     */
    public void alterDistance(float alterBy) {
        this.zoomCamera(alterBy);
    }
         
    @Override
    protected void computePosition(){
        alterDistance(getMaxDistance());//Forces the Camera to a specific distance

        super.computePosition();   
        this.collide();
    }

    /**
     * @return cam the camera registered by this controller
     */
    public Camera getCamera(){return cam;}
        
    public final void registerInput(InputManager inputManager) {
        String[] inputs = {ChaseCamToggleRotate,
            ChaseCamDown,
            ChaseCamUp,
            ChaseCamMoveLeft,
            ChaseCamMoveRight,
            ChaseCamZoomIn,
            ChaseCamZoomOut};

        this.inputManager = inputManager;
        if (!invertYaxis) {
            inputManager.addMapping(ChaseCamDown, new JoyAxisTrigger(0, 2, false));
            inputManager.addMapping(ChaseCamUp, new JoyAxisTrigger(0, 2, true));
        } else {
            inputManager.addMapping(ChaseCamDown, new JoyAxisTrigger(0, 2, true));
            inputManager.addMapping(ChaseCamUp, new JoyAxisTrigger(0, 2, false));
        }
        //inputManager.addMapping(ChaseCamZoomIn, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        //inputManager.addMapping(ChaseCamZoomOut, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        if(!invertXaxis){
            inputManager.addMapping(ChaseCamMoveLeft, new JoyAxisTrigger(0, 3, true));
            inputManager.addMapping(ChaseCamMoveRight, new JoyAxisTrigger(0, 3, false));
        }else{
            inputManager.addMapping(ChaseCamMoveLeft, new JoyAxisTrigger(0, 3, false));
            inputManager.addMapping(ChaseCamMoveRight, new JoyAxisTrigger(0, 3, true));
        }
        //inputManager.addMapping(ChaseCamToggleRotate, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        //inputManager.addMapping(ChaseCamToggleRotate, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, inputs);
    }
    
    @Override
    protected void rotateCamera(float value) {super.rotateCamera(sensitivity*value);}  
    @Override
    protected void vRotateCamera(float value) {super.vRotateCamera(sensitivity*value);}
    public void setCameraSensitivity(float sensitivity){this.sensitivity=sensitivity;}
    
}
