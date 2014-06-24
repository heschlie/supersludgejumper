package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.StringBuilder

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class UI {
    private Player player;
    private Level level;
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private Stage stage;
    private Table table;
    private Table buttonTable;
    private Button leftButton;
    private Button rightButton;
    private Button jumpButton;
    private Label score;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font;
    private StringBuilder builder;

    public UI(Player player, Level level, TextureAtlas atlas) {
        this.player = player;
        this.level = level;
        this.atlas = atlas;
        builder = new StringBuilder();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
        table = new Table();
        buttonTable = new Table();

        leftButton = new Button((Drawable) atlas.findRegion("leftButton"));
        rightButton = new Button((Drawable) atlas.findRegion("rightButton"));
        jumpButton = new Button((Drawable) atlas.findRegion("jumpButton"));

        generator = new FreeTypeFontGenerator(Gdx.files.internal("kenvector_future.ttf"));
        parameter.size = 16;
        font = generator.generateFont(parameter);
        init();
    }

    private void init() {
        stage.addActor(table);
        table.setFillParent(true);
        buttonTable.add(leftButton);
        buttonTable.add(rightButton);

        // Setup table
        table.add();
        table.add();
        table.add(score);
        table.row();
        table.add();
        table.add();
        table.add();
        table.row();
        table.add(buttonTable);
        table.add();
        table.add();

    }

    public void update(float delta) {
        builder.setLength(0);
        score.setText(builder.append(player.score));
        stage.act();
    }
}
