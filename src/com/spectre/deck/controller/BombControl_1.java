package com.spectre.deck.controller;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.spectre.deck.controller;
//
//import com.jme3.bullet.PhysicsSpace;
//import com.jme3.bullet.PhysicsTickListener;
//import com.jme3.bullet.collision.PhysicsCollisionEvent;
//import com.jme3.bullet.collision.PhysicsCollisionListener;
//import com.jme3.bullet.collision.PhysicsCollisionObject;
//import com.jme3.bullet.collision.shapes.CollisionShape;
//import com.jme3.bullet.collision.shapes.SphereCollisionShape;
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.bullet.objects.PhysicsGhostObject;
//import com.jme3.bullet.objects.PhysicsRigidBody;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.spectre.deck.Effect;
//import com.spectre.director.Director;
//import java.io.IOException;
//import java.util.Iterator;
//
///**
// *
// * @author normenhansen
// */
//public class BombControl extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {
//
//    private float explosionRadius = 10;
//    private PhysicsGhostObject ghostObject;
//    private Vector3f vector = new Vector3f();
//    private Vector3f vector2 = new Vector3f();
//    private float forceFactor = 1;
//    private EffectHandler effect;
//    private float fxTime = 0.5f;
//    private float maxTime = 4f;
//    private float curTime = -1.0f;
//    private float timer;
//Node effectNode;
//    public BombControl(CollisionShape shape, EffectHandler contactEffect, float mass) {
//        super(shape, mass);
//        effect = contactEffect;
//        createGhostObject();
//    }
//
//    @Override
//    public void setPhysicsSpace(PhysicsSpace space) {
//        super.setPhysicsSpace(space);
//        if (space != null) {
//            space.addCollisionListener(this);
//        }
//    }
//
//    private void createGhostObject() {
//        ghostObject = new PhysicsGhostObject(new SphereCollisionShape(explosionRadius));
//    }
//
//    @Override
//    public void collision(PhysicsCollisionEvent event) {
//        if (space == null) {
//            return;
//        }
//        if (event.getObjectA() != getUserObject() && event.getObjectB() != getUserObject()) {
//            if (event.getObjectA() == this || event.getObjectB() == this) {
//                space.add(ghostObject);
//                ghostObject.setPhysicsLocation(getPhysicsLocation(vector));
//                space.addTickListener(this);
//                if (effect != null && spatial.getParent() != null) {
//                    curTime = 0;
//                    effectNode = effect.play(spatial);
//
//                    effectNode.setLocalTranslation(spatial.getLocalTranslation());
//                    ((Node) spatial).attachChild(effectNode);
//                }
//                space.remove(this);
//                spatial.removeFromParent();
//            }
//        }
//    }
//
//    @Override
//    public void prePhysicsTick(PhysicsSpace space, float f) {
//        space.removeCollisionListener(this);
//    }
//
//    @Override
//    public void physicsTick(PhysicsSpace space, float f) {
//        //get all overlapping objects and apply impulse to them
//        for (Iterator<PhysicsCollisionObject> it = ghostObject.getOverlappingObjects().iterator(); it.hasNext();) {
//            PhysicsCollisionObject physicsCollisionObject = it.next();
//            if (physicsCollisionObject instanceof PhysicsRigidBody) {
//                PhysicsRigidBody pBody = (PhysicsRigidBody) physicsCollisionObject;
//                pBody.getPhysicsLocation(vector2);
//                vector2.subtractLocal(vector);
//                float force = explosionRadius - vector2.length();
//                force *= forceFactor;
//                force = force > 0 ? force : 0;
//                vector2.normalizeLocal();
//                vector2.multLocal(force);
//                ((PhysicsRigidBody) physicsCollisionObject).applyImpulse(vector2, Vector3f.ZERO);
//            }
//        }
//        space.removeTickListener(this);
//        space.remove(ghostObject);
//    }
//
//    @Override
//    public void update(float tpf) {
//        super.update(tpf);
//        if (enabled) {
//            timer += tpf;
//            if (timer > maxTime) {
//                if (spatial.getParent() != null) {
//                    space.removeCollisionListener(this);
//                    space.remove(this);
//                    spatial.removeFromParent();
//                }
//            }
//        }
//        if (enabled && curTime >= 0) {
//            curTime += tpf;
//            if (curTime > fxTime) {
//                curTime = -1;
//                effectNode.removeFromParent();
//            }
//        }
//    }
//
//    /**
//     * @return the explosionRadius
//     */
//    public float getExplosionRadius() {
//        return explosionRadius;
//    }
//
//    /**
//     * @param explosionRadius the explosionRadius to set
//     */
//    public void setExplosionRadius(float explosionRadius) {
//        this.explosionRadius = explosionRadius;
//        createGhostObject();
//    }
//
//    public float getForceFactor() {
//        return forceFactor;
//    }
//
//    public void setForceFactor(float forceFactor) {
//        this.forceFactor = forceFactor;
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        throw new UnsupportedOperationException("Reading not supported.");
//    }
//
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        throw new UnsupportedOperationException("Saving not supported.");
//    }
//}
