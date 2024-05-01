package com.waddle_ware.heslington_hustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.InputMultiplexer;

// CHANGELOG : Added Library to calculate sizes of text for centering purposes
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.waddle_ware.heslington_hustle.Leaderboard;

import java.util.Arrays;

/**
 * CHANGELOG : NEW CLASS
 *
 * This class handles the screen in which the
 * user enters their name upon game completion
 *
 */
public class NameScreen implements Screen, InputProcessor {
    private final HeslingtonHustle game;
    private final Stage stage;
    private final int score;
    private final Texture to_render;

    private final BitmapFont small_font;
    private final BitmapFont font;

    // Initialise the players name and length
    private char[] current_name = {' ', ' ', ' '};
    private int current_name_length;
    private String msg = null;

    /**
     * Constructs a new NameScreen.
     *
     * @param game      The game instance.
     * @param score     The player's score at the end of the game.
     */
    public NameScreen(HeslingtonHustle game, int score) {
        this.game = game;
        this.score = score;

        this.to_render = new Texture("Background_Blurred.png");


        this.small_font = Font.getGameFont(50, 3f);
        this.font = Font.getGameFont(150, 6f);


        this.stage = new Stage(new FitViewport(1920, 1080)); // Set virtual screen size to 16:9 aspect ratio
        initialiseMenu();
    }


    /**
     * Initialises menu elements, such as buttons and their listeners.
     */
    private void initialiseMenu(){
        VerticalGroup name_screen_group = new VerticalGroup();
        name_screen_group.setFillParent(true);
        name_screen_group.right().bottom().padBottom(7);
        this.stage.addActor(name_screen_group);

        // Continue Button
        ImageButton continue_button = new ImageButton(Button.createTexRegDraw("ContinueButton.png"));

        continue_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = new String(current_name);
                System.out.println(name);
                System.out.println(score);

                if (current_name_length == 3) {
                    Leaderboard l = new Leaderboard();
                    l.addScore(name, score);
                    game.setScreen(new MenuScreen(game));
                } else {
                    msg = "Name must be 3 digits";
                }
            }
        });

        name_screen_group.addActor(continue_button);
    }



    /**
     * Called when this screen becomes the current screen of the game.
     * Sets the input processor to a InputMultiplexer,
     * allowing it to receive input events from the keyboard and buttons.
     */
    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(this.stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

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
        final float scaleX = this.stage.getViewport().getWorldWidth() / to_render.getWidth();
        final float scaleY = this.stage.getViewport().getWorldHeight() / to_render.getHeight();
        final float scale = Math.min(scaleX, scaleY);
        final float width = this.to_render.getWidth() * scale;
        final float height = this.to_render.getHeight() * scale;
        final float x = (this.stage.getViewport().getWorldWidth() - width) / 2;
        final float y = (this.stage.getViewport().getWorldHeight() - height) / 2;
        this.stage.getBatch().draw(this.to_render, x, y, width, height);


        // Drawing Title to screen
        GlyphLayout titleText = new GlyphLayout(this.font, "Enter Name");
        this.font.draw(this.stage.getBatch(),
                "Enter Name",
                (float) ((width - titleText.width) / 2.0),
                (float) ((height + titleText.height) / 1.3));


        // DRAW CURRENT INPUTTED NAME TO SCREEN
        String name = "";
        for (char c : this.current_name){
            if (c != ' '){
                name += Character.toString(c);}
        }
        GlyphLayout nameText = new GlyphLayout(this.font, name);
        this.font.draw(this.stage.getBatch(),
                name,
                (float) ((width - nameText.width) / 2.0),
                (float) ((height + nameText.height) / 2.0));


        // SHOW MESSAGE POPUP IF THE USER TRIES TO CONTINUE WITH A NOT FULL NAME
        if (this.msg != null) {
            GlyphLayout msgText = new GlyphLayout(this.small_font, this.msg);
            this.small_font.draw(this.stage.getBatch(),
                    this.msg,
                    (float) ((width - msgText.width) / 2.0),
                    (float) (msgText.height + 30));
        }


        this.stage.getBatch().end();
        this.stage.draw();
    }


    /**
     * Called when a key is pressed down
     * Used to implement backspace functionality to
     * remove a letter from the inputted name
     * @param keycode
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            if (this.current_name_length > 0) {
                this.current_name_length--;
                this.current_name[this.current_name_length] = ' ';
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        if (c >= 'a' && c <= 'z') {
            if (this.current_name_length < 3) {
                this.current_name[this.current_name_length] = Character.toUpperCase(c);
                this.current_name_length++;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
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

    @Override
    public void hide() {
    }

    /**
     * Disposes of this screen's resources.
     * This method is called when this screen is no longer needed.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
        this.to_render.dispose();
    }


}
