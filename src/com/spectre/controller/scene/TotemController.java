/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.controller.scene;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Transform;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.director.Director;

/**
 *
 * @author Kyle Williams
 */
public class TotemController extends com.jme3.scene.control.AbstractControl{
    //The physical representation of this player's controlled model
    private RigidBodyControl pSpatial;
    
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);        
        pSpatial = new RigidBodyControl(CollisionShapeFactory.createMeshShape(this.spatial),0);
        spatial.addControl(pSpatial);
    }

    public void activate(Node n, Transform t) {
        Spatial spat = spatial.clone();
        RigidBodyControl physics = spat.getControl(RigidBodyControl.class);
        physics.setPhysicsLocation(t.getTranslation());
        physics.setPhysicsRotation(t.getRotation());
        Director.getPhysicsSpace().add(physics);
        n.attachChild(spat);
    }
    
    public void deactivate(){
        pSpatial.destroy();
    }
    
    
    
    @Override
    protected void controlUpdate(float tpf) { }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    @Override
    public Control cloneForSpatial(Spatial spatial) {return this;}
}
