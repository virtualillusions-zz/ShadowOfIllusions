/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract implementation of the Control interface.
 *
 * @author Kyle D. Williams
 */
public abstract class SpectreControl implements Control {

    protected static final Logger log = LoggerFactory.getLogger(SpectreControl.class.getName());
    protected boolean enabled = true;
    protected Spatial spatial;
    /**
     * Allows on the fly spatial switching
     */
    private boolean replaceSpatial = false;

    /**
     * Called Directly after setSpatial if spatial is not null
     */
    public abstract void SpectreControl();

    /**
     * called when removing control from spatial
     */
    public void cleanup() {
    }

    @Override
    public final void setSpatial(Spatial spatial) {
        if (this.spatial != null && spatial != null && spatial != this.spatial) {
            throw new IllegalStateException("This control has already been added to a Spatial");
        }
        this.spatial = spatial;
        if (spatial != null) {
            enabled = true;
            SpectreControl();
        } else if (!replaceSpatial) {
            enabled = false;
            cleanup();
        }
        /**
         * replace Spatial always gets set to false at the end regardless of
         * condition to prevent any issues
         */
        replaceSpatial = false;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * To be implemented in subclass.
     */
    protected void spectreUpdate(float tpf) {
    }

    /**
     * To be implemented in subclass.
     */
    protected void spectreRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Default implementation of cloneForSpatial() that simply clones the
     * control and sets the spatial.
     * <pre>
     *  AbstractControl c = clone();
     *  c.spatial = null;
     *  c.setSpatial(spatial);
     * </pre>
     *
     * Controls that wish to be persisted must be Cloneable.
     */
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        try {
            SpectreControl c = (SpectreControl) clone();
            c.spatial = null; // to keep setSpatial() from throwing an exception
            c.setSpatial(spatial);
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone control for spatial", e);
        }
    }

    @Override
    public final void update(float tpf) {
        if (!enabled) {
            return;
        }

        spectreUpdate(tpf);
    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {
        if (!enabled) {
            return;
        }

        spectreRender(rm, vp);
    }

    /**
     * Replaces this controls target spatial with a new one
     *
     * @param spat Spatial
     */
    public final void replaceSpatial(Spatial spat) {
        /**
         * TODO: Sensitive this is a hack hazard way of performing this to
         * prevent null pointers should reconsider at a later time
         */
        if (spatial != spat) {
            replaceSpatial = true;
            destroy();
            spat.addControl(this);
        }
    }

    /**
     * <b>Convience method</b> Remove this control from its parent Spatial
     */
    public final void destroy() {
        if (spatial != null) {
            enabled = false;
            spatial.removeControl(this);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        oc.write(spatial, "spatial", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        spatial = (Spatial) ic.readSavable("spatial", null);
    }
}
