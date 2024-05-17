package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.*;
import com.waddle_ware.heslington_hustle.Screens.*;
import com.waddle_ware.heslington_hustle.core.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class EndScreenTests {

    @Test
    public void testReturnTrue(){

        assertTrue(true);
    }

    /**

    @Test
    public void testTutorialScreen(){
        HeslingtonHustle game = new HeslingtonHustle();
        TutorialScreen tutorialScreen = new TutorialScreen(game, ScreenId.MenuScreen);
    }

    @Test
    public void testLeaderboardScreen(){
        HeslingtonHustle game = new HeslingtonHustle();
        LeaderboardScreen leaderboardScreen = new LeaderboardScreen(game, ScreenId.PlayScreen);

    }

    @Test
    public void testNameScreen(){

        HeslingtonHustle game = new HeslingtonHustle();
        NameScreen nameScreen = new NameScreen(game,100);
    }

    @Test
    public void testButton(){
        ImageButton continue_button = new ImageButton(Button.createTexRegDraw("ContinueButton.png"));

    }

    @Test
    public void testInteractionPopup(){

        InteractionPopup interactionPopup = new InteractionPopup("message","white");
    }
    */






//    @Test
//    public void testScoreCappedIfLose(){
//        HeslingtonHustle game = new HeslingtonHustle();
//        boolean[] streaks = {false,false,false};
//        EndScreen endScreen = new EndScreen(game,true,40, streaks);
//        assertTrue(endScreen.score < 40);
//
//
//    }

}

