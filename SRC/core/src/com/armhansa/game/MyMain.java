package com.armhansa.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MyMain extends Game {
	public SpriteBatch batch;
	public static int width = 1200;
	public static int height = 700;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MyMainMenu(this));
	}

	@Override
	public void render () {
		super.render(); //important!
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	public BitmapFont setStyleFont(int size, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arista.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = color;
		return generator.generateFont(parameter);
	}
}
