package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import static com.arkanoid.game.Constants.*;

public class Block extends GameObject {
    protected int x;

    protected int y;

    private int width;

    private int height;

    private Texture texture;

    private int hitsLeft;

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

    public int getHitsLeft() {
        return hitsLeft;
    }

    public void setHitsLeft(int hitsLeft) {
        this.hitsLeft = hitsLeft;
    }

    public void takeScoringHit() {
        this.hitsLeft--;
    }

    public boolean isInvincible() {
        return this.hitsLeft == -1;
    }

    public Block(int x, int y, int hitsLeft, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = BLOCK_WIDTH;
        this.height = BLOCK_HEIGHT;
        this.hitsLeft = hitsLeft;
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void render(final Arkanoid game) {
        game.batch.draw(texture, x, y, width, height);
        if (DEV_INFO_ON) {
            game.font.draw(game.batch, String.valueOf(this.hitsLeft), x + 17, y + 13);
        }
    }
}
