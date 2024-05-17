package com.thewafflers.tests;


import com.waddle_ware.heslington_hustle.core.*;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class TimeTests {

    /**
     * This tests that when update is called, it moves time along the day
     *
     * This is done by minutes remaining
     *
     */
    @Test
    public void testTimeUpdate(){


        Time time = new Time(15,3);

    }
}
