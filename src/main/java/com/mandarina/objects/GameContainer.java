package com.mandarina.objects;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.ObjectCts;

import javafx.geometry.Rectangle2D;

public class GameContainer extends GameObject {

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == ObjectCts.BOX) {
			initHitbox(25, 18);

			xDrawOffset = (int) (7 * GameCts.SCALE);
			yDrawOffset = (int) (12 * GameCts.SCALE);

		} else {
			initHitbox(23, 25);
			xDrawOffset = (int) (8 * GameCts.SCALE);
			yDrawOffset = (int) (5 * GameCts.SCALE);
		}

		hitbox = new Rectangle2D(hitbox.getMinX() + xDrawOffset / 2,
				hitbox.getMinY() + yDrawOffset + (int) (GameCts.SCALE * 2), hitbox.getWidth(), hitbox.getHeight());
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}
}
