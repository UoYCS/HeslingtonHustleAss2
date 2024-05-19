package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.core.*;
import com.waddle_ware.heslington_hustle.UserScore;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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


                // SLEEPING at Goodricke // Array 0
                new ActivityLocation(1786, 264, 20, "sleep", ActivityType.Sleep),

                // STUDYING at Library // Array 1
                new ActivityLocation(1136, 258, 20, "study at library", ActivityType.Study),

                // STUDYING at CS building // Array 2
                new ActivityLocation(1664, 24, 20, "study", ActivityType.Study),

                // RECREATION at Duck pond // Array 3
                new ActivityLocation(2031, 144, 20, "feed the ducks", ActivityType.Recreation),

                // RECREATION at Sports Centre // Array 4
                new ActivityLocation(970, 125, 20, "play football", ActivityType.Recreation),

                // RECREATION in town // Array 5
                new ActivityLocation(136, 280, 20, "go clubbing", ActivityType.Recreation),

                // EATING at Piazza // Array 6
                new ActivityLocation(2104, 264, 20, "eat", ActivityType.Food),

                // EATING at Courtyard // Array 7
                new ActivityLocation(1288, 55, 20, "eat", ActivityType.Food),

                // EATING in town // Array 8
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

        for (int i = 0; i < 7; i++) {spy_core.incrementDay();} // make day final day
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

    @Test(expected = RuntimeException.class)
    public void testScoringCalledBeforeLastDay(){
        Core core = new Core();
        core.generateScore(test_activityLocations);
    }

    @Test(expected = RuntimeException.class)
    public void testPassFailCalledBeforeLastDay(){
        Core core = new Core();
        core.hasPlayerFailed();
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

    /**
     * Tests that checkStreaks correctly identifies which achievements have been done
     * and that score is increased by doing streaks
     *
     * output = boolean array size 3,
     *
     * Index 0: football achievement (4 times)
     * Index 1: library achievement (4 times)
     * Index 2 : clubbing achievement (4 times)
     *
     * Also check that having achievements increase score
     */
    @Test
    public void testStreaks(){

        int[] test_meals = {3, 3, 3, 3, 3, 3, 3};
        int[] test_relax = {1, 1, 1, 1, 1, 1, 1};
        int[] test_study = {1, 1, 1, 1, 1, 1, 1};

        Core core = new Core();
        core.setRelax_count(test_relax);
        core.setStudy_count(test_study);
        core.setMeal_count(test_meals);

        for (int i = 0; i < 7; i++) {
            core.incrementDay();
        } // make day final day

        int initial_score = core.generateScore(test_activityLocations);

        for(int i=0;i < 4;i++){test_activityLocations[4].incrementCounter(2);}

        assertArrayEquals(new boolean[]{true, false, false}, core.checkStreaks(test_activityLocations));

        for(int i=0;i < 4;i++){test_activityLocations[1].incrementCounter(4);}

        assertArrayEquals(new boolean[]{true, true, false},core.checkStreaks(test_activityLocations));

        for(int i=0;i < 4;i++){test_activityLocations[5].incrementCounter(3);}

        assertArrayEquals(new boolean[]{true, true, true},core.checkStreaks(test_activityLocations));

        int final_score = core.generateScore(test_activityLocations);

        assertTrue(final_score > initial_score);

    }

    /**
     * This tests the utility method that outputs the number of locations of an activity type is used
     *
     * Input = array of activity location instances and type of activity
     *
     * output = integer of how many locations of that type are used
     *
     * This is used when generating score so that using a mix of locations increases score
     *
     * Input space partititions:
     *
     * Ensure that it only counts locations of that activity type, therefore some tests will include
     * irelevent actions used to check they arent counted towards the total
     *
     * Also ensure as determined that the amount of times differing locations are used does not effect output
     * We decided simply that using different locations should increase score, not the spread of that use
     * i.e. using a location once has the same effect as using it multiple times for the purposes
     * of increased scores due to a mix of locations used.
     *
     * Lastly ensure the correct output is determined for each study type, and that we test outputs from 0-3
     */


    @Test
    public void testGetNumLocationsActivity(){


        ActivityLocation mock_sleep = mock(ActivityLocation.class);
        ActivityLocation mock_study_1 = mock(ActivityLocation.class);
        ActivityLocation mock_study_2 = mock(ActivityLocation.class);
        ActivityLocation mock_eat_1 = mock(ActivityLocation.class);
        ActivityLocation mock_eat_2 = mock(ActivityLocation.class);
        ActivityLocation mock_eat_3 = mock(ActivityLocation.class);
        ActivityLocation mock_rec_1 = mock(ActivityLocation.class);
        ActivityLocation mock_rec_2 = mock(ActivityLocation.class);
        ActivityLocation mock_rec_3 = mock(ActivityLocation.class);

        doReturn(ActivityType.Sleep).when(mock_sleep).getType();
        doReturn(ActivityType.Study).when(mock_study_1).getType();
        doReturn(ActivityType.Study).when(mock_study_2).getType();
        doReturn(ActivityType.Food).when(mock_eat_1).getType();
        doReturn(ActivityType.Food).when(mock_eat_2).getType();
        doReturn(ActivityType.Food).when(mock_eat_3).getType();
        doReturn(ActivityType.Recreation).when(mock_rec_1).getType();
        doReturn(ActivityType.Recreation).when(mock_rec_2).getType();
        doReturn(ActivityType.Recreation).when(mock_rec_3).getType();

        int[] once_every_day = {1,1,1,1,1,1,1};
        int[] not_at_all = {0,0,0,0,0,0,0};
        int[] used_once = {0,0,0,1,0,0,0};

        ActivityLocation[] test_locations_array = {mock_sleep,mock_study_1,mock_study_2,
        mock_eat_1,mock_eat_2,mock_eat_3,mock_rec_1,mock_rec_2,mock_rec_3};

        // sets that returns are only 1 activity used for each type, each method call should return 1
        doReturn(once_every_day).when(mock_sleep).getInteractions();
        doReturn(once_every_day).when(mock_rec_1).getInteractions();
        doReturn(not_at_all).when(mock_rec_2).getInteractions();
        doReturn(not_at_all).when(mock_rec_3).getInteractions();
        doReturn(once_every_day).when(mock_study_1).getInteractions();
        doReturn(not_at_all).when(mock_study_2).getInteractions();
        doReturn(once_every_day).when(mock_eat_1).getInteractions();
        doReturn(not_at_all).when(mock_eat_2).getInteractions();
        doReturn(not_at_all).when(mock_eat_3).getInteractions();


        Core core = new Core();
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Sleep));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Study));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Recreation));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Food));

        // sets that study uses 2 locations, with second only used once everything else still using 1
        doReturn(used_once).when(mock_study_2).getInteractions();


        // assert outputs as expected, 2 for study, 1 for everything else
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Sleep));
        assertSame(2,core.getNumLocationsActivity(test_locations_array,ActivityType.Study));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Recreation));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Food));

        // sets that rec uses 3 locations
        doReturn(used_once).when(mock_rec_2).getInteractions();
        doReturn(used_once).when(mock_rec_3).getInteractions();

        //asserts as expected, 2 for study, 3 for rec and 1 for everything else
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Sleep));
        assertSame(2,core.getNumLocationsActivity(test_locations_array,ActivityType.Study));
        assertSame(3,core.getNumLocationsActivity(test_locations_array,ActivityType.Recreation));
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Food));

        // sets that eat used no locations
        // boundary test. Unlikely for any activity type to not be used at all but is possible.
        // check that it asserts 0 and doesnt count any other activity type.
        doReturn(not_at_all).when(mock_eat_1).getInteractions();

        //assert as expected, 2 for study, 3 for rec, 1 for sleep, and 0 for eat
        assertSame(1,core.getNumLocationsActivity(test_locations_array,ActivityType.Sleep));
        assertSame(2,core.getNumLocationsActivity(test_locations_array,ActivityType.Study));
        assertSame(3,core.getNumLocationsActivity(test_locations_array,ActivityType.Recreation));
        assertSame(0,core.getNumLocationsActivity(test_locations_array,ActivityType.Food));

    }



}






