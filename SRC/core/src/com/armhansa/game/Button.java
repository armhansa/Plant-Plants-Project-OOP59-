package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Button {
	Pixmap[] pm = new Pixmap[5];
	
	// Tool Button
	Circle[] btn = new Circle[5];
	static byte nowSelectTool = 0;
	boolean[] justTouchedBtn = {true, true, true, true, true};
	byte touchedBtn;
	int mouseX;
	int mouseY;
	
	// Seed Button
	Rectangle[] seedRect = new Rectangle[3];
	static byte nowSelectSeed = 0;
	boolean[] justTouchedSeed = {true, true, true};
	byte touchedSeed;
	
	public Button() {
		for(int i=0; i<3; i++) seedRect[i] = new Rectangle(92+90*i, 8, 46, 67);
		for(int i=0; i<5; i++) btn[i] = new Circle(24+28, 500-80*i+44, 38.4f);
		pm[0] = new Pixmap(Gdx.files.internal("img/cursor-1.png"));
		pm[1] = new Pixmap(Gdx.files.internal("img/cursor-2.png"));
//		pm[2] = new Pixmap(Gdx.files.internal("cursor-1-2.png"));
//		pm[3] = new Pixmap(Gdx.files.internal("cursor-1-3.png"));
//		pm[4] = new Pixmap(Gdx.files.internal("cursor-2.png"));
		
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm[Button.nowSelectTool], 0, 0));
	}
	
	public void checkAction() {
		for(byte i=0;i<5;i++) {
			if(btn[i].overlaps(new Circle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f))) {
				touchedBtn = i;
				if(justTouchedBtn[i] && i != nowSelectTool) MyGame.allSound[1].play();
				justTouchedBtn[i] = false;
				if(Gdx.input.isTouched() && Gdx.input.justTouched() && i != nowSelectTool) {
					MyGame.allSound[1].play();
					nowSelectTool = i;
					Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm[nowSelectTool], 0, 0));
					if(i==0) { mouseX = Gdx.input.getX();mouseY = Gdx.input.getY(); }
				}
			}
			else justTouchedBtn[i] = true;
			if((nowSelectTool == 0 || nowSelectTool == 1) && i < 3) {
				if(seedRect[i].overlaps(new Rectangle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f, 0.5f))) {
					touchedSeed = i;
					if(justTouchedSeed[i] && i != nowSelectSeed) MyGame.allSound[1].play();
					justTouchedSeed[i] = false;
					if(nowSelectTool == 1 && Gdx.input.justTouched() && i != nowSelectSeed) {
						MyGame.allSound[1].play();
						nowSelectSeed = i;
					}
					else if(nowSelectTool == 0 && Gdx.input.justTouched()) {
						nowSelectSeed = i;
						BuySeed.nowBuySeed = true;
					}
				}
				else justTouchedSeed[i] = true;
			}
		}
	}

}
