package com.arkanoid.game.util;

import com.arkanoid.game.Arkanoid;
import com.arkanoid.game.model.*;
import com.arkanoid.game.screens.GameScreen;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;

import static com.arkanoid.game.Constants.BOARD_WIDTH;

public class GameUtils {
    public static Array<Block> getCollisionBlocks(Ball ball, Array<Block> blocks) {
        // collect blocks that are hit
        Iterable<Block> collisionBlocks = blocks.select(block -> Intersector.overlaps(ball.getCircle(), block.getRectangle()));

        // collect blocks that are hit from up, down or sides
        Array<Block> collisionBlocksUpDown = new Array<>();
        Array<Block> collisionBlocksSides = new Array<>();
        for (Block block : collisionBlocks) {
            if (ball.getPrevX() - ball.getRadius() >= block.getX() && ball.getPrevX() + ball.getRadius() <= block.getX() + block.getWidth()) {
                collisionBlocksUpDown.add(block);
            } else if (ball.getPrevY() - ball.getRadius() >= block.getY() && ball.getPrevY() + ball.getRadius() <= block.getY() + block.getHeight()) {
                collisionBlocksSides.add(block);
            }
        }

        // collect blocks that are hit from corners
        Array<Block> collisionBlocksCorners = new Array<>();
        for (Block block : collisionBlocks) {
            if (!collisionBlocksUpDown.contains(block, true) &&
                    !collisionBlocksSides.contains(block, true)) {
                collisionBlocksCorners.add(block);
            }
        }

        // return them in correct order
        Array<Block> collisionBlocksCorrectOrder = new Array<>();
        collisionBlocksCorrectOrder.addAll(collisionBlocksUpDown);
        collisionBlocksCorrectOrder.addAll(collisionBlocksSides);
        collisionBlocksCorrectOrder.addAll(collisionBlocksCorners);
        return collisionBlocksCorrectOrder;
    }

    public static void takeHit(Paddle paddle, Array<Block> blocks, Block block) {
        if (!block.isInvincible()) {
            paddle.setScore(paddle.getScore() + 1);
            block.takeScoringHit();
            if (block.getHitsLeft() == 0) {
                blocks.removeValue(block, false);
            }
        }
    }

    public static void takeHitUpDown(GameScreen gameScreen, Block block, float delta) {
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;
        Array<Block> blocks = gameScreen.blocks;

        ball.setY(Math.round(ball.getY() - ball.getSpeedY() * delta));
        takeHit(paddle, blocks, block);
    }

    public static void takeHitSides(GameScreen gameScreen, Block block, float delta) {
        Ball ball = gameScreen.ball;
        Paddle paddle = gameScreen.paddle;
        Array<Block> blocks = gameScreen.blocks;

        ball.setX(Math.round(ball.getX() - ball.getSpeedX() * delta));
        takeHit(paddle, blocks, block);
    }

    public static Array<GameObject> collectGameObjects(GameScreen gameScreen) {
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
}
