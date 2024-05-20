package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.Leaderboard;
import com.waddle_ware.heslington_hustle.UserScore;
import org.junit.Test;


import com.waddle_ware.heslington_hustle.Leaderboard;
import com.waddle_ware.heslington_hustle.core.*;


import org.junit.Before;
import org.junit.Test;

import static java.time.Clock.systemUTC;
import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.time.Clock;

/**
 *
 * Tests the leaderboard class
 *
 * Leaderboard should store highscores in high --> low format
 * and should write to file in high -> low
 */
public class LeaderboardTests {


    /**
     * Checks that adding scores results in them being resorted so that scores are in order
     *
     */
    @Test
    public void testLeaderboardAddScore(){

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.highscores = new UserScore[10];
        leaderboard.addScore("ABC",55);
        assertTrue(leaderboard.getHighScores()[0].getPlayerName() == "ABC");
        assertTrue(leaderboard.getHighScores()[0].getScore() == 55);

        leaderboard.addScore("BCD",100);
        assertTrue(leaderboard.getHighScores()[0].getPlayerName() == "BCD");
        assertTrue(leaderboard.getHighScores()[0].getScore() == 100);

        leaderboard.addScore("CDE",75);
        assertTrue(leaderboard.getHighScores()[0].getPlayerName() == "BCD");
        assertTrue(leaderboard.getHighScores()[0].getScore() == 100);
        assertTrue(leaderboard.getHighScores()[1].getPlayerName() == "CDE");
        assertTrue(leaderboard.getHighScores()[1].getScore() == 75);
        assertTrue(leaderboard.getHighScores()[2].getPlayerName() == "ABC");
        assertTrue(leaderboard.getHighScores()[2].getScore() == 55);

    }

    /**
     * Checks that scores are written and read to in correct format
     *
     * This is done by writing to file using addscore with a fresh highscore list,
     * which should call the write method to write to file
     *
     * Followed by then creating a new leaderboard instance, which then reads this file
     *
     * If the score it written correctly they should share the same first line.
     *
     * To double check, if file was written to incorrectly, and it contained previous scores, for example from the
     * previous test then the second leaderboard that reads the file would have the old highscores.
     */
    @Test
    public void testLeaderboardWriteReadScore(){

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.highscores = new UserScore[10];
        leaderboard.addScore("ABC",50);
        leaderboard.addScore("BCD",100);

        Leaderboard leaderboard2 = new Leaderboard();

        assertEquals(leaderboard.getHighScores()[0].getScore(),leaderboard2.getHighScores()[0].getScore());
        assertEquals(leaderboard.getHighScores()[0].getPlayerName(),leaderboard2.getHighScores()[0].getPlayerName());



    }


}
