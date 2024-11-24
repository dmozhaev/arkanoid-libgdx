package com.arkanoid.game.game;

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

import static com.arkanoid.game.Constants.*;
import static com.arkanoid.game.Constants.PADDLE_SPEED_MAX;

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
                gameScreen.blocks.add(new Block(WALL_WIDTH + (BLOCK_WIDTH + BLOCK_GAP) * j, BLOCKS_TOP_Y - (BLOCK_HEIGHT + BLOCK_GAP) * i, gameScreen.blockTexture));
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
        Array<Wall> walls = gameScreen.walls;
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
        for (Block block: blocks) {
            if (Intersector.overlaps(ball.getCircle(), block.getRectangle())){
                gameScreen.blocksSound.play();
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(ball.getRectangle(), block.getRectangle(), intersection);

                // checking direction of the collision
                if (intersection.y == block.getY() || intersection.y + intersection.height == block.getY() + block.getHeight()) {
                    // ball touched top / bottom / corner of the block
                    ball.setY(Math.round(ball.getY() - ball.getSpeedY() * delta));
                    revertSpeedY = true;
                } else {
                    // ball touched left / right side of the block
                    ball.setX(Math.round(ball.getX() - ball.getSpeedX() * delta));
                    revertSpeedX = true;
                }

                paddle.setScore(paddle.getScore() + 1);
                blocks.removeValue(block, false);
            }
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
            if (Math.abs(speedXProjected) / Math.abs(speedYProjected) > BALL_BOUNCE_MAX_RATIO || Math.abs(speedYProjected) / Math.abs(speedXProjected) > BALL_BOUNCE_MAX_RATIO) {
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
            }
        }
    }

    /**
     * Draw / render game objects
     */
    private static Array<GameObject> collectGameObjects(GameScreen gameScreen) {
        Arkanoid game = gameScreen.game;
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;
        Array<GameObject> gameObjects = new Array<>();

        // copy static images
        gameObjects.add(gameScreen.background);
        gameObjects.add(gameScreen.panel);

        // copy game objects
        gameObjects.addAll((Array<GameObject>)(Array<?>)gameScreen.walls);
        gameObjects.add(paddle);
        gameObjects.add(ball);
        gameObjects.addAll((Array<GameObject>)(Array<?>)gameScreen.blocks);

        // add texts
        gameObjects.add(new TextBox(BOARD_WIDTH + 50, 550, "Paddle speed: " + paddle.getSpeed()));
        gameObjects.add(new TextBox(BOARD_WIDTH + 50, 500, "Ball speedX: " + Math.round(ball.getSpeedX()) + " Ball speedY: " + Math.round(ball.getSpeedY())));
        gameObjects.add(new TextBox(BOARD_WIDTH + 50, 450, "Ball ratioX: " + ball.getSpeedX() / ball.getSpeedY() + " Ball ratioY: " + ball.getSpeedY() / ball.getSpeedX()));
        gameObjects.add(new TextBox(BOARD_WIDTH + 50, 150, "Lives: " + paddle.getLives()));
        gameObjects.add(new TextBox(BOARD_WIDTH + 50, 100, "Score: " + paddle.getScore()));
        gameObjects.add(new TextBox(BOARD_WIDTH + 150, 100, "High Score: " + (game.prefs.highScores.isEmpty() ? 0 : game.prefs.highScores.getFirst())));

        return gameObjects;
    }

    public static void draw(GameScreen gameScreen) {
        Arkanoid game = gameScreen.game;
        OrthographicCamera camera = gameScreen.camera;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        for (GameObject gameObject: GameService.collectGameObjects(gameScreen)) {
            gameObject.render(game);
        }
        game.batch.end();
    }
}
