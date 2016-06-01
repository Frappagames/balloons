package com.frappagames.balloons.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.frappagames.balloons.Balloons;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Balloons.TITLE;
		config.width = Balloons.width;
		config.height = Balloons.height;
		new LwjglApplication(new Balloons(), config);
	}
}
