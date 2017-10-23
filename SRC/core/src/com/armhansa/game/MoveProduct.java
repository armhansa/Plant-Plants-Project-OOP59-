package com.armhansa.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MoveProduct {
	Account account;
	Array<Rectangle> products;
	Array<Integer> money;
	Array<Integer> type;
	
	public MoveProduct(Account account) {
		this.account = account;
		products = new Array<Rectangle>();
		money = new Array<Integer>();
		type = new Array<Integer>();
	}
	
	public void newProductMove(int type, int money) {
	      Rectangle product = new Rectangle();
	      product.x = Gdx.input.getX()-50;
	      product.y = 700-Gdx.input.getY()-50;
	      switch(type) {
	    	  case 0: product.width = 80;product.height = 80;break;
	    	  case 1: product.width = 100;product.height = 100;break;
	    	  case 2: product.width = 60;product.height = 60;break;
	      }
	      products.add(product);
	      this.money.add(money);
	      this.type.add(type);
	}
	public void update() {
		Iterator<Rectangle> iterProduct = products.iterator();
		Iterator<Integer> iterMoney = money.iterator();
		Iterator<Integer> iterType = type.iterator();
	    while(iterProduct.hasNext()) {
		    Rectangle product = iterProduct.next();
		    product.x += 2*(MyGame.zoom*400+MyGame.moveX-product.x) * Gdx.graphics.getDeltaTime();
		    product.y += 2*(MyGame.zoom*800+MyGame.moveY-product.y) * Gdx.graphics.getDeltaTime();
		    iterMoney.next();
		    int type = iterType.next();
		    if(product.overlaps(new Rectangle(MyGame.zoom*400+MyGame.moveX-40/2, MyGame.zoom*800+MyGame.moveY-40/2, 40, 40))) {
		    	account.storage.add(type);
		    	iterProduct.remove();
		    	iterMoney.remove();
		    	iterType.remove();
		    }
	    }
	}

}
