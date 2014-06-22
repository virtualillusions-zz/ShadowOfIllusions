/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.components;

import com.simsilica.es.CreatedBy;
import com.simsilica.es.EntityId;
import com.simsilica.es.StringType;

/**
 * <b>Purpose:</b> Organizational Purposes<br/>
 *
 * <b>Description:</b>Describe entity As Card and store's its creator's
 * entityId<br/>
 *
 * Note: Creator ID refers to the player's entityID unlike ArsenalPiece's
 * parentID which refers to the ID of the entity the RepositoryPiece its
 * referencing belongs to
 *
 * @author Kyle D. Williams
 */
public class CardPiece extends CreatedBy {

    @StringType(maxLength = 80)
    private String name;

    public CardPiece(String cardName, EntityId creatorId) {
        super(creatorId);
        this.name = cardName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CardPiece[CardName=" + getName() + ", CardCreatedBy=" + getCreatorId() + "]";
    }
}
