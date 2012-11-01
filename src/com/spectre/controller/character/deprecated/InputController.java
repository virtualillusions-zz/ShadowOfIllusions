/////*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.spectre.controller.character;
//
//import com.jme3.input.controls.ActionListener;
//import com.jme3.input.controls.AnalogListener;
//import com.jme3.input.controls.Trigger;
//import com.jme3.math.FastMath;
// 
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.control.Control;
//
//import com.spectre.util.Buttons;
//import com.spectre.util.Attribute.SpecialMovement;
//
//import java.util.ArrayList;
//
//import java.util.EnumMap;
//
///** 
// * Controls All Input related Activity of the attachedCharacter as well as basic movement
// * This Class is for Human Player Input Controls 
// * Remember to call the public command set to setUp the player and input Controls 
// * Note: This Controller is attached in PlayerController
// * TODO: find the direction of collision and negate it
// * TODO: Fix character speed in relation to joystick sensitivty and keyboar press
// * @see com.spectre.controller.PlayerController
// * @author Kyle Williams
// */
//public class InputController extends com.jme3.scene.control.AbstractControl{
//    private String playerName;
//    //temp vectors
//    private Vector3f walkDirection = new Vector3f();
//    //DirectionPressed
//    private boolean left = false, right = false, up = false, down = false;
//    //ValuesPressed
//    private float directionalValue=0;
//    private float maxPressure=0;
//    //Checks to See if the model isMoving
//    private boolean isMoving=false;
//    //Checks if the controller is active or not
//    private boolean active = true;
//    //animation
//    private float airTime = 0;
//    //Camera
//    private Camera cam;
//    private SpecialMovement type;
//    //Dash or dive
//    private boolean usingDiv = false;
//    private final Vector3f special = new Vector3f();
//    private CharacterAnimationController animctrl;
//
//
//    public InputController(SpecialMovement specialType){
//        type=specialType;
//        setEnabled(false);/*controller must be disabled to allow camera to be first initialized before running*/        
//    }    
//    //A list of the players input buttons
//    private EnumMap<com.spectre.util.Buttons,Trigger[]> inputControls;
//    //the physics entity of the controlled model
//    private SpectrePhysicsController pSpatial;
//
//    /**
//     * Registers all buttons to input manager
//     * @param inputControls
//     */
//    public void set(String name, EnumMap<Buttons,Trigger[]> inputcontrols, Camera camera){
//        inputControls = inputcontrols;        
//        playerName=name;        
//        cam = camera;                
//        setEnabled(true);
//        set();
//        animctrl = spatial.getControl(CharacterAnimationController.class);
//    }
//
//    /**
//     * Registers the players keys to the inputManager and attaches a listener
//     */
//    private void set(){
//       com.jme3.input.InputManager inputManager =  com.spectre.director.Director.getApp().getInputManager();
//       ArrayList<String> keys = new ArrayList<String>(inputControls.size());
//
//       for(Buttons button:inputControls.keySet()){
//            String c = playerName+button;
//            inputManager.addMapping(c, inputControls.get(button));
//            keys.add(c);
//       }
//
//       inputManager.addListener(actionListener, keys.toArray(new String[keys.size()-1]));
//       inputManager.addListener(analogListener,playerName+Buttons.FORWARD,
//                                               playerName+Buttons.BACKWARD,
//                                               playerName+Buttons.LEFT,
//                                               playerName+Buttons.RIGHT);       
//    }
//
//    /**
//     * The main ActionListener
//     */
//    public ActionListener actionListener = new ActionListener(){
//        @Override
//        public void onAction(String name, boolean isPressed, float tpf) {
//            if(active){
//                if (name.equals(playerName+Buttons.LEFT)) {
//                    left = isPressed;
//                } else if (name.equals(playerName+Buttons.RIGHT)) {
//                    right = isPressed;
//                } else if (name.equals(playerName+Buttons.FORWARD)) {
//                    up = isPressed;
//                } else if (name.equals(playerName+Buttons.BACKWARD)) {
//                    down = isPressed;
//                }else if (name.equals(playerName+Buttons.SPECIALMANEUVER)&&isPressed) {//TODO:FIX CENTERCAMERA
//                    if(type==SpecialMovement.jump){
//                        pSpatial.jump();
//                    }else if(type==SpecialMovement.dash){ 
//                        //TODO:CODE FOR DASH REGULAR ANIM
//                        //System.out.println("DIVING");
//                        float x = FastMath.sign(pSpatial.getViewDirection().x)*2;
//                        float z = FastMath.sign(pSpatial.getViewDirection().z)*2;
//                        special.set(x, 0, z);
//                        animctrl.setAnim(false, "dash");
//                        usingDiv=false;
//                        //walkDirection.add(temp);
//                    }else if(type==SpecialMovement.dive){
//                        //TODO:CODE FOR DIVE INTERUPTTING MOVE
//                        animctrl.setAnim(true, "dive");
//                        usingDiv=true;
//                    }
//                }
//            }
//        }
//    };
//    /**
//     * The main AnalogListener
//     * All this does is get the amount the joypad has been pressed
//     */
//    public AnalogListener analogListener = new AnalogListener(){
//        @Override
//        public void onAnalog(String name, float value, float tpf) {
//            if(directionalValue<value)directionalValue=value;
//        }        
//    };
//
//
//    @Override
//    protected void controlUpdate(float tpf) {
//        
//        if(!special.equals(Vector3f.ZERO)&&!usingDiv){//interrupts are more important than basic walking
//           special.interpolate(Vector3f.ZERO, special,.1f); 
//           walkDirection.set(special);
//           if(FastMath.abs(special.getX())<.01f&&FastMath.abs(special.getZ())<.01f)special.zero();
//        }else if(directionalValue!=0){
//            final Vector3f camDir = cam.getDirection();
//            final Vector3f camLeft = cam.getLeft();
//            if (up)     walkDirection.addLocal(camDir);
//            if (down)   walkDirection.addLocal(camDir.negate());  
//            if (left)   walkDirection.addLocal(camLeft);
//            if (right)  walkDirection.addLocal(camLeft.negate());
//            if(usingDiv)walkDirection.multLocal(2f);  
//
//            maxPressure=directionalValue/2>maxPressure?directionalValue/2:maxPressure;
//            float percent=directionalValue/maxPressure<.50f?.35f:1f; //percent can't be less than 2  
//            //System.out.println(percent);
//            //if(percent>.69f)percent=1f;//To solve an issue
//            //System.out.println(percent);
//            walkDirection.normalizeLocal().multLocal(percent/4f);
//            //System.out.println("walk Direction"+walkDirection);
//        }            
//            
//        airTime = pSpatial.onGround()? 0 : airTime+tpf;
//        isMoving = (walkDirection.length()==0)? false : true;
//        
//        //prevents the model from moving while in air
//        if(airTime<=.3f)pSpatial.setWalkDirection(walkDirection.setY(.25f));//setting Y to .25 helps prevent falling through mesh
//        if(isMoving)pSpatial.setViewDirection(pSpatial.getWalkDirection().clone().setY(0));
//        //reset determinants
//        directionalValue=0f;
//        walkDirection.zero();        
//    }
//
//    /**
//     * Returns the amount of time in Air
//     * Largely used for animation
//     * @return
//     */
//    public float getAirTime(){return airTime;}
//
//    /**
//     * Checks to see if this character isMoving
//     */
//    public boolean getIsMoving(){return isMoving;}
//    
//    @Override
//    public void setSpatial(Spatial spatial) {
//        super.setSpatial(spatial);
//        pSpatial = spatial.getControl(SpectrePhysicsController.class);
//    }
//
//    /**
//     * removes the currently controlled spatial
//     */
//    public void removeSpatial(){
//        this.spatial=null;
//        pSpatial=null;
//    }
//
//    /*
//     * Should the controller be active or inactive
//     */
//    public void setControllerActive(boolean active){this.active=active;}
//
//    @Override
//    protected void controlRender(RenderManager rm, ViewPort vp) { }
//
//    public Control cloneForSpatial(Spatial spatial) {return null;}           
//}
