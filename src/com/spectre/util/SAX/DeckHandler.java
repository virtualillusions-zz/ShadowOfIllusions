/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util.SAX;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.ParticlePointMesh;
import com.jme3.effect.shapes.EmitterMeshConvexHullShape;
import com.jme3.effect.shapes.EmitterMeshFaceShape;
import com.jme3.effect.shapes.EmitterMeshVertexShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.spectre.deck_old.Card;
//import com.spectre.deck.CardSetter;
import com.spectre.deck_old.MasterDeck;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Kyle Williams
 */
public class DeckHandler extends DefaultHandler{
    private AssetManager assetManager=null;
    /*
     * The assetManager used to load assets into the Game
     */
    public DeckHandler(AssetManager aM){assetManager=aM;}
    private DeckHandler(){}
    public Object getParameters(){return null;}
    
         /**
     * This is the SAX extension of DefaultHandler that will search for and load
      * all usable animations for the game
     * @return eZHandler
     */
    public DeckHandler retrieveAllUsableSeries(){
        return new DeckHandler(){
            ArrayList<String> series = new ArrayList<String>();

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
                if(qName.equals("series")){
                     String location = attributes.getValue("id");
                     series.add(location);
                 }
            }
            
            @Override
            public Object getParameters() {return series.toArray(new String[series.size()]);}
        };
    }
     
             /**
     * This is the SAX extension of DefaultHandler that will search for and load
      * all usable animations for the game
     * @return eZHandler
     */
    public DeckHandler retrieveAllCards(final com.jme3.audio.AudioRenderer audio){
        return new DeckHandler(){
//            private MasterDeck series = new MasterDeck();
//            private CardSetter card = null;
//            private ParticleEmitter effect = null;
//            private AudioNode track = null;
//            //private EffectPresets effectPresets = new EffectPresets(assetManager);
//
//            @Override
//            public void startElement(String uri, String localName, String q, Attributes a)
//            throws SAXException { 
//                if(card==null&&q.equals("Card")){ 
//                    card = new CardSetter();
//                    card.setName(a.getValue("name"));
//                    card.setMaxUses(a.getValue("maxUses"));
//                    card.setPredecessor(a.getValue("prev"));
//                    card.addSuccessor(a.getValue("next"));
//                }else{
//                    if(q.equals("Info")){
//                        if(a.getValue("description")!=null)
//                            card.setDescription(a.getValue("description"));                       
//                        if(a.getValue("range")!=null)
//                            card.setRange(a.getValue("range"));
//                        if(a.getValue("angle")!=null)
//                            card.setAngle(a.getValue("angle"));
//                        if(a.getValue("series")!=null)
//                            card.setSeries(a.getValue("series"));
//                        if(a.getValue("trait")!=null)
//                            card.setTrait(a.getValue("trait"));
//                        if(a.getValue("animationName")!=null)
//                            card.setAnimName(a.getValue("animationName"));
//                    }else if(q.equals("Stats")){
//                        if(a.getValue("HP")!=null)
//                            card.setHPStat(a.getValue("HP"));
//                        if(a.getValue("MP")!=null)
//                            card.setMPStat(a.getValue("MP"));
//                        if(a.getValue("knockback")!=null)
//                            card.setKnockbackStat(a.getValue("knockback"));
//                        if(a.getValue("speed")!=null)
//                            card.setSpeedStat(a.getValue("speed"));
//                        if(a.getValue("deffense")!=null)
//                            card.setDeffenseStat(a.getValue("deffense"));
//                        if(a.getValue("special")!=null)
//                            card.setSpecialStat(a.getValue("special"));
//                        if(a.getValue("translation")!=null)
//                            card.setTranslationStat(a.getValue("translation"));                    
//                    }else if(q.equals("Effect")){
//                        String name = a.getValue("name")!=null?a.getValue("name"):"";
////                        if(a.getValue("preset")!=null){
////                           String pre = a.getValue("preset");
////                           effect = effectPresets.getEffect(pre);
////                           effect.setUserData("preset", pre);
////                           effect.setName(name);                           
////                           if(a.getValue("particleNumber")!=null){
////                               String pNumber = a.getValue("particleNumber");
////                               effect.setNumParticles(Integer.parseInt(pNumber));
////                           }
////                        }else{
//                            Type type = Type.valueOf(a.getValue("type"));
//                            int pNumber = Integer.parseInt(a.getValue("particleNumber"));
//                            effect = new ParticleEmitter(name,type,pNumber);
//                        //}
//                    }else if(q.equals("EffectShape")&&a.getValue("shape")!=null){
//                        EmitterShape shape = null;
//                        if(a.getValue("shape").equals("box")){
//                            Vector3f min = toVector3f(a.getValue("min"));
//                            Vector3f max = toVector3f(a.getValue("max"));                                
//                            shape = new EmitterBoxShape(min,max);                          
//                        }else if(a.getValue("shape").equals("point")){
//                            Vector3f point = toVector3f(a.getValue("point"));
//                             shape = new EmitterPointShape(point);
//                        }else if(a.getValue("shape").equals("sphere")){
//                            Vector3f center = toVector3f(a.getValue("center"));
//                            float radius = Float.parseFloat(a.getValue("radius"));
//                            shape = new EmitterSphereShape(center,radius);                           
//                        }else{
//                            String loc = a.getValue("mesh");
//                            Node mesh = (Node) assetManager.loadModel(loc);
//                             java.util.ArrayList<com.jme3.scene.Mesh> list = new java.util.ArrayList<com.jme3.scene.Mesh>(); 
//                             for(com.jme3.scene.Spatial spat:mesh.getChildren()){
//                                list.add(((com.jme3.scene.Geometry)spat).getMesh());
//                             } 
//                             if(a.getValue("shape").equals("convexHull")){
//                                 shape = new EmitterMeshConvexHullShape(list); 
//                             }else if(a.getValue("shape").equals("meshFace")){
//                                 shape = new EmitterMeshFaceShape(list);
//                             }else if(a.getValue("shape").equals("meshVertex")){
//                                 shape = new EmitterMeshVertexShape(list);
//                             }
//                             effect.setUserData("mesh", loc);
//                        }
//                        effect.setShape(shape);
//                    }else if(a.getValue("bone")!=null){
//                            effect.setUserData("bone", a.getValue("bone"));                        
//                    }else if(q.equals("EffectProperty")){
//                        if(a.getValue("startColor")!=null){
//                            effect.setStartColor(this.toColorRGBA(a.getValue("startColor")));
//                        }
//                        if(a.getValue("endColor")!=null){
//                            effect.setEndColor(this.toColorRGBA(a.getValue("endColor")));
//                        }
//                        if(a.getValue("startSize")!=null){
//                            effect.setStartSize(Float.parseFloat(a.getValue("startSize")));
//                        }
//                        if(a.getValue("endSize")!=null){
//                            effect.setEndSize(Float.parseFloat(a.getValue("endSize")));
//                        }
//                        if(a.getValue("highLife")!=null){
//                            effect.setHighLife(Float.parseFloat(a.getValue("highLife")));
//                        }
//                        if(a.getValue("lowLife")!=null){
//                            effect.setLowLife(Float.parseFloat(a.getValue("lowLife")));
//                        }
//                        if(a.getValue("rotateSpeed")!=null){
//                            effect.setRotateSpeed(Float.parseFloat(a.getValue("rotateSpeed")));
//                        }
//                        if(a.getValue("image")!=null){
//                            String pic = a.getValue("image");
//                            effect.setUserData("image", pic);
//                            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
//                            mat.setTexture("Texture", assetManager.loadTexture(pic));                            
//                            mat.setBoolean("PointSprite", effect.getMesh() instanceof ParticlePointMesh);
//                            effect.setMaterial(mat);
//                        }                        
//                        if(a.getValue("randImgSelect")!=null){
//                            effect.setSelectRandomImage(Boolean.parseBoolean(a.getValue("randImgSelect")));
//                        }
//                        if(a.getValue("imagesX")!=null){
//                            effect.setImagesX(Integer.parseInt(a.getValue("imagesX")));
//                        }
//                        if(a.getValue("imagesY")!=null){
//                            effect.setImagesY(Integer.parseInt(a.getValue("imagesY")));
//                        }
//                        if(a.getValue("normal")!=null){
//                            effect.setFaceNormal(this.toVector3f(a.getValue("normal")));
//                        }
//                        if(a.getValue("facingVelocity")!=null){
//                            effect.setFacingVelocity(Boolean.parseBoolean(a.getValue("facingVelocity")));
//                        }
//                        if(a.getValue("gravity")!=null){
//                            effect.setGravity(toVector3f(a.getValue("gravity")));  
//                        }
//                        if(a.getValue("numParticles")!=null){
//                            effect.setNumParticles(Integer.parseInt(a.getValue("numParticles")));
//                        }
//                        if(a.getValue("particlesPerSec")!=null){
//                            effect.setParticlesPerSec(Integer.parseInt(a.getValue("particlesPerSec")));
//                        }
//                        if(a.getValue("randomAngle")!=null){
//                            effect.setRandomAngle(Boolean.parseBoolean("randomAngle"));  
//                        }
//                        if(a.getValue("initialVelocity")!=null){
//                            effect.setInitialVelocity(toVector3f(a.getValue("initialVelocity")));
//                        }
//                        if(a.getValue("velocityVariation")!=null){
//                            effect.setVelocityVariation(Float.parseFloat(a.getValue("velocityVariation")));
//                        }
//                    }else if(q.equals("Track")){
//                        track = new AudioNode(audio,assetManager,a.getValue("location"));
//                        if(a.getValue("pitch")!=null)
//                            track.setPitch(Float.parseFloat(a.getValue("pitch")));
//                        if(a.getValue("reverb")!=null)
//                            track.setReverbEnabled(Boolean.getBoolean(a.getValue("reverb")));
//                        if(a.getValue("timeOffset")!=null)
//                            track.setTimeOffset(Float.parseFloat(a.getValue("timeOffset")));
//                        if(a.getValue("volume")!=null)
//                            track.setVolume(Float.parseFloat(a.getValue("volume")));
//                        card.addSoundEffect(track);
//                        track = null;
//                    }
//                }
//            }
//            
//            @Override
//            public void endElement(String uri, String localName, String qName)
//            throws SAXException {
//                if(qName.equals("Effect")){
//                    card.addEffect(effect);
//                    effect = null;                
//                }else if(qName.equals("Card")){
//                    series.put(card.getName(),(Card)card);
//                    card = null;
//                }
//            }
//            
//            private Vector3f toVector3f(String param){
//                //will be in format (0.0, 0.1, 0.0) TODO:FIX
//                String[] k = param.split(",");
//                float x = Integer.parseInt(k[0]);
//                float y = Integer.parseInt(k[1]);
//                float z = Integer.parseInt(k[2]);
//                return new Vector3f(x,y,z);
//            }
//            
//            private ColorRGBA toColorRGBA(String param){
//                //will be in format Color[0, 0, 0, 0] TODO:FIX
//                String[] k = param.split(",");
//                float r = Integer.parseInt(k[0]);
//                float g = Integer.parseInt(k[1]);
//                float b = Integer.parseInt(k[2]);
//                float a = Integer.parseInt(k[3]);
//                return new ColorRGBA(r,g,b,a);
//            }
//            
//            @Override
//            public Object getParameters() {return series;}
        };
    }
    
}
