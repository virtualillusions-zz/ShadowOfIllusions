/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.director;
 
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.spectre.app.SpectreApplication;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class organizes and maintain all particle states/effects and Audio
 * This class both creates and initializes effects
 * @author Kyle Williams
 */
public final class FXDirector extends com.spectre.app.SpectreState{
    public enum ALBUMS{Ambient,Battle;}
    public ALBUMS currentAlbum = ALBUMS.Ambient;
    private static HashMap<String, ArrayList<AudioNode>> musicList = new HashMap<String, ArrayList<AudioNode>>();
    private static Node fxNode = new Node("Effects Node");
    private static AudioRenderer audio;

    @Override
    public void SpectreState(AppStateManager stateManager, Application app) {
        ((SpectreApplication)app).getRootNode().attachChild(fxNode);
        audio = app.getAudioRenderer();
    }

     public static void setMusicList(HashMap<String, ArrayList<AudioNode>> musiclist) {
        musicList.putAll(musiclist);
    }
    /**
     * @return the audioRenderer
     */
    public static AudioRenderer getAudio(){return audio;}
    /**
     * @param Album the album the song you are searching for is in
     * @param trackTitle the title of the song to play
     */
    public static void playSong(ALBUMS album,String trackTitle){
        AudioNode song = null;
        ArrayList<AudioNode> Album = musicList.get(album.toString());
        for(AudioNode a:Album){
            if(a.getUserData("title").equals(trackTitle)){song=a;break;}
        }
        if(song==null)throw new Error("No Such song in "+album+" Album");
        audio.playSource(song);
    }
     /**
      * @param Artist    the artist of the song to play
     * @param trackTitle the title of the song to play
     */
    public static void playSong(String artist,String trackTitle){
        AudioNode song = null;
        for(ArrayList<AudioNode> Album:musicList.values()){
            for(AudioNode a:Album){
                if(a.getUserData("title").equals(trackTitle)){song=a;break;}
            }
        }
        if(song==null)throw new Error("No Such song in Album");
        audio.playSource(song);
    }

    /*
     * Randomly Plays music in an Album
     */
    public static void playMusic(ALBUMS album){
        ArrayList<AudioNode> Album = musicList.get(album.toString());
        FastMath.nextRandomInt(0, Album.size()-1);
    }

    @Override
    public void update(float tpf){
        for(int i=0;i<fxNode.getChildren().size();i++){
            ParticleEmitter pE = (ParticleEmitter) fxNode.getChild(i);
            pE.emitAllParticles();
        }
    }

    public static void playFX(ParticleEmitter fx){
        fxNode.attachChild(fx);
    }

}
