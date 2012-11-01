///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.spectre.controller.character;
//
//import com.jme3.animation.AnimChannel;
//import com.jme3.animation.AnimControl;
//import com.jme3.animation.AnimEventListener;
//import com.jme3.animation.LoopMode;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.control.Control;
//
//import com.spectre.util.Attribute.SpecialMovement;
// 
///**
// * Controls all Animations for the attached Mesh
// * TODO: Animate and implement passive animations Buffers ie: turn head while running etc
// * Note: This controller is attached in spectreHandler
// * Note: This controllers inputController is set in InputController
// * Remember: need to limit AnimEventListener for this specific class so actions in animcycledone doesnt affect other operations
// * @see com.spectre.util.SAX.spectreHandler
// * @author Kyle Williams
// */
//public class CharacterAnimationController extends com.jme3.scene.control.AbstractControl implements AnimEventListener{
//    private final String idle;
//    private final String forward;
//    private final String fall;
//    private final String jump;
//    private final String dive;
//    private final String dash;
//    private String tempAnim;
//    private AnimControl control;
//    private AnimChannel mainChannel;
//    private float airTime = 0;
//    private float altitude = 0; //used to check if falling 
//    private SpecialMovement type;
//    //the physics entity of the controlled model
//    private SpectrePhysicsController pSpatial;
//    private boolean highPriority =false;
//    private boolean performingMove = false;
//    private LoopMode mode=LoopMode.DontLoop;
//
//    /**
//     * Creates an animation controller that handles all animations for the character
//     * @param basicMoveSet
//     * @param specialType 
//     */
//    public CharacterAnimationController(String[] basicMoveSet,SpecialMovement specialType){
//        idle = basicMoveSet[0];    forward = basicMoveSet[1];   fall = basicMoveSet[2];
//        jump = basicMoveSet[3];    dash = basicMoveSet[4];      dive = basicMoveSet[5];
//        type = specialType;        tempAnim="";
//    }
//    
//    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
//        performingMove=false;
//        highPriority=false;
//    }
//
//    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
//        //throw new UnsupportedOperationException("Not supported yet.");
//    }
//    
//    @Override
//    protected void controlUpdate(float tpf) {
//        airTime = pSpatial.onGround()? 0 : airTime+tpf;
//        if(highPriority){
//            if(!mainChannel.getAnimationName().equals(tempAnim)){
//                    performMove();
//            }
//        }else{
//            if(inAir()){            
//                 if(isFalling()&&!isJumping()&&!mainChannel.getAnimationName().equals(fall)){                 
//                    mainChannel.setAnim(fall,.3f);
//                    mainChannel.setLoopMode(LoopMode.Cycle);                
//                }
//                if(!isFalling()&&isJumping()&&!mainChannel.getAnimationName().equals(jump)){                             
//                    mainChannel.setAnim(jump,.3f);
//                    mainChannel.setLoopMode(LoopMode.DontLoop);                
//               }
//            }else if(!tempAnim.isEmpty()){//TODO: FIX AROUND THIS STATEMENT CALLS ARE BEING TRAPPED TO IT ALWAYS BEING TRUE FOR SOME REASON
//                if(!mainChannel.getAnimationName().equals(tempAnim)){
//                    performMove();
//                }
//            }else{  
//                if (!isMoving()){
//                    if(!mainChannel.getAnimationName().equals(idle)){
//                        mainChannel.setAnim(idle,.1f);
//                        mainChannel.setLoopMode(LoopMode.Loop);
//                    }
//                }else{
//                    if(!mainChannel.getAnimationName().equals(forward)){
//                        mainChannel.setAnim(forward,.1f);
//                        mainChannel.setLoopMode(LoopMode.Loop);
//                    }
//                }
//            }
//        }        
//        altitude = pSpatial.getPhysicsLocation().getY();
//    }
// 
//    /**
//     * Sets the animation for the character to perform
//     * @param isHighPriority
//     * @param animName 
//     */
//    public void setAnim(boolean isHighPriority,String animName){
//        highPriority = isHighPriority;  //for conditional update  
//        mode = LoopMode.DontLoop;
//             if(animName.equalsIgnoreCase("dash"))tempAnim=dash;
//        else if(animName.equalsIgnoreCase("dive"))tempAnim=dive;
//        else if(animName.equalsIgnoreCase("idle"))tempAnim=idle;
//        else if(animName.equalsIgnoreCase("jump"))tempAnim=jump;
//        else tempAnim = animName;
//    }
//    /**
//     * Sets the animation for the character to perform
//     * @param isHighPriority
//     * @param loopMode
//     * @param animName 
//     */
//    public void setAnim(boolean isHighPriority,LoopMode loopMode,String animName){        
//        setAnim(isHighPriority,animName);
//        mode = loopMode;
//    }
//
//    private void performMove(){ 
//       if(!tempAnim.equals("")&&!performingMove){
//            mainChannel.setAnim(tempAnim,.1f);
//            performingMove=true;
//            tempAnim="";
//            mainChannel.setLoopMode(mode);
//        }  
//    }
//    
//    @Override
//    public void setSpatial(Spatial spatial) {
//        super.setSpatial(spatial);
//        pSpatial = spatial.getControl(SpectrePhysicsController.class);
//        control=spatial.getControl(com.jme3.animation.AnimControl.class);
//        mainChannel = control.createChannel();
//        mainChannel.setAnim(idle,0);
//        control.addListener(this);    
//    }
//    
//    /**
//     * must subtract y because only time added is to prevent overlapping
//     */
//    private boolean isMoving(){return pSpatial.getWalkDirection().length()-pSpatial.getWalkDirection().getY()!=0;}
//    private boolean inAir(){return airTime>.03f;} 
//    private boolean isFalling(){return pSpatial.getPhysicsLocation().getY()<=altitude;}
//    private boolean isJumping(){return pSpatial.getPhysicsLocation().getY()>=altitude;}   
//    @Override
//    protected void controlRender(RenderManager rm, ViewPort vp) {
//        //throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public Control cloneForSpatial(Spatial spatial) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
