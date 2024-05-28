package com.arkanoid.game.screens;

import com.arkanoid.game.Arkanoid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

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

        // load current high scores
        game.highScorePrefs = Gdx.app.getPreferences("Arkanoid");
        game.highScore = game.highScorePrefs.getInteger("highScore",0);

        game.highScores = new ArrayList<>();
        String serializedInts = game.highScorePrefs.getString("highScores");
        Json json = new Json();
        int[] ints = json.fromJson(int[].class, serializedInts);
        if (ints != null) {
            for (int i : ints) {
                game.highScores.add(i);
            }
        }
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

        mainMenuMusic.play();

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
