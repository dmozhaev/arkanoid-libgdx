package com.arkanoid.game;

import com.badlogic.gdx.graphics.Color;

public final class Constants {
    // screen width and length
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    // board length
    public static final int BOARD_WIDTH = 519;
    public static final int WALL_WIDTH = 15;
    public static final Color WALL_COLOR = Color.RED;

    // paddle
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_SPEED_INITIAL = 200;
    public static final int PADDLE_SPEED_INCREMENT = 15;
    public static final int PADDLE_SPEED_MAX = 1000;
    public static final int PADDLE_LIVES = 1;
    public static final Color PADDLE_COLOR = Color.GRAY;

    // ball
    public static final Color BALL_COLOR = Color.WHITE;
    public static final int BALL_RADIUS = 6;
    public static final int BALL_SPEED_INITIAL = 200;
    public static final int BALL_SPEED_INCREMENT = 1;
    public static final float BALL_BOUNCE_MAX_RATIO = 1.5f;
    public static final float BALL_BOUNCE_MAGIC_DIVIDER = 1999f;   // used e.g. max = PADDLE_SPEED_MAX / BALL_BOUNCE_MAGIC_DIVIDER + 1

    // blocks
    public static final int BLOCK_WIDTH = 39;
    public static final int BLOCK_HEIGHT = 15;
    public static final int BLOCK_GAP = 1;
    public static final int BLOCK_COLUMNS = 13;
    public static final int BLOCK_ROWS = 25;
    public static final int BLOCKS_TOP_Y = SCREEN_HEIGHT - (WALL_WIDTH + BLOCK_HEIGHT);

    // preferences - high scores
    public static final String PREF_FILE = "arkanoid";
    public static final String HIGH_SCORES_KEY = "highScores";
    public static final int HIGH_SCORES_SIZE = 10;
}
