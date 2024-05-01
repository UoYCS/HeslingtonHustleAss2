package com.waddle_ware.heslington_hustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Button {
    private static final int MIN_WIDTH = 475;
    private static final int MIN_HEIGHT = 125;

    /**
     * Creates an ImageButtonStyle with a provided image path.
     *
     * @param path The path to the image file.
     * @return The ImageButtonStyle created with the specified image.
     */
    public static ImageButton.ImageButtonStyle createTexRegDraw(String path){
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable( new TextureRegion(new Texture(path)));
        style.imageUp.setMinWidth(MIN_WIDTH);
        style.imageUp.setMinHeight(MIN_HEIGHT);
        return style;
    }

}
