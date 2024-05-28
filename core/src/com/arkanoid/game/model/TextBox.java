package com.arkanoid.game.model;

import com.arkanoid.game.Arkanoid;

public class TextBox extends GameObject {
    private int x;

    private int y;

    private String text;

    public TextBox(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void render(final Arkanoid game) {
        game.font.draw(game.batch, text, x, y);
    }
}
