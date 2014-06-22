/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.spectre.app.SpectreApplicationState;

/**
 *
 * @author Kyle D. Williams
 */
public class Main extends SimpleApplication {
    
    @Override
    public void simpleInitApp() {
        this.stateManager.attach(new SpectreApplicationState());
    }
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        int i = FastMath.nextRandomInt(1, 4);//random images
        //Try using Resources.getResource
        settings.setSettingsDialogImage("../assets/Interface/Misc/titleScreen" + i + ".png");
        /**
         * Set Custom App Icon
         *
         * try { Class<TestChangeAppIcon> clazz = Main.class;
         *
         * settings.setIcons(new BufferedImage[]{
         * ImageIO.read(clazz.getResourceAsStream("/Interface/icons/SmartMonkey256.png")),
         * ImageIO.read(clazz.getResourceAsStream("/Interface/icons/SmartMonkey128.png")),
         * ImageIO.read(clazz.getResourceAsStream("/Interface/icons/SmartMonkey32.png")),
         * ImageIO.read(clazz.getResourceAsStream("/Interface/icons/SmartMonkey16.png")),
         * }); } catch (IOException e) {
         * log.log(java.util.logging.Level.WARNING, "Unable to load program
         * icons", e); }
         */
        app.setSettings(settings);
        app.start();
    }
}
