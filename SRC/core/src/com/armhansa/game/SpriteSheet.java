package com.armhansa.game;

import com.badlogic.gdx.graphics.Texture;

public class SpriteSheet {
	public Texture image;
	public int scaleX=0;
	public int scaleY=0;
	public int width;
	public int height;
	public int countX;
	public int countY;
	public int lose;
	
	public SpriteSheet(Texture img, int width, int height, int countX, int countY, int lose) {
		image = img;
		this.width = width;
		this.height = height;
		this.countX = countX;
		this.countY = countY;
		this.lose = lose;
	}
	
	public void nextSprite() {
		scaleX += width;
		if(scaleX >= width*countX || (scaleX >= width*(countX-lose) && scaleY >= height*(countY-1))) {
			scaleX = 0;
			scaleY += height;
			if(scaleY >= height*countY) {
				scaleY = 0;
			}
		}
		
	}
	public void dispose() {
		image.dispose();
		
	}

}
