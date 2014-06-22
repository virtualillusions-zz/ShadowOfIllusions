/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.player.components;

import com.simsilica.es.Name;

/**
 * A component used to distinguish players or AI
 *
 * @author Kyle D. Williams
 */
public class PlayerPiece extends Name {

    private final int type;

    public PlayerPiece(String playerName) {
        this(PlayerType.Player, playerName);
    }

    public PlayerPiece(PlayerType type, String playerName) {
        super(playerName);
        this.type = type.ordinal();
    }

    public PlayerType getPlayerType() {
        return PlayerType.values()[type];
    }

    @Override
    public String toString() {
        return "PlayerPiece[PlayerName=" + getName() + ", PlayerType=" + getPlayerType() + "]";
    }

    /**
     * The enum PlayerType is used to describe the type of player represented by
     * the entity
     */
    public enum PlayerType {

        Player, AI
    }
}
