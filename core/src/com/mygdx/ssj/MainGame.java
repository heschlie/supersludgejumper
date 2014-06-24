package com.mygdx.ssj;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class MainGame extends Game {
    private Level level;
    public TextureAtlas atlas;
    public OrthographicCamera camera;
    public float screenWidth = 15f;
    public float screenHeight = screenWidth * 1.77f;

    @Override
    public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("ssj.atlas"));
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(screenWidth / 2f, screenHeight / 2f, 0);
        camera.update();
        level = new Level(camera, atlas);
        setScreen(level);
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        level.dispose();
    }
}
