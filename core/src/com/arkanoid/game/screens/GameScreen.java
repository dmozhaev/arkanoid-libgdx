package com.arkanoid.game.screens;

import com.arkanoid.game.Arkanoid;
import com.arkanoid.game.game.GameService;
import com.arkanoid.game.model.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import static com.arkanoid.game.Constants.*;

public class GameScreen implements Screen {
    public final Arkanoid game;

    public StaticImage background;
    public StaticImage panel;
    public Array<Wall> walls;
    public Paddle paddle;
    public Ball ball;
    public Array<Block> blocks;

    public OrthographicCamera camera;

    public Music ingameMusic;
    public Sound paddleSound;
    public Sound blocksSound;
    public Sound lifeLostSound;

    public Texture backgroundTexture;
    public Texture panelTexture;
    public Texture paddleTexture;
    public Texture ballTexture;
    public Texture blockTexture;
    public Texture wallSideTexture;
    public Texture wallTopTexture;

    public GameScreen(final Arkanoid game) {
        this.game = game;

        // camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        // music
        ingameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/ingame.mp3"));
        ingameMusic.setLooping(true);
        ingameMusic.play();

        // sounds
        paddleSound = Gdx.audio.newSound(Gdx.files.internal("sound/collision_paddle.wav"));
        blocksSound = Gdx.audio.newSound(Gdx.files.internal("sound/collision_blocks.wav"));
        lifeLostSound = Gdx.audio.newSound(Gdx.files.internal("sound/life_lost.mp3"));

        // textures
        backgroundTexture = new Texture(Gdx.files.internal("graphics/background.jpg"));
        panelTexture = new Texture(Gdx.files.internal("graphics/panel.jpg"));
        paddleTexture = new Texture(Gdx.files.internal("graphics/paddle.jpg"));
        ballTexture = new Texture(Gdx.files.internal("graphics/ball.png"));
        blockTexture = new Texture(Gdx.files.internal("graphics/block.jpg"));
        wallSideTexture = new Texture(Gdx.files.internal("graphics/wall_side.jpg"));
        wallTopTexture = new Texture(Gdx.files.internal("graphics/wall_top.jpg"));

        // game objects
        GameService.createGameObjects(this);
    }

    @Override
    public void render(float delta) {
        // user input processing
        GameService.processInput(this, delta);

        // time-based moves (without user input)
        GameService.processTimeBased(this, delta);

        // collision detection
        GameService.detectCollisions(this, delta);

        // ball falls down / life lost / game over
        GameService.checkFallDown(this);

        // drawing
        GameService.draw(this);
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
        ingameMusic.dispose();
        lifeLostSound.dispose();
        paddleSound.dispose();
        blocksSound.dispose();

        backgroundTexture.dispose();
        panelTexture.dispose();
        paddleTexture.dispose();
        ballTexture.dispose();
        blockTexture.dispose();
        wallSideTexture.dispose();
        wallTopTexture.dispose();
    }
}
