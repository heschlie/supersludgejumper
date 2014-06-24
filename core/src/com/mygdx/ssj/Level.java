package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Level implements Screen {
    private Batch batch;
    public OrthographicCamera camera;
    private TextureAtlas atlas;
    private Array<Wall> walls = new Array<Wall>();
    public Array<Platform> platforms = new Array<Platform>();
    public Floor floor;
    private Array<TextureRegion> platformTextures;
    private Array<TextureRegion> wallTextures;
    private AtlasRegion platStart;
    private AtlasRegion platEnd;
    private Player player;
    private ShapeRenderer sr;

    public Level (OrthographicCamera camera, TextureAtlas atlas) {
        this.camera = camera;
        this.atlas = atlas;
        batch = new SpriteBatch();

        platformTextures = convertRegions(atlas.findRegions("platform"));
        wallTextures = convertRegions(atlas.findRegions("houseDark"));
        platStart = atlas.findRegion("platformStart");
        platEnd = atlas.findRegion("platformEnd");

        this.floor = new Floor(platformTextures, camera.viewportWidth);
        for (int i = 0; i < 14; i++) {
            walls.add(new Wall(wallTextures, camera.viewportWidth));
        }
        while (Row.lastPlatform < camera.viewportHeight) {
            platforms.add(new Platform(platformTextures, platStart, platEnd, camera.viewportWidth));
            System.out.println(Row.lastPlatform);
        }

        player = new Player(atlas, this);
        sr = new ShapeRenderer();
    }

    public void update(float delta) {
        player.update(delta);
        if (Row.lastRow < camera.position.y + camera.viewportHeight + 5) {
            walls.add(new Wall(wallTextures, camera.viewportWidth));
        }

        if (Row.lastPlatform < camera.position.y + camera.viewportHeight + 7) {
            platforms.add(new Platform(platformTextures, platStart, platEnd, camera.viewportWidth));
        }

        if (player.getY() > camera.viewportHeight * .5f) {
            camera.position.y = player.getY();
        }

        camera.update();
    }

    private Array<TextureRegion> convertRegions(Array<AtlasRegion> atlasRegions) {
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (AtlasRegion a : atlasRegions) {
            regions.add(a);
        }
        return regions;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
        batch.begin();
        floor.draw(batch);
        for (Wall w : walls) {
            w.draw(batch);
        }
        for (Platform platform : platforms) {
            platform.draw(batch);
        }
        player.draw(batch);
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.MAGENTA);
        for (Platform plat : platforms) {
            sr.rect(plat.rect.getX(),
                    plat.rect.getY(),
                    plat.rect.getWidth(),
                    plat.rect.getHeight());
        }
        sr.setColor(Color.RED);
        sr.rect(floor.rect.getX(),
                floor.rect.getY(),
                floor.rect.getWidth(),
                floor.rect.getHeight());
        sr.end();
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
