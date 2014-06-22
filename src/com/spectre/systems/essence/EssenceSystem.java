/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.systems.essence.components.*;
import com.spectre.systems.essence.components.CharacterStatePiece.CharacterState;
import java.util.Iterator;
import java.util.Set;

/**
 * REDO ITS BAD TO ACCESS STRAIGHT FROM THE ENTITY DATA CLASS
 *
 * @author Kyle D. Williams
 */
public class EssenceSystem extends SpectreAppState {
/////////REMEMBER TO ADD MODIFIER IN INPUT SEGMENT 

    private EntitySet essenceSet;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        this.essenceSet = getEntityData().getEntities(EssencePiece.class, InScenePiece.class);
    }

    @Override
    public void cleanUp() {
        this.essenceSet.release();
        this.essenceSet.applyChanges();
        remove(this.essenceSet);
    }

    @Override
    protected void spectreUpdate(float tpf) {
        if (essenceSet.applyChanges()) {
            add(essenceSet.getAddedEntities());
            remove(essenceSet.getRemovedEntities());
            add(essenceSet.getChangedEntities());//All I do is default components
        }
        essenceUpdate(tpf);
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            getEntityData().setComponents(id, new BeginingHealthPiece(),
                    new HealthPiece(),
                    new ReplenishRatePiece(),
                    new FocusLevelPiece(),
                    new FocusPiece(),
                    new PowerPiece(),
                    new AccuracyPiece(),
                    new DexterityPiece(),
                    new ConstitutionPiece(),
                    new IncreaseFocusRatePiece(),
                    new CharacterStatePiece(),
                    new ModifierButtonPiece());
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            getEntityData().removeComponent(id, BeginingHealthPiece.class);
            getEntityData().removeComponent(id, HealthPiece.class);
            getEntityData().removeComponent(id, ReplenishRatePiece.class);
            getEntityData().removeComponent(id, FocusLevelPiece.class);
            getEntityData().removeComponent(id, FocusPiece.class);
            getEntityData().removeComponent(id, PowerPiece.class);
            getEntityData().removeComponent(id, AccuracyPiece.class);
            getEntityData().removeComponent(id, DexterityPiece.class);
            getEntityData().removeComponent(id, ConstitutionPiece.class);
            getEntityData().removeComponent(id, IncreaseFocusRatePiece.class);
            getEntityData().removeComponent(id, CharacterStatePiece.class);
            getEntityData().removeComponent(id, ModifierButtonPiece.class);
        }
    }

    private void essenceUpdate(float tpf) {
        for (Iterator<Entity> it = essenceSet.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            int focus = getFocus(id);
            int focusLevel = getFocusLevel(id);
            if (focus < focusLevel) {
                float replenishRate = getReplenishRate(id);
                float cfiTime = getCurrentReplenishRate(id);
                if (cfiTime >= replenishRate) {
                    focus++;
                    getEntityData().setComponents(id, new FocusPiece(focus));
                    cfiTime = 0;
                } else {
                    cfiTime += tpf;
                    cfiTime *= isIncreaseFocusRate(id) ? getIncreaseFocusRate(id) : 1f;
                }
                getEntityData().setComponents(id, new ReplenishRatePiece(replenishRate, cfiTime));
            }

            CharacterState cS = CharacterState.Healthy;

            if (getHealth(id) <= getBeginingHealth(id) / 2) {
                cS = CharacterState.Injured;
            } else if (focus <= focusLevel) {
                cS = CharacterState.Tired;
            }

            getEntityData().setComponents(id, new CharacterStatePiece(cS));
        }
    }

    public int getAccuracy(EntityId id) {
        return getEntityData().getComponent(id, AccuracyPiece.class).getAccuracy();
    }

    public int getBeginingHealth(EntityId id) {
        return getEntityData().getComponent(id, BeginingHealthPiece.class).getBeginingHealth();
    }

    public int getConstitution(EntityId id) {
        return getEntityData().getComponent(id, ConstitutionPiece.class).getConstitution();
    }

    public int getDexterity(EntityId id) {
        return getEntityData().getComponent(id, DexterityPiece.class).getDexterity();
    }

    public int getFocusLevel(EntityId id) {
        return getEntityData().getComponent(id, FocusLevelPiece.class).getFocusLevel();
    }

    public int getFocus(EntityId id) {
        return getEntityData().getComponent(id, FocusPiece.class).getFocus();
    }

    public int getHealth(EntityId id) {
        return getEntityData().getComponent(id, HealthPiece.class).getHealth();
    }

    public int getPower(EntityId id) {
        return getEntityData().getComponent(id, PowerPiece.class).getPower();
    }

    public float getReplenishRate(EntityId id) {
        return getEntityData().getComponent(id, ReplenishRatePiece.class).getReplenishRate();
    }

    public float getCurrentReplenishRate(EntityId id) {
        return getEntityData().getComponent(id, ReplenishRatePiece.class).getCurrentReplenishRate();
    }

    public boolean isIncreaseFocusRate(EntityId id) {
        return getEntityData().getComponent(id, IncreaseFocusRatePiece.class).isIncreasedFocusRate();
    }

    public float getIncreaseFocusRate(EntityId id) {
        return getEntityData().getComponent(id, IncreaseFocusRatePiece.class).getIncreasedFocusRate();
    }

    public CharacterState getCharacterState(EntityId id) {
        return getEntityData().getComponent(id, CharacterStatePiece.class).getType();
    }

    public boolean getModifierButton(EntityId id) {
        return getEntityData().getComponent(id, ModifierButtonPiece.class).getModifierButton();
    }
}
