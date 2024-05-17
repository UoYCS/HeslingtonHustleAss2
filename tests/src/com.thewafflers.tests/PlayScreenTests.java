package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.Avatar;
import com.waddle_ware.heslington_hustle.core.*;
import com.waddle_ware.heslington_hustle.Screens.PlayScreen;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;

import com.badlogic.gdx.Gdx;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class PlayScreenTests {

    /**
     * Checks that the game has the correct number of locations as per requirements
     *
     * Study = Between 1 and 2
     * Sleep = 1
     * Recreation = Between 3 and 6
     * Eat = Between 1 and 3
     *
     */
    @Test
    public void testEnoughLocations(){
        HeslingtonHustle game  = new HeslingtonHustle();
        PlayScreen playScreen = new PlayScreen(game);

        int study_count = 0;
        int sleep_count = 0;
        int rec_count = 0;
        int eat_count = 0;

        for (int i = 0;i<playScreen.activityLocations.length - 1;i++){
            if(playScreen.activityLocations[i].getType() == ActivityType.Study){study_count++;}
            if(playScreen.activityLocations[i].getType() == ActivityType.Sleep){sleep_count++;}
            if(playScreen.activityLocations[i].getType() == ActivityType.Recreation){rec_count++;}
            if(playScreen.activityLocations[i].getType() == ActivityType.Food){eat_count++;}

        }

        assertTrue("Should be between 1 and 2 study locations",study_count <= 2 & study_count >= 1);
        assertEquals("Should be only 1 sleep location",1,sleep_count);
        assertTrue("Should be between 3 and 6 recreation locations",rec_count <= 6 & rec_count >= 3);
        assertTrue("Should be between 1 and 3 food locations",eat_count <= 3 & eat_count >= 1);

    }


    /**
     * GameArea is which zone you are in
     *
     * Map is split up into 3 zones which player traverses
     *
     * -1 for left map, 0 for center, and 1 for right
     * X 0-755, X 756 - 1523, X 1523 - 2280
     *
     */
    @Test
    public void testGameArea(){
        HeslingtonHustle game  = new HeslingtonHustle();
        PlayScreen playScreen = new PlayScreen(game);

        assertSame(playScreen.getGameArea(0), -1);
        assertSame(playScreen.getGameArea(755),-1);

        assertSame(playScreen.getGameArea(756),0);
        assertSame(playScreen.getGameArea(1523),0);

        assertSame(playScreen.getGameArea(1524),1);
        assertSame(playScreen.getGameArea(2280),1);

    }


    @Test
    public void testActivityLocationsinMap(){
        HeslingtonHustle game  = new HeslingtonHustle();
        PlayScreen playScreen = new PlayScreen(game);

        float height = 408;
        float width = 2280;
        boolean InsideMap = true;
        for (int i = 0;i<playScreen.activityLocations.length - 1;i++){
            if (playScreen.activityLocations[i].getX() > width
                    || playScreen.activityLocations[i].getX() < 0) {
                InsideMap = false;
                break;
            }
            if (playScreen.activityLocations[i].getY() > height
                    || playScreen.activityLocations[i].getY() < 0) {
                InsideMap = false;
                break;
            }
        }

        assertTrue(InsideMap);


    }


    /**
     * This tests that given a player position X and Y that
     * when the handleinteraction method is called
     * (Currently from pressing E)
     * that the activity that is nearby is interacted with
     *
     * core.interactedwith is mocked as this is already tested in unit tests for core
     *
     * This results in this just testing that the handleinteraction method
     * and its utility methods correctly call the methods in core when an interaction
     * is done at the activity
     */

     //DOESNT FUCKING WORK
//    @Test
//    public void testActivitiesCanBeInteractedWith(){
//        HeslingtonHustle game  = new HeslingtonHustle();
//        PlayScreen playScreen = new PlayScreen(game);
//        playScreen.player = new Avatar(0, 0, 408, 2280);
//        // set player loc at CS to check interact study
//
//        float CS_X = playScreen.activityLocations[2].getX();
//        float CS_Y = playScreen.activityLocations[2].getY();
//
//        ActivityLocation mock_study = mock(ActivityLocation.class);
//        doReturn(ActivityType.Study).when(mock_study).getType();
//        playScreen.activityLocations[2] = mock_study;
//
//        float duck_X = playScreen.activityLocations[3].getX();
//        float duck_Y = playScreen.activityLocations[3].getY();
//
//        float sleep_x = playScreen.activityLocations[0].getX();
//        float sleep_y = playScreen.activityLocations[0].getY();
//
//        float piazza_x = playScreen.activityLocations[6].getX();
//        float piazza_y = playScreen.activityLocations[6].getY();
//
//        playScreen.player.setPlayerLoc(CS_X,CS_Y);
//
//        Core spy_core = spy(Core.class);
//
//        ResourceExitConditions output = new ResourceExitConditions(ResourceTypes.Energy, ExitConditions.IsOk);
//        doReturn(output).when(spy_core).interactedWith(any());
//        playScreen.core = spy_core;
//        //verify(spy_core,times(1)).getCurrentDay();
//        verify(mock_study,times(1)).incrementCounter(any());


    //}

    /**
     * This tests the utility method isPlayerWithinInteractionArea
     *
     * Which is used by other methods to determine so
     *
     * This is used in areas that cannot be tested automatticaly so this is the
     * best way to automattiacaly check that interactions are
     * permitted if the player is nearby
     *
     */
    @Test
    public void testPlayerNearInteraction(){
        HeslingtonHustle game  = new HeslingtonHustle();
        PlayScreen playScreen = new PlayScreen(game);
        float player_x = playScreen.activityLocations[0].getX();
        float player_y = playScreen.activityLocations[0].getY();

        assertTrue(playScreen.isPlayerWithinInteractionArea(player_x,player_y,playScreen.activityLocations[0]));

        player_x = playScreen.activityLocations[1].getX();
        player_y = playScreen.activityLocations[1].getY();

        assertFalse(playScreen.isPlayerWithinInteractionArea(player_x,player_y,playScreen.activityLocations[0]));

    }



}
