package com.mygdx.ssj;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Platform extends Row {
    private TextureRegion platStart;
    private TextureRegion platEnd;
    private int startX;
    private float startY;
    private int platWidth;
    public Rectangle rect;

    public Platform(Array<TextureRegion> regions, TextureRegion platStart,TextureRegion platEnd ,float screenWidth) {
        super(screenWidth);
        this.regions = regions;
        tiles = new Array<Tile>();
        this.platStart = platStart;
        this.platEnd = platEnd;
        createRow();
        rect = createRect(tiles);
    }

    @Override
    public void createRow() {
        platWidth = MathUtils.random(1, 5);
        startX = MathUtils.random(0, MathUtils.ceil(screenWidth)) - platWidth;
        startY = MathUtils.random(300, 800) * .01f + lastPlatform;
        tiles.add(new Tile(platStart, startX, startY));
        for (int i = startX + 1; i < platWidth + startX; i++) {
            tiles.add(new Tile(regions.random(), i, startY));
        }
        tiles.add(new Tile(platEnd, tiles.size + startX, startY));
        lastPlatform = startY;
    }
}
