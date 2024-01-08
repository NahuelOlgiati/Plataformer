package com.mandarina.objects;

import com.mandarina.constants.GameCts;

import javafx.geometry.Rectangle2D;

public class Spike extends GameObject {

	public Spike(int x, int y, int objType) {
	    super(x, y, objType);
	    initHitbox(32, 16);
	    xDrawOffset = 0;
	    yDrawOffset = (int) (GameCts.SCALE * 16);
	    hitbox = new Rectangle2D(x + xDrawOffset, y + yDrawOffset, 32, 16);
	}
}
