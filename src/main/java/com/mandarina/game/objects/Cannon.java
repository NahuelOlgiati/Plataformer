package com.mandarina.game.objects;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Cannon extends GameObject {

	private int tileY;

	public Cannon(Point2D spawn, int objType) {
		super(spawn, objType);
		tileY = AppStage.GetTilesIn(y);
		initHitbox(40, 26);
		hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + AppStage.Scale(6), hitbox.getWidth(),
				hitbox.getHeight());
	}

	public void draw(GameDrawer g, Offset offset, Image[] animations) {
		int x = (int) (getHitbox().getMinX() - offset.getX());
		int width = AppStage.Scale(ObjectCts.CANNON_WIDTH_DEFAULT);

		int y = (int) (getHitbox().getMinY() - offset.getY());
		int height = AppStage.Scale(ObjectCts.CANNON_HEIGHT_DEFAULT);

		if (getObjType() == ObjectCts.CANNON_RIGHT) {
			x += width;
			width *= -1;
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

	@Override
	public void scale() {
		super.scale();
		tileY = AppStage.GetTilesIn(y);
		initHitbox(40, 26);
		hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + AppStage.Scale(6), hitbox.getWidth(),
				hitbox.getHeight());
	}
}
