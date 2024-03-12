package com.mandarina.utilz;

import static com.mandarina.utilz.SmallerThanTile.IsSolid;

import com.mandarina.game.levels.LevelData;
import com.mandarina.main.AppStage;

import javafx.geometry.Rectangle2D;

public class BiggerThanTile {

	public static boolean CanMoveHere(Rectangle2D hitbox, double xSpeed, double airSpeed, int hChecks, int vChecks,
			LevelData levelData) {
		double minXWithSpeed = hitbox.getMinX() + xSpeed;
		double minYWithSpeed = hitbox.getMinY() + airSpeed;
		if (!IsSolid(minXWithSpeed, minYWithSpeed, levelData)) {
			double maxXWithSpeed = hitbox.getMaxX() + xSpeed;
			double maxYWithSpeed = hitbox.getMaxY() + airSpeed;
			if (!IsSolid(maxXWithSpeed, maxYWithSpeed, levelData))
				if (!IsSolid(maxXWithSpeed, minYWithSpeed, levelData))
					if (!IsSolid(minXWithSpeed, maxYWithSpeed, levelData)) {
						if (xSpeed == 0 && airSpeed == 0) {
							return true;
						}
						float tileSize = AppStage.GetTileSize();

						if (vChecks != 0) {
							if (xSpeed > 0) {
								return hasVerticalCollision(maxXWithSpeed, minYWithSpeed, vChecks, tileSize, levelData);
							} else {
								return hasVerticalCollision(minXWithSpeed, minYWithSpeed, vChecks, tileSize, levelData);
							}
						}

						if (hChecks == 0) {
							return true;
						}

						if (airSpeed > 0) {
							return hasHorizontalCollision(minXWithSpeed, maxYWithSpeed, hChecks, tileSize, levelData);
						} else {
							return hasHorizontalCollision(minXWithSpeed, minYWithSpeed, hChecks, tileSize, levelData);
						}
					}
		}
		return false;
	}

	private static boolean hasVerticalCollision(double x, double y, int checks, float tileSize, LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			double deltaY = i * tileSize;
			if (IsSolid(x, y + deltaY, levelData)) {
				return false;
			}
		}
		return true;
	}

	private static boolean hasHorizontalCollision(double x, double y, int checks, float tileSize, LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			double deltaX = i * tileSize;
			if (IsSolid(x + deltaX, y, levelData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean IsFloor(Rectangle2D hitbox, double xSpeed, int vChecks, LevelData levelData) {
		return !CanMoveHere(hitbox, xSpeed, 1, 0, vChecks, levelData);
	}
}