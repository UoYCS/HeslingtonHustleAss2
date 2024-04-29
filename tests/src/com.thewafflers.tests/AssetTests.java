package com.thewafflers.tests;

import com.badlogic.gdx.Gdx;
import com.waddle_ware.heslington_hustle.Screens.MenuScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {
  @Test
  public void testMenuScreenAssetExists() {
    assertTrue("The png asset for menu screen exists",
        Gdx.files.internal("MenuScreen.png").exists());

    // this needs to be changed to a variable in MenuScreen which holds the png
    // once that has been implemented in MenuScreen
  }
}
