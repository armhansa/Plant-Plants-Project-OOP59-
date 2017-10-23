package com.armhansa.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Bean extends Product {
 	private final int[] SELL_PRICE = {75, 70, 80};
 	private final float[] MAX_GROW = {100/4, 100/5, 100/7};
 	private final float[] MIN_GROW = {100/8, 100/7, 100/9};
 	private final byte TYPE = 0;
 	private byte countLeft = 1;
 	private Texture plantImage;
 	private Texture productImage;
 	
 	public Bean() {
 		super();
 		plantImage = new Texture(Gdx.files.internal("img/plant/bean-step.png"));
 		productImage = new Texture(Gdx.files.internal("img/product/bean.png"));
 		
 	}
	
 	public void save(FileOutputStream fOutput) {
 		try {
 			fOutput.write(TYPE);
 			fOutput.write(nowLevel);
 			fOutput.write(percentSuccess);
 			fOutput.write((byte) (growwing?1:0));
 			fOutput.write(waitGrow);
 			fOutput.write(countLeft);
 			fOutput.write(randomed?1:0);
 			fOutput.write(nowWorm?1:0);
 			for(int k=0; k<5; k++) fOutput.write((int) lastWormTime[k]);
 			for(int k=0; k<5; k++) fOutput.write((int) lastDateWater[k]);
 			
 		} catch(IOException e) { }
 	}
 	public void load(FileInputStream fInput) {
 		try {
 			nowLevel = (byte) fInput.read();
 			percentSuccess = (byte) fInput.read();
 			growwing = fInput.read()==01?true:false;
 			waitGrow = (byte) fInput.read();
 			countLeft = (byte) fInput.read();
 			randomed = fInput.read()==1;
 			nowWorm = fInput.read()==1;
 			for(int k=0; k<5; k++) lastWormTime[k] = fInput.read();
 			for(int k=0; k<5; k++) lastDateWater[k] = fInput.read();
 			
 		} catch(IOException e) { }
 	}
 	public int sell() {
		countLeft--;
		percentSuccess = 75;
 		return SELL_PRICE[Account.time[3]-1];
 	}
	public byte getCountLeft() { return countLeft; }
	public byte getType() { return TYPE; }
	public byte growUp() { return (byte) MathUtils.random((int) MIN_GROW[Account.time[3]-1], (int) MAX_GROW[Account.time[3]-1]); }
	public Texture getPlantImage() { return plantImage; }
	public Texture getProductImage() { return productImage; }
	public int getSellPrice() { return SELL_PRICE[Account.time[3]-1]; }

}
