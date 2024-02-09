package com.mandarina.game.objects;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.constants.ObjectCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

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

	public static Image[][] load() {
		return LoadSave.GetAnimations(8, 2, 40, 30, LoadSave.GetAtlas(LoadSave.CONTAINER));
	}
}
