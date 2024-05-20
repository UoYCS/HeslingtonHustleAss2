/*
 * CHANGELOG:
 * NEW CLASS:
 *      Added class to reduce repeated code within Screen classes
 *      Provides Font generation functionality
 */

package com.waddle_ware.heslington_hustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * CHANGELOG : NEW CLASS
 * The Font class generates the games font that are used within the screen classes.
 * This class was added to remove a large amount of repeated code within other classes.
 */
public class Font {

    public static final String FONT_GEN_ASSET = "OETZTYP_.TTF";
    private static final FreeTypeFontGenerator font_gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_GEN_ASSET));

    /**
     * CHANGELOG: NEW METHOD IN NEW CLASS
     * Generates and returns games font with different sizes/border parameters
     * @param size Font size
     * @param border Font border size
     * @return Font
     */
    public static BitmapFont getGameFont(int size, float border){
        BitmapFont font = genFont(size, border);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    /**
     * CHANGELOG: NEW METHOD IN NEW CLASS
     * Private method to generate font used by other static methods in this class.
     * @param size Font size
     * @param borderWidth Font border size
     * @return Font
     */
    private static BitmapFont genFont(int size, float borderWidth) {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.borderColor = Color.BLACK;
        param.borderWidth = borderWidth;
        param.borderStraight = false;
        return font_gen.generateFont(param);
    }
}
