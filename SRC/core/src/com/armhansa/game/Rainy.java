package com.armhansa.game;


import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Rainy {
	Array<Rectangle> rainDrops;
	long lastDropTime;
	
	public Rainy() {
		rainDrops = new Array<Rectangle>();
		lastDropTime = TimeUtils.nanoTime();
	    spawnRaindrop();
	}
	
	public void spawnRaindrop() {
	      Rectangle raindrop = new Rectangle();
	      raindrop.x = MathUtils.random(20, 1351);
	      raindrop.y = 700;
	      raindrop.width = 20;
	      raindrop.height = 20;
	      rainDrops.add(raindrop);
	      lastDropTime = TimeUtils.nanoTime();
	}
	
	public void update() {
		Iterator<Rectangle> iter = rainDrops.iterator();
	    while(iter.hasNext()) {
		    Rectangle raindrop = iter.next();
		    raindrop.x -= 300 * Gdx.graphics.getDeltaTime();
		    raindrop.y -= 1000 * Gdx.graphics.getDeltaTime();
		    if(raindrop.y + 50 < 0 || raindrop.x + 50 < 0) iter.remove();
	    }
	}

}
