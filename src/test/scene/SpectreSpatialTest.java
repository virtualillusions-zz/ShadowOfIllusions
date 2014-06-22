/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;

/**
 *
 * @author Kyle Williams
 */
public class SpectreSpatialTest extends SimpleApplication {

    public static void main(String[] args) {
        SpectreSpatialTest app = new SpectreSpatialTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        /**
         * Load a model. Uses model and texture from jme3-test-data library!
         */
        Node teapot = (SpectreSpatial) assetManager.loadModel("testData/Sinbad.j3o");
        rootNode.attachChild(teapot);
    }
}
