package com.armhansa.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class Product {
	protected float[] lastDateWater = new float[5];
	protected byte nowLevel = 0; // 0=seed,1=sapling,2=..., 3=finish
 	protected byte percentSuccess=0;
 	protected byte waitGrow = 0;
 	protected boolean growwing = false;
 	protected int[] splitPlant={0, 0};
 	protected long clockPlant;
 	
 	public boolean nowWorm = false;
 	protected boolean randomed = false;
 	protected float[] lastWormTime = new float[5];
 	protected int[] splitWorm={0, 0};
 	protected long clockWorm;
 	
 	public Product() { // New Save
 		int day, month, year, hour, minite;
 		minite = (int) Account.time[1];
 		hour = (int) Account.time[0]-4;
		day = (int) Account.time[2];
		month = (int) Account.time[3];
		year = (int) Account.time[4];
 		if(Account.time[0] < 4) {
 			hour = (int) (Account.time[0]+20)%24;
 			if(Account.time[2] == 1) {
 				day = (int) Account.time[3]==3 ? 31 : 30;
 				if(Account.time[3]== 1) {
 					year = (int) Account.time[4]-1;
 				}
 				else month = (int) Account.time[3]-1;
 			}
 			else day = (int) Account.time[2]-1;
 		}
 		
 		lastDateWater[0] = hour;
 		lastDateWater[1] = minite;
 		lastDateWater[2] = day;
 		lastDateWater[3] = month;
 		lastDateWater[4] = year;
 		clockPlant = TimeUtils.nanoTime();
 	}
 	public Product(int save) {
 		clockPlant = TimeUtils.nanoTime();
 	}
 	
 	public boolean isDehydration() {
 		int lastHourFillWater = timeToMininte((int)lastDateWater[0], (int)lastDateWater[1], (int)lastDateWater[2], (int)lastDateWater[3], (int)lastDateWater[4]);
		int nowHour = timeToMininte(Account.time[0], Account.time[1], Account.time[2], Account.time[3], Account.time[4]);
 		if(nowHour-lastHourFillWater >= 2*60) {
 			if(growwing) {
 				percentSuccess += waitGrow;
 				if(percentSuccess > 100) percentSuccess = 100;
 				nowLevel = (byte) (percentSuccess<35?0:(percentSuccess<70?1:(percentSuccess<100?2:3)));
 				waitGrow = 0;
 				growwing = false;
 			}
 			return true;
 		}
 		else {
 			return false;
 		}
 	}
 	
 	public boolean updateWorm() {
 		int lastHourWorm = timeToMininte((int)lastWormTime[0], (int)lastWormTime[1], (int)lastWormTime[2], (int)lastWormTime[3], (int)lastWormTime[4]);
		int nowHour = timeToMininte(Account.time[0], Account.time[1], Account.time[2], Account.time[3], Account.time[4]);
 		if(nowHour-lastHourWorm >= 48*60) {
 			return true;
 		}
 		if(randomed && nowWorm) {
 			if(TimeUtils.nanoTime()-clockWorm >= 100000000) {
	 			splitWorm[0] += 400;
	 	 		if(splitWorm[0] >= 1600) {
	 	 			splitWorm[0] = 0;
	 	 			splitWorm[1] += 400;
	 	 		}
	 	 		if(splitWorm[1] >= 800) {
	 	 			splitWorm[0] = 0;
	 	 			splitWorm[1] = 0;
	 	 		}
	 	 		clockWorm = TimeUtils.nanoTime();
 			}
 		}
 		return false;
 	}
 	public boolean isFinished() {
 		if(percentSuccess >= 100) {
 			if(!randomed && MathUtils.randomBoolean(0.1f)) {
 				int day, month, year, hour, minite;
 		 		minite = (int) Account.time[1];
 		 		hour = (int) Account.time[0];
 				day = (int) Account.time[2];
 				month = (int) Account.time[3];
 				year = (int) Account.time[4];
 		 		if(Account.time[0] < 4) {
 		 			hour = (int) (Account.time[0]+20)%24;
 		 			if(Account.time[2] == 1) {
 		 				day = (int) Account.time[3]==3 ? 31 : 30;
 		 				if(Account.time[3]== 1) {
 		 					year = (int) Account.time[4]-1;
 		 				}
 		 				else month = (int) Account.time[3]-1;
 		 			}
 		 			else day = (int) Account.time[2]-1;
 		 		}
 		 		lastWormTime[0] = hour;
 		 		lastWormTime[1] = minite;
 		 		lastWormTime[2] = day;
 		 		lastWormTime[3] = month;
 		 		lastWormTime[4] = year;
 				nowWorm = true;
 			}
 			randomed = true;
 			return true;
 		}
 		else return false;
 	}
 	public void fillWater() {
 		lastDateWater[0] = Account.time[0]/1;
 		lastDateWater[1] = Account.time[1]/1;
 		lastDateWater[2] = Account.time[2]/1;
 		lastDateWater[3] = Account.time[3]/1;
 		lastDateWater[4] = Account.time[4]/1;
 		growwing = true;
 		waitGrow = growUp();
 	}
 	public int[] getSplit() {
 		if(TimeUtils.nanoTime()-clockPlant >= 200000000) {
 			splitPlant[1] = 200*nowLevel;
 			splitPlant[0] += 200;
 	 		if(splitPlant[0] >= 600) splitPlant[0] = 0;
 	 		clockPlant = TimeUtils.nanoTime();
 		}
 		return splitPlant;
 	}
 	private int timeToMininte(int hour, int min, int day, int month, int year) {
		switch(month) {
	 		case 2 : day += 30;break;
	 		case 3: day += 61;break;
 		}
		return ((year*91+day)*24+hour)*60+min;
	}
 	public abstract void save(FileOutputStream fOutput);
 	public abstract void load(FileInputStream fInput);
 	public abstract byte getCountLeft ();
 	public abstract int sell();
 	public abstract byte growUp();
 	public abstract Texture getPlantImage();
 	public abstract Texture getProductImage();
 	public abstract int getSellPrice();
 	public abstract byte getType();

}
