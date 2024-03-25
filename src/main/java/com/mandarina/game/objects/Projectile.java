package com.mandarina.game.objects;

import static com.mandarina.utilz.SmallerThanTile.IsSolid;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.LoadSave;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;

public class Projectile extends GameObject {
	private Box hitbox;
	private int dir;
	private boolean active = true;

	public Projectile(Point spawn, int dir) {
		super(spawn, ObjectCts.PROJECTILE);
		this.dir = dir;
		int xOffset = AppStage.Scale(-3);
		int yOffset = AppStage.Scale(5);

		if (dir == 1)
			xOffset = AppStage.Scale(29);

		hitbox = new Box(x + xOffset, y + yOffset, AppStage.Scale(ProjectileCts.CANNON_BALL_WIDTH_DEFAULT),
				AppStage.Scale(ProjectileCts.CANNON_BALL_HEIGHT_DEFAULT));
	}

	public void updatePos() {
		hitbox.setMinX(hitbox.getMinX() + dir * AppStage.Scale(ProjectileCts.SPEED_DEFAULT));
	}

	public void draw(GameDrawer g, Offset offset, Image cannonBallSprite) {
		g.drawImage(cannonBallSprite, getHitbox().getMinX() - offset.getX(), getHitbox().getMinY() - offset.getY(),
				AppStage.Scale(ProjectileCts.CANNON_BALL_WIDTH_DEFAULT),
				AppStage.Scale(ProjectileCts.CANNON_BALL_HEIGHT_DEFAULT));
	}

	public boolean isProjectileHittingLevel(LevelData levelData) {
		return IsSolid(hitbox.getMinX() + hitbox.getWidth() / 2, hitbox.getMinY() + hitbox.getHeight() / 2, levelData);
	}

	public void setPos(int x, int y) {
		hitbox.set(x, y, hitbox.getWidth(), hitbox.getHeight());
	}

	@Override
	public Box getHitbox() {
		return hitbox;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public static Image load() {
		return LoadSave.GetSprite(LoadSave.CANNON_BALL);
	}

	@Override
	public void scale() {
		super.scale();
		int xOffset = AppStage.Scale(-3);
		int yOffset = AppStage.Scale(5);

		if (dir == 1)
			xOffset = AppStage.Scale(29);

		hitbox.set(x + xOffset, y + yOffset, AppStage.Scale(ProjectileCts.CANNON_BALL_WIDTH_DEFAULT),
				AppStage.Scale(ProjectileCts.CANNON_BALL_HEIGHT_DEFAULT));
	}

}
