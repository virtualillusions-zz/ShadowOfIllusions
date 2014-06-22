/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.util.SAX;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.BoneAnimation;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.spectre.controller.character.CharacterController;
import com.spectre.util.Attribute.SpecialMovement;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
/**   
 *  A Sax Handler For This Application
 * @author Kyle Williams
 */
public class SpectreHandler extends DefaultHandler{
    private AssetManager assetManager=null;
    /*
     * The assetManager used to load assets into the Game
     */
    public SpectreHandler(AssetManager aM){assetManager=aM;}
    private SpectreHandler(){}
    public Object getParameters(){return null;}


     /**
     * This is the SAX extension of DefaultHandler that will search for and load
      * all usable animations for the game
     * @return eZHandler
     */
    public SpectreHandler retrieveAllAnimations(){
        return new SpectreHandler(){
            Object[] parameters =
            {new ArrayList<AnimData>(),new HashMap<String,String[]>()};

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
                if(qName.contains("Skeleton")){
                    AnimData animation = (AnimData) assetManager.loadAsset(attributes.getValue("location"));
                    ((ArrayList<AnimData>)parameters[0]).add(animation);
                }else if(qName.contains("Set")){
                    String[] temp = new String[6];
                    temp[0]=attributes.getValue("idle")==null?"Basic_idle":attributes.getValue("idle");
                    temp[1]=attributes.getValue("forward")==null?"Basic_forward":attributes.getValue("forward");
                    temp[2]=attributes.getValue("fall")==null?"Basic_fall":attributes.getValue("fall");
                    temp[3]=attributes.getValue("jump")==null?"Basic_jump":attributes.getValue("jump");
                    temp[4]=attributes.getValue("dash")==null?"Basic_dash":attributes.getValue("dash");
                    temp[5]=attributes.getValue("dive")==null?"Basic_dive":attributes.getValue("dive");
                    
                    //TODO:LOOK INTO LOADING ANIM
//                    int i = 0;
//                    while(attributes.getLocalName(i)!=null){                        
//                        System.out.println(attributes.getQName(i)); 
//                        System.out.println(attributes.getValue(i));
//                                                System.out.println(""); 
//                        i++;
//                    }  
                    
                    ((HashMap<String,String[]>)parameters[1]).put(qName, temp);
                }
            }
            
            @Override
            public Object getParameters() {return parameters;}
        };
    }


    /**
     * This is the SAX extension of DefaultHandler that will assist in searching the characterList for characterNames
     * to accurately find the characters location and load them
     * @return eZ Handler
     */
    public SpectreHandler retrieveAllCharacters(final ArrayList<AnimData> animData,
                                                    final HashMap<String,String[]> basicMovement){
        return new SpectreHandler(){
             private HashMap<String,CharacterController> characterList = new HashMap<String,CharacterController>();
             @Override
             public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {  
                 if(qName.equals("character")){
                     String name = attributes.getValue("name");
                     String location = attributes.getValue("location");
                     Spatial character = assetManager.loadModel(location);

                     //Add physics controller of attached character to Director's characterList 
                     //addAllControllers returns a CharacterController of the spatial
                     characterList.put(name, addAllControllers(character,attributes));
                 }
             }
             
             /**
              * Loads all animations and attaches input animation and physics Controllers
              */
             public CharacterController addAllControllers(Spatial spat,Attributes attributes){
                //Gives Character all moves
                AnimControl ctrl = spat.getControl(AnimControl.class);
                for (AnimData data : animData){
                    for(Animation anim :data.anims){
                        ctrl.addAnim(anim);
                    }
                }
                //Set Up Physics
                com.jme3.bounding.BoundingBox bb = (com.jme3.bounding.BoundingBox)spat.getWorldBound();
                float radius = bb.getXExtent()>bb.getZExtent()? bb.getXExtent():bb.getZExtent();
                float height = bb.getYExtent(); 
                CharacterController pSpatial = new CharacterController(new CapsuleCollisionShape(radius, height), height/3);
                pSpatial.setGravity(pSpatial.getGravity()*3f);
                spat.addControl(pSpatial);                
                //Attach The CharacterAnimationController & InputController to all spatials
                String[] basics = basicMovement.get(attributes.getValue("BasicMovement")+"Set");  
                SpecialMovement sT = SpecialMovement.valueOf(attributes.getValue("SpecialMovement")); 
                spat.addControl(new com.spectre.controller.character.InputController(sT));              
                spat.addControl(new com.spectre.controller.character.CharacterAnimationController(basics,sT));  
                
                return pSpatial;
            }
             
             @Override
             public Object getParameters() {return characterList;}
         };
    }

     /**
     * This is the SAX extension of DefaultHandler that will assist in searching the sceneList for sceneNames
     * to accurately find the scene location and load them
     * @return eZ Handler
     */
    public SpectreHandler retrieveAllScenes() {
        return new SpectreHandler(){
             private HashMap<String,Spatial> sceneList = new HashMap<String,Spatial>();
             @Override
             public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
                 if(qName.equals("scene")){
                     String name = attributes.getValue("name");
                     String location = attributes.getValue("location");
                     String totem = attributes.getValue("totem"); 
                     Spatial scene = assetManager.loadModel(location);
                     scene.addControl(new com.spectre.controller.scene.SceneController(totem));
                     sceneList.put(name, scene);
                 }else if(qName.equals("totem")){
                     String id = attributes.getValue("id");
                     String location = attributes.getValue("location");
                     Spatial scene = assetManager.loadModel(location);
                     scene.addControl(new com.spectre.controller.scene.TotemController());
                     sceneList.put(id, scene);
                 }
             }
             @Override
             public Object getParameters() {return sceneList;}
         };
    }

    /**
     * This is the SAX extension of DefualtHandler that will assist in searching the musicList
     * to accurately and efficiently find the location of music and load them into their respective mapping
     */
    public SpectreHandler retrieveAllMusic(final com.jme3.audio.AudioRenderer audio){
        return new SpectreHandler(){
            private HashMap<String,ArrayList<AudioNode>> musicList = new HashMap<String,ArrayList<AudioNode>>();
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
                if(qName.equals("Song")){
                    String tag = attributes.getValue("tag");
                    if(!musicList.containsKey(tag)){
                        musicList.put(tag, new ArrayList<AudioNode>());
                    }
                    musicList.get(tag).add(addSong(attributes));
                }
            }

            public AudioNode addSong(Attributes attributes){
                AudioNode song = new AudioNode(assetManager,attributes.getValue("location"),true,true);
                song.setUserData("title", attributes.getValue("title"));
                song.setUserData("artist", attributes.getValue("artist"));
                return song;
            }
            @Override
            public Object getParameters(){return musicList;}
        };
    }
}
 
