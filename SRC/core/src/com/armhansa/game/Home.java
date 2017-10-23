package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Home {
	Account account;
	Texture[] homeImg = new Texture[2];
	static boolean nowSleep = false;
	Polygon homePolygon;
	static boolean nowOverlap;
	float[] timeSleep = new float[2]; // hour and day
	Polygon mousePolygon;
	
	public Home(Account account) {
		this.account = account;
		homeImg[0] = new Texture(Gdx.files.internal("img/building/house.png"));
		homeImg[1] = new Texture(Gdx.files.internal("img/building/select-house.png"));
		homePolygon = new Polygon();
		mousePolygon = new Polygon();
	}
	
	public void sleep() {
		timeSleep[0] = Account.time[0]/1;
		timeSleep[1] = Account.time[2]/1;
		nowSleep = true;
	}

	public void updatePositionAndCheck() {
		float[] tmp = {19.5f, 109, 19.5f, 180, 205.5f, 258, 272, 166.5f, 272, 94.5f, 150, 33};
		float[] vertices = new float[12];
		for(int i=0; i<6; i++) {
			vertices[i*2] = MyGame.zoom*(950+tmp[i*2])+MyGame.moveX;
			vertices[i*2+1] = MyGame.zoom*(650+tmp[i*2+1])+MyGame.moveY;
		}
		homePolygon.setVertices(vertices);
		mouseToPolygon();
		
		if(!BuySeed.nowBuySeed && Intersector.overlapConvexPolygons(mousePolygon, homePolygon)) {
			nowOverlap = true;
			if(Gdx.input.justTouched()) {
				sleep();
			}
		}
		else nowOverlap = false;
	}
	public void update() {
		if((timeSleep[0] < 6 && Account.time[0] == timeSleep[0]+7) || (Account.time[2] != timeSleep[1] && Account.time[0] == 6)) {
 			nowSleep = false;
 			account.save();
 		}
	}
	private void mouseToPolygon() {
		float x = Gdx.input.getX();
		float y = MyMain.height-Gdx.input.getY();
		float[] vertices = {x-1, y-1, x-1, y+1, x+1, y+1, x+1, y-1};
		mousePolygon.setVertices(vertices);
	}
	
}
