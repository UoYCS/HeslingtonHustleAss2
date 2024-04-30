package com.waddle_ware.heslington_hustle;
import java.io.*;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class Leaderboard {
    private final String PATH = "leaderboard.txt";
    private final int NUM_SCORES = 10;
    private TreeMap<String, Integer> highscores;


    public Leaderboard() {
        highscores = new TreeMap<>(Comparator.reverseOrder());
        getScoresFromFile();
    }

    private void getScoresFromFile(){

    }

    private void addScore(String userName, int score){
        highscores.put(userName, score);
        if (highscores.size() > NUM_SCORES){
            highscores.remove(highscores.lastKey());}
    }

    public TreeMap<String, Integer> getHighscores() {
        return highscores;
    }
}
