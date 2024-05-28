package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import static com.arkanoid.game.Constants.BLOCK_HEIGHT;
import static com.arkanoid.game.Constants.BLOCK_WIDTH;

public class Block extends GameObject {
    protected int x;

    protected int y;

    private int width;

    private int height;

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = BLOCK_WIDTH;
        this.height = BLOCK_HEIGHT;
        this.color = Color.FIREBRICK;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void render(final Arkanoid game) {
        game.drawer.filledRectangle(x, y, width, height, color);
    }
}
