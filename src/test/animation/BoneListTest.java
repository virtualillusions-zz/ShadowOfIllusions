/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.animation;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import com.spectre.util.anim.AnimUtil;

/**
 *
 * @author Kyle
 */
public class BoneListTest extends SimpleApplication {

    public static void main(String[] args) {
        BoneListTest app = new BoneListTest();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        Spatial spat = getAssetManager().loadModel("testData/Jaime/Jaime.j3o");

        for (String bone : AnimUtil.getBoneList(null, spat)) {
            System.out.println(bone);
        }

        stop();
    }
}
