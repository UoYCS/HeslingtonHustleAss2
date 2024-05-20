/*
 * CHANGELOG:
 * SEVERAL CHANGES REQUIRED:
 *      Leaderboard functionality
 *          - Added functionality to access leaderboard screen
 *      General changes
 *          - Updated Background
 *      Testing
 *          - Updated code to assist in unit testing
 *
 */


package com.waddle_ware.heslington_hustle.Screens;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.waddle_ware.heslington_hustle.Button;
/**
 * The MenuScreen class represents the screen where the game menu is displayed.
 * It implements the Screen interface and handles user input for menu navigation.
 */
public class MenuScreen implements Screen {
    private final HeslingtonHustle game;
    private final Stage stage;
    private final Texture background;

    // CHANGELOG: ADDED ATTRIUBTES TO STORE SPRITE PNGS
    public static final String BACKGROUND_ASSET = "MenuScreen_New.png";
    public static final String PLAYBUTTON_ASSET = "PlayButton.png";
    public static final String TUTORIALBUTTON_ASSET = "TutorialButton.png";
    public static final String LEADERBOARDBUTTON_ASSET = "LeaderButton.png";
    public static final String EXITBUTTON_ASSET = "ExitButton.png";

    /**
     * Constructs a new MenuScreen.
     *
     * @param game The game instance.
     */
    public MenuScreen(HeslingtonHustle game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(this.stage);

        // CHANGELOG : UPDATED MAIN MENU BACKGROUND
        this.background  = new Texture(BACKGROUND_ASSET);
        initialiseMenu(); // Add menu elements
    }

    // CHANGELOG : REMOVED IN-FILE BUTTON STYLE GENERATION
    // private ImageButton.ImageButtonStyle createTexRegDraw(String path) {
    //       ....
    // }

    /**
     * Initialises menu elements, such as buttons and their listeners.
     */
    private void initialiseMenu() {
        VerticalGroup menu_group = new VerticalGroup();
        menu_group.setFillParent(true);
        menu_group.center(); // centre align vertically
        menu_group.align(Align.bottom);
        this.stage.addActor(menu_group);

        // CHANGELOG: UPDATED BUTTONS TO USE BUTTON CLASS FOR GENERATION
        // CHANGELOG: UPDATED BUTTONS TO USE NEW ATTRIBUTES FOR TEXTURES

        // Play button
        ImageButton play_button = new ImageButton(Button.createTexRegDraw(PLAYBUTTON_ASSET));
        play_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

        // Tutorial button
        ImageButton tutorial_button = new ImageButton(Button.createTexRegDraw(TUTORIALBUTTON_ASSET));
        tutorial_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TutorialScreen(game, ScreenId.MenuScreen));
            }
        });

        // CHANGELOG : ADDED LEADERBOARD BUTTON TO MAIN MENU
        ImageButton leader_button = new ImageButton(Button.createTexRegDraw(LEADERBOARDBUTTON_ASSET));
        leader_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, ScreenId.MenuScreen));
            }
        });

        // Exit button
        ImageButton exit_button = new ImageButton(Button.createTexRegDraw(EXITBUTTON_ASSET));
        exit_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        menu_group.addActor(play_button);
        menu_group.addActor(tutorial_button);

        // CHANGELOG: ADDED LEADERBOARD BUTTON
        menu_group.addActor(leader_button);
        menu_group.addActor(exit_button);
    }

    /**
     * Called when this screen becomes the current screen of the game.
     * Sets the input processor to the stage, allowing it to receive input events.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
    }

    /**
     * Called when this screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        this.stage.getBatch().begin();
        final float scaleX = this.stage.getViewport().getWorldWidth() / this.background.getWidth();
        final float scaleY = this.stage.getViewport().getWorldHeight() / this.background.getHeight();
        final float scale = Math.min(scaleX, scaleY);
        final float width = this.background.getWidth() * scale;
        final float height = this.background.getHeight() * scale;
        final float x = (this.stage.getViewport().getWorldWidth() - width) / 2;
        final float y = (this.stage.getViewport().getWorldHeight() - height) / 2;
        this.stage.getBatch().draw(this.background, x, y, width, height);
        this.stage.getBatch().end();

        this.stage.draw();
    }

    /**
     * Called when the screen is resized.
     *
     * @param width  The new width in pixels.
     * @param height The new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    /**
     * Hides the screen and clears the input processor, preventing further input events.
     * This method is called when the screen is no longer visible.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Disposes of this screen's resources.
     * This method is called when this screen is no longer needed.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
        this.background.dispose();
    }
}
