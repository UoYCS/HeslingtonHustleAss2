package com.waddle_ware.heslington_hustle;

public class UserScore {
    private String playerName;
    private int score;

    public UserScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
