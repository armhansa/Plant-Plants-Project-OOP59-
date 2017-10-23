package com.armhansa.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame implements Screen {
	final MyMain MAIN;
	Plantation plant;
	Tangibly tangibly;
	Account account;
	
	float nowMonth;
	// Time In Game
	long clockGame;
	
	boolean nowTouchPlane;
	Rectangle planeRect = new Rectangle(400, 0, 100, 100);
	
	// Control Viewer When Tool is Mouse
	public static float moveX=-300;
	public static float moveY=-300;
	public static float zoom=1;
	
	// AllButton in Desktop Game
	Button button;
	
	// When Buy Seed
	BuySeed buySeed;
	
	// to Check Touched Farm
	boolean nowOverlap=false;
	byte nowI=0;
	byte nowJ=0;
	
	// All Image In Game
	Image image;

	// All Audio In Game
	Music[] allMusic = new Music[3];
	static Sound[] allSound = new Sound[5];
	
	// Create Raining When Month = 2
	Rainy rain = new Rainy();
	
	// Move Product from farm to Storage
	MoveProduct moveProduct;
	
	// All Font In Game
	BitmapFont[] font = new BitmapFont[5];
	
	// Create for Use Scroll Mouse to Zoom
	MyInputProcessor inputProcessor = new MyInputProcessor();
	
	public MyGame(MyMain main, Account account) {
		this.MAIN = main;
		this.account = account;
		this.plant = account.plant;
		tangibly = new Tangibly();
		
		int tmp = Account.time[3]/1;
		nowMonth = tmp;
		
		
		

		moveProduct = new MoveProduct(account);
		
		Gdx.input.setInputProcessor(inputProcessor);
		
		button = new Button();
		buySeed = new BuySeed(account);
		
		// Load All Image
		image = new Image();
		
		allSound[0] = Gdx.audio.newSound(Gdx.files.internal("Sound&Music/fillWater.wav"));
		allSound[1] = Gdx.audio.newSound(Gdx.files.internal("Sound&Music/push-btn.wav"));
		allSound[2] = Gdx.audio.newSound(Gdx.files.internal("Sound&Music/harvest.wav"));
		allSound[3] = Gdx.audio.newSound(Gdx.files.internal("Sound&Music/spray.wav"));
		allSound[4] = Gdx.audio.newSound(Gdx.files.internal("Sound&Music/coin.wav"));
		
        allMusic[0] = Gdx.audio.newMusic(Gdx.files.internal("Sound&Music/summer.mp3"));
        allMusic[1] = Gdx.audio.newMusic(Gdx.files.internal("Sound&Music/rainny.mp3"));
        allMusic[2] = Gdx.audio.newMusic(Gdx.files.internal("Sound&Music/winter.mp3"));
        
        for(int i=0; i<3; i++) allMusic[i].setLooping(true);
    	allMusic[(int)nowMonth-1].play();
        // Create Different Fonts
		font[0] = MAIN.setStyleFont(40, Color.GOLD);
		font[1] = MAIN.setStyleFont(25, Color.WHITE);
		font[2] = MAIN.setStyleFont(30, Color.BLACK);
		font[3] = MAIN.setStyleFont(40, Color.GREEN);
		font[4] = MAIN.setStyleFont(30, Color.GOLD);
		
		clockGame = TimeUtils.nanoTime();
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		if(Account.time[3] != nowMonth && allMusic[(int)nowMonth-1].isPlaying()) {
			allMusic[(int)nowMonth-1].stop();
			nowMonth = Account.time[3]/1;
			allMusic[Account.time[3]-1].play();
		}
		
		
		account.home.updatePositionAndCheck();
		account.well.updatePositionAndCheck();
		account.storage.updatePosition();
		
		// Next Time of Game
		if(Home.nowSleep || TimeUtils.nanoTime()-clockGame >= 100000000) {
			account.nextTime();
			clockGame = TimeUtils.nanoTime();
		}
		
		// when Want to Save
		if(Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyJustPressed(Keys.S)) {
			account.save();
			
		}
		
		// When Rainy and MoveProduct to store
		moveProduct.update();
		if(rain.rainDrops.size>0 || Account.time[3] == 2) rain.update();
		if(Account.time[3] == 2) {
			rain.spawnRaindrop();
			for(byte i=0; i<9; i++) {
				for(byte j=0; j<9; j++) {
					if(plant.hasField[i][j] && !plant.farm[i][j].isFinished() && plant.farm[i][j].isDehydration())
						plant.farm[i][j].fillWater();
				}
			}
		}
		
		if(Home.nowSleep) {
			for(int i=0; i<4; i++) account.nextTime();
			account.home.update();
			MAIN.batch.begin();
			font[1].draw(MAIN.batch, (Account.time[0]<10?"0":"")+Account.time[0]+(Account.time[1]<10?":0":":")+Account.time[1], 20, 680);
			font[1].draw(MAIN.batch, (Account.time[2]<10?" 0":" ")+Account.time[2]+(Account.time[3]<10?"/0":"/")+Account.time[3]+"/"+Account.time[4], 80, 680);
			MAIN.batch.end();
		}
		else {
			
			// get Input for Tool Button and Seed Button
			button.checkAction();
			
			// Get Input for Button when BuySeed
			if(BuySeed.nowBuySeed) buySeed.checkAction();
			
			// When User Use All Tool
			if(Gdx.input.isTouched() && !BuySeed.nowBuySeed) {
				if(Button.nowSelectTool == 0) {
					if(Gdx.input.justTouched()) { button.mouseX = Gdx.input.getX();button.mouseY = 700-Gdx.input.getY(); }
					else {
						moveX += (Gdx.input.getX()-button.mouseX)/zoom;
						moveY += (700-Gdx.input.getY()-button.mouseY)/zoom;
						
						if(moveX < zoom*zoom*(-600)+10) {
							moveX = zoom*zoom*(-600)+10;
						} else if(moveX > 0) moveX = 0;
						
						if(moveY < zoom*zoom*(-350)+10){
							moveY = zoom*zoom*(-350)+10;
						} else if(moveY > 0) moveY = 0;
						button.mouseX = Gdx.input.getX();button.mouseY = MyMain.height-Gdx.input.getY();
					}
				}
				else {
					int ij = tangibly.isOverlap();
					if(ij != 100) {
						nowOverlap = true;
						nowI = (byte) (ij/10);
						nowJ = (byte) (ij%10);
						if(plant.hasField[nowI][nowJ]) {
							if(Button.nowSelectTool == 2 && plant.farm[nowI][nowJ].isDehydration() && Well.waterRemain > 0) {
								allSound[0].play();
								plant.farm[nowI][nowJ].fillWater();
								Well.waterRemain--;
							}
							else if(Button.nowSelectTool == 3 && plant.farm[nowI][nowJ].nowWorm && Account.money > 15) {
								allSound[3].play();
								Account.money -= 15;
								plant.farm[nowI][nowJ].nowWorm = false;
							}
							else if(Button.nowSelectTool == 4 && plant.farm[nowI][nowJ].isFinished() && !plant.farm[nowI][nowJ].nowWorm) {
								if(account.storage.canAdd(plant.farm[nowI][nowJ].getType())) {
									allSound[2].play();
									moveProduct.newProductMove(plant.farm[nowI][nowJ].getType(), plant.farm[nowI][nowJ].sell());
								}
								else {
									// Sound Alert
								}
								if(plant.farm[nowI][nowJ].getCountLeft() == 0) plant.hasField[nowI][nowJ] = false;
							}
						}
						else if(Button.nowSelectTool==1) {
							if(account.seeds[Button.nowSelectSeed] > 0) {
								plant.hasField[nowI][nowJ] = true;
								account.sowSeed(nowI, nowJ, Button.nowSelectSeed);
							}
							else BuySeed.nowBuySeed = true;
						}
					}
					else nowOverlap = false;
				}
			}
			else {
				int ij;
				try {
					ij = tangibly.isOverlap();
				} catch(Exception e) {
					ij = 100;
				}
				if(ij != 100) {
					nowOverlap = true;
					nowI = (byte) (ij/10);
					nowJ = (byte) (ij%10);
				}
				else nowOverlap = false;
			}
			tangibly.updateFarm();
			
			if(planeRect.overlaps(new Rectangle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f, 0.5f))) {
				nowTouchPlane = true;
				if(Gdx.input.justTouched() && Account.money>=5000000) {
					Gdx.app.exit();
				}
			}
			else nowTouchPlane = false;
			
			
			
			
			MAIN.batch.begin();
			MAIN.batch.draw(image.bgImage, moveX, moveY, 1800*zoom, 1050*zoom);
			if(Account.time[3]!=1) MAIN.batch.draw(image.season[Account.time[3]-1], moveX, moveY, 1800*zoom, 1050*zoom);
			MAIN.batch.draw(account.home.homeImg[Home.nowOverlap?1:0], zoom*950+moveX, zoom*650+moveY, zoom*300, zoom*300);
			MAIN.batch.draw(account.well.wellImg[Well.nowOverlap?1:0], zoom*1200+moveX, zoom*625+moveY, zoom*100, zoom*100);
			MAIN.batch.draw(account.storage.storageImg[ProductStorage.nowOverlap?1:0], zoom*270+moveX, zoom*650+moveY, zoom*400, zoom*400);
			if(ProductStorage.nowOverlap) {
				font[0].draw(MAIN.batch, "SELL", zoom*400+moveX, zoom*900+moveY);
				font[4].draw(MAIN.batch, "BEAN  : "+account.storage.storage[0], zoom*400+moveX, zoom*(850)+moveY);
				font[4].draw(MAIN.batch, "CARROT: "+account.storage.storage[1], zoom*400+moveX, zoom*(850-30)+moveY);
				font[4].draw(MAIN.batch, "MELON : "+account.storage.storage[2], zoom*400+moveX, zoom*(850-60)+moveY);
			}
			
			float positionX, positionY, width;
			int[] splitPlant;
			for(int i=0; i<9;i++) {
				for(int j=0; j<9; j++) {
					positionX = zoom*(750+55*(i-j))+moveX;
					positionY = zoom*(750-38*(i+j))+moveY;
					width = zoom*100;
					if(plant.hasField[i][j]) {
						if(plant.farm[i][j].nowWorm && plant.farm[i][j].updateWorm()) {
							plant.hasField[i][j] = false;
						}
						else {
							splitPlant = plant.farm[i][j].getSplit();
							if(!nowOverlap || i!=nowI || j!=nowJ) {
								MAIN.batch.draw(image.allImage[(Account.time[3]==2 || !plant.farm[i][j].isDehydration())?1:0], positionX, positionY, width, width);
								MAIN.batch.draw(plant.farm[i][j].getPlantImage(), positionX, positionY, width, width, splitPlant[0], splitPlant[1], 200, 200, false, false);
							}
							else {
								positionX = zoom*(750+55*(i-j)-10)+moveX;
								positionY = zoom*(750-38*(i+j)-8)+moveY;
								width = zoom*(100+20);
								MAIN.batch.draw(image.allImage[(Account.time[3]==2 || !plant.farm[i][j].isDehydration())?1:0], positionX, positionY, width, width);
								MAIN.batch.draw(plant.farm[i][nowJ].getPlantImage(), positionX, positionY, width, width, splitPlant[0], splitPlant[1], 200, 200, false, false);
								if(plant.farm[i][j].isFinished()) font[1].draw(MAIN.batch, plant.farm[i][j].getSellPrice()+"$", positionX+zoom*45, positionY+zoom*100);
								else font[1].draw(MAIN.batch, plant.farm[i][j].percentSuccess+"%", positionX+zoom*45, positionY+zoom*100);
							}
							if(plant.farm[i][j].nowWorm) {
								MAIN.batch.draw(image.wormImg, positionX, positionY, width, width, plant.farm[i][j].splitWorm[0], plant.farm[i][j].splitWorm[1], 400, 400, false, false);
							}
						}
					}
					else MAIN.batch.draw(image.allImage[Account.time[3]==2?1:0], positionX, positionY, width, width);
				}
			}
			if(Account.time[3]==1) MAIN.batch.draw(image.season[0], moveX, moveY, 1800*zoom, 1050*zoom);
			for(Rectangle raindrop: rain.rainDrops) {
				MAIN.batch.draw(image.rainImg, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
		    }
			Iterator<Integer> iterType = moveProduct.type.iterator();
			for(Rectangle product: moveProduct.products) {
				int tmpType = iterType.next();
				MAIN.batch.draw(image.productImage[tmpType], product.x, product.y, product.width, product.height);
			}
			if(BuySeed.nowBuySeed) MAIN.batch.draw(buySeed.darkBackgroundImg, 0, 0, 1200, 700);
			for(int i=0;i<5;i++) {
				if(i == Button.nowSelectTool) MAIN.batch.draw(image.toolImg[i][0], 10, 500-80*i, 80, 80);
				else if(button.touchedBtn == i && !(button.justTouchedBtn[0] && button.justTouchedBtn[1] && button.justTouchedBtn[2] && button.justTouchedBtn[3] && button.justTouchedBtn[4])) {
					MAIN.batch.draw(image.toolImg[i][2], 10, 500-80*i, 80, 80);
				}
				else MAIN.batch.draw(image.toolImg[i][1], 10, 500-80*i, 80, 80);
			}
			
			
			for(int i=0; i<3; i++) {
				if(Button.nowSelectTool == 1) {
					if(i == Button.nowSelectSeed) MAIN.batch.draw(image.seedImg[i][0], 70+90*i, -10, 100, 100);
					else if(button.touchedSeed == i && !(button.justTouchedSeed[0] && button.justTouchedSeed[1] && button.justTouchedSeed[2])) {
						MAIN.batch.draw(image.seedImg[i][2], 70+90*i, -10, 100, 100);
					}
					else MAIN.batch.draw(image.seedImg[i][1], 70+90*i, -10, 100, 100);
				}
				else if(BuySeed.nowBuySeed) {
					if(i == Button.nowSelectSeed) MAIN.batch.draw(image.seedImg[i][0], 70+90*i, -10, 100, 100);
					else if(button.touchedSeed == i && !(button.justTouchedSeed[0] && button.justTouchedSeed[1] && button.justTouchedSeed[2])) {
						MAIN.batch.draw(image.seedImg[i][2], 70+90*i, -10, 100, 100);
					}
					else MAIN.batch.draw(image.seedImg[i][1], 70+90*i, -10, 100, 100);
				}
				else MAIN.batch.draw(image.seedImg[i][1], 70+90*i, -10, 100, 100);
				font[2].draw(MAIN.batch, "x"+account.seeds[i], 120+90*i, 20);
			}
			if(Account.money >= 5000000) {
				if(nowTouchPlane) MAIN.batch.draw(image.airplane[1], 400, 0, 100, 100);
				else MAIN.batch.draw(image.airplane[0], 400, 0, 100, 100);
			}
			else MAIN.batch.draw(image.airplane[2], 400, 0, 100, 100);
			
			// When Player Buy Seed
			if(BuySeed.nowBuySeed) {
				MAIN.batch.draw(buySeed.bgImg, 400, 225, 400, 250);
				MAIN.batch.draw(buySeed.headImg, 460, 400, 302, 42, 0, 84*Button.nowSelectSeed, 604, 84, false, false);
				MAIN.batch.draw(image.productImage[Button.nowSelectSeed], 415, 220, 200, 200);
				MAIN.batch.draw(buySeed.priceTagImg, 470, 270, 100, 50, 100*(Account.time[3]-1), 50*Button.nowSelectSeed, 100, 50, false, false);
				if(!buySeed.touchedReduce && buySeed.countOrder>1) MAIN.batch.draw(buySeed.buyImg[0][0], 590, 340, 30, 30);
				else MAIN.batch.draw(buySeed.buyImg[0][1], 590, 340, 30, 30);
				if(!buySeed.touchedAdd && buySeed.countOrder<10) MAIN.batch.draw(buySeed.buyImg[1][0], 710, 340, 30, 30);
				else MAIN.batch.draw(buySeed.buyImg[1][1], 710, 340, 30, 30);
				font[2].draw(MAIN.batch, buySeed.countOrder+"", 660, 370);
				font[2].draw(MAIN.batch, buySeed.countOrder*Account.SEED_PRICE[Button.nowSelectSeed][Account.time[3]-1]+"", 660, 335);
				if(!buySeed.touchedNo) MAIN.batch.draw(buySeed.buyImg[3][0], 770, 445, 36, 36);
				else MAIN.batch.draw(buySeed.buyImg[3][1], 770, 445, 36, 36);
				if(!buySeed.touchedYes && account.getMoney()>=buySeed.countOrder*Account.SEED_PRICE[Button.nowSelectSeed][Account.time[3]-1])
					MAIN.batch.draw(buySeed.buyImg[2][0], 650, 255, 45, 45);
				else MAIN.batch.draw(buySeed.buyImg[2][1], 650, 255, 45, 45);
			}
			
			// Main Data for Player Watch
			font[2].draw(MAIN.batch, (Account.time[0]<10?"0":"")+Account.time[0]+(Account.time[1]<10?":0":":")+Account.time[1], 20, 680);
			font[2].draw(MAIN.batch, (Account.time[2]<10?" 0":" ")+Account.time[2]+(Account.time[3]<10?"/0":"/")+Account.time[3]+"/"+Account.time[4], 80, 680);
			MAIN.batch.draw(image.coin, 1030, 0, 60, 60);
			font[0].draw(MAIN.batch, ""+account.getMoney(), 1100, 40);
			font[1].draw(MAIN.batch, "Left: "+Well.waterRemain, zoom*(910+300)+moveX, zoom*(330+300)+moveY);
			
			MAIN.batch.end();
		}
		
	}
	
	@Override
	public void resize(int width, int height) {
		MyMain.height = height;
		MyMain.width = width;
		Gdx.app.log("Resize", width+","+height);
	}
	@Override
	public void dispose() { 
		allMusic[1].dispose();
	}
	@Override
	public void pause() { }
	@Override
	public void resume() { }
	@Override
	public void show() { }
	@Override
	public void hide() { }

}
