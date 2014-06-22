/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated.com.spectre.physics;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.filter.FieldFilter;
import deprecated.com.spectre.app.SpectreApplication;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.camera.components.CameraDirectionPiece;
import deprecated.com.spectre.scene.camera.components.CameraLeftPiece;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.scene.visual.components.VisualRepPiece;
import com.spectre.app.input.Buttons;
import com.spectre.app.input.InputSpectreAppState;
import com.spectre.scene.visual.components.ActionModePiece;
import com.spectre.util.math.MathUtil;
import com.spectre.util.math.Vector3fPiece;
import deprecated.com.spectre.physics.components.DashPiece;
import deprecated.com.spectre.physics.components.DirectionForwardPiece;
import deprecated.com.spectre.physics.components.DirectionLeftPiece;
import deprecated.com.spectre.physics.components.EvadePiece;
import deprecated.com.spectre.physics.components.JumpPiece;
import deprecated.com.spectre.physics.components.PhysicsDampingPiece;
import deprecated.com.spectre.physics.components.RigidBodyLocationPiece;
import deprecated.com.spectre.physics.components.ViewDirectionPiece;
import deprecated.com.spectre.physics.components.WalkDirectionPiece;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Kyle
 */
public abstract class AbstractCharacterPhysicsSystem extends InputSpectreAppState implements PhysicsTickListener, PhysicsCollisionListener {

    protected EntitySet physicsDataSet;//HAS TO BE DONE THIS WAY TO MAINTAIN THREAD SAFETY
    protected EntitySet camSet;//MUST BE CREATED SEPERATE SINCE AI MAY NOT HAVE PIECES READILY
    protected HashBasedTable<EntityId, Integer, Object> physicsData;
    protected boolean applyLocal = false;
    protected PhysicsSpace physicsSpace;
    private HashMap<EntityId, Spatial> modelBindingsList;
    private InputManager inputManager;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        ComponentFilter filter = FieldFilter.create(VisualRepPiece.class, "type", VisualRepPiece.VisualType.Character);
        this.mainSet = getEntityData().getEntities(filter, VisualRepPiece.class, InScenePiece.class);
        this.physicsDataSet = getEntityData().getEntities(ActionModePiece.class, DashPiece.class,
                DirectionForwardPiece.class, DirectionLeftPiece.class, EvadePiece.class,
                JumpPiece.class, PhysicsDampingPiece.class, RigidBodyLocationPiece.class,
                ViewDirectionPiece.class, WalkDirectionPiece.class);
        this.camSet = getEntityData().getEntities(CameraDirectionPiece.class, CameraLeftPiece.class);
        //at least 4 players and 16 variables in total
        this.physicsData = HashBasedTable.create(4, COMP_END - 1);
        this.physicsSpace = sAppState.getPhysicsSpace();
        this.modelBindingsList = sAppState.getModelBindingsList();
        this.inputManager = sAppState.getInputManager();
        this.physicsSpace.getGravity(defaultLocalUp).normalizeLocal().negateLocal();
    }

    //FUNCTIONS
    public boolean isApplyPhysicsLocal() {
        return applyLocal;
    }

    /**
     * When set to true, the physics coordinates will be applied to the local
     * translation of the Spatial
     *
     * @param applyPhysicsLocal
     */
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        applyLocal = applyPhysicsLocal;
    }

    /**
     * <code>getSpatialTranslation</code> retrieves the absolute translation of
     * the Spatial.
     *
     * @return the Spatial's world translation quaternion.
     */
    protected Vector3f getSpatialTranslation(EntityId id) {
        if (applyLocal) {
            return ((Spatial) physicsData.get(id, SPATIAL)).getLocalTranslation();
        }
        return ((Spatial) physicsData.get(id, SPATIAL)).getWorldTranslation();
    }

    /**
     * <code>getSpatialRotation</code> retrieves the absolute rotation of the
     * Spatial.
     *
     * @return the Spatial's world rotation quaternion.
     */
    protected Quaternion getSpatialRotation(EntityId id) {
        if (applyLocal) {
            return ((Spatial) physicsData.get(id, SPATIAL)).getLocalRotation();
        }
        return ((Spatial) physicsData.get(id, SPATIAL)).getWorldRotation();
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * spatial is attached for example.
     *
     * @param id
     * @param vec
     */
    public void setPhysicsLocation(EntityId id, Vector3f vec) {
        PhysicsRigidBody rgb = get(id, RIGID_BODY);
        rgb.setPhysicsLocation(vec);
        //Set Variables
        set(id, RIGID_BODY_LOCATION_V, vec);
    }

    /**
     * This is implemented from AbstractPhysicsControl and called when the
     * spatial is attached for example. We don't set the actual physics rotation
     * but the view rotation here. It might actually be altered by the
     * calculateNewForward method.
     *
     * @param id
     * @param quat
     */
    public void setPhysicsRotation(EntityId id, Quaternion quat) {
        Vector3f viewDirection = get(id, VIEW_DIRECTION_V);
        Quaternion rotation = get(id, ROTATION_Q);
        rotation.set(quat);
        rotation.multLocal(viewDirection);
        //SET VARIABLES
        set(id, ROTATION_Q, rotation);
        updateLocalViewDirection(id);
    }

    /**
     * Updates the local coordinate system from the localForward and localUp
     * vectors, adapts localForward, sets localForwardRotation quaternion to
     * local z-forward rotation.
     *
     * ALTERS & SETS ITS OWN VALUES
     */
    protected void updateLocalCoordinateSystem(EntityId id) {
        Quaternion localForwardRotation = get(id, LOCAL_FORWARD_ROTATION_Q);
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        Vector3f localLeft = get(id, LOCAL_LEFT_V);
        Vector3f localForward = get(id, LOCAL_FORWARD_V);
        Vector3f localUp = get(id, LOCAL_UP_V);
        //gravity vector has possibly changed, calculate new world forward (UNIT_Z)
        calculateNewForward(localForwardRotation, localForward, localUp);
        localLeft.set(localUp).crossLocal(localForward);
        rigidBody.setPhysicsRotation(localForwardRotation);
        //Set Variables
        updateLocalViewDirection(id);
    }

    /**
     * Updates the local x/z-flattened view direction and the corresponding
     * rotation quaternion for the spatial.
     *
     * ALTERS & SETS ITS OWN VALUES
     */
    protected void updateLocalViewDirection(EntityId id) {
        Quaternion localForwardRotation = get(id, LOCAL_FORWARD_ROTATION_Q);
        Vector3f viewDirection = get(id, VIEW_DIRECTION_V);
        Quaternion rotation = get(id, ROTATION_Q);
        Vector3f localUp = get(id, LOCAL_UP_V);
        //update local rotation quaternion to use for view rotation
        localForwardRotation.multLocal(viewDirection);
        calculateNewForward(rotation, viewDirection, localUp);
    }

    /**
     * This method works similar to Camera.lookAt but where lookAt sets the
     * priority on the direction, this method sets the priority on the up vector
     * so that the result direction vector and rotation is guaranteed to be
     * perpendicular to the up vector.
     *
     * ALTERS VALUES
     *
     * @param rotation The rotation to set the result on or null to create a new
     * Quaternion, this will be set to the new "z-forward" rotation if not null
     * @param direction The direction to base the new look direction on, will be
     * set to the new direction
     * @param worldUpVector The up vector to use, the result direction will be
     * perpendicular to this
     * @return
     */
    protected void calculateNewForward(Quaternion rotation, Vector3f direction, Vector3f worldUpVector) {
        if (direction == null) {
            return;
        }
        TempVars vars = TempVars.get();
        Vector3f newLeft = vars.vect1;
        Vector3f newLeftNegate = vars.vect2;

        newLeft.set(worldUpVector).crossLocal(direction).normalizeLocal();
        if (newLeft.equals(Vector3f.ZERO)) {
            if (direction.x != 0) {
                newLeft.set(direction.y, -direction.x, 0f).normalizeLocal();
            } else {
                newLeft.set(0f, direction.z, -direction.y).normalizeLocal();
            }
            SpectreApplication.logger.log(Level.INFO, "Zero left for direction {0}, up {1}",
                    new Object[]{direction, worldUpVector});
        }
        newLeftNegate.set(newLeft).negateLocal();
        direction.set(worldUpVector).crossLocal(newLeftNegate).normalizeLocal();
        if (direction.equals(Vector3f.ZERO)) {
            direction.set(Vector3f.UNIT_Z);
            SpectreApplication.logger.log(Level.INFO, "Zero left for left {0}, up {1}",
                    new Object[]{newLeft, worldUpVector});
        }
        if (rotation != null) {
            rotation.fromAxes(newLeft, worldUpVector, direction);
        }
        vars.release();
    }

    @Override
    public void setEnabled(boolean active) {
        super.setEnabled(active);
        if (physicsSpace != null) {
            for (Iterator<Object> it = physicsData.column(RIGID_BODY).values().iterator(); it.hasNext();) {
                PhysicsRigidBody prb = (PhysicsRigidBody) it.next();
                if (active) {
                    physicsSpace.add(prb);
                } else {
                    physicsSpace.remove(prb);
                }
            }
        }
    }

    public void removeAllProperties(EntityId id) {
        Entity e = physicsDataSet.getEntity(id);
        for (EntityComponent ec : e.getComponents()) {
            getEntityData().removeComponent(id, ec.getClass());
        }
        physicsData.rowMap().remove(id);
    }

//////UPDATE
    @Override
    protected void spectreUpdate(float tpf) {
        if (mainSet.applyChanges()) {
            add(mainSet.getAddedEntities());
            remove(mainSet.getRemovedEntities());
            change(mainSet.getChangedEntities());
        }
        //UPDATE EVERY FRAME
        camSet.applyChanges();

        TempVars vars = TempVars.get();
        for (Iterator<Entity> it = mainSet.iterator(); it.hasNext();) {
            spectreUpdate(it.next().getId(), vars, tpf);
        }
        vars.release();

        //THINK IN TERMS THAT MAIN THREAD IS USER THREAD SO
        //UPDATE PROPERTY SET ACCORDINGLY
        updateComponents();
    }

    protected abstract void spectreUpdate(EntityId id, TempVars vars, float tpf);

    /**
     * Applies a physics transform to the spatial
     *
     * ALTERS VALUES SENT
     *
     * @param worldLocation
     * @param worldRotation
     */
    protected void applyPhysicsTransform(EntityId id, Vector3f worldLocation, Quaternion worldRotation) {
        Spatial spatial = ((Spatial) physicsData.get(id, SPATIAL));
        if (isEnabled() && spatial != null) {
            Vector3f localLocation = spatial.getLocalTranslation();
            Quaternion localRotationQuat = spatial.getLocalRotation();
            if (!applyLocal && spatial.getParent() != null) {
                localLocation.set(worldLocation).subtractLocal(spatial.getParent().getWorldTranslation());
                localLocation.divideLocal(spatial.getParent().getWorldScale());
                //tmp_inverseWorldRotation.set(spatial.getParent().getWorldRotation()).inverseLocal().multLocal(localLocation);
                localRotationQuat.set(worldRotation);
                //tmp_inverseWorldRotation.set(spatial.getParent().getWorldRotation()).inverseLocal().mult(localRotationQuat, localRotationQuat);

                spatial.setLocalTranslation(localLocation);
                spatial.setLocalRotation(localRotationQuat);
            } else {
                spatial.setLocalTranslation(worldLocation);
                spatial.setLocalRotation(worldRotation);
            }
        }

    }

    private void add(Entity entity) {
        EntityId id = entity.getId();
        //SET COMPONENTS
        entity.set(new DashPiece());
        entity.set(new DirectionForwardPiece());
        entity.set(new DirectionLeftPiece());
        entity.set(new EvadePiece());
        entity.set(new JumpPiece());
        entity.set(new PhysicsDampingPiece());
        entity.set(new RigidBodyLocationPiece());
        entity.set(new ViewDirectionPiece());
        entity.set(new WalkDirectionPiece());
        //ADD FOR TESTING PURPOSES CAN LEAVE SINCE INITIAL SHOULD BE FALSE ANYWAY
        entity.set(new ActionModePiece());
        //SET TABLE
        Spatial spat = modelBindingsList.get(id);
        BoundingBox bb = (BoundingBox) spat.getWorldBound();
        //use smaller side extent over larger side to create a more narrow hitbox 
        //and because A-Pose and T-Pose difference greatly impacts value
        float radius = bb.getXExtent() < bb.getZExtent() ? bb.getXExtent() : bb.getZExtent();
        radius /= 2;
        float height = bb.getYExtent() * 2;
        float mass = 1 * bb.getVolume();//random

        //     create collision shapes and wrap them in compound shape to help with offset
        CompoundCollisionShape ccs = new CompoundCollisionShape();
        CollisionShape shape = new CapsuleCollisionShape(radius, height - (2 * radius));//standing     
        ccs.addChildShape(shape, new Vector3f(0, height / 2.0f, 0));
        shape = ccs;

        ccs = new CompoundCollisionShape();
        CollisionShape shape2 = new CapsuleCollisionShape(radius, ((height * duckedFactor) - (2 * radius)));//ducked    
        ccs.addChildShape(shape2, new Vector3f(0, (height * duckedFactor) / 2.0f, 0));
        shape2 = ccs;

        PhysicsRigidBody rigidBody = new PhysicsRigidBody(shape, mass);
        rigidBody.setAngularFactor(0);
        //createSpatialData
        rigidBody.setUserObject(spat);

        this.set(id, RIGID_BODY, rigidBody);
        this.set(id, SPATIAL, spat);
        this.set(id, NORM_SHAPE, shape);
        this.set(id, DUCK_SHAPE, shape2);
        this.set(id, HEIGHT_F, height);
        this.set(id, RADIUS_F, radius);
        this.set(id, MASS_F, mass);
        this.set(id, AIR_TIME_F, 0.0f);
        this.set(id, ALTITUDE_F, 0.0f);
        this.set(id, AUXILLARY_VEC, new Vector3f());
        this.set(id, LOCAL_FORWARD_ROTATION_Q, new Quaternion(Quaternion.DIRECTION_Z));
        this.set(id, ROTATION_Q, new Quaternion(Quaternion.DIRECTION_Z));
        this.set(id, VELOCITY_V, new Vector3f());
        this.set(id, DUCKED_B, false);
        this.set(id, ON_GROUND_B, false);
        this.set(id, WANT_TO_UNDUCK_B, false);
        this.set(id, LOCAL_LEFT_V, new Vector3f(1, 0, 0));
        this.set(id, LOCAL_UP_V, defaultLocalUp.clone());
        this.set(id, LOCAL_FORWARD_V, new Vector3f(0, 0, 1));
        //update table early to prevent any null pointers
        this.updateTable();

        //ADD AND CONFIGURE PHYSICS BODY
        updateLocalCoordinateSystem(id);
        physicsSpace.addCollisionObject(rigidBody);
        setPhysicsLocation(id, getSpatialTranslation(id));
        setPhysicsRotation(id, getSpatialRotation(id));

        //SET UP INPUTMANAGER
        //REMEMBER: this never gets removed by the entity
        inputManager.addListener(this, Buttons.getPhysicsButtons(id));

        SpectreApplication.logger.log(Level.INFO, "Created Collision Object/s for {0}", spat.getName());
    }

    private void remove(Entity entity) {
        EntityId id = entity.getId();
        PhysicsRigidBody rigidBody = get(id, RIGID_BODY);
        //removeSpatialData
        rigidBody.setUserObject(null);
        String name = ((Spatial) get(id, SPATIAL)).getName();
        //remove body from space
        physicsSpace.remove(rigidBody);
        //REMOVE ALL DATA
        removeAllProperties(id);
        SpectreApplication.logger.log(Level.INFO, "Removed Collision Object/s for {0}", name);
    }

    protected void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            add(it.next());
        }
    }

    protected void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            remove(it.next());
        }
    }

    protected void change(Set<Entity> changedEntities) {
        for (Iterator<Entity> it = changedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            remove(e);
            add(e);
        }
    }

    /**
     * Used internally, don't call manually
     *
     * @param space
     * @param tpf
     */
    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        TempVars vars = TempVars.get();
        for (Iterator<Entity> it = mainSet.iterator(); it.hasNext();) {
            physicsTick(it.next().getId(), vars, tpf);
        }
        vars.release();
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        //MAIN THREAD IS USER THREAD STAY IN SYNC WITH USER THREAD
        updateTable();

        TempVars vars = TempVars.get();
        for (Iterator<Entity> it = mainSet.iterator(); it.hasNext();) {
            prePhysicsTick(it.next().getId(), vars, tpf);
        }
        vars.release();
    }

    protected abstract void physicsTick(EntityId id, TempVars vars, float tpf);

    protected abstract void prePhysicsTick(EntityId id, TempVars vars, float tpf);

    /**
     * Updates properties table
     */
    private void updateTable() {
        //UPDATE DATASET BEFORE UPDATING TABLE TO KEEP SYNC WITH CHANGES IN TABLE
        //THIS THREAD MOVES QUICKER BUT APPLYCHANGES IS A FREE CALL
        boolean changes = physicsDataSet.applyChanges();
        if (changes == true) {
            for (Iterator<Entity> it = physicsDataSet.iterator(); it.hasNext();) {
                EntityId id = it.next().getId();
                for (int i = COMP_START; i < COMP_START_VEC; i++) {
                    Object o = this.getComp(id, i);
                    this.set(id, i, o);
                }
                for (int i = COMP_START_VEC; i < COMP_END; i++) {
                    Vector3f v = this.get(id, i);
                    this.getCompVec(id, i, v);
                }
            }
        }
    }

    /**
     * Updates all entity components
     */
    private void updateComponents() {
        for (Iterator<Entity> it = physicsDataSet.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            for (int i = COMP_START; i < COMP_END; i++) {
                Object o = physicsData.get(id, i);
                this.setComp(id, i, o);
            }
        }
    }
//////GETTERS&SETTERS

    public boolean getActionMode(EntityId id) {
        Entity e = physicsDataSet.getEntity(id);
        if (e != null) {
            return e.get(ActionModePiece.class).isActionMode();
        }
        return false;
    }

    public Vector3f getCameraDirection(EntityId id, Vector3f tempVec) {
        Entity e = camSet.getEntity(id);
        if (e != null) {
            Vector3fPiece v = e.get(CameraDirectionPiece.class);
            return MathUtil.pieceToVec(tempVec, v);
        }
        tempVec.zero();
        tempVec.setZ(1f);
        return tempVec;
    }

    public Vector3f getCameraLeft(EntityId id, Vector3f tempVec) {
        Entity e = camSet.getEntity(id);
        if (e != null) {
            Vector3fPiece v = e.get(CameraLeftPiece.class);
            return MathUtil.pieceToVec(tempVec, v);
        }
        tempVec.zero();
        tempVec.setX(1f);
        return tempVec;
    }

    /**
     *
     * NOTE: THE WAY IN WHICH TABLE PROPERTIES ARE HANDLED IS INCREDIBLY UNSAFE.
     * PLEASE KEEP TABS ON THE VALUES YOU ARE GETTING
     *
     * @param <T>
     * @param id
     * @param value
     * @return
     */
    protected <T> T get(EntityId id, int value) {
        T t = (T) physicsData.get(id, value);
        if (t == null) {
            if (value >= COMP_START && value < COMP_START_VEC) {
                t = getComp(id, value);
            } else if (value >= COMP_START_VEC && value < COMP_END) {
                t = (T) getCompVec(id, value, new Vector3f());
            } else {
                throw new UnsupportedOperationException("ERROR GETTING VARIABLE: " + value);
            }
            set(id, value, t);
        }
        return t;
    }

    /**
     *
     * NOTE: THE WAY IN WHICH TABLE PROPERTIES ARE HANDLED IS INCREDIBLY UNSAFE.
     * PLEASE KEEP TABS ON THE VALUES YOU ARE SETTING
     *
     * @param id
     * @param value
     * @param o
     */
    protected void set(EntityId id, int value, Object o) {
        physicsData.put(id, value, o);
    }

    private <T> T getComp(EntityId id, int value) {
        Entity e = physicsDataSet.getEntity(id);
        Preconditions.checkNotNull(e, "ID not found in PhysicsPropertiesSet");
        switch (value) {
            case DASH_B: {
                Boolean b = e.get(DashPiece.class).getIsDash();
                return (T) b;
            }
            case DIRECTION_FORWARD_F: {
                Float i = e.get(DirectionForwardPiece.class).getDirectionForward();
                return (T) i;
            }
            case DIRECTION_LEFT_F: {
                Float i = e.get(DirectionLeftPiece.class).getDirectionLeft();
                return (T) i;
            }
            case EVADE_B: {
                Boolean b = e.get(EvadePiece.class).getIsEvade();
                return (T) b;
            }
            case JUMP_B: {
                Boolean b = e.get(JumpPiece.class).getIsJump();
                return (T) b;
            }
            case PHYSICS_DAMPING_F: {
                Float f = e.get(PhysicsDampingPiece.class).getPhysicsDamping();
                return (T) f;
            }
            case RIGID_BODY_LOCATION_V:
            case VIEW_DIRECTION_V:
            case WALK_DIRECTION_V: {
                throw new UnsupportedOperationException("USING INCORRECT METHOD IN GET STATEMENT OF "
                        + this.getClass().getSimpleName() + "THESE REQUIRE TEMP VECTORS");
            }
            default:
                return null;

        }
    }

    private Vector3f getCompVec(EntityId id, int num, Vector3f tempVec) {
        Entity e = physicsDataSet.getEntity(id);
        Preconditions.checkNotNull(e, "ID not found in PhysicsPropertiesSet");
        Vector3fPiece v = null;
        switch (num) {
            case RIGID_BODY_LOCATION_V:
                v = e.get(RigidBodyLocationPiece.class);
                break;
            case VIEW_DIRECTION_V:
                v = e.get(ViewDirectionPiece.class);
                break;
            case WALK_DIRECTION_V:
                v = e.get(WalkDirectionPiece.class);
                break;
        }
        if (v == null) {
            throw new UnsupportedOperationException("USING INCORRECT METHOD IN GET STATEMENT OF "
                    + this.getClass().getSimpleName() + "THIS VARIABLE DOES NOT REQUIRE TEMP VECTORS");
        }
        return MathUtil.pieceToVec(tempVec, v);
    }

    private void setComp(EntityId id, int value, Object o) {
        Entity e = this.physicsDataSet.getEntity(id);
        Preconditions.checkNotNull(e, "ID not found in PhysicsPropertiesSet");
        switch (value) {
            case DASH_B: {
                if (!o.equals(e.get(DashPiece.class).getIsDash())) {
                    e.set(new DashPiece((Boolean) o));
                }
                break;
            }
            case DIRECTION_FORWARD_F: {
                if (!o.equals(e.get(DirectionForwardPiece.class).getDirectionForward())) {
                    e.set(new DirectionForwardPiece((Float) o));
                }
                break;
            }
            case DIRECTION_LEFT_F: {
                if (!o.equals(e.get(DirectionLeftPiece.class).getDirectionLeft())) {
                    e.set(new DirectionLeftPiece((Float) o));
                }
                break;
            }
            case EVADE_B: {
                if (!o.equals(e.get(EvadePiece.class).getIsEvade())) {
                    e.set(new EvadePiece((Boolean) o));
                }
                break;
            }
            case JUMP_B: {
                if (!o.equals(e.get(JumpPiece.class).getIsJump())) {
                    e.set(new JumpPiece((Boolean) o));
                }
                break;
            }
            case PHYSICS_DAMPING_F: {
                if (!o.equals(e.get(PhysicsDampingPiece.class).getPhysicsDamping())) {
                    e.set(new PhysicsDampingPiece((Float) o));
                }
                break;
            }
            case RIGID_BODY_LOCATION_V: {
                Vector3f v = (Vector3f) o;
                RigidBodyLocationPiece p = e.get(RigidBodyLocationPiece.class);
                if (!(p.getX() == v.x
                        && p.getY() == v.y
                        && p.getZ() == v.z)) {
                    e.set(new RigidBodyLocationPiece(v.x, v.y, v.z));
                }
                break;
            }
            case VIEW_DIRECTION_V: {
                Vector3f v = (Vector3f) o;
                ViewDirectionPiece p = e.get(ViewDirectionPiece.class);
                if (!(p.getX() == v.x
                        && p.getY() == v.y
                        && p.getZ() == v.z)) {
                    e.set(new ViewDirectionPiece(v.x, v.y, v.z));
                }
                break;
            }
            case WALK_DIRECTION_V: {
                Vector3f v = (Vector3f) o;
                WalkDirectionPiece p = e.get(WalkDirectionPiece.class);
                if (!(p.getX() == v.x
                        && p.getY() == v.y
                        && p.getZ() == v.z)) {
                    e.set(new WalkDirectionPiece(v.x, v.y, v.z));
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("UNABLE TO LOCATE VALUE FOUND IN PARAMETER int value ");
            }
        }
    }
/////TABLE VALUES/////// ENDING INDICATES primative or object
    protected static final int RIGID_BODY = 0;
    protected static final int SPATIAL = 1;
    protected static final int NORM_SHAPE = 2;
    protected static final int DUCK_SHAPE = 3;
    protected static final int HEIGHT_F = 4;
    protected static final int RADIUS_F = 5;
    protected static final int MASS_F = 6;
    protected static final int AIR_TIME_F = 7;
    /**
     * Calculate altitude, used specifically to check if jumping or falling
     */
    protected static final int ALTITUDE_F = 8;
    protected static final int AUXILLARY_VEC = 9;
    /**
     * Local Z-Forward Quaternion for the local absolute z-forward direction
     */
    protected static final int LOCAL_FORWARD_ROTATION_Q = 10;
    /**
     * Stores final spatial rotation, is a z-forward rotation based on the view
     * direction and the current local x/z plane. See also rotatedViewDirection.
     */
    protected static final int ROTATION_Q = 11;
    protected static final int VELOCITY_V = 12;
    protected static final int DUCKED_B = 13;
    protected static final int ON_GROUND_B = 14;
    protected static final int WANT_TO_UNDUCK_B = 15;
    /**
     * Local left direction, derived from up and forward.
     */
    protected static final int LOCAL_LEFT_V = 16;
    /**
     * Local up direction, derived from gravity.
     */
    protected static final int LOCAL_UP_V = 17;
    /**
     * Local absolute z-forward direction, derived from gravity and UNIT_Z,
     * updated continuously when gravity changes.
     */
    protected static final int LOCAL_FORWARD_V = 18;
///COMPONENT VALUES///////  
    private static final int COMP_START = 19;//MARKS THE BEGINING OF COMPONENTS
    protected static final int DASH_B = 19;
    protected static final int DIRECTION_FORWARD_F = 20;
    protected static final int DIRECTION_LEFT_F = 21;
    protected static final int EVADE_B = 22;
    protected static final int JUMP_B = 23;
    protected static final int PHYSICS_DAMPING_F = 24;
    protected static final int COMP_START_VEC = 25;//MARKS THE BEGINING OF VEC COMPONENTS
    /**
     * VEC instead of V indicates uses get(id,i,vec)
     */
    protected static final int RIGID_BODY_LOCATION_V = 25;
    /**
     * VEC instead of V indicates uses get(id,i,vec)
     */
    protected static final int VIEW_DIRECTION_V = 26;
    /**
     * VEC instead of V indicates uses get(id,i,vec)
     */
    protected static final int WALK_DIRECTION_V = 27;
    private static final int COMP_END = 28;//MARKS THE END OF COMPONENTS
//////FINAL VALUES////////   
    protected final int BASIC_MAX_SPEED = 50;
    protected final float runningSpeed = 2.00f;
    protected final float joggingSpeed = 0.50f;
    protected final float walkingSpeed = 0.25f;
    protected final float duckedFactor = 0.4f;
    private final Vector3f defaultLocalUp = new Vector3f(0, 1, 0);
////CLEANUP

    @Override
    public void cleanUp() {
        this.physicsData.clear();
        this.physicsData = null;
        this.physicsDataSet.release();
        this.physicsDataSet.applyChanges();
        this.physicsDataSet = null;
        this.mainSet.release();
        this.mainSet.applyChanges();
        this.mainSet = null;
        this.inputManager.removeListener(this);
        this.physicsSpace.removeTickListener(this);
        this.physicsSpace.removeCollisionListener(this);
        inputManager = null;
        physicsSpace = null;
    }
}
