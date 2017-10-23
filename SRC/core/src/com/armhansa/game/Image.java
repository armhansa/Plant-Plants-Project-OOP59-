package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Image {
	Texture[] allImage = new Texture[2];
	Texture[][] toolImg = new Texture[5][3];
	Texture[][] seedImg = new Texture[3][3];
	Texture[] productImage = new Texture[3];
	Texture[] season = new Texture[3];
	Texture bgImage;
	Texture coin;
	Texture rainImg;
	Texture wormImg;
	Texture[] airplane = new Texture[3];
	
	public Image() {
		String[] tool = {"btn-pointer.png", "btn-hoe.png", "btn-shower.png", "btn-insecticide.png", "btn-sickle.png"};
		String[] type = {"", "drop-", "selected-"};
		for(byte i=0; i<5; i++) {
			for(byte j=0; j<3; j++) {
				toolImg[i][j] = new Texture(Gdx.files.internal("img/btn/tool-"+type[j]+tool[i]));
			}
		}
		String[] seed = {"bean-seed.png", "carrot-seed.png", "wtml-seed-pack.png"};
		String[] typeSeed = {"", "selected-b-", "selected-"};
		for(byte i=0; i<3; i++) {
			for(byte j=0; j<3; j++) {
				seedImg[i][j] = new Texture(Gdx.files.internal("img/plant/"+typeSeed[j]+seed[i]));
			}
		}
		
		bgImage = new Texture(Gdx.files.internal("img/full-background.png"));
		allImage[0] = new Texture(Gdx.files.internal("img/plant/f-blank-1.png"));
		allImage[1] = new Texture(Gdx.files.internal("img/plant/f-blank-2.png"));
		coin = new Texture(Gdx.files.internal("img/plant/coin.png"));
		rainImg = new Texture(Gdx.files.internal("img/rain drop.png"));
		wormImg = new Texture(Gdx.files.internal("img/plant/worm.png"));
		
		productImage[0] = new Texture(Gdx.files.internal("img/product/bean.png"));
		productImage[1] = new Texture(Gdx.files.internal("img/product/carrot.png"));
		productImage[2] = new Texture(Gdx.files.internal("img/product/melon.png"));
		
		airplane[0] = new Texture(Gdx.files.internal("img/airplane-pay.png"));
		airplane[1] = new Texture(Gdx.files.internal("img/airplane-pay-selected.png"));
		airplane[2] = new Texture(Gdx.files.internal("img/airplane-dark.png"));
		
		season[0] = new Texture(Gdx.files.internal("img/background-sunny.png"));
		season[1] = new Texture(Gdx.files.internal("img/background-rain.png"));
		season[2] = new Texture(Gdx.files.internal("img/background-snow.png"));
		
	}
	
	public void dispose() {
		
	}

}
