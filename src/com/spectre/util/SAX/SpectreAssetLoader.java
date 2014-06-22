/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.util.SAX;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.AnimData;
import com.spectre.controller.character.CharacterController;
import com.spectre.deck_old.MasterDeck;
import com.spectre.director.Director;
import com.spectre.director.FXDirector;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
/**
 * This class is in charge of finding all models to be loaded in game
 * @author Kyle Williams
 * TODO: change to hashsets instead
 */
public class SpectreAssetLoader {
    private AssetManager assetManager = null;
    private AudioRenderer audio = null;

    public SpectreAssetLoader(AssetManager aM,AudioRenderer aR) {
        assetManager = aM;
        audio = aR;
    }

      /**
     * This method searches for all characters and load them 
     */
    public void findAndLoadAllCharacters() {        
        HashMap<String,CharacterController> characterList = null;        
        try{
            HashMap<String,String[]> basicMovement = null;
            ArrayList<AnimData> tempAnim = null;
            
            SpectreHandler handler = new SpectreHandler(assetManager),temp;
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            //LOAD ALL ANIMATIONS
            temp=handler.retrieveAllAnimations();
            saxParser.parse(new FileInputStream("assets/Animations/Animations.xml"),temp);
            tempAnim = (ArrayList<AnimData>) ((Object[])temp.getParameters())[0];
            basicMovement = (HashMap<String,String[]>) ((Object[])temp.getParameters())[1];
            //LOAD ALL CHARACTERS
            temp=handler.retrieveAllCharacters(tempAnim,basicMovement);
            saxParser.parse(new FileInputStream("assets/Models/characterList.xml"),temp);
            characterList = (HashMap<String, CharacterController>) temp.getParameters();
        }catch(Throwable err){
             com.spectre.app.SpectreApplication.logger.log(
                     java.util.logging.Level.SEVERE,
                     "Finding All Models Have Failed",
                     new java.io.IOException());

            throw new java.io.IOError(err);
        }

        //LOAD ALL IMPORTANT PARTS INTO THE DIRECTOR
        Director.setCharacterList(characterList);
    }

      /**
     * This method searches for all scenes and load them
     */
    public void findAndLoadAllScenes() {
        HashMap<String,Spatial> sceneList = null;
        try{
            final SpectreHandler handler = new SpectreHandler(assetManager).retrieveAllScenes();
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            //LOAD ALL Scenes
            saxParser.parse(new FileInputStream("assets/Scenes/sceneList.xml"),handler);
            sceneList = (HashMap<String, Spatial>) handler.getParameters();
        }catch(Throwable err){
             com.spectre.app.SpectreApplication.logger.log(
                     java.util.logging.Level.SEVERE,
                     "Finding All Scenes Have Failed",
                     new java.io.IOException());

            throw new java.io.IOError(err);
        }
        Director.setSceneList(sceneList);
    }

    /**
     * This method searches for all music and loads them
     */
    public void findAndLoadAllMusic(){
        HashMap<String,ArrayList<AudioNode>> musicList;
        try{
            final SpectreHandler handler = new SpectreHandler(assetManager).retrieveAllMusic(audio);
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(new FileInputStream("assets/Sounds/MusicList.xml"),handler);
            musicList = (HashMap<String,ArrayList<AudioNode>>) handler.getParameters();
        }catch(Throwable err){
             com.spectre.app.SpectreApplication.logger.log(
                     java.util.logging.Level.SEVERE,
                     "Loading All Music Have Failed",
                     new java.io.IOException());

            throw new java.io.IOError(err);
        }
        FXDirector.setMusicList(musicList);
    }
    
    /**
     * This method searches for and loads all Cards
     */
    public void findAndLoadAllCards(){
//        MasterDeck deck = new MasterDeck();
//        try{            
//            DeckHandler handler = new DeckHandler(assetManager),temp;
//            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
//            //Find all Series in use
//            temp = handler.retrieveAllUsableSeries();
//            saxParser.parse(new FileInputStream("assets/Deck/SeriesList.xml"),temp);            
//            String[] series = (String[]) temp.getParameters(); 
//            temp = handler.retrieveAllCards(audio);
//            for(String s:series){
//                saxParser.parse(new FileInputStream("assets/Deck/"+s+".xml"),temp); 
//                deck.putAll((MasterDeck)temp.getParameters());
//            }  
//        }catch(Throwable err){
//             com.spectre.app.SpectreApplication.logger.log(
//                     java.util.logging.Level.SEVERE,
//                     "Loading All Cards Have Failed",
//                     new java.io.IOException());     
//            throw new java.io.IOError(err);
//        }
//        Director.setCardList(deck);
//        System.out.println(deck); 
    }
}
