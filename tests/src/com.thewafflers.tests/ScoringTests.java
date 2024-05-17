package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.core.*;
import com.waddle_ware.heslington_hustle.UserScore;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ScoringTests {

    /**
     * Sets up activity locations array for testing of scoring
     *
     *
     * This is run before each test
     *
     *
     * These are unit tests that cover 4 methods in core.
     * 1. hasplayerfailed()
     * 2. generataescore()
     * 3. getnumlocations()
     * 4. getnumdaysactivity()
     *
     * the getter methods are utility methods that retrieve the array counters
     * for each activity location instance in the list.
     *
     *
     * Still to do: testing numlocations and numdaysactivity and streaks
     */

    ActivityLocation[] test_activityLocations = {};


    /**
     * Generating score requires passing in list of activity locations, which among functionality for playscreen
     * also track usage across days. This is then used by the getnumlocations to determine if a range of places
     * are used for studying/eating/recreating
     *
     * This is called before each test so that any modification of the count by other tests is reset.
     * Most tests mock the getnumlocations method, to focus on testing their specific aspect
     * however it is best to reset each time to avoid any unforeseen issues arising due to code reuse among tests.
     */
    @Before
    public void ResetActivityLocations(){
        test_activityLocations = new ActivityLocation[]{


                // SLEEPING at Goodricke
                new ActivityLocation(1786, 264, 20, "sleep", ActivityType.Sleep),

                // STUDYING at Library
                new ActivityLocation(1136, 258, 20, "study at library", ActivityType.Study),

                // STUDYING at CS building
                new ActivityLocation(1664, 24, 20, "study", ActivityType.Study),

                // RECREATION at Duck pond
                new ActivityLocation(2031, 144, 20, "feed the ducks", ActivityType.Recreation),

                // RECREATION at Sports Centre
                new ActivityLocation(970, 125, 20, "play football", ActivityType.Recreation),

                // RECREATION in town
                new ActivityLocation(136, 280, 20, "go clubbing", ActivityType.Recreation),

                // EATING at Piazza
                new ActivityLocation(2104, 264, 20, "eat", ActivityType.Food),

                // EATING at Courtyard
                new ActivityLocation(1288, 55, 20, "eat", ActivityType.Food),

                // EATING in town
                new ActivityLocation(633, 260, 20, "eat", ActivityType.Food)


        };

    }

    @Test
    public void testStudiedEveryday(){

        int[] test_study = {1,1,1,1,1,1,1};
        int[] test_meals = {3,3,3,3,3,3,3};
        int[] test_relax = {1,1,1,1,1,1,1};

        int[] test_no_meals_eaten = {0,0,0,0,0,0,0};
        int[] test_no_relax = {0,0,0,0,0,0,0};

        Core core = new Core();
        for (int i = 0;i < 7;i++){core.incrementDay();}

        core.setMeal_count(test_meals);
        core.setStudy_count(test_study);
        core.setRelax_count(test_relax);
        assertFalse("Player has met all conditions to pass",core.hasPlayerFailed());

        core.setMeal_count(test_no_meals_eaten);
        core.setRelax_count(test_no_relax);
        assertFalse("Player can pass even if no meals eaten and no relaxation according to requirements",core.hasPlayerFailed() );


    }

    /**
     *Tests that player still passes if they have caught up in a valid way
     */
    @Test
    public void testCaughtUp(){
        int[] test_study = {1,0,1,1,1,2,1};
        int[] test_meals = {3,3,3,3,3,3,3};
        int[] test_relax = {1,1,1,1,1,1,1};

        Core core = new Core();
        for (int i = 0;i < 7;i++){core.incrementDay();} // make day final day
        core.setMeal_count(test_meals);core.setStudy_count(test_study);core.setRelax_count(test_relax); // set count parameter

        assertFalse("Player has caught up for day missed studying",core.hasPlayerFailed());
    }

    /**
     * Tests input space boundaries where player should fail as has not adequately caught up
     *
     * first study input : no day has multiple studies to catchup
     * second study input : can only catchup for one missed day
     */
    @Test
    public void testNotCaughtUp(){
        int[] test_meals = {3,3,3,3,3,3,3};
        int[] test_relax = {1,1,1,1,1,1,1};

        int[] test_study_1 = {1,0,1,1,1,1,1}; // missed day and didn't catchup
        int[] test_study_2 = {1,0,0,2,2,1,1}; // missed over one day even with catchup

        Core core = new Core();
        for (int i = 0;i < 7;i++){core.incrementDay();} // make day final day
        core.setMeal_count(test_meals);
        core.setRelax_count(test_relax); // set count parameters

        core.setStudy_count(test_study_1);
        assertTrue("Player missed day and didn't catchup",core.hasPlayerFailed());

        core.setStudy_count(test_study_2);
        assertTrue("Player missed too many days to catchup",core.hasPlayerFailed());
    }

    @Test
    public void testHigherFromStudyingMore() {



        int[] test_study_min = {1, 1, 1, 1, 1, 1, 1}; // studied minimum to pass (7 times)
        int[] test_study_extra = {2, 2, 2, 1, 1, 1, 1}; // studied extra (10 times)
        int[] test_study_too_much = {2, 2, 2, 2, 1, 1, 1}; // studied too much (11 times)

        int[] test_meals = {3, 3, 3, 3, 3, 3, 3};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};


        Core core = new Core();
        Core spy_core = spy(core);
        // use a spy to make a partial mock, to fake the output from the getnumlocations method
        // this means that building the test isn't reliant on using the functionality of activitylocation class

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals);
        spy_core.setStudy_count(test_study_min);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int score_study_min = spy_core.generateScore(test_activityLocations);

        spy_core.setStudy_count(test_study_extra);

        int score_study_extra = spy_core.generateScore(test_activityLocations);

        spy_core.setStudy_count(test_study_too_much);

        int score_study_too_much = spy_core.generateScore(test_activityLocations);


        assertTrue("Score should be higher when studied 10 vs 7 times /n " +
                        " extra : " + Integer.toString(score_study_extra) + "min : " +
                Integer.toString(score_study_min),
                score_study_extra > score_study_min);

        assertTrue("Score should be lower if you study too much, too much:  " +
                        Integer.toString(score_study_too_much) + " extra: " +
                        Integer.toString(score_study_extra),
                score_study_too_much < score_study_extra);


    }


    /**
     * The player should get a higher score by studying in different locations
     *
     *
     * This test uses a mock of the getnumlocationsstudied() method
     * Other unit tests check that the functionality is correct
     *
     */
    @Test
    public void testDifferentStudyLocations(){
        int[] test_meals = {3, 3, 3, 3, 3, 3, 3};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        Core spy_core = spy(core);
        // use a spy to make a partial mock, to fake the output from the getnumlocations method
        // this means that building the test isn't reliant on using the functionality of activitylocation class

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals);
        spy_core.setStudy_count(test_study);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int test_study_one_loc = spy_core.generateScore(test_activityLocations);

        doReturn(2).when(spy_core).getNumLocationsActivity(any(),any());

        int test_study_multiple_loc = spy_core.generateScore(test_activityLocations);

        assertTrue(String.format("Studying in multiple locations should increase the score," +
                        " 1 location: %s , 2 locations: %s", test_study_one_loc, test_study_multiple_loc),
                test_study_multiple_loc > test_study_one_loc);

    }

    /**
     * The player should get a higher score if they eat in regular intervals
     *
     * We have decided in our game this means that they have eaten all 3 meals, compared to say just eating
     * once in that day
     *
     * A mock is used for getting the number of locations ate at to avoid this becoming an integeration test
     * for 2 methods and the activitylocations class
     */
    @Test
    public void testEatingIntervals(){
        int[] test_meals_3ADay = {3, 3, 3, 3, 3, 3, 3};
        int[] test_meals_2ADay = {2,2,2,2,2,2,2};
        int[] test_meals_1ADay = {1 ,1 ,1 ,1 ,1 ,1 ,1};
        int[] test_meals_NoEat = {0,0,0,0,0,0,0};

        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        Core spy_core = spy(core);

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals_3ADay);

        spy_core.setStudy_count(test_study);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int test_eat_3ADay = spy_core.generateScore(test_activityLocations);

        spy_core.setMeal_count(test_meals_2ADay);

        int test_eat_2ADay = spy_core.generateScore(test_activityLocations);

        spy_core.setMeal_count(test_meals_1ADay);

        int test_eat_1ADay = spy_core.generateScore(test_activityLocations);

        spy_core.setMeal_count(test_meals_NoEat);

        int test_eat_none = spy_core.generateScore(test_activityLocations);

        assertTrue(String.format("Eating more during a day should result in higher score," +
                        " not eaten : %s , eaten once a day: %s , eaten twice a day: %s , eaten three times a day :%s",
                        test_eat_none ,test_eat_1ADay, test_eat_2ADay, test_eat_3ADay),
                test_eat_3ADay > test_eat_1ADay & test_eat_2ADay > test_eat_1ADay & test_eat_1ADay > test_eat_none);

    }

    /**
     * Eating at multiple locations should result in a higher score
     *
     */
    @Test
    public void testEatMultipleLocations(){
        int[] test_meals = {3, 3, 3, 3, 3, 3, 3};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        Core spy_core = spy(core);
        // use a spy to make a partial mock, to fake the output from the getnumlocations method
        // this means that building the test isn't reliant on using the functionality of activitylocation class

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals);
        spy_core.setStudy_count(test_study);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int test_eat_one_loc = spy_core.generateScore(test_activityLocations);

        doReturn(2).when(spy_core).getNumLocationsActivity(any(),any());

        int test_eat_multiple_loc = spy_core.generateScore(test_activityLocations);

        assertTrue(String.format("Eating in multiple locations should increase the score," +
                        " 1 location: %s , 2 locations: %s", test_eat_one_loc, test_eat_multiple_loc),
                test_eat_multiple_loc > test_eat_one_loc);


    }

    /**
     * The player should be rewards for spending time for themselves
     *
     * For our game this is interpreted as more recreation results in higher score
     *
     * Hence, score should be higher when player recreates compared to when not.
     *
     */
    @Test
    public void testRecreatingScore(){
        int[] test_meals = {1 ,1 ,1 ,1 ,1 ,1 ,1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        int[] test_no_relax = {0, 0 ,0, 0, 0, 0, 0};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        Core spy_core = spy(core);

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals);

        spy_core.setStudy_count(test_study);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int test_rec_1ADay = spy_core.generateScore(test_activityLocations);

        spy_core.setRelax_count(test_no_relax);

        int test_no_rec = spy_core.generateScore(test_activityLocations);

        assertTrue("Recreating should result in a higher score than not doing so",
                test_rec_1ADay > test_no_rec);

    }

    @Test
    public void testRecreateMultipleAreas(){
        int[] test_meals = {3, 3, 3, 3, 3, 3, 3};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        Core spy_core = spy(core);
        // use a spy to make a partial mock, to fake the output from the getnumlocations method
        // this means that building the test isn't reliant on using the functionality of activitylocation class

        for (int i = 0; i < 7; i++) {
            spy_core.incrementDay();
        } // make day final day
        spy_core.setMeal_count(test_meals);
        spy_core.setStudy_count(test_study);
        spy_core.setRelax_count(test_relax); // set count parameter

        doReturn(1).when(spy_core).getNumLocationsActivity(any(),any());

        int test_relax_one_loc = spy_core.generateScore(test_activityLocations);

        doReturn(2).when(spy_core).getNumLocationsActivity(any(),any());

        int test_relax_2_loc = spy_core.generateScore(test_activityLocations);

        doReturn(3).when(spy_core).getNumLocationsActivity(any(),any());

        int test_relax_3_loc = spy_core.generateScore(test_activityLocations);

        assertTrue(String.format("relaxing in multiple locations should increase the score," +
                        " 1 location: %s , 2 locations: %s , 3 locations: %s", test_relax_one_loc, test_relax_2_loc, test_relax_3_loc),
                test_relax_3_loc > test_relax_2_loc & test_relax_2_loc > test_relax_one_loc);


    }

    @Test
    public void testGetUsername(){
        String username = "testname";
        UserScore userScore = new UserScore(username,100);

        assertSame(username, userScore.getPlayerName());

    }

    @Test
    public void testGetUserscore(){
        String username = "testname";
        UserScore userScore = new UserScore(username,100);

        assertSame(100, userScore.getScore());

    }

}






