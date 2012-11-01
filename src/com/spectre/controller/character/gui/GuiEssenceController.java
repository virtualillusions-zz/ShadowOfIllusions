///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.spectre.controller.character.gui;
//
//
//import com.jme3.niftygui.NiftyJmeDisplay;
//
//import com.jme3.scene.Spatial;
//
//import com.spectre.deck_old.CardStats.CardTrait;
//import com.spectre.director.Director;
//
//import de.lessvoid.nifty.Nifty;
//import de.lessvoid.nifty.elements.Element;
//import de.lessvoid.nifty.elements.render.ImageRenderer;
//import de.lessvoid.nifty.elements.render.TextRenderer;
//import de.lessvoid.nifty.render.NiftyImage;
//import de.lessvoid.nifty.screen.Screen;
//import de.lessvoid.nifty.screen.ScreenController;
//import de.lessvoid.nifty.tools.SizeValue;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * Maintains and Handles the Graphical Interface for Life and Focus Representation as well as current MoveSet and Queued
// * @author Kyle Williams
// */
//public class GuiEssenceController implements ScreenController{
//    private Nifty nifty;
//    private Screen screen;
//    private Element focusText;
//    private Element lifeText;
//    private Element cardsLeftText;
//    private Element mainLayer;
//    
//    @Override
//    public void bind(Nifty nifty, Screen screen) {
//        this.nifty=nifty;
//        this.screen=screen;
//        mainLayer = screen.findElementByName("main");
//        focusText = screen.findElementByName("focusText");
//        lifeText = screen.findElementByName("lifeText");
//        cardsLeftText = screen.findElementByName("cardsLeftText");
//    }
//    public void updatePosition(SizeValue newX,SizeValue newY,SizeValue newHeight,SizeValue newWidth){
//        mainLayer.setConstraintHeight(newHeight);
//        mainLayer.setConstraintWidth(newWidth);
//        mainLayer.setConstraintX(newX);
//        mainLayer.setConstraintY(newY);
//        screen.layoutLayers();
//    }
//
//    @Override
//    public void onStartScreen() {}
//    @Override
//    public void onEndScreen() {}
//    /**
//     * Sets the GUI's life visual parameters
//     * The life given should always be based from the 100 percentile
//     * @param life the life
//     */
//    public void setLife(int life){
//        lifeText.getRenderer(TextRenderer.class).setText("currentLife: "+life);
//        setGUILife(life);
//    }
//    /*** Sets the GUI's visual current focus parameter*/
//    public void setFocus(int currentFocus,int totalFocus){focusText.getRenderer(TextRenderer.class).setText(currentFocus+" / "+totalFocus);}
//    /**Sets the GUI's RemainingCards parameters*/
//    public void setRemainingCards(int remainingCards){cardsLeftText.getRenderer(TextRenderer.class).setText(""+remainingCards);}
//    /**Sets the desired Emblem to the top position 
//     * @param type the CardType*/
//    public void setEmblemUp(CardTrait type){setElement("emblemUp","Emblem",type.toString()+"Up");}
//    /**Sets the desired Emblem to the bottom position
//     * @param type the CardType*/
//    public void setEmblemDown(CardTrait type){setElement("emblemDown","Emblem",type.toString()+"Down");}
//    /**Sets the desired Emblem to the left position
//     * @param type the CardType*/
//    public void setEmblemLeft(CardTrait type){setElement("emblemLeft","Emblem",type.toString()+"Left");}
//    /**Sets the desired Emblem to the right position
//     * @param type the CardType*/
//    public void setEmblemRight(CardTrait type){setElement("emblemRight","Emblem",type.toString()+"Right");}
//    /**Removes the current Emblem from the top position*/
//    public void removeEmblemUp(){hideElement("emblemUp",true);}
//    /**Removes the current Emblem from the bottom position*/
//    public void removeEmblemDown(){hideElement("emblemDown",true);}
//    /**Removes the current Emblem from the left position*/
//    public void removeEmblemLeft(){hideElement("emblemLeft",true);}
//    /**Removes the current Emblem from the right position*/
//    public void removeEmblemRight(){hideElement("emblemRight",true);}
//    /**
//     * This sets the queued card lists in bulk for the player's GUI.
//     * Their should only ever be three queued cards
//     * @param queuedCards an array of three queued cards
//     */
//    public void setQueued(CardTrait queuedCards[]){
//        for(int i=0;i<3;i++){
//            if(queuedCards[i]==null){hideElement("queued"+(i+1),true);                              //if array position null hide what ever previous image was in its place
//            } else {setElement("queued"+(i+1),"Queued","queued"+queuedCards[i].toString()+(i+1));}  //sets the queued positions image
//        }
//    }
//    /*
//     * Loads or removes the emblem currently in the selected position
//     * @param part is it an emblem or is it queued
//     * @param type the CardType
//     * @param mapping the direction that is being mapped
//     * @param show show or hide the defined emblem
//     */
//    private void setElement(String partName,String folder,String filename){        
//        Element element = screen.findElementByName(partName); // find the element with it's id
//        NiftyImage newImage = nifty.getRenderEngine().createImage("Interface/EssenceInterface/"+folder+"/"+filename+".png", false); // false means don't linear filter the image, true would apply linear filtering
//        element.getRenderer(ImageRenderer.class).setImage(newImage); // change the image with the ImageRenderer
//        element.show(); //shows the element in case it has been hidden
//    }
//    /**
//     * Hides or shows the given element
//     * @param elementID
//     * @param hide
//     */
//    private void hideElement(String elementID,boolean hide){
//        // find the element with it's id
//        Element element = screen.findElementByName(elementID);
//        if(hide){element.hide();}   //hides the element if the boolean says to
//        else{element.show();}       //shows the element if the boolean says to
//    }
//   /**
//    * Sets the visual life bars for the player
//    * @param life
//    */
//   private void setGUILife(int life) {
//        //Sets the main Life Segements
//        for(int i=1; i<=5;i++){
//            if(life/20+1>=i){hideElement("mainLife"+i,false);}
//            else{hideElement("mainLife"+i,true);}
//            //Sets the sub Life Bars
//            if(i<5){
//                if((((life)%20)/5)+1>=i){hideElement("subLife"+i,false);}
//                else {hideElement("subLife"+i,true);}
//            }
//        }
//    }
//
//   /**
//    * This is the method that should be called directly to set up the GUI portion of the EssenceControls
//     * TODO: Fix GUI to be dynamic for now will be static
//    * @param spatial
//    * @return GuiEssenceController this instance of the GUI essenceController
//    */
//   public GuiEssenceController start(final Spatial spatial){
//      
//       com.spectre.app.SpectreApplication app = Director.getApp();
//        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
//                                                          app.getInputManager(),
//                                                          app.getAudioRenderer(),
//                                                          app.getGuiViewPort());
//        //Set Up Nifty
//        nifty = niftyDisplay.getNifty();
//        try {
//            nifty.validateXml("Interface/EssenceInterface/EssenceInterface.xml");
//        } catch (Exception ex) {
//            Logger.getLogger(GuiEssenceController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        nifty.fromXml("Interface/EssenceInterface/EssenceInterface.xml", "start");
//        app.getGuiViewPort().addProcessor(niftyDisplay);
//        
//
//        return this;
//   }
//}
