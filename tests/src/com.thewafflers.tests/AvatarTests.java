package com.thewafflers.tests;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.Avatar;
import com.waddle_ware.heslington_hustle.core.*;
import com.waddle_ware.heslington_hustle.Screens.PlayScreen;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class AvatarTests {


    /**
     * Tests that the velocity of the player is correct when the W key is pressed
     *
     */
    @Test
    public void testMovePlayerUp(){
        Input fake_input = mock(Input.class);


        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.W);
        Gdx.input = fake_input;

        Avatar avatar = new Avatar(100,100,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be > 0 when up arrow pressed",avatar.velocity.y > 0);
        assertTrue("velocity horizontal should be 0",avatar.velocity.x == 0);

    }
    /**
     * Tests that the velocity of the player is correct when the S key is pressed
     *
     */
    @Test
    public void testMovePlayerDown(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.S);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(100,100,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be < 0 when only down arrow pressed",avatar.velocity.y < 0);
        assertTrue("velocity horizontal should be 0",avatar.velocity.x == 0);
    }

    /**
     * Tests that the velocity of the player is correct when the A key is pressed
     *
     */
    @Test
    public void testMovePlayerLeft(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.A);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(100,100,408,2280);
        avatar.handleInput();
        assertTrue("velocity horizontal should be < 0 when only left arrow pressed",avatar.velocity.x < 0);
        assertTrue("velocity vertical should be 0",avatar.velocity.y == 0);
    }

    /**
     * Tests that the velocity of the player is correct when the D key is pressed
     *
     */
    @Test
    public void testMovePlayerRight(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.D);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(100,100,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be > 0 when only right arrow pressed",avatar.velocity.x > 0);
        assertTrue("velocity vertical should be 0",avatar.velocity.y == 0);
    }
    /**
     * Tests that the velocity of the player is 0 when keys for opposite directions are both pressed
     *
     */
    @Test
    public void testDontMovePlayer(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.D);
        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.S);
        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.A);
        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.W);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(100,100,408,2280);
        avatar.handleInput();
        assertTrue("velocity horizontal 0 when only both horizontal pressed",avatar.velocity.x == 0);
        assertTrue("velocity vertical should be 0 when both vertical pressed",avatar.velocity.y == 0);
    }

    /**
     *Checks the getter methods which return player position are valid
     *
     */
    @Test
    public void testGetPlayerXYPos(){
        float x_pos = 50;
        float y_pos = 100;
        Avatar avatar = new Avatar(x_pos,y_pos,1000,1000);
        assertTrue(x_pos == avatar.getPlayerX());
        assertTrue(y_pos == avatar.getPlayerY());

    }

    /**
     * Checks the setter methods for player position set the playerloc correctly
     * Doesn't test whether its a valid input, i.e. inside the map
     */
    @Test
    public void testSetPlayerLoc(){
        Avatar avatar = new Avatar(50,100,1000,1000);
        float x_pos = 250;
        float y_pos = 500;
        avatar.setPlayerLoc(x_pos,y_pos);
        assertTrue(x_pos == avatar.getPlayerX());
        assertTrue(y_pos == avatar.getPlayerY());

    }
}
