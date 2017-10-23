package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
	
	public boolean keyDown (int keycode) {
		return false;
	}

	public boolean keyUp (int keycode) {
		return false;
	}

	public boolean keyTyped (char character) {
		return false;
	}

	public boolean touchDown (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchUp (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	public boolean mouseMoved (int x, int y) {
		return false;
	}

	public boolean scrolled (int amount) {
	   if(amount == 1 && MyGame.zoom-3*Gdx.graphics.getDeltaTime() > 1) {
		   MyGame.zoom -= 3*Gdx.graphics.getDeltaTime();
		   MyGame.moveX += 3000*Gdx.graphics.getDeltaTime()/(MyGame.zoom);
	   }
	   else if(amount == -1 && MyGame.zoom+3*Gdx.graphics.getDeltaTime() < 2){
		   MyGame.zoom += 3*Gdx.graphics.getDeltaTime();
		   MyGame.moveX -= 3000*Gdx.graphics.getDeltaTime()/(MyGame.zoom);
	   }
	   if(MyGame.moveX < MyGame.zoom*MyGame.zoom*(-600)+10) {
		   MyGame.moveX = MyGame.zoom*MyGame.zoom*(-600)+10;
	   } else if(MyGame.moveX > 0) MyGame.moveX = 0;
	   
	   if(MyGame.moveY < MyGame.zoom*MyGame.zoom*(-350)+10){
		   MyGame.moveY = MyGame.zoom*MyGame.zoom*(-350)+10;
	   } else if(MyGame.moveY > 0) MyGame.moveY = 0;
	   return false;
	}
	
}