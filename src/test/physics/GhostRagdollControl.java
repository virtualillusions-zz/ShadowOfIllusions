///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package test.physics;
//
//import com.jme3.animation.AnimControl;
//import com.jme3.animation.Bone;
//import com.jme3.animation.Skeleton;
//import com.jme3.asset.AssetManager;
//import com.jme3.bullet.PhysicsSpace;
//import com.jme3.bullet.collision.PhysicsCollisionEvent;
//import com.jme3.bullet.collision.PhysicsCollisionListener;
//import com.jme3.bullet.collision.PhysicsCollisionObject;
//import com.jme3.bullet.collision.RagdollCollisionListener;
//import com.jme3.bullet.collision.shapes.BoxCollisionShape;
//import com.jme3.bullet.collision.shapes.HullCollisionShape;
//import com.jme3.bullet.control.PhysicsControl;
//import com.jme3.bullet.objects.PhysicsGhostObject;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Mesh;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.VertexBuffer;
//import com.jme3.scene.control.AbstractControl;
//import com.jme3.util.TempVars;
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Kyle
// */
//class GhostRagdollControl extends AbstractControl implements PhysicsControl, PhysicsCollisionListener {
//
//    private static final Logger logger = Logger.getLogger(GhostRagdollControl.class.getName());
//    private Vector3f initScale;
//    private Skeleton skeleton;
//    private float weightThreshold = 0.5f;//-1.0f;
//    private PhysicsGhostObject rootBone;
//    private HashMap<String, PhysicsBoneLink> boneLinks = new HashMap<String, PhysicsBoneLink>();
//    protected List<RagdollCollisionListener> listeners;
//    private PhysicsSpace space;
//    private boolean debug = false;
//    private float eventDispatchImpulseThreshold = 10;
//
//    @Override
//    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
//        if (this.enabled == enabled) {
//            return;
//        }
//        this.enabled = enabled;
//        if (!enabled && space != null) {
//            removeFromPhysicsSpace();
//        } else if (enabled && space != null) {
//            addToPhysicsSpace();
//        }
//    }
//
//    @Override
//    protected void controlUpdate(float tpf) {
//        TempVars vars = TempVars.get();
//        Quaternion tmpRot = vars.quat1;
//        //the ragdoll does not have the controll, so the keyframed animation updates the physic position of the physic bonces
//        for (PhysicsBoneLink link : boneLinks.values()) {
//            Vector3f position = vars.vect1; //setting skeleton transforms to the ragdoll
//            //computing position from rotation and scale
//            spatial.getWorldTransform().transformVector(link.bone.getModelSpacePosition(), position);
//
//            //computing rotation
//            tmpRot.set(link.bone.getModelSpaceRotation()).multLocal(link.bone.getWorldBindInverseRotation());
//            spatial.getWorldRotation().mult(tmpRot, tmpRot);
//            tmpRot.normalizeLocal();
//
//            //updating physic location/rotation of the physic bone
//            link.ghostBone.setPhysicsLocation(position);
//            link.ghostBone.setPhysicsRotation(tmpRot);
//        }
//
//        vars.release();
//    }
//
//    @Override
//    protected void controlRender(RenderManager rm, ViewPort vp) {
//        if (enabled && space != null && space.getDebugManager() != null) {
//            if (!debug) {
//                attachDebugShape(space.getDebugManager());
//            }
//            for (Iterator<PhysicsBoneLink> it = boneLinks.values().iterator(); it.hasNext();) {
//                PhysicsBoneLink physicsBoneLink = it.next();
//                Spatial debugShape = physicsBoneLink.ghostBone.debugShape();
//                if (debugShape != null) {
//                    debugShape.setLocalTranslation(physicsBoneLink.ghostBone.getPhysicsLocation());
//                    debugShape.setLocalRotation(physicsBoneLink.ghostBone.getPhysicsRotation());
//                    debugShape.updateGeometricState();
//                    rm.renderScene(debugShape, vp);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void setSpatial(Spatial model) {
//        super.setSpatial(model);
//        clearData();
//        initScale = spatial.getLocalScale().clone();
//        scanSpatial();
//        logger.log(Level.INFO, "Created physics ghost skeleton for {0}", spatial.getName());
//    }
//
//    private void clearData() {
//        boneLinks.clear();
//        rootBone = null;
//    }
//
//    private void scanSpatial() {
//        getSkeleton().resetAndUpdate();
//        for (int i = 0; i < skeleton.getRoots().length; i++) {
//            Bone childBone = skeleton.getRoots()[i];
//            if (childBone.getParent() == null) {
//                logger.log(Level.INFO, "Found root bone in skeleton {0}", skeleton);
//                rootBone = new PhysicsGhostObject(new BoxCollisionShape(Vector3f.UNIT_XYZ.mult(0.1f)));
//                boneRecursion(childBone, rootBone);
//            }
//        }
//    }
//
//    private void boneRecursion(Bone bone, PhysicsGhostObject parent) {
//        PhysicsBoneLink link = new PhysicsBoneLink();
//        //creating the collision shape from the bone's associated vertices
//        link.bone = bone;
//        link.ghostBone = new PhysicsGhostObject(makeShape(link));
//        boneLinks.put(bone.getName(), link);
//        link.ghostBone.setUserObject(link);
//        for (Iterator<Bone> it = bone.getChildren().iterator(); it.hasNext();) {
//            Bone childBone = it.next();
//            boneRecursion(childBone, parent);
//        }
//    }
//
//    private HullCollisionShape makeShape(PhysicsBoneLink link) {
//        Bone bone = link.bone;
//        LinkedList<Integer> boneIndices = new LinkedList<Integer>();
//        boneIndices.add(skeleton.getBoneIndex(bone));
//
//        ArrayList<Float> points = new ArrayList<Float>();
//        if (spatial instanceof Geometry) {
//            Geometry g = (Geometry) spatial;
//            for (Integer index : boneIndices) {
//                points.addAll(getPoints(g.getMesh(), index, bone.getModelSpacePosition()));
//            }
//        } else if (spatial instanceof Node) {
//            Node node = (Node) spatial;
//            for (Spatial s : node.getChildren()) {
//                if (s instanceof Geometry) {
//                    Geometry g = (Geometry) s;
//                    for (Integer index : boneIndices) {
//                        points.addAll(getPoints(g.getMesh(), index, bone.getModelSpacePosition()));
//                    }
//                }
//            }
//        }
//
//        float[] p = new float[points.size()];
//        for (int i = 0; i < points.size(); i++) {
//            p[i] = points.get(i);
//        }
//
//        return new HullCollisionShape(p);
//    }
//
//    /**
//     * returns a list of points for the given bone
//     *
//     * @param mesh
//     * @param boneIndex
//     * @param offset
//     * @param link
//     * @return
//     */
//    private List<Float> getPoints(Mesh mesh, int boneIndex, Vector3f offset) {
//
//        FloatBuffer vertices = mesh.getFloatBuffer(VertexBuffer.Type.Position);
//        ByteBuffer boneIndices = (ByteBuffer) mesh.getBuffer(VertexBuffer.Type.BoneIndex).getData();
//        FloatBuffer boneWeight = (FloatBuffer) mesh.getBuffer(VertexBuffer.Type.BoneWeight).getData();
//
//        vertices.rewind();
//        boneIndices.rewind();
//        boneWeight.rewind();
//
//        ArrayList<Float> results = new ArrayList<Float>();
//
//        int vertexComponents = mesh.getVertexCount() * 3;
//
//        for (int i = 0; i < vertexComponents; i += 3) {
//            int k;
//            boolean add = false;
//            int start = i / 3 * 4;
//            for (k = start; k < start + 4; k++) {
//                if (boneIndices.get(k) == boneIndex && boneWeight.get(k) >= weightThreshold) {
//                    add = true;
//                    break;
//                }
//            }
//            if (add) {
//
//                Vector3f pos = new Vector3f();
//                pos.x = vertices.get(i);
//                pos.y = vertices.get(i + 1);
//                pos.z = vertices.get(i + 2);
//                pos.subtractLocal(offset).multLocal(initScale);
//                results.add(pos.x);
//                results.add(pos.y);
//                results.add(pos.z);
//
//            }
//        }
//
//        return results;
//    }
//
//    public void setPhysicsSpace(PhysicsSpace space) {
//        if (space == null) {
//            removeFromPhysicsSpace();
//            this.space = space;
//        } else {
//            if (this.space == space) {
//                return;
//            }
//            this.space = space;
//            addToPhysicsSpace();
//        }
//        this.space.addCollisionListener(this);
//    }
//
//    public PhysicsSpace getPhysicsSpace() {
//        return space;
//    }
//
//    public void collision(PhysicsCollisionEvent event) {
//        PhysicsCollisionObject objA = event.getObjectA();
//        PhysicsCollisionObject objB = event.getObjectB();
//
//        //excluding collisions that involve 2 parts of the ragdoll
//        if (event.getNodeA() == null && event.getNodeB() == null) {
//            return;
//        }
//
//        //discarding low impulse collision
//        if (event.getAppliedImpulse() < eventDispatchImpulseThreshold) {
//            return;
//        }
//
//        boolean hit = false;
//        Bone hitBone = null;
//        PhysicsCollisionObject hitObject = null;
//
//        //Computing which bone has been hit
//        if (objA.getUserObject() instanceof PhysicsBoneLink) {
//            PhysicsBoneLink link = (PhysicsBoneLink) objA.getUserObject();
//            if (link != null) {
//                hit = true;
//                hitBone = link.bone;
//                hitObject = objB;
//            }
//        }
//
//        if (objB.getUserObject() instanceof PhysicsBoneLink) {
//            PhysicsBoneLink link = (PhysicsBoneLink) objB.getUserObject();
//            if (link != null) {
//                hit = true;
//                hitBone = link.bone;
//                hitObject = objA;
//
//            }
//        }
//
//        //dispatching the event if the ragdoll has been hit
//        if (hit) {
//            for (RagdollCollisionListener listener : listeners) {
//                listener.collide(hitBone, hitObject, event);
//            }
//        }
//    }
//
//    private Skeleton getSkeleton() {
//        if (skeleton == null) {
//            AnimControl animControl = spatial.getControl(AnimControl.class);
//            skeleton = animControl.getSkeleton();
//        }
//        return skeleton;
//    }
//
//    private void addToPhysicsSpace() {
//        if (space == null) {
//            return;
//        }
//        if (rootBone != null) {
//            space.add(rootBone);
//        }
//        for (Iterator<PhysicsBoneLink> it = boneLinks.values().iterator(); it.hasNext();) {
//            PhysicsBoneLink physicsBoneLink = it.next();
//            if (physicsBoneLink.ghostBone != null) {
//                space.add(physicsBoneLink.ghostBone);
//            }
//        }
//    }
//
//    protected void removeFromPhysicsSpace() {
//        if (space == null) {
//            return;
//        }
//        if (rootBone != null) {
//            space.remove(rootBone);
//        }
//        for (Iterator<PhysicsBoneLink> it = boneLinks.values().iterator(); it.hasNext();) {
//            PhysicsBoneLink physicsBoneLink = it.next();
//            space.remove(physicsBoneLink.ghostBone);
//        }
//    }
//
//    protected void attachDebugShape(AssetManager manager) {
//        for (Iterator<PhysicsBoneLink> it = boneLinks.values().iterator(); it.hasNext();) {
//            PhysicsBoneLink physicsBoneLink = it.next();
//            physicsBoneLink.ghostBone.createDebugShape(manager);
//        }
//        debug = true;
//    }
//
//    protected void detachDebugShape() {
//        for (Iterator<PhysicsBoneLink> it = boneLinks.values().iterator(); it.hasNext();) {
//            PhysicsBoneLink physicsBoneLink = it.next();
//            physicsBoneLink.ghostBone.detachDebugShape();
//        }
//        debug = false;
//    }
//
//    /**
//     * add a
//     *
//     * @param listener
//     */
//    public void addCollisionListener(RagdollCollisionListener listener) {
//        if (listeners == null) {
//            listeners = new ArrayList<RagdollCollisionListener>();
//        }
//        listeners.add(listener);
//    }
//
//    private static class PhysicsBoneLink {
//
//        Bone bone;
//        PhysicsGhostObject ghostBone;
//    }
//
//    public static void main(String[] main) {
//        PhysicsCharacterHullCollisionTest app = new PhysicsCharacterHullCollisionTest();
//        app.start();
//    }
//}
