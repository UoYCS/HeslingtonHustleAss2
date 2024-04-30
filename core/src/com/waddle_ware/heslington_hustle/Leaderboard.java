package com.waddle_ware.heslington_hustle;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class Leaderboard {
    private final String PATH = "leaderboard.txt";
    private final int NUM_SCORES = 10;
    private UserScore[] highscores;


    public Leaderboard() {
        highscores = new UserScore[NUM_SCORES];
        readScores();
    }

    private void readScores(){

    }

    public void addScore(String userName, int score){
        UserScore newScore = new UserScore(userName, score);

        if (highscores[NUM_SCORES - 1] == null || score > highscores[NUM_SCORES - 1].getScore()) {

            highscores[NUM_SCORES - 1] = newScore;
            Arrays.sort(highscores, Comparator.comparingInt(us -> {
                if (us == null) {
                    return Integer.MIN_VALUE;
                } else {
                    return ((UserScore) us).getScore();
                }
            }).reversed());

            writeScores();
        }
    }

    public void writeScores(){

    }
    public UserScore[] getHighScores() {
        return highscores;
    }
}
