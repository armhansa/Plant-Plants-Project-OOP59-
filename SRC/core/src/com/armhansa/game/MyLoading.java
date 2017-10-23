package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class MyLoading implements Screen {
	final MyMain MAIN;
	Account account;
	Texture loadingImg;
	int countLeft = 0;
	public MyLoading(MyMain main, Account account) {
		this.MAIN = main;
		
		this.account = account;
		loadingImg = new Texture(Gdx.files.internal("img/menu/loading.jpg"));
		
	}
	
	@Override
	public void render(float delta) {
		MAIN.batch.begin();
		MAIN.batch.draw(loadingImg, 0, 0, 1200, 700);
		MAIN.batch.end();
		
		countLeft++;
		if(countLeft>1) {
			account.load();
			MAIN.setScreen(new MyGame(MAIN, account));
		}
		
	}

	@Override
	public void dispose() {
		
	}
	@Override
	public void resize(int width, int height) {	}
	@Override
	public void pause() { }
	@Override
	public void resume() { }
	@Override
	public void show() { }
	@Override
	public void hide() { }

}
