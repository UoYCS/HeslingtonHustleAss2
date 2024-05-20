/*
 * CHANGELOG:
 * MINIMAL CHANGES REQUIRED:
 *      HUD
 *          - New HUD design implementation
 *      Testing
 *          - Updated code to assist in unit testing
 */

package com.waddle_ware.heslington_hustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.waddle_ware.heslington_hustle.core.Core;

/**
 * The HUD  class displays game information on the screen, including:
 * - Energy level
 * - Time remaining
 * - Number of times studied today
 * - Number of times eaten today
 * - Current day
 */
public class HUD {
    private final BitmapFont font;
    CharSequence energy;
    CharSequence time;
    CharSequence studied;
    CharSequence eaten;
    CharSequence current_day;
    CharSequence recreation;
    FreeTypeFontGenerator font_gen;
    private int current_map_section;

    // CHANGELOG: ADDED NEW ATTRIBUTES FOR CHANGES
    private Texture hudBackground = new Texture("hudBackground.png");
    public static final String FONT_GEN_ASSET = "BebasNeue-Regular.ttf";

  
    /**
     * Constructs a HUD object.
     *
     * @param c The Core object providing necessary game data.
     */
    public HUD(Core c) {
        this.font_gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_GEN_ASSET));
        this.font   = genFont();
        this.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // CHANGELOG: ENERGY DISPLAYS AS A VALUE FROM 0-100
        this.energy = String.format("Energy: %d", (int) (100*c.getCurrentEnergy()/c.getEnergyLimit()));
        // CHANGELOG: TIME NOW FORMATS AS A hh:mm TIME INSTEAD OF MINUTES REMAINING
        this.time   = String.format("Time: %02d:%02d",((1440 - c.getTimeRemaining())/60), (1440 - c.getTimeRemaining())%60);
        // CHANGELOG: ADDED COUNTER FOR TIMES RELAXED
        this.recreation = String.format("Relaxed: %d", c.getTimesRelaxedToday());

        this.studied = String.format("Studied: %d", c.getTimesStudiedToday());
        this.eaten = String.format("Eaten: %d", c.getTimesEatenToday());
        this.current_day = String.format("Day %d", c.getCurrentDay());
    }

    /**
     * Generates a BitmapFont object for rendering text.
     *
     * @return The generated BitmapFont object.
     */
    private BitmapFont genFont() {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 15;
        param.borderColor = Color.BLACK;
        param.borderWidth = 1.5f;
        param.borderStraight = false;
        return font_gen.generateFont(param);
    }

    /**
     * CHANGELOG: UPDATED METHOD
     *            ADDED NEW HUD BACKGROUND
     *            HUD IS NOW DRAWN IN DIFFERENT X-LOCATIONS BASED ON CURRENT MAP LOCATION
     * Renders HUD on the screen.
     *
     * @param batch The Batch object used for rendering.
     */
    public void render(Batch batch) {

        int x_location = 665 + ((current_map_section+1)*(768));

        // Draws HUD Background and information
        batch.draw(hudBackground, x_location - 33, 10, 140, 140);
        this.font.draw(batch, this.energy,       x_location, 123);
        this.font.draw(batch, this.time,         x_location, 108);
        this.font.draw(batch, this.studied,      x_location, 93);
        this.font.draw(batch, this.eaten,        x_location, 78);
        this.font.draw(batch, this.recreation,   x_location, 63);
        this.font.draw(batch, this.current_day,  x_location, 48);
    }

    /**
     * CHANGELOG: UPDATED METHOD
     *            ADDED MAP_SECTION PARAMETER TO CORRECTLY DISPLAY HUD
     * Updates the HUD with new game data.
     *
     * @param map_section section of the map in which to draw HUD
     * @param c The Core object providing updated game data.
     */
    public void update(Core c, int map_section) {
        this.current_map_section = map_section;
        this.energy = String.format("Energy: %d", (int) (100*c.getCurrentEnergy()/c.getEnergyLimit()));
        this.time   = String.format("Time: %02d:%02d",((1440 - c.getTimeRemaining())/60), (1440 - c.getTimeRemaining())%60);
        this.studied = String.format("Studied: %d", c.getTimesStudiedToday());
        this.eaten = String.format("Eaten: %d", c.getTimesEatenToday());
        this.recreation = String.format("Relaxed: %d", c.getTimesRelaxedToday());
        this.current_day = String.format("Day %d", c.getCurrentDay());
    }

    /**
     * Disposes resources used by the HUD.
     */
    public void dispose() {
        this.font.dispose();
        this.font_gen.dispose();
    }
}
