package com.mygdx.ssj;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/24/2014. Copyright under Iridium Flare Games LLC.
 */
public class SludgeRow extends Row {
    private int yStart;

    public SludgeRow(Array<TextureRegion> regions, float screenWidth, int yStart) {
        super(screenWidth);
        this.yStart = yStart;
        tiles = new Array<Tile>(MathUtils.ceil(screenWidth));
        this.regions = regions;
        createRow();
    }

    @Override
    public void createRow() {
        for (int i = 0; i <= MathUtils.ceil(screenWidth); i++) {
            tiles.add(new Tile(regions.get(0), i - screenWidth * 0.2f, yStart));
        }
    }
}
