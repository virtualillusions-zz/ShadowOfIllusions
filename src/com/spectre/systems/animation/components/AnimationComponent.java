/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.animation.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author Kyle D. Williams
 */
public class AnimationComponent implements EntityComponent {

    private final String animName;
    private final int animPriority;
    private final float blendTime;

    public AnimationComponent(String animName, AnimPriority animPriority) {
        this(animName, animPriority, 0.15f);
    }

    public AnimationComponent(String animName, AnimPriority animPriority, float blendTime) {
        this.animName = animName;
        this.animPriority = animPriority.ordinal();
        this.blendTime = blendTime;
    }

    /**
     * @return the animName String
     */
    public String getAnimName() {
        return animName;
    }

    /**
     * @return the animPriority AnimPriority
     */
    public AnimPriority getAnimPriority() {
        return AnimPriority.values()[animPriority];
    }

    /**
     * @return the blendTime float
     */
    public float getBlendTime() {
        return blendTime;
    }

    @Override
    public String toString() {
        return "AnimationPiece[animName=" + animName + ", animPriority="
                + getAnimPriority() + ", blendTime" + blendTime + "]";
    }

    public enum AnimPriority {

        /**
         * Animation can change anytime Applies to: Idle, Basic Movements
         */
        Level_1,
        /**
         * Animation can only change with a call to interrupt Applies to:
         * Special Movements, Cards
         */
        Level_2;
    }
}
