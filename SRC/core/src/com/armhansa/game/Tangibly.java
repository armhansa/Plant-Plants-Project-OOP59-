package com.armhansa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Tangibly {
	Intersector check = new Intersector();
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	Polygon[][] fieldPolygon;
	Polygon mousePolygon;
	
	public Tangibly() {
		fieldPolygon = new Polygon[9][9];
		mousePolygon = new Polygon();
		
	}
	
	public void updateFarm() {
		float x;
		float y;
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				x = 750+55*i-55*j+10;
				y = 750-38*(i+j)+10;
				float[] vertices = {MyGame.zoom*(x-1.5f)+MyGame.moveX, MyGame.zoom*(y+26.6f)+MyGame.moveY, MyGame.zoom*(x+38.4f)+MyGame.moveX, MyGame.zoom*(y+54.9f)+MyGame.moveY, MyGame.zoom*(x+81.1f)+MyGame.moveX, MyGame.zoom*(y+26.6f)+MyGame.moveY, MyGame.zoom*(x+40.6f)+MyGame.moveX, MyGame.zoom*(y-1.2f)+MyGame.moveY};
				fieldPolygon[i][j] = new Polygon(vertices);
			}
		}
		
	}
	private void mouseToPolygon() {
		float x = Gdx.input.getX();
		float y = MyMain.height-Gdx.input.getY();
		float[] vertices = {x-1, y-1, x-1, y+1, x+1, y+1, x+1, y-1};
		mousePolygon.setVertices(vertices);
	}
	public int isOverlap() {
		mouseToPolygon();
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(Intersector.overlapConvexPolygons(mousePolygon, fieldPolygon[i][j])) {
					return 10*i+j;
				}
			}
		}
		return (int) 100;
	}

}
