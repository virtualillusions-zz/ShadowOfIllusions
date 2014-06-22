/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.controller.character.gui;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;

/**
 * Handles the secondary GUI for display of onField player cards
 * @author Kyle Williams
 */
public class CardGUIController extends com.jme3.scene.control.AbstractControl{
    
    private Geometry[] cards = new Geometry[4];
    private Node cardHolder;
    private AssetManager aM;

    /**
     * Sets the Card on the field
     * @param card The name of the card to be used
     * @param set should the card position be visible on the field
     */
    public void setCard1(String card,boolean set){
        if(!set){cardHolder.detachChild(cards[0]);return;}
        cards[0].getMaterial().setTexture("ColorMap", (Texture2D) aM.loadTexture("Textures/Cards/"+card+".png"));
        cardHolder.attachChild(cards[0]);
    }
    /**
     * Sets the Card on the field
     * @param card The name of the card to be used
     * @param set should the card position be visible on the field
     */
    public void setCard2(String card,boolean set){
        if(!set){cardHolder.detachChild(cards[1]);return;}
        cards[1].getMaterial().setTexture("ColorMap", (Texture2D) aM.loadTexture("Textures/Cards/"+card+".png"));
        cardHolder.attachChild(cards[1]);
    }
    /**
     * Sets the Card on the field
     * @param card The name of the card to be used
     * @param set should the card position be visible on the field
     */
    public void setCard3(String card,boolean set){
        if(!set){cardHolder.detachChild(cards[2]);return;}
        cards[2].getMaterial().setTexture("ColorMap", (Texture2D) aM.loadTexture("Textures/Cards/"+card+".png"));
        cardHolder.attachChild(cards[2]);

    }
    /**
     * Sets the Card on the field
     * @param card The name of the card to be used
     * @param set should the card position be visible on the field
     */
    public void setCard4(String card,boolean set){
        if(!set){cardHolder.detachChild(cards[3]);return;}
        cards[3].getMaterial().setTexture("ColorMap", (Texture2D) aM.loadTexture("Textures/Cards/"+card+".png"));
        cardHolder.attachChild(cards[3]);
    }

    @Override
    public void setSpatial(Spatial spat){
        super.setSpatial(spat);
        BoundingBox bb = (BoundingBox) spat.getWorldBound();
        aM = com.spectre.director.Director.getApp().getAssetManager();
        for(int i=0;i<4;i++){
            cards[i]=newCard(bb,aM,i);
        }
        cardHolder = new Node("cardHolder");
        ((com.jme3.scene.Node)spat).attachChild(cardHolder);
    }

    //Creates a new Card to be placed on the field
    private Geometry newCard(BoundingBox root,AssetManager aM,int cardNum){
        //Create The card
        Geometry card = new Geometry("Box", new Quad(1, 1.5f));
        Material mat = new Material(aM, "Common/MatDefs/Misc/SolidColor.j3md");
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        card.setMaterial(mat);


        card.rotateUpTo(Vector3f.UNIT_Z.negate());
        card.setLocalTranslation(-0.5f, -0.75f, 0.75f);//Z is to center it once it is rotated

        float x = root.getXExtent()*2;
        float z = root.getZExtent()*4;

        if(cardNum==0){
            card.setLocalTranslation(card.getLocalTranslation().add(x*1.5f, 0, z));
        }else if(cardNum==1){
            card.setLocalTranslation(card.getLocalTranslation().add(x/2, 0, z));
        }else if(cardNum==2){
            card.setLocalTranslation(card.getLocalTranslation().add(-x/2, 0, z));
        }else if(cardNum==3){
            card.setLocalTranslation(card.getLocalTranslation().add(-x*1.5f, 0, z));
        }

        return card;
    }


    @Override
    protected void controlUpdate(float tpf) {
        //TODO: properly Rotate cards accordingly
    }

   
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
