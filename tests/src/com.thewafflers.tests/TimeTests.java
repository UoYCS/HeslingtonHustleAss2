package com.thewafflers.tests;


import com.waddle_ware.heslington_hustle.core.*;


import org.junit.Before;
import org.junit.Test;

import static java.time.Clock.systemUTC;
import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.time.Clock;

public class TimeTests {

    /**
     * This tests that when update is called, it moves time along the day
     * <p>
     * This is done by minutes remaining
     * When valid the number of minutes remaining should decrease
     */
    @Test
    public void testTimeUpdate() {

        Time time = new Time(15, 3);
        Clock fake_timer = mock(Clock.class);
        doReturn(time.end_point + 1).when(fake_timer).millis();
        int old_mins_remaining = time.getMinutesRemaining();
        time.timer = fake_timer;
        time.update();
        assert (old_mins_remaining > time.getMinutesRemaining());
    }

    /**
     * This tests that when update is called, if enough time has not elapsed then minutes remaining is not decreased
     */
    @Test
    public void testNotEnoughTimeToUpdate() {
        Time time = new Time(15, 3);
        Clock fake_timer = mock(Clock.class);
        doReturn(time.end_point - 1).when(fake_timer).millis();
        int old_mins_remaining = time.getMinutesRemaining();
        time.timer = fake_timer;
        time.update();
        assert (old_mins_remaining == time.getMinutesRemaining());
    }

    /**
     * This tests that when reset method is called the time resets to god knows
     */
    @Test
    public void testResetTime() {
        Time time = new Time(15, 3);
        time.set_minutes_remaining(time.getMinutesRemaining() - 1);
        time.reset();
        assertTrue(time.getMinutesRemaining() == Time.MINUTES_PER_DAY);
    }

    @Test
    public void testGetMinsRemaining() {
        Time time = new Time(15, 3);
        time.set_minutes_remaining(100);
        assert (time.getMinutesRemaining() == 100);

        time.set_minutes_remaining(-1);
        assert(time.getMinutesRemaining() == 0);

    }


    /**
     * Tests that tryactivity method correctly outputs it is ok to do activity when there is valid time
     * <p>
     * Input space boundary: time required + 1 is the boundary between valid and invalid
     * amount of time remaining to do an activity
     */
    @Test
    public void testValidTimeForActivity() {
        Time time = new Time(15, 3);
        time.set_minutes_remaining((Time.TIME_PER_STUDY * -1) + 1);
        ResourceExitConditions output = time.tryActivityType(ActivityType.Study);
        assertSame(output.getConditions(), ExitConditions.IsOk);

        time.set_minutes_remaining((Time.TIME_PER_RECREATIONAL * -1) + 1);
        output = time.tryActivityType(ActivityType.Recreation);
        assertSame(output.getConditions(), ExitConditions.IsOk);

        time.set_minutes_remaining((Time.TIME_PER_FOOD * -1) + 1);
        output = time.tryActivityType(ActivityType.Food);
        assertSame(output.getConditions(), ExitConditions.IsOk);

    }

    /**
     * tests that tryactivity method doesnt output that an activity can be done with not enough
     * time to complete
     * <p>
     * Input space boundary: the amount of time required as time should not be set to 0
     */
    @Test
    public void testInvalidTimeForActivity() {
        Time time = new Time(15, 3);
        time.set_minutes_remaining((Time.TIME_PER_STUDY * -1));
        ResourceExitConditions output = time.tryActivityType(ActivityType.Study);
        assertSame(output.getConditions(), ExitConditions.TooLow);

        time.set_minutes_remaining((Time.TIME_PER_RECREATIONAL * -1));
        output = time.tryActivityType(ActivityType.Recreation);
        assertSame(output.getConditions(), ExitConditions.TooLow);

        time.set_minutes_remaining((Time.TIME_PER_FOOD * -1));
        output = time.tryActivityType(ActivityType.Food);
        assertSame(output.getConditions(), ExitConditions.TooLow);

        output = time.tryActivityType(ActivityType.Sleep);
        assertNotSame(output.getConditions(),ExitConditions.IsOk);
        // sleep activity type should not be passed into try activity type
        // if it is, the method should return it being invalid.


    }

    /**
     * Check that when doActivity is called it correctly reduces time by amount
     * specified in constants
     *
     * Also checks the constants are negative so time is reduced when an activity is performed
     *
     *
     */
    @Test
    public void testDoActivity(){
        Time time = new Time(15, 3);
        time.set_minutes_remaining((Time.TIME_PER_STUDY * -1) + 100);
        time.doActivity(ActivityType.Study);
        assertEquals(100, time.getMinutesRemaining());
        assertTrue("Study should reduce time",Time.TIME_PER_STUDY < 0);

        time.set_minutes_remaining((Time.TIME_PER_RECREATIONAL * -1) + 100);
        time.doActivity(ActivityType.Recreation);
        assertEquals(100, time.getMinutesRemaining());
        assertTrue("Study should reduce time",Time.TIME_PER_RECREATIONAL < 0);

        time.set_minutes_remaining((Time.TIME_PER_FOOD * -1) + 100);
        time.doActivity(ActivityType.Food);
        assertEquals(100, time.getMinutesRemaining());
        assertTrue("Study should reduce time",Time.TIME_PER_FOOD < 0);

        time.set_minutes_remaining(100);
        time.doActivity(ActivityType.Sleep);
        assertEquals("Sleep shouldnt be called to this method, if so nothing should happen",100, time.getMinutesRemaining());



    }

    @Test
    public void testGetIntervalsRemaining(){
        Time time = new Time(15, 3);


        time.set_minutes_remaining(-1);
        assertEquals(0, time.getIntervalsRemaining());
        // if time is somehow negative it still returns 0 intervals remaining

        time.set_minutes_remaining(0);
        assertEquals(0, time.getIntervalsRemaining());
        // should return 0 when no intervals remaining

        time.set_minutes_remaining(Time.MINS_IN_INTERVAL * 10);
        assert(time.getIntervalsRemaining() == 10);
        // randomly set the time so that intervals returned is 10
        // just checks the logic of the return works correct.
        // output should be rounded up integer of mins remaining / mins per interval



    }
}

