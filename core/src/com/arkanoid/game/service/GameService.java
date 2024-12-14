package com.arkanoid.game.service;

import com.arkanoid.game.Arkanoid;
import com.arkanoid.game.model.*;
import com.arkanoid.game.screens.GameOverScreen;
import com.arkanoid.game.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import static com.arkanoid.game.Constants.*;
import static com.arkanoid.game.Constants.PADDLE_SPEED_MAX;
import static com.arkanoid.game.util.GameUtils.*;
import static java.lang.Math.abs;

public class GameService {
    /**
     * Create game objects
     */
    public static void createGameObjects(GameScreen gameScreen) {
        gameScreen.background = new StaticImage(WALL_WIDTH, 0, BOARD_WIDTH, SCREEN_HEIGHT - WALL_WIDTH, gameScreen.backgroundTexture);
        gameScreen.panel = new StaticImage(2 * WALL_WIDTH + BOARD_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT, gameScreen.panelTexture);
        gameScreen.walls = new Array<>();
        gameScreen.walls.add(
                new Wall(0, 0, WALL_WIDTH, SCREEN_HEIGHT, gameScreen.wallSideTexture),                                  // left wall
                new Wall(WALL_WIDTH, SCREEN_HEIGHT - WALL_WIDTH, BOARD_WIDTH, WALL_WIDTH, gameScreen.wallTopTexture),      // top wall
                new Wall(BOARD_WIDTH + WALL_WIDTH, 0, WALL_WIDTH, SCREEN_HEIGHT, gameScreen.wallSideTexture)            // right wall
        );
        gameScreen.paddle = new Paddle(0, PADDLE_LIVES, gameScreen.paddleTexture);
        gameScreen.ball = new Ball(gameScreen.ballTexture);
        gameScreen.blocks = new Array<>();
        for (int i = 0; i < BLOCK_ROWS; i++) {
            for (int j = 0; j < BLOCK_COLUMNS; j++) {
                int hitsLeft = new int[]{-1, 1, 2}[new Random().nextInt(3)];
                if (hitsLeft != 2) {
                    gameScreen.blocks.add(new Block(WALL_WIDTH + (BLOCK_WIDTH + BLOCK_GAP) * j, BLOCKS_TOP_Y - (BLOCK_HEIGHT + BLOCK_GAP) * i, hitsLeft, gameScreen.blockTexture));
                }
            }
        }
    }

    /**
     * Input processing
     */
    public static void processInput(GameScreen gameScreen, float delta) {
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;

        // user input processing
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.exit(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            ball.setFlying(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            paddle.setDirection(PaddleDirection.LEFT);
            paddle.setX(Math.round(paddle.getX() - paddle.getSpeed() * delta));
            if (!ball.isFlying()) {
                ball.setX(Math.round(ball.getX() - paddle.getSpeed() * delta));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            paddle.setDirection(PaddleDirection.RIGHT);
            paddle.setX(Math.round(paddle.getX() + paddle.getSpeed() * delta));
            if (!ball.isFlying()) {
                ball.setX(Math.round(ball.getX() + paddle.getSpeed() * delta));
            }
        }

        // paddle speed increase
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            paddle.setSpeed(PADDLE_SPEED_INITIAL);
            paddle.setDirection(PaddleDirection.NONE);
        } else {
            paddle.setSpeed(Math.min(paddle.getSpeed() + PADDLE_SPEED_INCREMENT, PADDLE_SPEED_MAX));
        }
    }

    /**
     * Time-based moves (without user input)
     */
    public static void processTimeBased(GameScreen gameScreen, float delta) {
        Ball ball = gameScreen.ball;

        if (ball.isFlying()) {
            ball.setX(Math.round(ball.getX() + ball.getSpeedX() * delta));
            ball.setY(Math.round(ball.getY() + ball.getSpeedY() * delta));
        }
    }

    /**
     * Collision detection
     */
    public static void detectCollisions(GameScreen gameScreen, float delta) {
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;
        Array<Block> blocks = gameScreen.blocks;

        // paddle <-> left wall
        if (paddle.getX() < WALL_WIDTH){
            paddle.setX(WALL_WIDTH);
        }
        // paddle <-> right wall
        if (paddle.getX() > WALL_WIDTH + BOARD_WIDTH - PADDLE_WIDTH){
            paddle.setX(WALL_WIDTH + BOARD_WIDTH - PADDLE_WIDTH);
        }
        // ball <-> paddle
        boolean revertSpeedX = false, revertSpeedY = false;
        if (Intersector.overlaps(ball.getCircle(), paddle.getRectangle())){
            gameScreen.paddleSound.play();
            ball.setY(PADDLE_HEIGHT + BALL_RADIUS);
            revertSpeedY = true;

            // ball course change / bounce ratio from moving paddle
            float bounceRatio = paddle.getSpeed() / BALL_BOUNCE_MAGIC_DIVIDER + 1f;
            if (paddle.getDirection() == PaddleDirection.LEFT && ball.getSpeedX() < 0 || paddle.getDirection() == PaddleDirection.RIGHT && ball.getSpeedX() > 0) {
                ball.setBounceRatio(bounceRatio);
            } else if (paddle.getDirection() == PaddleDirection.LEFT && ball.getSpeedX() > 0 || paddle.getDirection() == PaddleDirection.RIGHT && ball.getSpeedX() < 0) {
                ball.setBounceRatio(1 / bounceRatio);
            }
        }
        // ball <-> blocks
        // 1 - if ball comes from up or down (oldY) -> revertY
        // 2 - if ball comes from left or right (oldX) -> revertX
        // 3 (else) - corner,
        //    a - if intersection.width >= intersection.height -> revertY
        //    b - else -> revertX
        Array<Block> collisionBlocks = getCollisionBlocks(ball, blocks);
        for (Block block : collisionBlocks) {
            gameScreen.blocksSound.play();
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(ball.getRectangle(), block.getRectangle(), intersection);

            // checking direction of the collision
            if (!revertSpeedY && ball.getPrevX() - ball.getRadius() >= block.getX() && ball.getPrevX() + ball.getRadius() <= block.getX() + block.getWidth()) {
                takeHitUpDown(gameScreen, block, delta);
                revertSpeedY = true;
            } else if (!revertSpeedX && ball.getPrevY() - ball.getRadius() >= block.getY() && ball.getPrevY() + ball.getRadius() <= block.getY() + block.getHeight()) {
                takeHitSides(gameScreen, block, delta);
                revertSpeedX = true;
            } else {
                // corner
                if (!revertSpeedY && intersection.width >= intersection.height) {
                    takeHitUpDown(gameScreen, block, delta);
                    revertSpeedY = true;
                }
                if (!revertSpeedX && intersection.width < intersection.height) {
                    takeHitSides(gameScreen, block, delta);
                    revertSpeedX = true;
                }
            }

            if (revertSpeedY && revertSpeedX) break;
        }
        // ball <-> left wall
        if (ball.getX() < WALL_WIDTH + BALL_RADIUS){
            ball.setX(WALL_WIDTH + BALL_RADIUS);
            revertSpeedX = true;
        }
        // ball <-> top wall
        if (ball.getY() > SCREEN_HEIGHT - WALL_WIDTH - BALL_RADIUS){
            ball.setY(SCREEN_HEIGHT - WALL_WIDTH - BALL_RADIUS);
            revertSpeedY = true;
        }
        // ball <-> right wall
        if (ball.getX() > WALL_WIDTH + BOARD_WIDTH - BALL_RADIUS){
            ball.setX(WALL_WIDTH + BOARD_WIDTH - BALL_RADIUS);
            revertSpeedX = true;
        }

        // ball speed increase on every collision
        if (ball.isFlying() && (revertSpeedX || revertSpeedY)) {
            int incrementX = ball.getSpeedX() > 0 ? BALL_SPEED_INCREMENT : -BALL_SPEED_INCREMENT;
            int incrementY = ball.getSpeedY() > 0 ? BALL_SPEED_INCREMENT : -BALL_SPEED_INCREMENT;
            float speedXProjected = (ball.getSpeedX() + incrementX) * ball.getBounceRatio();
            float speedYProjected = (ball.getSpeedY() + incrementY) / ball.getBounceRatio();
            if (abs(speedXProjected) / abs(speedYProjected) > BALL_BOUNCE_MAX_RATIO || abs(speedYProjected) / abs(speedXProjected) > BALL_BOUNCE_MAX_RATIO) {
                ball.setSpeedX(ball.getSpeedX() + incrementX);
                ball.setSpeedY(ball.getSpeedY() + incrementY);
            } else {
                ball.setSpeedX(speedXProjected);
                ball.setSpeedY(speedYProjected);
            }
            ball.setBounceRatio(1f);
        }

        // ball course change / speed revert if needed
        if (revertSpeedX) {
            ball.setSpeedX(-ball.getSpeedX());
        }
        if (revertSpeedY) {
            ball.setSpeedY(-ball.getSpeedY());
        }
    }

    /**
     * Check life loss (ball falls down / life lost / game over)
     */
    public static void checkFallDown(GameScreen gameScreen) {
        Arkanoid game = gameScreen.game;
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;

        if (ball.getY() < 0) {
            paddle.setLives(paddle.getLives() - 1);
            if (paddle.getLives() == 0) {
                // check high scores
                game.prefs.checkAndSaveHighScores(paddle.getScore());

                // game over
                game.setScreen(new GameOverScreen(game));
                gameScreen.dispose();
            } else {
                gameScreen.paddle = new Paddle(paddle.getScore(), paddle.getLives(), gameScreen.paddleTexture);
                gameScreen.ball = new Ball(gameScreen.ballTexture);
                gameScreen.lifeLostSound.play();
            }
        }
    }

    /**
     * Draw / render game objects
     */
    public static void draw(GameScreen gameScreen) {
        Arkanoid game = gameScreen.game;
        OrthographicCamera camera = gameScreen.camera;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        for (GameObject gameObject : collectGameObjects(gameScreen)) {
            gameObject.render(game);
        }
        game.batch.end();
    }
}
