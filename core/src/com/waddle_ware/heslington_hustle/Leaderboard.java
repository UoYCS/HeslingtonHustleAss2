package com.waddle_ware.heslington_hustle;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Leaderboard {
    private final String FILE_NAME = "leaderboard.txt";
    private final int NUM_SCORES = 10;
    public UserScore[] highscores;


    public Leaderboard() {
        highscores = new UserScore[NUM_SCORES];
        readScores();
    }

    private void readScores(){
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line = reader.readLine();

            int i = 0;
            while (line != null) {
                if (line != null) {
                    if (line.length() > 3) {
                        String[] entrySplit = line.split(",");
                        String name = entrySplit[0];
                        int score = Integer.parseInt(entrySplit[1]);
                        highscores[i] = new UserScore(name, score);
                        i++;
                    }
                }
                line = reader.readLine();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
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
        try{
            FileWriter writer = new FileWriter(FILE_NAME, false);
            writer.write("");

            for (UserScore s : highscores){
                if (s != null){
                    writer.write(s.getPlayerName()+","+s.getScore()+"\n");
                }
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }




    public UserScore[] getHighScores() {
        return highscores;
    }
}
