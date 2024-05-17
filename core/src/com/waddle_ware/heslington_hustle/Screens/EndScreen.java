package com.waddle_ware.heslington_hustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.waddle_ware.heslington_hustle.Font;
import com.waddle_ware.heslington_hustle.Button;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;

// CHANGELOG : Added Library to calculate sizes of text for centering purposes
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * The EndScreen class represents the screen displayed at the end of the game.
 * It displays either a win or lose screen based on the game outcome.
 */
public class EndScreen implements Screen {
    private final HeslingtonHustle game;
    private final Stage stage;
    public final int score;
    private final Texture to_render;
    private final BitmapFont font;

    // CHANGELOG : ADDED MORE PRIVATE VARIABLES
    private final CharSequence status_text; // Text the displays if the user has won or lost.
    private final BitmapFont small_font; // Smaller font to use for streaks
    private final BitmapFont big_font; // Bigger font to use for title
    private final boolean streak_athlete; // Boolean value of if Athlete streak was achieved
    private final boolean streak_bookworm; // Boolean value of if Bookworm streak was achieved
    private final boolean streak_clubber; // Boolean value of if Clubber streak was achieved

    public static final String ACHIEVEMENTS_ASSET = "achievements.png";
    private final Texture achievementsPNG = new Texture(Gdx.files.internal(ACHIEVEMENTS_ASSET));

    public static final String BLURRED_BACKGROUND_ASSET = "Background_Blurred.png";

    private final TextureRegion[][] achievements = TextureRegion.split(achievementsPNG, achievementsPNG.getWidth() / 2, achievementsPNG.getHeight()/3);


    /**
     * Constructs a new EndScreen.
     *
     * CHANGELOG : ADDED STREAKS PARAMETER TO CLASS
     *
     * @param game      The game instance.
     * @param has_won   Boolean value indicating whether the player has won the game.
     * @param score     The player's score at the end of the game.
     * @param streaks   Boolean array (size 3) of which streaks have been completed
     */
    public EndScreen(HeslingtonHustle game, boolean has_won, int score, boolean[] streaks) {
        this.game = game;


        // CHANGELOG : ADDED UPDATED MAP BACKGROUND FOR END SCREEN
        this.to_render = new Texture(BLURRED_BACKGROUND_ASSET);

        this.streak_athlete = streaks[0];
        this.streak_bookworm = streaks[1];
        this.streak_clubber = streaks[2];

        // CHANGELOG : If the player has won and the score > 40, set the status text accordingly
        if(has_won && score >= 40) {
            this.status_text = "You Won!";
            this.score = score;
        }
        else {
            // CHANGELOG : If the player lost, cap the score at 39 and set the status text
            this.score = Math.min(39, score);
            this.status_text = "You Lose";}


        // CHANGELOG : REMOVED IN-FILE FONT-GENERATION
        // this.font_gen = new FreeTypeFontGenerator(Gdx.files.internal("OETZTYP_.TTF"));
        // this.font = genFont(150, 6f);
        // this.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.small_font = Font.getGameFont(30, 2f);
        this.font = Font.getGameFont(150, 6f);
        this.big_font = Font.getGameFont(300, 10f);

        this.stage = new Stage(new FitViewport(1920, 1080)); // Set virtual screen size to 16:9 aspect ratio
        Gdx.input.setInputProcessor(this.stage);
        initialiseMenu();
    }


    private void initialiseMenu(){
        VerticalGroup end_screen_group = new VerticalGroup();
        end_screen_group.setFillParent(true);
        end_screen_group.right().bottom().padBottom(7);
        this.stage.addActor(end_screen_group);

        // Continue Button
        ImageButton continue_button = new ImageButton(Button.createTexRegDraw("ContinueButton.png"));
        continue_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // NameEntryScreen
                game.setScreen(new NameScreen(game, score ));
            }
        });
        end_screen_group.addActor(continue_button);

    }


      // CHANGELOG : REMOVED IN-FILE FONT GENERATION

//    /**
//     * Generates a custom font for displaying the player's score on the end screen.
//     *
//     * @return The generated BitmapFont object with custom font settings.
//     * @param size Font size of generated font
//     * @param borderWidth Border width of generated font
//     */
//    private BitmapFont genFont(int size, float borderWidth) {
//        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        param.size = size;
//        param.borderColor = Color.BLACK;
//        param.borderWidth = borderWidth;
//        param.borderStraight = false;
//        return font_gen.generateFont(param);
//    }

    @Override
    public void show() {
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

        /*
         * CHANGELOG : UPDATED HOW END SCREEN IS DISPLAYED USING ONLY TEXT
         */

        // Drawing Title to screen
        GlyphLayout endText = new GlyphLayout(this.big_font, this.status_text);
        this.big_font.draw(this.stage.getBatch(),
                this.status_text,
                (float) ((width - endText.width) / 2.0),
                (float) ((height + endText.height) / 1.3));


        // Drawing score to screen
        GlyphLayout scoreText = new GlyphLayout(this.font, "Your Score");
        this.font.draw(this.stage.getBatch(),
                "Your Score",
                (float) ((width - scoreText.width) / 2.0),
                (float) ((height + scoreText.height) / 1.8));
        GlyphLayout scoreNum = new GlyphLayout(this.font, Integer.toString(this.score) + " / 100");
        this.font.draw(this.stage.getBatch(),
                Integer.toString(this.score) + " / 100",
                (float) ((width - scoreNum.width) / 2.0),
                (float) ((height + scoreNum.height) / 2.4));



        // DRAW TEXT/ICONS FOR WHICH STREAKS WERE OBTAINED

        if (streak_athlete){
            GlyphLayout athleteText = new GlyphLayout(this.small_font, "Athlete +10");
            this.small_font.draw(this.stage.getBatch(),
                    "Athlete +10",
                    (float) ((width - athleteText.width) * 0.3),
                    (float) ((height + athleteText.height) * 0.1));

            // DRAW OBTAINED ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[1][0],
                    (float) ((width - achievements[1][0].getRegionWidth()*4) * 0.3),
                    (float) ((height - achievements[1][0].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        } else{
            // DRAW GREYED-OUT ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[1][1],
                    (float) ((width - achievements[1][1].getRegionWidth()*4) * 0.3),
                    (float) ((height - achievements[1][1].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        }

        if (streak_bookworm){
            GlyphLayout bookwormText = new GlyphLayout(this.small_font, "Bookworm +10");
            this.small_font.draw(this.stage.getBatch(),
                    "Bookworm +10",
                    (float) ((width - bookwormText.width) * 0.5),
                    (float) ((height + bookwormText.height) * 0.1));
            // DRAW OBTAINED ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[0][0],
                    (float) ((width - achievements[0][0].getRegionWidth()*4) * 0.5),
                    (float) ((height - achievements[0][0].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        } else{
            // DRAW GREYED-OUT ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[0][1],
                    (float) ((width - achievements[0][1].getRegionWidth()*4) * 0.5),
                    (float) ((height - achievements[0][1].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        }



        if (streak_clubber){
            GlyphLayout clubberText = new GlyphLayout(this.small_font, "Clubber +10");
            this.small_font.draw(this.stage.getBatch(),
                    "Clubber +10",
                    (float) ((width - clubberText.width) * 0.7),
                    (float) ((height + clubberText.height) * 0.1));
            // DRAW OBTAINED ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[2][0],
                    (float) ((width - achievements[2][0].getRegionWidth()*4) * 0.7),
                    (float) ((height - achievements[2][0].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        } else{
            // DRAW GREYED-OUT ACHIEVEMENT ICON

            this.stage.getBatch().draw(achievements[2][1],
                    (float) ((width - achievements[2][1].getRegionWidth()*4) * 0.7),
                    (float) ((height - achievements[2][1].getRegionHeight()*4) * 0.15),
                    200,
                    200);
        }



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
