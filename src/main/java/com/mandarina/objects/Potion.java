package com.mandarina.objects;

import com.mandarina.constants.GameCts;

import javafx.geometry.Rectangle2D;

public class Potion extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;

		initHitbox(7, 14);

		xDrawOffset = (int) (3 * GameCts.SCALE);
		yDrawOffset = (int) (2 * GameCts.SCALE);

		maxHoverOffset = (int) (10 * GameCts.SCALE);
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * GameCts.SCALE * hoverDir);

		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox = new Rectangle2D(hitbox.getMinX(), y + hoverOffset, hitbox.getWidth(), hitbox.getHeight());
	}
}
