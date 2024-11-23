package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import static com.arkanoid.game.Constants.*;

public class Ball extends GameObject {
    private int x;

    private int y;

    private int radius;

    private float speedX;

    private float speedY;

    private float bounceRatio;

    private boolean flying;

    private Texture texture;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getBounceRatio() {
        return bounceRatio;
    }

    public void setBounceRatio(float bounceRatio) {
        this.bounceRatio = bounceRatio;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Ball(Texture texture) {
        this.x = BOARD_WIDTH / 2 + WALL_WIDTH;
        this.y = PADDLE_HEIGHT + BALL_RADIUS;
        this.radius = BALL_RADIUS;
        this.speedX = BALL_SPEED_INITIAL;
        this.speedY = BALL_SPEED_INITIAL;
        this.bounceRatio = 1f;
        this.flying = false;
        this.texture = texture;
    }

    public Circle getCircle() {
        return new Circle(x, y, radius);
    }

    public Rectangle getRectangle() {
        return new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public void render(final Arkanoid game) {
        game.batch.draw(texture, x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
