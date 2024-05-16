package com.thewafflers.tests;

import com.badlogic.gdx.Gdx;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

import com.waddle_ware.heslington_hustle.Screens.*;
import com.waddle_ware.heslington_hustle.Font;
import com.waddle_ware.heslington_hustle.HUD;
import com.waddle_ware.heslington_hustle.InteractionPopup;
import com.waddle_ware.heslington_hustle.PlayerAnimator;

import java.awt.*;

@RunWith(GdxTestRunner.class)
public class AssetTests {


  /*
  before these tests are completed the classes should be refactored so they
  have string storing the name of the asset file, which are used in the code
  instead of directly using filename when creating textures
   */
  @Test
  public void testMenuScreenAssetsExists() {
    assertTrue("The assets for menu screen exists",
        Gdx.files.internal(MenuScreen.BACKGROUND_ASSET).exists() &
                Gdx.files.internal(MenuScreen.TUTORIALBUTTON_ASSET).exists()&
                Gdx.files.internal(MenuScreen.EXITBUTTON_ASSET).exists()&
                Gdx.files.internal(MenuScreen.PLAYBUTTON_ASSET).exists()&
                Gdx.files.internal(MenuScreen.LEADERBOARDBUTTON_ASSET).exists());

  }

  @Test
  public void testLeaderboardScreenAssetsExists(){
    assertTrue("The assets for leaderboard screen exists",
            Gdx.files.internal(LeaderboardScreen.BACK_BUTTON_ASSET).exists() &
                    Gdx.files.internal(LeaderboardScreen.BLURRED_BACKGROUND_ASSET).exists());


  }

  @Test
  public void testPlayScreenAssetsExists(){
    assertTrue("The assets for play screen exists",
            Gdx.files.internal(PlayScreen.ICON_ANIMATION_ASSET).exists() &
                    Gdx.files.internal(PlayScreen.TILEMAP_ASSET).exists());


  }

  @Test
  public void testTutorialScreenAssetsExist(){
    assertTrue("The assets for tutorial screen exists",
            Gdx.files.internal(TutorialScreen.TUTORIAL_SCREEN_ASSET).exists() &
                    Gdx.files.internal(TutorialScreen.BACK_BUTTON_ASSET).exists());

  }

  @Test
  public void testFontAssetsExist(){
    assertTrue("The assets for font class exists",
            Gdx.files.internal(Font.FONT_GEN_ASSET).exists());

  }

  @Test
  public void testHUDAssetsExists(){
    assertTrue("The assets for font class exists",
            Gdx.files.internal(HUD.FONT_GEN_ASSET).exists());

  }

  @Test
  public void testInteractionPopupAssetsExists(){
    assertTrue("The assets for font class exists",
            Gdx.files.internal(InteractionPopup.FONT_GEN_ASSET).exists());

  }

  @Test
  public void testPlayerAnimatorAssetsExists(){
    assertTrue("The assets for menu screen exists",
            Gdx.files.internal(PlayerAnimator.PLAYER_DOWN_SPRITE_SHEET_ASSET).exists() &
                    Gdx.files.internal(PlayerAnimator.PLAYER_LEFT_SPRITE_SHEET_ASSET).exists()&
                    Gdx.files.internal(PlayerAnimator.PLAYER_UP_SPRITE_SHEET_ASSET).exists()&
                    Gdx.files.internal(PlayerAnimator.PLAYER_RIGHT_SPRITE_SHEET_ASSET).exists()&
                    Gdx.files.internal(PlayerAnimator.PLAYER_STILL_SPRITE_SHEET_ASSET).exists());

  }
  
}
