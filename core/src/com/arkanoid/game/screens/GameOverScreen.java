package com.arkanoid.game.screens;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.arkanoid.game.Constants.SCREEN_HEIGHT;
import static com.arkanoid.game.Constants.SCREEN_WIDTH;

public class GameOverScreen implements Screen {
    final Arkanoid game;
    OrthographicCamera camera;
    Music gameOverMusic;

    public GameOverScreen(final Arkanoid gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3"));
        gameOverMusic.setLooping(true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Game over :(", 100, 150);
        game.batch.end();

        gameOverMusic.play();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            System.exit(0);
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
        gameOverMusic.dispose();
    }
}
