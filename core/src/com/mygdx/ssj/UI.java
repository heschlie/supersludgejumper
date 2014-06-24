package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.Timer;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class UI {
    private Player player;
    private Level level;
    private TextureAtlas atlas;
    private Stage stage;
    private Table table;
    private Table buttonTable;
    private Button leftButton;
    private Button rightButton;
    private Button jumpButton;
    private TextureRegionDrawable leftButtonImage;
    private TextureRegionDrawable rightButtonImage;
    private TextureRegionDrawable jumpButtonImage;
    private Label score;
    private Label averageSpeed;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont fontBig;
    private BitmapFont fontMed;
    private BitmapFont fontSmall;
    private StringBuilder builder;
    private float screenWidth, screenHeight;
    private MyTouchListener touchListener = new MyTouchListener();
    private float speed = 0f;
    private Array<Float> speedTracker = new Array<Float>();
    private DecimalFormat df = new DecimalFormat("##.##");

    public UI(final Player player, Level level, TextureAtlas atlas) {
        this.player = player;
        this.level = level;
        this.atlas = atlas;
        builder = new StringBuilder();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        leftButtonImage = new TextureRegionDrawable(atlas.findRegion("leftButton"));
        rightButtonImage = new TextureRegionDrawable(atlas.findRegion("rightButton"));
        jumpButtonImage = new TextureRegionDrawable(atlas.findRegion("jumpButton"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.debug();
        buttonTable = new Table();

        leftButton = new Button(leftButtonImage);
        rightButton = new Button(rightButtonImage);
        jumpButton = new Button(jumpButtonImage);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("kenvector_future.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = MathUtils.ceil(screenWidth * .03f);
        fontBig = generator.generateFont(parameter);
        parameter.size = MathUtils.ceil(screenWidth * .02f);
        fontMed = generator.generateFont(parameter);
        parameter.size = MathUtils.ceil(screenWidth * .01f);
        fontSmall = generator.generateFont(parameter);

        score = new Label("", new Label.LabelStyle(fontBig, Color.BLACK));
        score.setAlignment(Align.right);

        averageSpeed = new Label("", new Label.LabelStyle(fontMed, Color.BLACK));
        averageSpeed.setAlignment(Align.center);

        df.setRoundingMode(RoundingMode.UP);

        init();

        Timer.schedule(new Timer.Task() {
            private float average;

            @Override
            public void run() {
                speedTracker.add(player.velocity.y);
                average = 0f;
                for (float vel : speedTracker) {
                    average += vel;
                }
                average = average / speedTracker.size;
                if (speedTracker.size == 16)
                    speedTracker.removeIndex(15);

                speed = average;
            }
        }, 1f, 1f);
    }

    private void init() {
        stage.addActor(table);
        table.setFillParent(true);
        buttonTable.add(leftButton).size(screenWidth * .17f, screenWidth * .17f);
        buttonTable.add(rightButton).size(screenWidth * .17f, screenWidth * .17f);

        // Setup table
        table.row().height(screenHeight * .05f);
        table.add().width(screenWidth * .25f);
        table.add().expandX();
        table.add().width(screenWidth * .25f);
        table.row().expandY();
        table.add();
        table.add();
        table.add(score).top().right().width(screenWidth * .25f);
        table.row();
        table.add(buttonTable);
        table.add(averageSpeed);
        table.add(jumpButton).bottom().right().size(screenWidth * .17f, screenWidth * .17f);

        // Button actions
        leftButton.addListener(touchListener);
        rightButton.addListener(touchListener);
        jumpButton.addListener(touchListener);

    }

    public void update(float delta) {
        builder.setLength(0);
        builder.append("Score\n").append(player.score);
        score.setText(builder);

        builder.setLength(0);
        builder.append("Speed: ").append(df.format(speed)).append("m/s");
        averageSpeed.setText(builder);

        stage.act(delta);
        stage.draw();
//        table.drawDebug(stage);
    }

    public class MyTouchListener extends InputListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (event.getTarget() == leftButton) {
                player.movestate = Player.movement.LEFT;
            }
            if (event.getTarget() == rightButton) {
                player.movestate = Player.movement.RIGHT;
            }
            if (event.getTarget() == jumpButton) {
                player.jump();
            }
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (event.getTarget() == leftButton) {
                player.movestate = Player.movement.STOP;
            }
            if (event.getTarget() == rightButton) {
                player.movestate = Player.movement.STOP;
            }
        }
    }

    public void dispose() {
        generator.dispose();
        fontBig.dispose();
    }
}
