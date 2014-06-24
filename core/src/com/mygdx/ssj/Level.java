package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Level implements Screen {
    private Batch batch;
    public OrthographicCamera camera;
    private TextureAtlas atlas;
    private Array<Wall> walls = new Array<Wall>();
    public Floor floor;
    private Array<AtlasRegion> platformTextures;
    private Array<AtlasRegion> wallTextures;
    private AtlasRegion platStart;
    private AtlasRegion platEnd;
    private Player player;

    public Level (OrthographicCamera camera, TextureAtlas atlas) {
        this.camera = camera;
        this.atlas = atlas;
        batch = new SpriteBatch();

        platformTextures = atlas.findRegions("platform");
        wallTextures = atlas.findRegions("houseDark");
        platStart = atlas.findRegion("platformStart");
        platEnd = atlas.findRegion("platformEnd");

        this.floor = new Floor(platformTextures);
        for (int i = 0; i < 14; i++) {
            walls.add(new Wall(wallTextures));
        }

        player = new Player(atlas, this);
    }

    public void update(float delta) {
        player.update(delta);
        if (Row.lastRow < camera.position.y + camera.viewportHeight + 5) {
            walls.add(new Wall(wallTextures));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += 5 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= 5 * delta;
        }

        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        floor.draw(batch);
        for (Wall w : walls) {
            w.draw(batch);
        }
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
