package com.mygdx.ssj;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Floor extends Row {

    public Rectangle rect;

    public Floor(Array<TextureRegion> regions, float screenWidth) {
        super(screenWidth);
        tiles = new Array<Tile>(MathUtils.ceil(screenWidth));
        this.regions = regions;
        createRow();
        createRow();
        createRow();
        rect = createRect(tiles);
    }
}
