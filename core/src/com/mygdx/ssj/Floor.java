package com.mygdx.ssj;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Floor extends Row {

    public Rectangle rect;

    public Floor(Array<AtlasRegion> regions) {
        this.regions = regions;
        createRow();
        rect = createRect(tiles);
    }
}
