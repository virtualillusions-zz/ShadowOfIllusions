package com.spectre;

import com.jme3.material.Material;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * test
 * @author normenhansen
 */
public class Main extends com.spectre.app.SpectreApplication {

    public static void main(String[] args) {
        ArrayList<Integer> names = new ArrayList<Integer>();
        names.add(1);
        names.add(2);
        names.add(3);
        names.add(4);
        names.add(5);
        names.add(6);
        names.add(7);
        names.add(8);
        names.add(9);
        names.add(10);
        
        
        System.out.println(names.size());
        for (Iterator<Integer> i = names.iterator(); i.hasNext();) {
            //Do Something
            //i.remove();
            Integer s = i.next();
            if(s%2==0){
                i.remove();
            }
            System.out.println(s);
        }
        System.out.println(names.size());
        for(Integer i:names){
            System.out.println(i);
        }
//        Main app = new Main();
//        AppSettings settings = new AppSettings(true);
//        //settings.setUseJoysticks(true);
//        app.setSettings(settings);
//        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
//        app.start();
    }

    @Override
    public void spectreApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
    protected void spectreUpdate(float tpf) {
    }

    @Override
    public void spectreRender(RenderManager rm) {
    }
}
