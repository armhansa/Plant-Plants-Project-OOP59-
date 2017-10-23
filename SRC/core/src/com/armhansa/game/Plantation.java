package com.armhansa.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;

public class Plantation {
	
	Product[][] farm = new Product[9][9];
	public boolean[][] hasField = new boolean[9][9];
	
	public Plantation() {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				hasField[i][j] = false;
			}
		}
	}
	
	public void save(FileOutputStream fOutput) {
		try {
			for(int i=0; i<9; i++) {
				for(int j=0; j<9; j++) {
					fOutput.write(hasField[i][j]?01:00);
					if(hasField[i][j]) {
						farm[i][j].save(fOutput);
					}
				}
			}
		} catch(IOException e) { }
 	}
	public void load(FileInputStream fInput) {
		try {
			for(int i=0; i<9; i++) {
				for(int j=0; j<9; j++) {
					hasField[i][j] = (fInput.read() == 00?false:true);
					if(hasField[i][j]) {
						int total = fInput.read();
						Gdx.app.log("total",  total+"");
						switch(total) {
							case 0 : farm[i][j] = new Bean();break;
							case 1 : farm[i][j] = new Carrot();break;
							case 2 : farm[i][j] = new Melon();break;
						}
						farm[i][j].load(fInput);
					}
				}
			}
		} catch(IOException e) { }
 	}
	public void sowSeed(int i, int j, int selectSeed) {
		hasField[i][j] = true;
		switch(selectSeed) {
			case 0 : farm[i][j] = new Bean();break;
			case 1 : farm[i][j] = new Carrot();break;
			case 2 : farm[i][j] = new Melon();break;
		}
	}

}
