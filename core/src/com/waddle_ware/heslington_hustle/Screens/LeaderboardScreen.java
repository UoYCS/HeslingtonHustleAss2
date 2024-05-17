package com.waddle_ware.heslington_hustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.waddle_ware.heslington_hustle.Button;
import com.waddle_ware.heslington_hustle.Font;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;
import com.waddle_ware.heslington_hustle.Leaderboard;
import com.waddle_ware.heslington_hustle.UserScore;

/**
 * This class represents the games Leaderboard Screen.
 * It displays the top 10 scores achieved in the game.
 */
public class LeaderboardScreen implements Screen {
    private final HeslingtonHustle game;
    private final Stage stage;
    private final ScreenId previous_screen;
    private final Texture background_image;
    private final BitmapFont font;
    private String displayText;

    public static final String BLURRED_BACKGROUND_ASSET = "Background_Blurred.png";
    public static final String BACK_BUTTON_ASSET = "BackButton.png";

    private Leaderboard leaderboard = new Leaderboard();

    /**
     * Constructs a new LeaderboardScreen.
     *
     * @param game             The game instance.
     * @param previous_screen The screen to return to upon pressing the back button.
     */
    public LeaderboardScreen(HeslingtonHustle game, ScreenId previous_screen) {
        this.previous_screen = previous_screen;
        this.game = game;
        this.background_image = new Texture(BLURRED_BACKGROUND_ASSET);
        this.stage = new Stage(new FitViewport(1920, 1080)); // Set virtual screen size to 16:9 aspect ratio
        Gdx.input.setInputProcessor(this.stage);
        initialiseMenu(); // Add menu elements



        this.font = Font.getGameFont(50, 4f);

    }




    /**
     * Initialises the Leaderboard screen with associated UI elements.
     */
    private void initialiseMenu() {
        VerticalGroup leader_group = new VerticalGroup();
        leader_group.setFillParent(true);
        leader_group.left().top().padTop(7);
        this.stage.addActor(leader_group);

        // Back button
        ImageButton back_button = new ImageButton(Button.createTexRegDraw(BACK_BUTTON_ASSET));
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
        leader_group.addActor(back_button);
    }

    /**
     * Called when this screen becomes the current screen of the game.
     * Sets the input processor to the stage, allowing it to receive input events.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);

        int i = 1;
        String result = "";
        for (UserScore score : this.leaderboard.getHighScores()){
            if (score == null){
                break;
            }
            int dots = (20 - (Integer.toString(score.getScore())).length());

            if (i != 10){
                result += " ";
            }

            result += i + ". " + score.getPlayerName() + ".".repeat(dots) + score.getScore() + "\n";
            i++;

        }
        this.displayText = result;





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
        final float scaleX = this.stage.getViewport().getWorldWidth() / this.background_image.getWidth();
        final float scaleY = this.stage.getViewport().getWorldHeight() / this.background_image.getHeight();
        final float scale = Math.min(scaleX, scaleY);
        final float width = this.background_image.getWidth() * scale;
        final float height = this.background_image.getHeight() * scale;
        final float x = (this.stage.getViewport().getWorldWidth() - width) / 2;
        final float y = (this.stage.getViewport().getWorldHeight() - height) / 2;
        this.stage.getBatch().draw(this.background_image, x, y, width, height);


        GlyphLayout endText = new GlyphLayout(this.font, this.displayText);
        this.font.draw(this.stage.getBatch(),
                this.displayText,
                (float) ((width - endText.width) / 2),
                (float) ((height) / 1.3));



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
    }
}
