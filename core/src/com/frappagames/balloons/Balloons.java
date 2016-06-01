package com.frappagames.balloons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frappagames.balloons.Tools.Assets;
import com.frappagames.balloons.Tools.Settings;
import com.frappagames.balloons.screens.SplashScreen;

public class Balloons extends Game {
	public static final String TITLE = "Ballons et couleurs";
	public static int width = 1280;
	public static int height = 720;
	public SpriteBatch batch;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();

		Assets.load();
		Settings.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose()
	{
		Assets.dispose();
	}
}
