package com.thewafflers.tests;

import com.badlogic.gdx.Gdx;
import com.waddle_ware.heslington_hustle.Screens.MenuScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {


  /*
  before these tests are completed the classes should be refactored so they
  have string storing the name of the asset file, which are used in the code
  instead of directly using filename when creating textures
   */
  @Test
  public void testMenuScreenAssetExists() {
    assertTrue("The png asset for menu screen exists",
        Gdx.files.internal("MenuScreen.png").exists());

  }

  // tests for remaining screen assets

  // tests for all the button assets

  // tests for icon assets

  // tests for sprite sheet assets

  // tests for any other used assets
}
