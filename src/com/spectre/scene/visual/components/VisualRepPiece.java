/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.visual.components;

import com.simsilica.es.EntityComponent;

/**
 * Component used to represent the entity in the Viewport and which helps
 * determine if the model should be attached to the scene or model node
 *
 * @author Kyle D. Williams
 */
public class VisualRepPiece implements EntityComponent {

    private String assetName;
    private int type;

    /**
     * @param assetName The name of the model being represented in the Scene
     */
    public VisualRepPiece(String assetName) {
        this.assetName = assetName;
        this.type = VisualType.Character.ordinal();
    }

    /**
     * Used to determine the type of visual element this is defaults VisualType
     * to Character type
     *
     * @param assetName The name of the model being represented in the Scene
     */
    public VisualRepPiece(String assetName, VisualType type) {
        this.assetName = assetName;
        this.type = type.ordinal();
    }

    /**
     * The name of the model being represented in the Scene
     *
     * @return assetName String
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * @return the type of visual element the entity represents
     */
    public VisualType getVisualType() {
        return VisualType.values()[type];
    }

    @Override
    public String toString() {
        return "VisualRepPiece[AssetName=" + assetName + ", type=" + getVisualType() + "]";
    }

    public enum VisualType {

        Character, Scene;
    }
}
