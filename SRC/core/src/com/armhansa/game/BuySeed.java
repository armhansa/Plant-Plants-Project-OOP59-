package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class BuySeed {
	Account account;
	static boolean nowBuySeed = false;
	Texture bgImg;
	Texture priceTagImg;
	Texture headImg;
	Texture darkBackgroundImg;
	Texture[][] buyImg = new Texture[4][2];
	byte countOrder = 1;
	Rectangle dialogBuy = new Rectangle(400, 225, 400, 250);
//	Rectangle buySeedRect = new Rectangle(565, 305, 50, 20);
	Circle yesBuy = new Circle(650+45/2, 255+45/2, 45/2);
	boolean touchedYes = false;
//	Rectangle closeBtn = new Rectangle(630, 380, 30, 30);
	Circle noBuy = new Circle(770+36/2, 445+36/2, 36/2);
//	boolean touchedClose = false;
	boolean touchedNo = false;
	Circle addOrder = new Circle(710+30/2, 340+30/2, 30/2);
	boolean touchedAdd = false;
	Circle reduceOrder = new Circle(590+30/2, 340+30/2, 30/2);
	boolean touchedReduce = false;
	
	public BuySeed(Account account) {
		this.account = account;
		bgImg = new Texture(Gdx.files.internal("img/buySeed/bg.png"));
		priceTagImg = new Texture(Gdx.files.internal("img/buySeed/pricetable.png"));
		headImg = new Texture(Gdx.files.internal("img/buySeed/head.png"));
		darkBackgroundImg = new Texture(Gdx.files.internal("img/buySeed/dark-background.png"));
		
		buyImg[0][0] = new Texture(Gdx.files.internal("img/buySeed/btn-(-).png"));
		buyImg[1][0] = new Texture(Gdx.files.internal("img/buySeed/btn-(+).png"));
		buyImg[2][0] = new Texture(Gdx.files.internal("img/buySeed/btn-check.png"));
		buyImg[3][0] = new Texture(Gdx.files.internal("img/buySeed/btn-cross.png"));
		
		buyImg[0][1] = new Texture(Gdx.files.internal("img/buySeed/btn-selected-(-).png"));
		buyImg[1][1] = new Texture(Gdx.files.internal("img/buySeed/btn-selected-(+).png"));
		buyImg[2][1] = new Texture(Gdx.files.internal("img/buySeed/btn-selected-check.png"));
		buyImg[3][1] = new Texture(Gdx.files.internal("img/buySeed/btn-selected-cross.png"));
		
		
	}
	
	public void checkAction() {
		if(noBuy.overlaps(new Circle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f))) {
			touchedNo = true;
			if(Gdx.input.justTouched()) {
				nowBuySeed = false;
			}
		}
		else touchedNo = false;
		if(yesBuy.overlaps(new Circle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f))) {
			touchedYes = true;
			if(Gdx.input.justTouched()) {
				boolean success = account.buy((byte) Button.nowSelectSeed, countOrder);
				if(success) nowBuySeed = false;
			}
		}
		else touchedYes = false;
		if(addOrder.overlaps(new Circle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f))) {
			touchedAdd = true;
			if(Gdx.input.justTouched()) {
				if(countOrder < 10) countOrder++;
			}
		}
		else touchedAdd = false;
		if(reduceOrder.overlaps(new Circle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f))) {
			touchedReduce = true;
			if(Gdx.input.justTouched()) {
				if(countOrder > 1) countOrder--;
			}
		}
		else touchedReduce = false;
	}
}
