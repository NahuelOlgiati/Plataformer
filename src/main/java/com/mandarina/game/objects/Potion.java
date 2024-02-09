package com.mandarina.game.objects;

import com.mandarina.game.constants.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

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

	public static Image[][] load() {
		Image potionSprite = LoadSave.GetAtlas(LoadSave.POTION);
		Image[][] potionImgs = new Image[2][7];
		for (int j = 0; j < potionImgs.length; j++)
			for (int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = new WritableImage(potionSprite.getPixelReader(), 12 * i, 16 * j, 12, 16);

		return potionImgs;
	}
}
