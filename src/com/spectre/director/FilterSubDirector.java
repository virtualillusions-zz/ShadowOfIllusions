/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spectre.director;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Caps;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 * This class controls all Filters and postProcessing in the game
 * @author Kyle Williams
 */
public class FilterSubDirector {
       private FogFilter fog;
       private BloomFilter bloom;
       private FilterPostProcessor fpp;
       private AssetManager assetManager;
       private CartoonEdgeFilter cartoon;
       private ColorOverlayFilter colorOverlay;
       private DepthOfFieldFilter dof;

       /**
        * A Director that Handel all Filter Related tasks
        * @param aM
        */
       public FilterSubDirector(AssetManager aM){assetManager = aM;}
       /**@return CartoonEdgeFilter*/
       public CartoonEdgeFilter getCartoonFilter(){return cartoon;}
       /**Toggles the state of the CartoonEdgeFilter*/
       public void toggleCartoonFilter(){cartoon.setEnabled(!cartoon.isEnabled());}
        /**@return BloomFilter*/
       public BloomFilter getBloomFilter(){return bloom;}
       /**Toggles the state of the BloomFilter*/
       public void toggleBloomFilter(){bloom.setEnabled(!bloom.isEnabled());}
        /**@return ColorOverlayFilter*/
       public ColorOverlayFilter getColorOverlayFilter(){return colorOverlay;}
       /**Toggles the state of the ColorOverlayFilter*/
       public void toggleColorOverlayFilter(){colorOverlay.setEnabled(!colorOverlay.isEnabled());}
        /**@return FogFilter*/
       public FogFilter getFogFilter(){return fog;}
       /**Toggles the state of the FogFilter*/
       public void toggleFogFilter(){fog.setEnabled(!fog.isEnabled());}
       /**@return D.O.F.Filter*/
       public DepthOfFieldFilter getDOFFilter(){return dof;}
       /**Toggles the state of the FogFilter*/
       public void toggleDOFFilter(){dof.setEnabled(!dof.isEnabled());}


       public void setupFilters(Renderer renderer,ViewPort viewPort){
        if (renderer.getCaps().contains(Caps.GLSL100)){            
            fpp=new FilterPostProcessor(assetManager);
            //CARTOONFILTER 
            cartoon = new CartoonEdgeFilter();
            fpp.addFilter(cartoon);
            //BLOOM
            bloom=new BloomFilter();
            bloom.setDownSamplingFactor(2);
            bloom.setBlurScale(1.37f);
            bloom.setExposurePower(3.30f);
            bloom.setExposureCutOff(0.2f);
            bloom.setBloomIntensity(2.45f);
            fpp.addFilter(bloom);
            //ColorOverlay
            colorOverlay = new ColorOverlayFilter(ColorRGBA.White);
            fpp.addFilter(colorOverlay);
             //FogFilter
            fog=new FogFilter();
            //fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
            fog.setFogColor(ColorRGBA.Black);
            fog.setFogDistance(155);
            fog.setFogDensity(2.0f);
            fpp.addFilter(fog);
            //DepthOfField
            dof = new DepthOfFieldFilter();
            dof.setFocusDistance(0);
            dof.setFocusRange(10);
            dof.setBlurScale(1.4f);            
            fpp.addFilter(dof);
            //Add Filter To ViewPort
            viewPort.addProcessor(fpp);
            
            this.toggleBloomFilter();
        }
    }

    /**
     * CURRENTLY UNUSED
     * Makes the spatial appear more cartoony
     * @param spatial
     */
    public void makeToonish(Spatial spatial){
        if (spatial instanceof Node){
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren())
                makeToonish(child);
        }else if (spatial instanceof Geometry){
            Geometry g = (Geometry) spatial;
            Material m = g.getMaterial();
            if (m.getMaterialDef().getName().equals("Phong Lighting")){
                Texture t = assetManager.loadTexture("Textures/util/toon.png");
                m.setTexture("ColorRamp", t);
                m.setBoolean("UseMaterialColors", true);
                m.setColor("Specular", ColorRGBA.Black);
                m.setColor("Diffuse", ColorRGBA.White);
                m.setBoolean("VertexLighting", true);
            }
        }
    }
}
