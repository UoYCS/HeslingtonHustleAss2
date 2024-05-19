package com.thewafflers.tests;

import com.badlogic.gdx.Gdx;
import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;
import com.waddle_ware.heslington_hustle.Screens.PlayScreen;
import com.waddle_ware.heslington_hustle.core.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

// @RunWith(GdxTestRunner.class) - not needed
public class CoreTests {

    /**

    These tests will test the game logic in core class
    The string at the start of a multiline comment

    I need to check whethrer its ok to have multiple unit tests in a method or whether i need
    to only have 1 test per method.
     */



    /**
    test core.islastday()

    unit test:
        invalid inputs: day incremented less than 6 times
        valid : day incremented 6 times
     */

    @Test
    public void testIsLastDay(){
        Core core = new Core();
        for (int i = 0;i < 6;i++){core.incrementDay();}
        // the last day should be after incrementing day 6 times as set out by requirements
        // which specify that game should last 7 days
        assertTrue(core.isLastDay());

        core = new Core();
        for (int i = 0;i < 5;i++){core.incrementDay();}
        assertFalse(core.isLastDay());

    }


    @Test
    public void testValidIncrementDay(){
        Core core = new Core();
        int day = core.getCurrentDay();
        core.incrementDay();
        assertEquals(day + 1, core.getCurrentDay() );

    }
    @Test(expected = RuntimeException.class)
    public void testIncrementOver7Days(){
        Core core = new Core();
        for (int i = 0;i < 7;i++){core.incrementDay();}
        //increments to final day which will always be after 6th increment, as requirements specify game lasts 7 days
        core.incrementDay(); // increment one day over

        // it maybe? should fail after 7th increment but only fails after 7th as its > not >=

    }


    

    /**
     * Tests returns string representation of ActivityType
     *
     *
     */
    @Test
    public void testActivityTypeToString(){
        assertSame(ActivityType.Food.toString(),"Food");
        assertSame(ActivityType.Sleep.toString(),"Sleep");
        assertSame(ActivityType.Recreation.toString(),"Recreation");
        assertSame(ActivityType.Study.toString(),"Study");
    }

    /**
     * Tests returns string representation of ExitCondition
     *
     */
    @Test
    public void testExitConditionsToString(){
        assertSame(ExitConditions.TooLow.toString(),"Was too low");
        assertSame(ExitConditions.TooHigh.toString(),"Was too high");
        assertSame(ExitConditions.IsOk.toString(),"Is ok");

    }

    /**
     *
     * Tests returns string representation of ResourceTypes
     */
    @Test
    public void testResourceTypesToString(){
        assertSame(ResourceTypes.Energy.toString(),"Energy");
        assertSame(ResourceTypes.Time.toString(),"Time");

    }

    /**
     * Tests that the method correctly calls the update method of the time.
     *
     * Time class is mocked, the update method is tested in unit tests for time
     *
     * The test just detects that the update method is called.
     */
    @Test
    public void testTimeUpdate(){

        Time mock_time = mock(Time.class);
        Core core = new Core();
        core.time = mock_time;
        core.update();
        verify(mock_time).update();
    }


    /**
     * Tests that method returns true when conditions satisfied for the end of the game
     * Conditions == Final day and no minutes remaining in day
     *
     * 3 branches:
     *
     * Notlastday
     * LastDay but still time remaining
     * Last Day and no time remaining
     *
     */
    @Test
    public void testHasEnded(){
        Time mock_time = mock(Time.class);
        doReturn(1).when(mock_time).getMinutesRemaining();

        Core core = new Core();
        core.time = mock_time;

        for (int i = 0;i < 5;i++){core.incrementDay();}

        assertFalse(core.hasEnded());

        core.incrementDay();

        assertFalse(core.hasEnded());

        doReturn(0).when(mock_time).getMinutesRemaining();

        assertTrue(core.hasEnded());

    }

    @Test
    public void testInteraction(){
        ActivityType test_type = ActivityType.Sleep;

        Time mock_time = mock(Time.class);
        Energy mock_energy = mock(Energy.class);

        ResourceExitConditions energy_output = new ResourceExitConditions(ResourceTypes.Energy, ExitConditions.IsOk);
        ResourceExitConditions time_output = new ResourceExitConditions(ResourceTypes.Time, ExitConditions.IsOk);

        doReturn(energy_output).when(mock_energy).tryActivityType(test_type);
        doReturn(time_output).when(mock_time).tryActivityType(test_type);

        Core core = new Core();

        core.energy = mock_energy;
        core.time = mock_time;

        ResourceExitConditions correct_output = new ResourceExitConditions(null,ExitConditions.IsOk);
        ResourceExitConditions actual_output;

        actual_output = core.interactedWith(test_type);

        assertSame(correct_output.getConditions(),actual_output.getConditions());
        assertEquals(2, core.getCurrentDay());

        // core now on day 2 (1 if counting 0 based)

        test_type = ActivityType.Food;
        doReturn(energy_output).when(mock_energy).tryActivityType(test_type);
        doReturn(time_output).when(mock_time).tryActivityType(test_type);

        actual_output = core.interactedWith(test_type);
        assertSame(correct_output.getConditions(),actual_output.getConditions());
        assertEquals(1,core.getTimesEatenToday());

        test_type = ActivityType.Study;
        doReturn(energy_output).when(mock_energy).tryActivityType(test_type);
        doReturn(time_output).when(mock_time).tryActivityType(test_type);

        actual_output = core.interactedWith(test_type);
        assertSame(correct_output.getConditions(),actual_output.getConditions());
        assertEquals(1,core.getTimesStudiedToday());

        test_type = ActivityType.Recreation;
        doReturn(energy_output).when(mock_energy).tryActivityType(test_type);
        doReturn(time_output).when(mock_time).tryActivityType(test_type);

        actual_output = core.interactedWith(test_type);
        assertSame(correct_output.getConditions(),actual_output.getConditions());
        assertEquals(1,core.getTimesRelaxedToday());

        energy_output = new ResourceExitConditions(ResourceTypes.Energy, ExitConditions.TooLow);
        time_output = new ResourceExitConditions(ResourceTypes.Time, ExitConditions.TooLow);


        doReturn(time_output).when(mock_time).tryActivityType(test_type);

        actual_output = core.interactedWith(test_type);
        assertSame(time_output.getConditions(),actual_output.getConditions());

        doReturn(energy_output).when(mock_energy).tryActivityType(test_type);

        actual_output = core.interactedWith(test_type);
        assertSame(energy_output.getConditions(),actual_output.getConditions());


    }

    @Test
    public void testGetEnergyLimit(){
        Core core = new Core();
        int test_limit = 5;
        core.energy = new Energy(test_limit);
        assertSame(test_limit,core.getEnergyLimit());

    }

    @Test
    public void testGetCurrentEnergy(){
        Core core = new Core();
        int test_limit = 5;
        core.energy = new Energy(test_limit);
        assertSame(test_limit,core.getCurrentEnergy());
    }

    @Test
    public void testGetTimeLimit(){
        Core core = new Core();
        assertEquals(Time.MINUTES_PER_DAY,core.getTimeLimit());
    }

    @Test
    public void testGetTimeRemaining(){

        Core core = new Core();
        assertEquals(core.time.getMinutesRemaining(),core.getTimeRemaining());

    }

    @Test
    public void testSetCounts(){
        Core core = new Core();
        int[] test_count_1= {1,1,1,1,1,1,1};
        int[] test_count_2= {1,1,1,1,1,1,2};
        int[] test_count_3= {1,1,1,1,1,1,3};
        core.setMeal_count(test_count_1);
        core.setStudy_count(test_count_2);
        core.setRelax_count(test_count_3);
        assertEquals(test_count_1,core.getMealCount());
        assertEquals(test_count_2,core.getStudyCount());
        assertEquals(test_count_3,core.getRelaxCount());

    }

    @Test(expected = RuntimeException.class)
    public void testSetBadMealCount(){
        Core core = new Core();
        int[] bad_count = {1,1};
        core.setMeal_count(bad_count);

    }
    @Test(expected = RuntimeException.class)
    public void testSetBadRelaxCount(){
        Core core = new Core();
        int[] bad_count = {1,1};
        core.setRelax_count(bad_count);

    }
    @Test(expected = RuntimeException.class)
    public void testSetBadStudyCount(){
        Core core = new Core();
        int[] bad_count = {1,1};
        core.setStudy_count(bad_count);

    }




}


