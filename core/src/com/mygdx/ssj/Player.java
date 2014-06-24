package com.mygdx.ssj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    public float terminalVelocity = -10f;
    private float maxSpeed = 5f;
    public boolean grounded = false;
    public boolean goingUp = false;
    public movement movestate;
    private float time = 0f;
    public int score = 0;
    private float fixedDelta = 0f;


    // Feet!
    public Array<Rectangle> feet = new Array<Rectangle>(2);
    public boolean[] feetTouching = {false, false};

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
        setPosition(7, 3);
        createFeet();
        movestate = movement.STOP;
    }

    public void createFeet() {
        feet.add(new Rectangle(getX() + getWidth() * .15f, getY() - .5f, .05f, 1f));
        feet.add(new Rectangle(getX() + getWidth() * .85f, getY() - .5f, .05f, 1f));
    }

    public void setFeet() {
        feet.get(0).setPosition(getX() + getWidth() * .15f, getY() - .5f);
        feet.get(1).setPosition(getX() + getWidth() * .85f, getY() - .5f);
    }

    public void jump() {
        if (grounded) {
            velocity.y = 15f;
            grounded = false;
        }
    }

    public void update(float delta) {
        time += delta;
        score = Math.max(MathUtils.floor(getY() / 10f), score);
        setFeet();

        if (velocity.y > 0 && !goingUp) {
            goingUp = true;
        } else if (velocity.y <= 0 && goingUp){
            goingUp = false;
        }

        //player movement on keyboard for debugging
        //movestate = movement.STOP;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movestate = movement.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movestate = movement.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump();
        }
        fixedDelta = (delta > 1/30f) ? 1/30f : delta;
        move(fixedDelta);
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
        if (!grounded && direction.equals(left)) setRegion(jumpL);
        if (!grounded && direction.equals(right)) setRegion(jumpR);
        velocity.y += gravity * delta;

        if (velocity.y < terminalVelocity) velocity.y = terminalVelocity;
        if (velocity.x > maxSpeed) velocity.x = maxSpeed;
        else if (velocity.x < -maxSpeed) velocity.x = -maxSpeed;

        checkCollisions(delta);

        if (grounded) {
            velocity.y = 0f;
        }
        setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
    }

    private void checkCollisions(float delta) {

        feetTouching[0] = false;
        feetTouching[1] = false;
        for (int i = 0; i < feetTouching.length; i++) {
            for (Platform platform : level.platforms) {
                if (feet.get(i).overlaps(platform.rect)) {
                    feetTouching[i] = true;
                    if (getY() > platform.rect.getY() + platform.rect.getHeight() && !goingUp &&
                            getBoundingRectangle().setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta).overlaps(platform.rect)) {
                        setY(platform.rect.getY() + platform.rect.getHeight());
                        grounded = true;
                    }
                    break;
                }
            }
            if (feet.get(i).overlaps(level.floor.rect)) {
                feetTouching[i] = true;
            }
        }

        // If neither foot touched anything, we're no longer grounded
        if (!feetTouching[0] && !feetTouching[1]) {
            grounded = false;
        }

        if (getBoundingRectangle().overlaps(level.floor.rect) && getY() < 3) {
            setY(3);
            velocity.y = 0;
            grounded = true;
        }

        // Keep the player on the screen
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
