package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import static com.arkanoid.game.Constants.BLOCK_HEIGHT;
import static com.arkanoid.game.Constants.BLOCK_WIDTH;

public class Block extends GameObject {
    protected int x;

    protected int y;

    private int width;

    private int height;

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

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Block(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = BLOCK_WIDTH;
        this.height = BLOCK_HEIGHT;
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void render(final Arkanoid game) {
        game.batch.draw(texture, x, y, width, height);
    }
}
