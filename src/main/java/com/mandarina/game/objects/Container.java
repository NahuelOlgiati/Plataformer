package com.mandarina.game.objects;

import com.mandarina.game.geometry.Box;
import com.mandarina.game.geometry.Point;
import com.mandarina.game.main.AppStage;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Container extends GameObject {

	public Container(Point spawn, int objType) {
		super(spawn, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == ObjectCts.BOX) {
			initHitbox(25, 18);

			xDrawOffset = AppStage.Scale(7);
			yDrawOffset = AppStage.Scale(12);

		} else {
			initHitbox(23, 25);
			xDrawOffset = AppStage.Scale(8);
			yDrawOffset = AppStage.Scale(5);
		}

		hitbox = new Box(hitbox.getMinX() + xDrawOffset / 2, hitbox.getMinY() + yDrawOffset + AppStage.Scale(2),
				hitbox.getWidth(), hitbox.getHeight());
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(8, 2, 40, 30, LoadSave.GetAtlas(Catalog.CONTAINER));
	}

	@Override
	public void scale() {
		super.scale();
		createHitbox();
	}
}
