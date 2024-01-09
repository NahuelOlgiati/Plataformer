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
		return new Point2D((int) getNewX(0), getNewY(0));
	}

	public void update(int lvlOffsetX, int lvlOffsetY) {
		for (int i = 0; i < drops.length; i++) {
			Point2D p = drops[i];
			drops[i] = new Point2D(p.getX(), p.getY() + rainSpeed);
			if (drops[i].getY() >= GameCts.GAME_HEIGHT + lvlOffsetY) {
				drops[i] = new Point2D(getNewX(lvlOffsetX), rand.nextInt(-GameCts.GAME_HEIGHT + lvlOffsetY, lvlOffsetY));
			}
		}
	}

	private float getNewX(int lvlOffsetX) {
		return rand.nextInt((int) (GameCts.GAME_WIDTH * -1.5f) + lvlOffsetX,
				(int) (GameCts.GAME_WIDTH * 1.5f) + lvlOffsetX);
	}

	private float getNewY(int lvlOffsetY) {
		return rand.nextInt((int) (GameCts.GAME_HEIGHT * -1.5f) + lvlOffsetY,
				(int) (GameCts.GAME_HEIGHT * 1.5f) + lvlOffsetY);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Point2D p : drops) {
			g.drawImage(rainParticle, p.getX() - lvlOffsetX, p.getY() - lvlOffsetY, 2 * GameCts.SCALE,
					8 * GameCts.SCALE);
		}
	}

}
