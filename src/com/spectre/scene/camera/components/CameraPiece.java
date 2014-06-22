/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.scene.camera.components;

import com.simsilica.es.EntityComponent;

/**
 * Component to determine if the entity should be added to the Camera System
 *
 * @author Kyle D. Williams
 */
public class CameraPiece implements EntityComponent {

    private int camNum;

    public CameraPiece() {
        this(-1);
    }

    public CameraPiece(int camNum) {
        this.camNum = camNum;
    }

    public int getCamNum() {
        return camNum;
    }

    @Override
    public String toString() {
        return "CameraPiece[CamNum=" + camNum + "]";
    }
}
