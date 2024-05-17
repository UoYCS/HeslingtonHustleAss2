package com.waddle_ware.heslington_hustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {

    public static final String FONT_GEN_ASSET = "OETZTYP_.TTF";
    private static final FreeTypeFontGenerator font_gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_GEN_ASSET));

    public static BitmapFont getGameFont(int size, float border){
        BitmapFont font = genFont(size, border);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    private static BitmapFont genFont(int size, float borderWidth) {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.borderColor = Color.BLACK;
        param.borderWidth = borderWidth;
        param.borderStraight = false;
        return font_gen.generateFont(param);
    }
}
