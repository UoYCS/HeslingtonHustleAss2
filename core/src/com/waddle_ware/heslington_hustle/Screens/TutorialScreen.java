package com.waddle_ware.heslington_hustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.waddle_ware.heslington_hustle.Button;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;

/**
 * This class represents the games tutorial screen.
 * It displays controls and instructions on how to play the game.
 */
public class TutorialScreen implements Screen {
    private final HeslingtonHustle game;
    private final Stage stage;
    private final ScreenId previous_screen;
    private final Texture tutorial_img;

    /**
     * Constructs a new TutorialScreen.
     *
     * @param game             The game instance.
     * @param previous_screen The screen to return to upon pressing the back button.
     */
    public TutorialScreen(HeslingtonHustle game, ScreenId previous_screen) {
        this.previous_screen = previous_screen;
        this.game = game;

        // CHANGELOG : UPDATED TUTORIAL SCREEN BACKGROUND
        this.tutorial_img = new Texture("TutorialScreen_New.png");
        this.stage = new Stage(new FitViewport(1920, 1080)); // Set virtual screen size to 16:9 aspect ratio
        Gdx.input.setInputProcessor(this.stage);
        initialiseMenu(); // Add menu elements
    }


    // CHANGELOG : REMOVED IN-FILE BUTTON STYLE GENERATION
//    private ImageButton.ImageButtonStyle createTexRegDraw(String path) {
//        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
//        style.imageUp = new TextureRegionDrawable( new TextureRegion(new Texture(path)));
//        style.imageUp.setMinWidth(475);
//        style.imageUp.setMinHeight(125);
//        return style;
//    }

    /**
     * Initialises the tutorial screen with associated UI elements.
     */
    private void initialiseMenu() {
        VerticalGroup tutorial_group = new VerticalGroup();
        tutorial_group.setFillParent(true);
        tutorial_group.left().top().padTop(7);
        this.stage.addActor(tutorial_group);

        // Back button
        ImageButton back_button = new ImageButton(Button.createTexRegDraw("BackButton.png"));
        back_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (previous_screen) {
                    case MenuScreen:
                        game.setScreen(new MenuScreen(game));
                        break;
                    case PlayScreen:
                        game.setScreen(new PlayScreen(game));
                        break;
                }
            }
        });
        tutorial_group.addActor(back_button);
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
        final float scaleX = this.stage.getViewport().getWorldWidth() / this.tutorial_img.getWidth();
        final float scaleY = this.stage.getViewport().getWorldHeight() / this.tutorial_img.getHeight();
        final float scale = Math.min(scaleX, scaleY);
        final float width = this.tutorial_img.getWidth() * scale;
        final float height = this.tutorial_img.getHeight() * scale;
        final float x = (this.stage.getViewport().getWorldWidth() - width) / 2;
        final float y = (this.stage.getViewport().getWorldHeight() - height) / 2;
        this.stage.getBatch().draw(this.tutorial_img, x, y, width, height);
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
        this.tutorial_img.dispose();
    }
}
