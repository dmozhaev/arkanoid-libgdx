package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import static com.arkanoid.game.Constants.*;

public class Paddle extends GameObject {
    protected int x;

    protected int y;

    private int width;

    private int height;

    private int speed;

    private PaddleDirection direction;

    private int score;

    private int lives;

    private Color color;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public PaddleDirection getDirection() {
        return direction;
    }

    public void setDirection(PaddleDirection direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Paddle(int score, int lives) {
        this.x = BOARD_WIDTH / 2 - PADDLE_WIDTH / 2 + WALL_WIDTH;
        this.y = 0;
        this.width = PADDLE_WIDTH;
        this.height = PADDLE_HEIGHT;
        this.speed = PADDLE_SPEED_INITIAL;
        this.direction = PaddleDirection.NONE;
        this.score = score;
        this.lives = lives;
        this.color = PADDLE_COLOR;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void render(final Arkanoid game) {
        game.drawer.filledRectangle(x, y, width, height, color);
    }
}
