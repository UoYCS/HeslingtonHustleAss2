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

    @Test
    public void testMovePlayerUp(){
        Input fake_input = mock(Input.class);
        //Graphics fake_time = spy(Graphics.class);


        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.W);
        //doReturn(0.5F).when(fake_time).getDeltaTime();
        //doReturn(true).when(fake_input).isKeyPressed(Input.Keys.S);
        Gdx.input = fake_input;
        //Gdx.graphics = fake_time;

        Avatar avatar = new Avatar(0,0,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be > 0 when up arrow pressed",avatar.velocity.y > 0);
        assertTrue("velocity horizontal should be 0",avatar.velocity.x == 0);

        //assertSame(avatar.getPlayerX(), 1);
        //Gdx.input = fake_input;
        //avatar.handleInput();
        //assertTrue(avatar.velocity.y == 0);
    }
    @Test
    public void testMovePlayerDown(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.S);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(0,0,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be < 0 when only down arrow pressed",avatar.velocity.y < 0);
        assertTrue("velocity horizontal should be 0",avatar.velocity.x == 0);
    }

    @Test
    public void testMovePlayerLeft(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.A);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(0,0,408,2280);
        avatar.handleInput();
        assertTrue("velocity horizontal should be < 0 when only left arrow pressed",avatar.velocity.x < 0);
        assertTrue("velocity vertical should be 0",avatar.velocity.y == 0);
    }

    @Test
    public void testMovePlayerRight(){
        Input fake_input = mock(Input.class);

        doReturn(true).when(fake_input).isKeyPressed(Input.Keys.D);

        Gdx.input = fake_input;

        Avatar avatar = new Avatar(0,0,408,2280);
        avatar.handleInput();
        assertTrue("velocity upwards should be > 0 when only right arrow pressed",avatar.velocity.x > 0);
        assertTrue("velocity vertical should be 0",avatar.velocity.y == 0);
    }
}
