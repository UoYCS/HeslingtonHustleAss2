/*
 * CHANGELOG:
 * NEW CLASS:
 *      Added class to Implement new leaderboard functionality
 */

package com.waddle_ware.heslington_hustle;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * CHANGELOG: NEW CLASS
 * The Leaderboard class handles all backend logic for the leaderboard requirements.
 */
public class Leaderboard {
    // Set base attributes for leaderboard
    private final String FILE_NAME = "leaderboard.txt";
    private final int NUM_SCORES = 10;
    public UserScore[] highscores;


    /**
     * Creates a new Leaderboard
     */
    public Leaderboard() {
        // Initialise highs cores as array of empty UserScore objects
        highscores = new UserScore[NUM_SCORES];
        // Read the leaderboard file to get the names and scores
        readScores();
    }

    /**
     * This method reads the leaderboard from the leaderboard .txt file
     * The scores are stored in the txt file as the 3 letter username followed by an integer score
     *      e.g. ABC,100
     */
    private void readScores(){
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line = reader.readLine();

            int i = 0;
            while (line != null) {
                if (line != null) {
                    if (line.length() > 3) {
                        // Split the read line into the name and score
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

    /**
     * This method is used to add a new score to the leaderboard
     *
     * @param userName Name of the user
     * @param score Score from 0-100
     */
    public void addScore(String userName, int score){
        // Create a new UserScore object with the name and score
        UserScore newScore = new UserScore(userName, score);

        // Check if there is space in the leaderboard positions (top 10 scores)
        // Or whether the new score is higher than the lowest score
        if (highscores[NUM_SCORES - 1] == null || score > highscores[NUM_SCORES - 1].getScore()) {

            // Set the lowest score as the new score
            highscores[NUM_SCORES - 1] = newScore;

            // Sort the scores to correctly input the new score in the necessary place
            Arrays.sort(highscores, Comparator.comparingInt(us -> {
                if (us == null) {
                    return Integer.MIN_VALUE;
                } else {
                    return ((UserScore) us).getScore();
                }
            }).reversed());

            // Write the scores to the leaderboard file
            writeScores();
        }
    }


    /**
     * This method writes the current leaderboard to the .txt file
     */
    public void writeScores(){
        try{
            FileWriter writer = new FileWriter(FILE_NAME, false);
            writer.write("");

            for (UserScore s : highscores){
                if (s != null){
                    // Write the score in the format "{Name},{Score}"
                    writer.write(s.getPlayerName()+","+s.getScore()+"\n");
                }
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Get the current leaderboard
     * @return Array of UserScore objects
     */
    public UserScore[] getHighScores() {
        return highscores;
    }
}
