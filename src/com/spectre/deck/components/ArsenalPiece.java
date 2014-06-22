/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.components;

import com.simsilica.es.EntityId;
import com.simsilica.es.PersistentComponent;
import com.simsilica.es.StringType;

/**
 * <pre>
 * <b>NickName:</b> Arsenal
 *
 * <b>Purpose:</b> Organizational Purposes<br/>
 *
 * <b>Description:</b> An ArsenalDeck contains no more than 40 cards and may
 * consist of 1-3 Different Card Series however the added bonus each series
 * grants is decreased with each additional series; Also each additional series
 * added to the ArsenalDeck decreases its capacity by 5 (ie: 2 Card Series: Max
 * Capacity 35, 3 Card Series: Max Capacity 30).
 *
 * A arsenal may contain a maximum of 3 same card or card evolutions per
 * arsenal; however Cards in the RAW Series are not counted and may spawn
 * randomly within the match
 *
 * When Talking about a ArsenalDeck it is first ordered by Series and then
 * by Trait. Each Card in the ArsenalDeck is called a flair and grants the user
 * the ability to use what is recorded on it for a specified amount of times. *
 *
 * <b>This Classes main function is
 * <code> createDeck </code></b>
 *
 * Consider: Readding a Dynamic Deck Remember: The Connection to Repository is
 * Implied when creating handler for the two their must be an implicit check
 * between the amount of cards available in Repository Deck and the Amount of
 * Cards Used in ArsenalDeck
 * </pre>
 *
 * Note: Creator ID refers to the player's entityID unlike ArsenalPiece's
 * parentID which refers to the ID of the entity the RepositoryPiece its
 * referencing belongs to
 *
 * @author Kyle D. Williams
 */
public class ArsenalPiece implements PersistentComponent {

    private EntityId parentId;
    private final int deckType;
    @StringType(maxLength = 80)
    private String arsenalName;

    public ArsenalPiece(String arsenalName, EntityId parentId) {
        this(arsenalName, parentId, DeckType.SINGLE_CAPACITY);
    }

    public ArsenalPiece(String nameOfArsenal, EntityId parentId, DeckType type) {
        this.arsenalName = nameOfArsenal;
        this.parentId = parentId;
        this.deckType = type.ordinal();
    }

    public String getArsenalName() {
        return arsenalName;
    }

    /**
     * The ID of the entity of the RepositoryPiece that this Arsenal Represents
     *
     * IE this entity is only a sub entity created to represent one of the
     * children of the repository which is a component held by another entity
     */
    public EntityId getParentId() {
        return parentId;
    }

    public DeckType getDeckType() {
        return DeckType.values()[deckType];
    }

    @Override
    public String toString() {
        return "ArsenalPiece[ArsenalName=" + getArsenalName()
                + " ParentId=" + getParentId()
                + ", DeckType=" + getDeckType() + "]";
    }

    /**
     * The enum DeckType is used to describe the type of deck being used
     */
    public enum DeckType {

        /**
         * <pre>
         * <b>SINGLE_CAPACITY</b>
         *      Series Limit: 1
         *      Card Capacity: 40 Card
         *      Spawn Speed: Fast
         * </pre>
         */
        SINGLE_CAPACITY,
        /**
         * <pre>
         * <b>DOUBLE_CAPACITY</b>
         *      Series Limit: 2
         *      Card Capacity: 35 Card Spawn
         *      Speed: Fast
         * </pre>
         */
        DOUBLE_CAPACITY,
        /**
         * <pre>
         * <b>TRIPLE_CAPACITY</b>
         *      Series Limit: 3
         *      Card Capacity: 30 Card Spawn
         *      Speed: Fast
         * </pre>
         */
        TRIPLE_CAPACITY;
    }
}
