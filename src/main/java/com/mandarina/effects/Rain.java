package com.mandarina.effects;

import java.util.Random;

import com.mandarina.constants.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rain {

	private Point2D[] drops;
	private Random rand;
	private float rainSpeed = 1.25f;
	private Image rainParticle;

	// Worth knowing, adding particles this way can cost a lot in
	// computer power. Disable it if the game lags.
	public Rain() {
		rand = new Random();
		drops = new Point2D[1000];
		rainParticle = LoadSave.GetSpriteAtlas(LoadSave.RAIN_PARTICLE);
		initDrops();
	}

	private void initDrops() {
		for (int i = 0; i < drops.length; i++)
			drops[i] = getRndPos();
	}

	private Point2D getRndPos() {
		return new Point2D((int) getNewX(0), rand.nextInt(GameCts.GAME_HEIGHT));
	}

	public void update(int xLvlOffset) {
	    for (int i = 0; i < drops.length; i++) {
	        Point2D p = drops[i];
	        drops[i] = new Point2D(p.getX(), p.getY() + rainSpeed);
	        if (drops[i].getY() >= GameCts.GAME_HEIGHT) {
	            drops[i] = new Point2D(getNewX(xLvlOffset), -20);
	        }
	    }
	}

	private float getNewX(int xLvlOffset) {
		float value = (-GameCts.GAME_WIDTH) + rand.nextInt((int) (GameCts.GAME_WIDTH * 3f)) + xLvlOffset;
		return value;
	}

	public void draw(GraphicsContext g, int xLvlOffset) {
		for (Point2D p : drops) {
			g.drawImage(rainParticle, p.getX() - xLvlOffset, p.getY(), 3, 12);
		}
	}

}
