/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.director;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.spectre.controller.character.gui.CardGUIController;
import com.spectre.controller.character.gui.GuiEssenceController;
import com.spectre.app.SpectreState;
import com.spectre.controller.character.CharacterAnimationController;
import com.spectre.controller.character.CharacterController;
import com.spectre.controller.character.EssenceController;
import com.spectre.controller.character.PlayerController;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Kyle Williams
 */
public class GameState extends SpectreState{

    private String scene;   //The Scene to be loaded
    private String totemType;//The type of the totmes
    private ArrayList<Transform> spawnPoints;
    private ArrayList<String> activePlayers; //A list of characters to be loaded
    private ArrayList<Node> activeTotems; // A list of active totems
    private float[] totemSpawnCounter;    //A counter for totem generation of each activePlayer    
    
    @Override
    public void SpectreState(AppStateManager stateManager, Application app) {
        activePlayers = new ArrayList<String>();
        activeTotems = new ArrayList<Node>();
        Director.getPhysicsSpace().addCollisionListener(pCL);
        this.setEnabled(false);
    } 
        
    public void setUp(String scene,String... addedPlayers){
        setScene(scene);
        activePlayers.addAll(Arrays.asList(addedPlayers));        
    }
    public void setScene(String Scene){
        scene=Scene;
        spawnPoints=Director.getScene(scene).getSpawnPoints();
        totemType=Director.getScene(scene).getTotemType();}
    public void addPlayer(String extraPlayer){activePlayers.add(extraPlayer);}
         
    public void start(){        
        Director.getScene(scene).attachScene();  
        setUpBoundaries();
        
        totemSpawnCounter = new float[activePlayers.size()];
        ArrayList<Transform> sP = (ArrayList<Transform>) spawnPoints.clone();
        for(int i=0;i<activePlayers.size();i++){
            //wtotemSpawnCounter[i]=0;
            int rand = FastMath.nextRandomInt(0, sP.size()-1);
            Transform spawn = sP.remove(rand);//sP.get(rand);             
            PlayerController player = Director.getPlayer(activePlayers.get(i));
            player.getPhysicsModel().setPhysicsLocation(spawn.getTranslation().add(0, 10f, 0));
            //For Initial rotation
            Vector3f axis = new Vector3f(1,0,1);
            Quaternion rotation = spawn.getRotation();
            player.getPhysicsModel().setViewDirection(rotation.mult(axis));
            //Set any other needed controllers
            //Remember: CardGUIController Not Set
            //player.getModel().addControl(new CardGUIController());
            
            EssenceController e = new EssenceController();
                              e.setGuiController(new GuiEssenceController());
            player.getModel().addControl(new EssenceController());
            
            player.addIntoPlay();         
        }
        this.setEnabled(true);
    }
    
    public PhysicsCollisionListener pCL = new PhysicsCollisionListener(){
        @Override
        public void collision(PhysicsCollisionEvent event){
            if(event.getNodeA().getName().contains(totemType)){
                for(String s:activePlayers){
                    if(event.getNodeB().getName().contains(s)){
                        Node c = (Node) event.getNodeB();
                        CharacterController cc = ((CharacterController)event.getObjectB());
                        c.getControl(CharacterAnimationController.class).setAnim(true, "jump");
                    }   
                }
            }else if(event.getNodeB().getName().contains(totemType)){
                for(String s:activePlayers){
                    if(event.getNodeA().getName().contains(s)){
                        Node c = (Node) event.getNodeA();
                        CharacterController cc = ((CharacterController)event.getObjectA());
                        c.getControl(CharacterAnimationController.class).setAnim(true, "jump");
                    } 
                }
            }
        }
    };
    
       
    @Override
    public void update(float tpf){ 
        for(int i=0; i<totemSpawnCounter.length;i++){    
            EssenceController eC = Director.getPlayer(activePlayers.get(i)).getEssenceController();
            if(totemSpawnCounter[i]>=eC.getTotemSpawnInterval()){
                //System.out.println("READY TO SPAWN");
            }else{
                totemSpawnCounter[i]+=tpf;
            }
        }
    }  
        
    public void setUpBoundaries(){
        Node Boundary1 = new Node("PhysicsNode"); 
        Node Boundary2 = new Node("PhysicsNode");
        Node Boundary3 = new Node("PhysicsNode");
        Node Boundary4 = new Node("PhysicsNode");
        BoundingBox bb = (BoundingBox) Director.getSceneNode().getWorldBound();
        Boundary1.setLocalTranslation(bb.getCenter().add(bb.getXExtent(),0,0));
        Boundary1.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(1,bb.getYExtent(),bb.getZExtent()*2)), 0));
        Director.getPhysicsSpace().add(Boundary1);
        Boundary2.setLocalTranslation(bb.getCenter().subtract(bb.getXExtent(),0,0));
        Boundary2.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(1,bb.getYExtent(),bb.getZExtent()*2)), 0));
        Director.getPhysicsSpace().add(Boundary2);
        Boundary3.setLocalTranslation(bb.getCenter().add(0,0,bb.getZExtent()));
        Boundary3.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(bb.getXExtent()*2,bb.getYExtent(),1)), 0));
        Director.getPhysicsSpace().add(Boundary3);
        Boundary4.setLocalTranslation(bb.getCenter().subtract(0,0,bb.getZExtent()));
        Boundary4.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f(bb.getXExtent()*2,bb.getYExtent(),1)), 0));
        Director.getPhysicsSpace().add(Boundary4);
        
    }
}
