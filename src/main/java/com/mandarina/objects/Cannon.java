package com.mandarina.objects;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.ObjectCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cannon extends GameObject {

	private int tileY;

	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / GameCts.TILES_SIZE;
		initHitbox(40, 26);
		hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + (int) (6 * GameCts.SCALE), hitbox.getWidth(),
				hitbox.getHeight());
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image[] animations) {
		int x = (int) (getHitbox().getMinX() - lvlOffsetX);
		int width = ObjectCts.CANNON_WIDTH;

		int y = (int) (getHitbox().getMinY() - lvlOffsetY);
		int height = ObjectCts.CANNON_HEIGHT;

		if (getObjType() == ObjectCts.CANNON_RIGHT) {
			x += width;
			width *= -1;
			y += height;
			height *= -1;
		}
		g.drawImage(animations[getAniIndex()], x, y, width, height);
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public int getTileY() {
		return tileY;
	}

	public static Image[] load() {
		return LoadSave.GetAnimations(7, 40, 26, LoadSave.GetAtlas(LoadSave.CANNON));
	}
}
