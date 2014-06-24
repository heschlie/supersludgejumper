package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by heschlie on 6/23/2014. Copyright under Iridium Flare Games LLC.
 */
public class Player extends Sprite {

    public enum movement {
        LEFT,
        RIGHT,
        STOP
    }

    private TextureAtlas atlas;
    private Level level;
    private Animation walkR;
    private Animation walkL;
    private TextureRegion standR;
    private TextureRegion standL;
    private TextureRegion jumpR;
    private TextureRegion jumpL;
    public Vector2 velocity = new Vector2();
    public float gravity = -9.5f;
    public float terminalVelocity = -7f;
    public boolean grounded = false;
    public movement movestate;
    private float time = 0f;

    // Where are you facing
    public String left = "left";
    public String right = "right";
    public String direction = right;

    public Player(TextureAtlas atlas, Level level) {
        this.atlas = atlas;
        this.level = level;
        setSize(1, 1.34f);
        walkR = new Animation(.05f, atlas.findRegions("p2_walk"), Animation.PlayMode.LOOP);
        walkL = new Animation(.05f, flipAnimation(atlas.findRegions("p2_walk")), Animation.PlayMode.LOOP);

        System.out.println(walkL.getKeyFrames().length);
        System.out.println(walkR.getKeyFrames().length);

        standR = atlas.findRegion("p2_stand");
        standL = new TextureRegion(atlas.findRegion("p2_stand"));
        standL.flip(true, false);
        jumpR = atlas.findRegion("p2_jump");
        jumpL = new TextureRegion(atlas.findRegion("p2_jump"));
        jumpL.flip(true, false);
        setRegion(standR);
        setPosition(5, 10);
        movestate = movement.STOP;
    }

    public void update(float delta) {
        time += delta;

        //player movement on keyboard for debugging
        movestate = movement.STOP;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movestate = movement.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movestate = movement.RIGHT;
        }
        if (grounded && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            velocity.y = 10;
            grounded = false;
        }

        move(delta);
    }

    public void move(float delta) {
        switch (movestate) {
            case LEFT:
                velocity.x -= 1.5f;
                if (Math.abs(velocity.x) > .3f) {
                    setRegion(walkL.getKeyFrame(time));
                }
                direction = left;
                break;
            case RIGHT:
                velocity.x += 1.5f;
                if (Math.abs(velocity.x) > .3f) {
                    setRegion(walkR.getKeyFrame(time));
                }
                direction = right;
                break;
            case STOP:
                velocity.x *= .9f;
                if (direction.equals(left)) {
                    setRegion(standL);
                }
                if (direction.equals(right)) {
                    setRegion(standR);
                }
                break;
        }
        velocity.y += gravity * delta;

        if (velocity.y < terminalVelocity) velocity.y = terminalVelocity;
        if (velocity.x > 4) velocity.x = 4;
        else if (velocity.x < -4) velocity.x = -4;

        checkCollisions();

        if (grounded) {
            velocity.y = 0f;
        }
        setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
    }

    private void checkCollisions() {
        if (getBoundingRectangle().overlaps(level.floor.rect) && getY() < 1) {
            setY(1);
            velocity.y = 0;
            grounded = true;
        }

        if (getX() < 0) {
            setX(0);
        }
        else if (getX() + getWidth() > level.camera.viewportWidth) {
            setX(level.camera.viewportWidth - getWidth());
        }
    }

    // Flips over the animation frames on the X axis
    protected Array<TextureAtlas.AtlasRegion> flipAnimation(Array<TextureAtlas.AtlasRegion> frames) {
        for(TextureRegion frame : frames) {
            frame.flip(true, false);
        }
        return frames;
    }
}
