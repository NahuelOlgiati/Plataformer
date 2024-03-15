package com.mandarina.utilz;

import static com.mandarina.utilz.SmallerThanTile.IsSolid;

import com.mandarina.game.levels.LevelData;
import com.mandarina.main.AppStage;

import javafx.geometry.Rectangle2D;

public class BiggerThanTile {

	public static boolean CanMoveHere(Rectangle2D hitbox, double xSpeed, double ySpeed, int hChecks, int vChecks,
			LevelData levelData) {
		double minXWithSpeed = hitbox.getMinX() + xSpeed;
		double minYWithSpeed = hitbox.getMinY() + ySpeed;
		if (!IsSolid(minXWithSpeed, minYWithSpeed, levelData)) {
			double maxXWithSpeed = hitbox.getMaxX() + xSpeed;
			double maxYWithSpeed = hitbox.getMaxY() + ySpeed;
			if (!IsSolid(maxXWithSpeed, maxYWithSpeed, levelData))
				if (!IsSolid(maxXWithSpeed, minYWithSpeed, levelData))
					if (!IsSolid(minXWithSpeed, maxYWithSpeed, levelData)) {
						if (xSpeed == 0 && ySpeed == 0) {
							return true;
						}
						float tileSize = AppStage.GetTileSize();

						boolean canMoveHereVertically = true;
						if (vChecks != 0) {
							if (xSpeed > 0) {
								canMoveHereVertically = CanMoveHereVertically(maxXWithSpeed, minYWithSpeed, vChecks,
										tileSize, levelData);
							} else {
								canMoveHereVertically = CanMoveHereVertically(minXWithSpeed, minYWithSpeed, vChecks,
										tileSize, levelData);
							}
						}
						if (!canMoveHereVertically) {
							return false;
						}

						if (hChecks == 0) {
							return true;
						}

						if (ySpeed > 0) {
							return CanMoveHereHorizontally(minXWithSpeed, maxYWithSpeed, hChecks, tileSize, levelData);
						} else {
							return CanMoveHereHorizontally(minXWithSpeed, minYWithSpeed, hChecks, tileSize, levelData);
						}
					}
		}
		return false;
	}

	public static boolean CanMoveDown(Rectangle2D hitbox, double xSpeed, double ySpeed, int hChecks,
			LevelData levelData) {
		double minXWithSpeed = hitbox.getMinX() + xSpeed;
		double maxXWithSpeed = hitbox.getMaxX() + xSpeed;
		double maxYWithSpeed = hitbox.getMaxY() + ySpeed;
		if (!IsSolid(maxXWithSpeed, maxYWithSpeed, levelData))
			if (!IsSolid(minXWithSpeed, maxYWithSpeed, levelData)) {
				if (xSpeed == 0 && ySpeed == 0) {
					return true;
				}

				if (hChecks == 0) {
					return true;
				}

				double tileSize = AppStage.GetTileSize();
				if (ySpeed > 0) {
					return CanMoveHereHorizontally(minXWithSpeed, maxYWithSpeed, hChecks, tileSize, levelData);
				} else {
					double minYWithSpeed = hitbox.getMinY() + ySpeed;
					return CanMoveHereHorizontally(minXWithSpeed, minYWithSpeed, hChecks, tileSize, levelData);
				}
			}
		return false;
	}

	private static boolean CanMoveHereVertically(double x, double y, int checks, double tileSize, LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			double deltaY = i * tileSize;
			if (IsSolid(x, y + deltaY, levelData)) {
				return false;
			}
		}
		return true;
	}

	private static boolean CanMoveHereHorizontally(double x, double y, int checks, double tileSize,
			LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			double deltaX = i * tileSize;
			if (IsSolid(x + deltaX, y, levelData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean IsFloor(Rectangle2D hitbox, double xSpeed, int hChecks, LevelData levelData) {
		return !CanMoveDown(hitbox, xSpeed, AppStage.getGameScale(), hChecks, levelData);
	}
}