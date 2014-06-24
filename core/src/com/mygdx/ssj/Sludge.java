package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/24/2014. Copyright under Iridium Flare Games LLC.
 */
public class Sludge {
    public Rectangle sludgeLevel;
    private Array<SludgeRow> sludge = new Array<SludgeRow>(32);
    public float speed = 0.05f;
    private Array<TextureRegion> top = new Array<TextureRegion>(1);
    private Array<TextureRegion> body = new Array<TextureRegion>(1);
    private float xVel = 0f;
    private float xSpeed = 1f;
    private float distance = 3f;

    public Sludge(TextureRegion top, TextureRegion body) {
        this.top.add(top);
        this.body.add(body);
        sludgeLevel = new Rectangle(0, 0, Gdx.graphics.getWidth(), .5f);
        init();
    }

    private void init() {
        for (int i = 0; i < 32; i++) {
            if (i != 0) {
                sludge.add(new SludgeRow(body, MainGame.screenWidth * 1.5f, i));
            } else {
                sludge.add(new SludgeRow(top, MainGame.screenWidth * 1.5f, -i));
            }
        }
    }

    public void update(float delta) {
        // Speed up the sludge!
        speed += delta * .01f;

        // Move the sludge up
        sludgeLevel.y += speed * delta;

        xVel += xSpeed;
        // Move the images up to match
        for (int i = 0; i < sludge.size; i++) {
            SludgeRow row = sludge.get(i);
            for (Row.Tile tile : row.tiles) {
                tile.setY(sludgeLevel.y - i);
                tile.setX(tile.startX + MathUtils.sin(xVel * MathUtils.PI / 180) * distance);
            }
        }
    }

    public void draw(Batch batch) {
        for (SludgeRow row : sludge) {
            row.draw(batch);
        }
    }
}
