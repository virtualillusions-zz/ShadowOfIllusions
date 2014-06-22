/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.controller.scene;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.Light;
import com.jme3.math.FastMath;
import com.jme3.math.Transform;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import jme3tools.optimize.GeometryBatchFactory;


import com.spectre.director.Director;

import java.util.ArrayList;


//TODO:ADD XML MAX PLAYER CAPACITY TO GIVE TO GAMESTATE CLASS
//TODO: Add XML control Filters for each level
public class SceneController extends com.jme3.scene.control.AbstractControl{
    //The physical representation of this player's controlled model
    private RigidBodyControl pSpatial;
    //A list of possible spawn areas for plaeyrs and totems
    private ArrayList<Transform> spawnPoints;
    private final String totemType;
    
    /**
     * Creates a SceneController
     * @param totemType the name of the totems to be used on this level;
     */
    public SceneController(String totemType){this.totemType=totemType;}

    /**
     * attachScene attaches a scene to the games sceneNode.
     * Parameters: child - the child to attach to this node.
     * Throws: NullPointerException - If child is null.
     */
    public void attachScene() {
        attachTotems(true);
        Director.getSceneNode().attachChild(spatial);
        attachLightsToRoot(true);
        Director.getPhysicsSpace().add(pSpatial);
    }

     /**
     * detachScene detaches a scene to the games sceneNode.
     * Parameters: child - the child to attach to this node.
     * Throws: NullPointerException - If child is null.
     */
    public void dettachScene(){
        attachTotems(false);
        Director.getSceneNode().detachChild(spatial);
        attachLightsToRoot(false);
        Director.getPhysicsSpace().remove(pSpatial);
    }

    /**
     * Returns an arrayList of all spawnPoints and totem locations within the scene
     * @return
     */
    public ArrayList<Transform> getSpawnPoints(){return spawnPoints;}

    /**
     * Returns a random Totem pointS
     */
    public Transform getRandomTotem(){return spawnPoints.get(FastMath.nextRandomInt(0, spawnPoints.size()-1));}
    public String getTotemType(){return totemType;}

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);        
        findSpawnLocations();
        pSpatial = new RigidBodyControl(CollisionShapeFactory.createMeshShape(this.spatial),0);
        spatial.addControl(pSpatial);
        GeometryBatchFactory.optimize((Node)spatial);
    }

    /**
    * Extracts from the scene locations set as players start point
    */
    private void findSpawnLocations(){
        spawnPoints = new ArrayList<Transform>();
        for(Spatial s: ((Node)spatial).getChildren()){
            if(s instanceof Node){
                if((((Node)s).getName().startsWith("spawn"))){
                    spawnPoints.add(((Node)s).getLocalTransform());
                }
            }
        }
    }

   /**
    * Attaches or Detaches all lights from the scene to the root so that all models thats not part of the scene 
    * such as the character models can share the environmental light
    * @param attachLight true to attach all lights, false to remove it
    */
   private void attachLightsToRoot(boolean attachLight) {
        Node root = Director.getApp().getRootNode();
        for(Light light:((Node)spatial).getLocalLightList()){
            if(attachLight)
                root.addLight(light);
            else
                root.removeLight(light);
        }
    }
   /**
    * attaches the totems to both the visual and physical world
    * @param attach 
    * TODO: POTENTIAL MEMORY LEAK FIX IN FUTURE (totems arn't properly handeled with)
    * **NOT YET FIXED
    */
   public void attachTotems(boolean attach){
       if(attach){
           Node totems = new Node(totemType);
           TotemController totem = Director.getTotem(totemType);           
           for(Transform t:getSpawnPoints()){
               totem.activate((Node)spatial,t); 
           }
           ((Node)spatial).attachChild(totems);
       }else{
           Node totems = (Node) ((Node)spatial).getChild(totemType);
           for(Spatial s:totems.getChildren()){
               s.getControl(TotemController.class).deactivate();
           }
           ((Node)spatial).detachChild(totems);
       }
   }

    @Override
    protected void controlUpdate(float tpf) { }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    @Override
    public Control cloneForSpatial(Spatial spatial) {return null;}
}
