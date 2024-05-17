package com.thewafflers.tests;

import com.badlogic.gdx.Gdx;
import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.core.Core;
import com.waddle_ware.heslington_hustle.core.Time;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// @RunWith(GdxTestRunner.class) - not needed
public class CoreTests {

    /*

    These tests will test the game logic in core class
    The string at the start of a multiline comment

    I need to check whethrer its ok to have multiple unit tests in a method or whether i need
    to only have 1 test per method.
     */

    //@Test
    //public void testHasEnergyTracker(){
        //Core core = new Core();


    //}

    /*
     test core.update()
     will need time instance created in addition to a core instance
     integration test, check if when core.update is called the time instance acts correctly
     */



    /* This is commented as i dont think this needs to be tested , it just calls times method
    which can be tested itself

    @Test
    public void testUpdate(){
        Core core = new Core();

    }

     */


    /*
     test core.interactedwith()

     this method is called when a player interacts with a building

     We can assume resourceexistconditions and the other objects work from their own unit tests
     We can also do integration tests which check they both work together (assuming i understand integation tests
     correectly)

     unit tests for this:

        check invalid energy or time results in correct return and no change in counters
        when valid input:
        check counters are correct
        check the return resourceexitcondition is correct? Need to check how this links up


     */
    public void testInteractedWith(){

    }

    /*
    test core.islastday()

    unit test:
        invalid inputs: < 7 : assert false
        valid : 7, 7+ : assert true
     */

    @Test
    public void testIsLastDay(){
        Core core = new Core();
        for (int i = 0;i < 6;i++){core.incrementDay();}
        // the last day should be after incrementing day 6 times as set out by requirements
        // which specify that game should last 7 days
        assertTrue(core.isLastDay());

    }

    /*
    test core.hasended() ... test whether or not the game ends if player doesnt sleep by midnight on last day...


     */


    /*
    test core.incrementday()

    unit test:
        check throws exception if tries to increment when at end
        can check whether time and energy are reset
     */

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



    // to do: hasplayefailed, generatescore(all of scoring), getNumLocationsActivity







}


