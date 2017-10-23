package com.armhansa.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Account {
	public String name;
	Plantation plant;
	
	public static int money = 245;
	public static int[] time = new int[5];
//	public boolean raining=false;
	public int[] seeds = {0, 0, 0};
	public static final int[][] SEED_PRICE = {{50, 60, 50}, {250, 280, 220}, {750, 800, 700}};
	
	ProductStorage storage = new ProductStorage();
	Delivery delivery = new Delivery(storage);
	Well well = new Well();
	Home home = new Home(this);
	
	public Account(Plantation plant, int[] time, String name) {
		this.name = name;
		int[] total = {6, 0, time[0], time[1], time[2]};
		Account.time = total;
		this.plant = plant;
	}
	public Account(Plantation plant) {
		this.plant = plant;
	}
	
	public void sell(int money) {
		Account.money += money;
		save();
	}
	public boolean buy(byte selectSeed, byte count) {
		if(Account.money >= SEED_PRICE[selectSeed][time[3]-1]*count) {
			Account.money -= SEED_PRICE[selectSeed][time[3]-1]*count;
			seeds[selectSeed] += count;
			return true;
		}
		else return false;
	}
	public void nextTime() {
		time[1]++;
		if(time[1] >= 60) {
			time[0]++;
			time[1] = 0;
		}
		if(time[0] >= 24) {
			time[2]++;
			time[0] = 0;
		}
		if(time[2] > (time[3]==2? 31: 30)) {
			time[3]++;
			time[2] = 1;
		}
		if(time[3] > 3) {
			time[4]++;
			time[3] = 1;
		}
	}
	public boolean sowSeed(int i, int j, byte selectSeed) {
		if(seeds[selectSeed] > 0) {
			seeds[selectSeed]--;
			plant.sowSeed(i, j, selectSeed);
			return true;
		}
		else return false;
	}
	public int getMoney() {
		return money;
	}
	public void save() {
		try {
			File filename = new File("save.armhansa");
			FileOutputStream fOutput = new FileOutputStream(filename, false);
			fOutput.write(name.length());
			char[] total = name.toCharArray();
			for(byte i=0; i<name.length(); i++) {
				fOutput.write(total[i]);
			}
			
			int tmpMoney = money;
			for(byte i=0; i<4; i++) {
				fOutput.write(tmpMoney%256);
				tmpMoney =  (int) (tmpMoney/256);
			}
			
			for(int i=0; i<3; i++) fOutput.write(seeds[i]);
			for(int i=0; i<5; i++) fOutput.write(time[i]);
			
			storage.save(fOutput);
			delivery.save(fOutput);
			well.save(fOutput);
			fOutput.write(Home.nowSleep?1:0);
			plant.save(fOutput);
			
			fOutput.close();
		} catch(IOException e) { }
	}
	public void load() {
		try {
			FileInputStream fInput = new FileInputStream("save.armhansa");
			byte count = (byte) fInput.read();
			char[] name = new char[count];
			for(byte i=0; i<count; i++) {
				name[i] = (char) fInput.read();
			}
			this.name = String.valueOf(name);

			int tmpMoney = 0;
			int countByte = 1;
			for(byte i=0; i<4; i++) {
				tmpMoney += countByte*fInput.read();
				countByte*=256;
			}
			money = tmpMoney;
			
			for(int i=0; i<3; i++) seeds[i] = fInput.read();
			for(int i=0; i<5; i++) time[i] = fInput.read();
			
			storage.load(fInput);
			delivery.load(fInput);
			well.load(fInput);
			Home.nowSleep = fInput.read()==1;
			plant.load(fInput);
			
			fInput.close();
		} catch(IOException e) { }
	}

}
