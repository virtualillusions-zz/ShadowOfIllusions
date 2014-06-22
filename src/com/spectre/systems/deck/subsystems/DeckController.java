/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.deck.subsystems;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.filter.FieldFilter;
import com.spectre.app.SpectreControl;
import com.spectre.deck.components.ArsenalPiece;
import com.spectre.deck.components.ArsenalPiece.DeckType;
import com.spectre.deck.components.CardExperiencePiece;
import com.spectre.deck.components.CardPiece;
import com.spectre.deck.components.RepositoryPiece;
import java.util.Iterator;
import java.util.Set;

/**
 * <b>NickName:</b> DeckController<br/>
 *
 * <b>Purpose:</b> Organize Cards<br/>
 *
 * <b>Description:</b>A defacto Deck handling Systems that attaches the base
 * PhysicsControl to a spatial
 *
 * The general flow is the player creates decks/arsenals from cards left in the
 * repository these decks are replicated only through name reference during play
 * and updated through its links ie createdBy/CardPiece
 *
 * Cards are first in Repository as a whole These Cards Contain CardPiece,
 * CardExperiencePiece and RepositoryPiece(These group cards)
 *
 * Cards Then are organized into Arsenals/Decks, the ammounts depending on the
 * cardCount/cardsUsed in RepositoryPiece The cards in Arsenal are not grouped
 * and if their is 3 of a type of card unlike in the repo they appear 3 times.
 * These Entities Contain CardPiece and ArsenalPiece
 *
 * Cards Added to the game are those from the arsenals and contain
 *
 * @author Kyle D. Williams
 */
public class DeckController extends SpectreControl {

//MOVE TO DEDICATED CONTROLLER FOR CLARITY
//Controller(EntityId id, InputManager inputManager, EntityData ed)
    /**
     * The cards that are currently in the player's hand
     */
    //private EntityId[] hand;
    //private EntityId deck
    /**
     * The current active deck of the player Should be recreated each match
     */
//    private String activeDeck;
//
//    public void setActionMode(boolean actionMode) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }  
//    public void setArsenal(String name) {
//        Preconditions.checkArgument(arsenals.containsKey(name), "Arsenal Name not found");
//        activeDeck = name;
//    }
////    
    private EntityData ed;
    private EntitySet arsenalSet;
    private EntitySet repoSet;
    private EntityId playerId;
    /**
     * A map of arsenals that the player has created
     */
    private HashMultimap<String, EntityId> arsenals;

    public DeckController(EntityId id, EntityData ed) {
        this.ed = ed;
        this.playerId = id;
        ComponentFilter defaultFilter = FieldFilter.create(CardPiece.class, "creatorId", playerId);
        this.repoSet = ed.getEntities(defaultFilter, CardPiece.class, CardExperiencePiece.class, RepositoryPiece.class);
        this.arsenalSet = ed.getEntities(defaultFilter, CardPiece.class, ArsenalPiece.class);
        this.arsenals = HashMultimap.create();
    }

    /**
     * Adds a new Card To the Repository
     *
     * @param cardName
     * @param creatorId
     * @return
     */
    public boolean addCardToRepository(String cardName, EntityId creatorId) {
        if (!Objects.equal(creatorId, playerId)) {
            return false;
        }


        EntityId id = null;
        RepositoryPiece rp = null;
        CardPiece cp = null;
        //Check if card already exists
        for (Iterator<Entity> it = repoSet.iterator(); it.hasNext();) {
            Entity e = it.next();
            CardPiece cp2 = e.get(CardPiece.class);
            if (cardName.equals(cp2.getName())) {
                cp = cp2;
                id = e.getId();
                rp = e.get(RepositoryPiece.class);
                break;
            }
        }
        //if not set it
        id = Objects.firstNonNull(id, ed.createEntity());
        rp = Objects.firstNonNull(rp, new RepositoryPiece(0, 0));//set to 0 because to reduce logic during increment
        cp = Objects.firstNonNull(cp, new CardPiece(cardName, creatorId));
        //finally update or set components
        ed.setComponents(id,
                new RepositoryPiece(rp.getCardCount() + 1, rp.getCardsUsed()),
                cp);


        return true;
    }

    /**
     * @see DeckController#addCardToArsenal(java.lang.String,
     * com.simsilica.es.Entity,
     * com.spectre.deck.components.ArsenalPiece.DeckType)
     * @param arsenalName
     * @param repositoryEntity
     * @return
     */
    public boolean addCardToArsenal(String arsenalName, Entity repositoryEntity) {
        return addCardToArsenal(arsenalName, repositoryEntity, DeckType.SINGLE_CAPACITY);
    }

    /**
     * Adds a new Card to the Arsenal based on entities in the repository The
     * RepositoyEntity must be a child entity created by the player their are
     * checks to prevent trivial connections
     *
     * @param arsenalName
     * @param repositoryEntity
     * @param type
     * @return
     */
    public boolean addCardToArsenal(String arsenalName, Entity repositoryEntity, DeckType type) {
        RepositoryPiece rp = repositoryEntity.get(RepositoryPiece.class);
        CardPiece cp = repositoryEntity.get(CardPiece.class);
        if (!arsenals.containsKey(arsenalName) || rp == null || cp == null || Objects.equal(cp.getCreatorId(), playerId)) {
            return false;
        }
        //Check Repository to see if another card can be allocated to an arsenal
        int cc = rp.getCardCount();
        int cu = rp.getCardsUsed();
        cu += 1;//increment by one
        if (cc > cu) {
            return false;
        }
        //update RepositoryEntity then reset it
        rp = new RepositoryPiece(cc, cu);
        repositoryEntity.set(rp);

        //finally create new entity and add an arsenal piece to it also copy cardPiece
        EntityId id = ed.createEntity();
        ArsenalPiece ap = new ArsenalPiece(arsenalName, repositoryEntity.getId(), type);
        ed.setComponents(id, cp, ap);

        setArsenalDeckType(arsenalName, type);

        return true;
    }

    /**
     * Updates the Arsenal's DeckType
     *
     * @param arsenalName
     * @param type
     */
    public void setArsenalDeckType(String arsenalName, DeckType type) {
        if (!arsenals.containsKey(arsenalName)) {
            return;
        }
        for (EntityId id : arsenals.get(arsenalName)) {
            ArsenalPiece ap = ed.getComponent(id, ArsenalPiece.class);
            ArsenalPiece ap2 = new ArsenalPiece(arsenalName, ap.getParentId(), type);
            ed.setComponent(id, ap2);
        }
    }

    /**
     * Updates the Arsenal Name attached to each ArsenalPiece
     *
     * @param oldArsenalName
     * @param newArsenalName
     */
    public void changeArsenalName(String oldArsenalName, String newArsenalName) {
        if (!arsenals.containsKey(oldArsenalName)) {
            return;
        }
        for (EntityId id : arsenals.get(oldArsenalName)) {
            ArsenalPiece ap = ed.getComponent(id, ArsenalPiece.class);
            ArsenalPiece ap2 = new ArsenalPiece(newArsenalName, ap.getParentId(), ap.getDeckType());
            ed.setComponent(id, ap2);
        }
    }

    /**
     * Removes the Card from the Arsenal based soley on its Entity
     *
     * @param id EntityId
     */
    public boolean removeCardFromArsenal(EntityId id) {
        ArsenalPiece ap = ed.getComponent(id, ArsenalPiece.class);
        try {
            EntityId parentId = ap.getParentId();
            Entity e = ed.getEntity(parentId, RepositoryPiece.class);
            RepositoryPiece rp = e.get(RepositoryPiece.class);
            int cc = rp.getCardCount();
            int cu = rp.getCardsUsed();
            cu -= 1;//decrement by one
            e.set(new RepositoryPiece(cc, cu));
            //TODO: Look Into Card Reallocation
            /**
             * Maybe i should do something different here like maybe set up card
             * reallocation in other words instead of delete the entity leave
             * them and when creating new cards search for arsenal card entities
             * not in use and reuse it by rewriting its components
             */
            ed.removeEntity(id);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Removes all Cards from a deck and returns them to its repository to be
     * used
     *
     * Note: Creation of Arsenal Decks are dynamic and by user the second an
     * entity is given an arsenalPiece a deck is created
     *
     * @param name String: the name of the arsenal to delete
     * @return boolean was the removal successful
     */
    public boolean deleteArsenal(String arsenalName) {
        if (!arsenals.containsKey(arsenalName)) {
            return false;
        }

        //Recalculate Repository measurements
        for (EntityId id : arsenals.get(arsenalName)) {
            removeCardFromArsenal(id);
        }

        arsenals.removeAll(arsenalName);
        return true;
    }

    @Override
    public void SpectreControl() {
    }

    @Override
    public void cleanup() {
        arsenalSet.release();
        spectreUpdate(0);
        arsenalSet = null;
        this.arsenals.clear();
    }

    @Override
    protected void spectreUpdate(float tpf) {
        if (arsenalSet.applyChanges()) {
            addArsenalEntity(arsenalSet.getAddedEntities());
            removeArsenalEntity(arsenalSet.getRemovedEntities());
            addArsenalEntity(arsenalSet.getChangedEntities());
        }
    }

    private void addArsenalEntity(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            ArsenalPiece ap = e.get(ArsenalPiece.class);
            String name = ap.getArsenalName();
            arsenals.put(name, id);//ID is safer than entity since components may change
        }
    }

    private void removeArsenalEntity(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            Entity e = it.next();
            EntityId id = e.getId();
            ArsenalPiece ap = e.get(ArsenalPiece.class);
            String name = ap.getArsenalName();
            arsenals.remove(name, id);//ID is safer than entity since components may change
        }
    }

    public Set getArsenalNames() {
        return arsenals.keySet();
    }
}
