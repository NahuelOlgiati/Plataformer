package com.mandarina.game.objects;

import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.LoadSave;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;

public class Potion extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Potion(Point spawn, int objType) {
		super(spawn, objType);
		doAnimation = true;

		initHitbox(7, 14);

		xDrawOffset = AppStage.Scale(3);
		yDrawOffset = AppStage.Scale(2);

		maxHoverOffset = AppStage.Scale(10);
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += AppStage.Scale(0.075f * hoverDir);

		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox = new Box(hitbox.getMinX(), y + hoverOffset, hitbox.getWidth(), hitbox.getHeight());
	}

	public static Image[][] load() {
		Image potionSprite = LoadSave.GetAtlas(LoadSave.POTION);
		Image[][] potionImgs = new Image[2][7];
		for (int j = 0; j < potionImgs.length; j++)
			for (int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = LoadSave.GetSubimage(potionSprite, i, j, 12, 16);

		return potionImgs;
	}

	@Override
	public void scale() {
		initHitbox(7, 14);

		xDrawOffset = AppStage.Scale(3);
		yDrawOffset = AppStage.Scale(2);

		maxHoverOffset = AppStage.Scale(10);
	}
}
