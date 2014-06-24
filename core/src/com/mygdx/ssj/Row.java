package com.mygdx.ssj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Row {
    public static int lastRow = 0;
    public Array<Tile> tiles = new Array<Tile>(10);
    protected Array<AtlasRegion> regions;

    protected class Tile extends Sprite {

        public Tile(TextureRegion region, float x, float y) {
            setRegion(region);
            setSize(1, 1);
            setPosition(x, y);
        }
    }

    public Row() {}

    public Rectangle createRect(Array<Tile> tiles) {
        Rectangle rect = new Rectangle();
        for (Tile tile : tiles) {
            rect.merge(tile.getBoundingRectangle());
        }
        return rect;
    }

    public void createRow() {
        for (int i = 0; i < 10; i++) {
            tiles.add(new Tile(regions.random(), i, lastRow));
        }
        lastRow++;
    }

    public void draw(Batch batch) {
        for (Tile tile : tiles) {
            tile.draw(batch);
        }
    }
}
