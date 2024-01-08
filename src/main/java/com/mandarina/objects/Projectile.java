package com.mandarina.objects;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.ProjectileCts;

import javafx.geometry.Rectangle2D;

public class Projectile {
	private Rectangle2D hitbox;
	private int dir;
	private boolean active = true;

	public Projectile(int x, int y, int dir) {
		int xOffset = (int) (-3 * GameCts.SCALE);
		int yOffset = (int) (5 * GameCts.SCALE);

		if (dir == 1)
			xOffset = (int) (29 * GameCts.SCALE);

		hitbox = new Rectangle2D(x + xOffset, y + yOffset, ProjectileCts.CANNON_BALL_WIDTH,
				ProjectileCts.CANNON_BALL_HEIGHT);
		this.dir = dir;
	}

	public void updatePos() {
		hitbox = new Rectangle2D(hitbox.getMinX() + dir * ProjectileCts.SPEED, hitbox.getMinY(), hitbox.getWidth(),
				hitbox.getHeight());
	}

	public void setPos(int x, int y) {
		hitbox = new Rectangle2D(x, y, hitbox.getWidth(), hitbox.getHeight());
	}

	public Rectangle2D getHitbox() {
		return hitbox;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
