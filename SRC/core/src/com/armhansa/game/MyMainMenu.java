package com.armhansa.game;

import java.io.FileInputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class MyMainMenu implements Screen {
	final MyMain MAIN;
    int moveX = -200;
    boolean stopFon=false;
    
    boolean canLoad = false;
    String name;
    int money;
    int[] seeds = new int[4];
	int[] time = new int[5];
	BitmapFont whiteBigFont;
	
	long wait;
	Music menuMusic;
	Texture menuBgImg;
	Texture btnImg;
	Texture btnNotLoad;
	Texture sign;
	Texture hoverSign;
	Rectangle newGameBtn;
	Rectangle loadGameBtn;
	Rectangle exitGameBtn;
	
	Plantation plant = new Plantation();
	Account account;
	
	SpriteSheet fonfly;
	boolean toLeft = false;
    
    public MyMainMenu(MyMain main) {
    	this.MAIN = main;
    	
    	menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound&Music/menuMusic.mp3"));
    	menuMusic.setLooping(true);
    	menuMusic.play();
    	menuBgImg = new Texture(Gdx.files.internal("img/menu/bgMenu.jpg"));
    	btnImg = new Texture(Gdx.files.internal("img/menu/button.png"));
    	btnNotLoad = new Texture(Gdx.files.internal("img/menu/cant_load.png"));
    	sign = new Texture(Gdx.files.internal("img/menu/sign.png"));
    	hoverSign = new Texture(Gdx.files.internal("img/menu/hoversign.png"));
    	newGameBtn = new Rectangle(250, 160, 200, 96);
    	loadGameBtn = new Rectangle(500, 160, 200, 96);
    	exitGameBtn = new Rectangle(750, 160, 200, 96);
    	
    	whiteBigFont = MAIN.setStyleFont(25, Color.WHITE);
    	
    	try {
	    	FileInputStream fInput = new FileInputStream("save.armhansa");
			int count = fInput.read();
	    	if(count != -1) {
	    		char[] chrName = new char[count];
	    		for(byte i=0; i<count; i++) {
					chrName[i] = (char) fInput.read();
				}
	    		name = String.valueOf(chrName);
	    		int tmpMoney = 0;
				int countByte = 1;
				for(byte i=0; i<4; i++) {
					tmpMoney += countByte*fInput.read();
					countByte*=256;
				}
				money = tmpMoney;
				for(int i=0; i<4; i++) seeds[i] = fInput.read();
				for(int i=0; i<5; i++) time[i] = fInput.read();
				canLoad = true;
    		}
	    	fInput.close();
    	} catch(IOException e) { }
    	
    	wait = TimeUtils.nanoTime();
    	
        fonfly = new SpriteSheet(new Texture(Gdx.files.internal("img/fonBird.png")), 109, 101, 5, 3, 1);
        
    }
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MAIN.batch.begin();
        
        MAIN.batch.draw(menuBgImg, 0, 0, 1200, 700);
        MAIN.batch.draw(sign, 1200/2-600/2, 300, 600, 400);
        MAIN.batch.draw(fonfly.image, moveX, 450, 160, 200, 10+fonfly.scaleX, fonfly.scaleY, 106, 101, false, false);
        
        if(newGameBtn.overlaps(new Rectangle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f, 0.5f))) {
        	MAIN.batch.draw(btnImg, 250, 160, 200, 96, 250*0, 120*1, 250, 120, false, false);
        	if(Gdx.input.isTouched() && Gdx.input.justTouched()) {
        		int[] tmp = {1, 1, 1};
        		account = new Account(plant, tmp, "Player");
        		dispose();
        		MAIN.setScreen(new MyGame(MAIN, account));
        	}
        }
        else MAIN.batch.draw(btnImg, 250, 160, 200, 96, 250*0, 120*0, 250, 120, false, false);
        if(canLoad && loadGameBtn.overlaps(new Rectangle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f, 0.5f))) {
        	MAIN.batch.draw(hoverSign, 500, 70, 200, 96);
        	MAIN.batch.draw(btnImg, 500, 160, 200, 96, 250*1, 120*1, 250, 120, false, false);
        	whiteBigFont.draw(MAIN.batch, "Money: "+money, 545, 130);
        	whiteBigFont.draw(MAIN.batch, (time[0]<10?"0":"")+time[0]+(time[1]<10?":0":":")+time[1], 530, 100);
        	whiteBigFont.draw(MAIN.batch, (time[2]<10?" 0":" ")+time[2]+(time[3]<10?"/0":"/")+time[3]+"/"+time[4], 590, 100);
    		
        	if(Gdx.input.isTouched() && Gdx.input.justTouched()) {
	        	account = new Account(plant);
	        	dispose();
	        	MAIN.setScreen(new MyLoading(MAIN, account));
        	}
        }
        else {
        	if(canLoad) {
        		MAIN.batch.draw(btnImg, 500, 160, 200, 96, 250*1, 120*0, 250, 120, false, false);
        	}
        	else MAIN.batch.draw(btnNotLoad, 490, 145, 220, 120);
        }
        if(exitGameBtn.overlaps(new Rectangle(Gdx.input.getX(), 700-Gdx.input.getY(), 0.5f, 0.5f))) {
        	MAIN.batch.draw(btnImg, 750, 160, 200, 96, 250*2, 120*1, 250, 120, false, false);
        	if(Gdx.input.isTouched() && Gdx.input.justTouched()) {
        		Gdx.app.exit();
        	}
        }
        else MAIN.batch.draw(btnImg, 750, 160, 200, 96, 250*2, 120*0, 250, 120, false, false);
        
        MAIN.batch.end();
        
        
        if(moveX >= 1050) toLeft=true;
        else if(moveX <= 0-200) toLeft=false;
		moveX += toLeft?-1:1*500*(toLeft?2:1)*Gdx.graphics.getDeltaTime();
        if(Gdx.input.justTouched() && Gdx.input.isTouched()) {
        	stopFon = !stopFon;
        }
		if(!stopFon && TimeUtils.nanoTime()-wait >= 100000000) {
			fonfly.nextSprite();
			wait = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void dispose() {
		whiteBigFont.dispose();
		menuMusic.stop();
		menuMusic.dispose();
		menuBgImg.dispose();
		btnImg.dispose();
		btnNotLoad.dispose();
		sign.dispose();
		hoverSign.dispose();
		fonfly.dispose();
		
	}
	@Override
	public void resize(int width, int height) {	}
	@Override
	public void pause() { }
	@Override
	public void resume() { }
	@Override
	public void show() { }
	@Override
	public void hide() { }

}
