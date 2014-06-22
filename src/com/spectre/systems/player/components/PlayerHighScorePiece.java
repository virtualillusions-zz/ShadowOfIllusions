/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.player.components;

import com.simsilica.es.PersistentComponent;

/**
 * A component used to represent the players current high score
 *
 * @author Kyle D. Williams
 */
public class PlayerHighScorePiece implements PersistentComponent {

    private long highScore;

    public PlayerHighScorePiece() {
        this(0L);
    }

    public PlayerHighScorePiece(long highScore) {
        this.highScore = highScore;
    }

    /**
     * returns the players high score
     *
     * @return highScore
     */
    public long getHighScore() {
        return highScore;
    }

    @Override
    public String toString() {
        return "PlayerHighScorePiece[HighScore=" + highScore + "]";
    }
}
