package com.mandarina.objects;

import com.mandarina.constants.GameCts;

import javafx.geometry.Rectangle2D;

public class Cannon extends GameObject {

	private int tileY;

	public Cannon(int x, int y, int objType) {
	    super(x, y, objType);
	    tileY = y / GameCts.TILES_SIZE;
	    initHitbox(40, 26);
	    hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + (int) (6 * GameCts.SCALE),
	                            hitbox.getWidth(), hitbox.getHeight());
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public int getTileY() {
		return tileY;
	}

}
