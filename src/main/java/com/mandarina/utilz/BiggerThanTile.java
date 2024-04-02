package com.mandarina.utilz;

import static com.mandarina.utilz.SmallerThanTile.IsSolid;

import com.mandarina.game.geometry.Box;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.AppStage;

public class BiggerThanTile {

	public static boolean CanMoveHere(Box hitbox, float xSpeed, float ySpeed, int hChecks, int vChecks,
			LevelData levelData) {
		float minXWithSpeed = hitbox.getMinX() + xSpeed;
		float minYWithSpeed = hitbox.getMinY() + ySpeed;
		if (!IsSolid(minXWithSpeed, minYWithSpeed, levelData)) {
			float maxXWithSpeed = hitbox.getMaxX() + xSpeed;
			float maxYWithSpeed = hitbox.getMaxY() + ySpeed;
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

	public static boolean CanMoveDown(Box hitbox, float xSpeed, float ySpeed, int hChecks,
			LevelData levelData) {
		float minXWithSpeed = hitbox.getMinX() + xSpeed;
		float maxXWithSpeed = hitbox.getMaxX() + xSpeed;
		float maxYWithSpeed = hitbox.getMaxY() + ySpeed;
		if (!IsSolid(maxXWithSpeed, maxYWithSpeed, levelData))
			if (!IsSolid(minXWithSpeed, maxYWithSpeed, levelData)) {
				if (xSpeed == 0 && ySpeed == 0) {
					return true;
				}

				if (hChecks == 0) {
					return true;
				}

				float tileSize = AppStage.GetTileSize();
				if (ySpeed > 0) {
					return CanMoveHereHorizontally(minXWithSpeed, maxYWithSpeed, hChecks, tileSize, levelData);
				} else {
					float minYWithSpeed = hitbox.getMinY() + ySpeed;
					return CanMoveHereHorizontally(minXWithSpeed, minYWithSpeed, hChecks, tileSize, levelData);
				}
			}
		return false;
	}

	private static boolean CanMoveHereVertically(float x, float y, int checks, float tileSize, LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			float deltaY = i * tileSize;
			if (IsSolid(x, y + deltaY, levelData)) {
				return false;
			}
		}
		return true;
	}

	private static boolean CanMoveHereHorizontally(float x, float y, int checks, float tileSize,
			LevelData levelData) {
		for (int i = 1; i <= checks; i++) {
			float deltaX = i * tileSize;
			if (IsSolid(x + deltaX, y, levelData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean IsFloor(Box hitbox, float xSpeed, int hChecks, LevelData levelData) {
		return !CanMoveDown(hitbox, xSpeed, AppStage.getGameScale(), hChecks, levelData);
	}
}