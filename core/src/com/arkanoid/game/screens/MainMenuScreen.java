package com.arkanoid.game.screens;

import com.arkanoid.game.Arkanoid;
import com.arkanoid.game.model.Prefs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.arkanoid.game.Constants.SCREEN_HEIGHT;
import static com.arkanoid.game.Constants.SCREEN_WIDTH;

public class MainMenuScreen implements Screen {
    final Arkanoid game;
    OrthographicCamera camera;
    Music mainMenuMusic;

    public MainMenuScreen(final Arkanoid gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.play();

        // load current high scores list
        game.prefs = new Prefs();
        game.prefs.initHighScores();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Arkanoid!!! ", 100, 150);
        game.font.draw(game.batch, "Tap any key to begin!", 100, 100);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        mainMenuMusic.dispose();
    }
}
