package com.armhansa.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.armhansa.game.MyMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Plant Plants";
		config.width = 1200;
		config.height = 700;
		config.resizable = false;
		new LwjglApplication(new MyMain(), config);
	}
}
