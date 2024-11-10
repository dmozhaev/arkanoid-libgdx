package com.arkanoid.game;

import com.arkanoid.game.model.Prefs;
import com.arkanoid.game.screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Arkanoid extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public ShapeDrawer drawer;
	private Pixmap pixmap;
	private Texture texture;
	private TextureRegion region;

	public Prefs prefs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();

		// drawer
		pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
		pixmap.drawPixel(0, 0);
		texture = new Texture(pixmap);
		pixmap.dispose();
		region = new TextureRegion(texture, 0, 0, 1, 1);
		drawer = new ShapeDrawer(batch, region);

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render(); // important!
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		pixmap.dispose();
		texture.dispose();
	}
}
