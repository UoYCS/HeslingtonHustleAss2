/*
 * CHANGELOG:
 * NEW CLASS:
 *      Added class to assist in implementing new Leaderboard Functionality
 */

package com.waddle_ware.heslington_hustle;

/**
 * This class acts as a data type that stores a users playerName and their game score
 */
public class UserScore {
    private String playerName;
    private int score;

    /**
     * Initialise the UserScore object with name+score
     * @param playerName Players 3Digit Name
     * @param score Score from 0-100
     */
    public UserScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    /**
     * Gets players name
     * @return Players name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets players score
     * @return Players score
     */
    public int getScore() {
        return score;
    }
}
