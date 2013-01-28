/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.render;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Kyle
 */
public class MultiViewport_PostRender_Bug extends SimpleApplication {

    private FogFilter fog;
    private BloomFilter bloom;
    private FilterPostProcessor fpp;
    private CartoonEdgeFilter cartoon;
    private ColorOverlayFilter colorOverlay;
    private DepthOfFieldFilter dof;

    public static void main(String[] args) {
        MultiViewport_PostRender_Bug app = new MultiViewport_PostRender_Bug();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        
         // Setup first view
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        cam.setViewPort(.5f, 1f, 0f, 0.5f);
        cam.setLocation(new Vector3f(3.3212643f, 4.484704f, 4.2812433f));
        cam.setRotation(new Quaternion(-0.07680723f, 0.92299235f, -0.2564353f, -0.27645364f));
        setupFilters(viewPort);
        // Setup second view
        Camera cam2 = cam.clone();
        cam2.setViewPort(0f, 0.5f, 0f, 0.5f);
        cam2.setLocation(new Vector3f(-0.10947256f, 1.5760219f, 4.81758f));
        cam2.setRotation(new Quaternion(0.0010108891f, 0.99857414f, -0.04928594f, 0.020481428f));

        ViewPort view2 = renderManager.createMainView("Bottom Left", cam2);
        view2.setClearFlags(true, true, true);
        view2.attachScene(rootNode);
        setupFilters(view2);
        // Setup third view
        Camera cam3 = cam.clone();
        cam3.setViewPort(0f, .5f, .5f, 1f);
        cam3.setLocation(new Vector3f(0.2846221f, 6.4271426f, 0.23380789f));
        cam3.setRotation(new Quaternion(0.004381671f, 0.72363687f, -0.69015175f, 0.0045953835f));

        ViewPort view3 = renderManager.createMainView("Top Left", cam3);
        view3.setClearFlags(true, true, true);
        view3.attachScene(rootNode);
        setupFilters(view3);
        // Setup fourth view
        Camera cam4 = cam.clone();
        cam4.setViewPort(.5f, 1f, .5f, 1f);
        cam4.setLocation(new Vector3f(4.775564f, 1.4548365f, 0.11491505f));
        cam4.setRotation(new Quaternion(0.02356979f, -0.74957186f, 0.026729556f, 0.66096294f));

        ViewPort view4 = renderManager.createMainView("Top Right", cam4);
        view4.setClearFlags(true, true, true);
        view4.attachScene(rootNode);
        setupFilters(view4);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * Toggles the state of the BloomFilter
     */
    public void toggleBloomFilter() {
        bloom.setEnabled(!bloom.isEnabled());
    }

    public void setupFilters(ViewPort viewPort) {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            if (fpp == null) {
                createFilterPostProcessor();
            }
            //Add Filter To ViewPort
            viewPort.addProcessor(fpp);
        }
    }

    private void createFilterPostProcessor() {
        fpp = new FilterPostProcessor(assetManager);
        //CARTOONFILTER 
        cartoon = new CartoonEdgeFilter();
        fpp.addFilter(cartoon);
        //BLOOM
        bloom = new BloomFilter();
        bloom.setDownSamplingFactor(2);
        bloom.setBlurScale(1.37f);
        bloom.setExposurePower(3.30f);
        bloom.setExposureCutOff(0.2f);
        bloom.setBloomIntensity(2.45f);
        //fpp.addFilter(bloom);
        //ColorOverlay
        colorOverlay = new ColorOverlayFilter(ColorRGBA.White);
        fpp.addFilter(colorOverlay);
        //FogFilter
        fog = new FogFilter();
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

        this.toggleBloomFilter();
    }
}
