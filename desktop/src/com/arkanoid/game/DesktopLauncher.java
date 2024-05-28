package com.arkanoid.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static com.arkanoid.game.Constants.SCREEN_HEIGHT;
import static com.arkanoid.game.Constants.SCREEN_WIDTH;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Arkanoid Game");
		config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
		new Lwjgl3Application(new Arkanoid(), config);
	}
}
