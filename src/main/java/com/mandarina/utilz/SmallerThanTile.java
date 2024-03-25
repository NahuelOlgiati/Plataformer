package com.mandarina.utilz;

import com.mandarina.game.levels.LevelData;
import com.mandarina.main.AppStage;

public class SmallerThanTile {

	public static boolean CanMoveHere(float x, float y, float width, float height, LevelData levelData) {
		if (!IsSolid(x, y, levelData))
			if (!IsSolid(x + width, y + height, levelData))
				if (!IsSolid(x + width, y, levelData))
					if (!IsSolid(x, y + height, levelData))
						return true;
		return false;
	}

	public static boolean IsSolid(float x, float y, LevelData levelData) {
		float maxHeight = levelData.getLevel().getHeight();
		if (y < 0 || y >= maxHeight)
			return true;

		float maxWidth = levelData.getLevel().getWidth();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x);
		int yIndex = AppStage.GetTilesIn(y);

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	public static boolean IsSolid(float x, float y, int xTiles, int yTiles, LevelData levelData) {
		float maxHeight = levelData.getLevel().getHeight();
		if (y < 0 || y >= maxHeight)
			return true;

		float maxWidth = levelData.getLevel().getWidth();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x) + xTiles;
		int yIndex = AppStage.GetTilesIn(y) + yTiles;

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	public static boolean IsEntityInWater(Box hitbox, LevelData levelData) {
		return IsEntityInWater(hitbox.getMinX(), hitbox.getMaxY(), levelData)
				|| IsEntityInWater(hitbox.getMaxX(), hitbox.getMaxY(), levelData);
	}

	private static boolean IsEntityInWater(float xPos, float yPos, LevelData levelData) {
		int xCord = AppStage.GetTilesIn(xPos);
		int yCord = AppStage.GetTilesIn(yPos);
		return levelData.getIsWater()[yCord][xCord];
	}

	public static boolean IsTileSolid(int xTile, int yTile, LevelData levelData) {
		return levelData.getIsSolid()[yTile][xTile];
	}

	public static boolean IsEntityOnFloor(Box hitbox, float xSpeed, float ySpeed, LevelData levelData) {
		if (IsSolid(hitbox.getMinX() + xSpeed, hitbox.getMaxY() + ySpeed, 0, 1, levelData)
				|| IsSolid(hitbox.getMaxX() + xSpeed, hitbox.getMaxY() + ySpeed, 0, 1, levelData)) {
			return true;
		}
		return false;
	}

	public static boolean IsEntityOnFloor(Box hitbox, float xSpeed, LevelData levelData) {
		if (IsSolid(hitbox.getMinX() + xSpeed, hitbox.getMaxY(), 0, 1, levelData)
				|| IsSolid(hitbox.getMaxX() + xSpeed, hitbox.getMaxY(), 0, 1, levelData)) {
			return true;
		}
		return false;
	}

	public static boolean IsEntityOnFloor(Box hitbox, LevelData levelData) {
		if (IsSolid(hitbox.getMinX(), hitbox.getMaxY(), 0, 1, levelData)
				|| IsSolid(hitbox.getMaxX(), hitbox.getMaxY(), 0, 1, levelData)) {
			return true;
		}
		return false;
	}

	public static boolean IsFloor(Box hitbox, float xSpeed, LevelData levelData) {
		if (xSpeed > 0) {
			return IsSolid(hitbox.getMaxX() + xSpeed, hitbox.getMaxY(), 0, 1, levelData);
		} else {
			return IsSolid(hitbox.getMinX() + xSpeed, hitbox.getMaxY(), 0, 1, levelData);
		}
	}

	public static boolean IsFloor(Box hitbox, LevelData levelData) {
		if (!IsSolid(hitbox.getMaxX(), hitbox.getMaxY(), 0, 1, levelData)
				|| !IsSolid(hitbox.getMinX(), hitbox.getMaxY(), 0, 1, levelData)) {
			return false;
		}
		return true;
	}

	public static boolean CanCannonSeePlayer(LevelData levelData, Box playerBox, Box cannonBox,
			int yTile) {
		int playerXTile = AppStage.GetTilesIn(playerBox.getMinX() + playerBox.getWidth() / 2);
		int cannonXTile = AppStage.GetTilesIn(cannonBox.getMinX());

		if (playerXTile > cannonXTile) {
			return IsAllTilesClear(cannonXTile, playerXTile, yTile, levelData);
		} else {
			return IsAllTilesClear(playerXTile, cannonXTile, yTile, levelData);
		}
	}

	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, LevelData levelData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, levelData))
				return false;
		return true;
	}

	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, LevelData levelData) {
		if (IsAllTilesClear(xStart, xEnd, y, levelData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, levelData))
					return false;
			}
		return true;
	}

	public static boolean IsSightClear(LevelData levelData, Box enemyBox, Box playerBox, int yTile) {
		int enemyXTile = AppStage.GetTilesIn(enemyBox.getMinX());

		int playerXTile;
		if (IsSolid(playerBox.getMinX(), playerBox.getMaxY(), 0, 1, levelData))
			playerXTile = AppStage.GetTilesIn(playerBox.getMinX());
		else
			playerXTile = AppStage.GetTilesIn(playerBox.getMaxX());

		if (enemyXTile > playerXTile) {
			return IsAllTilesWalkable(playerXTile, enemyXTile, yTile, levelData);
		} else {
			return IsAllTilesWalkable(enemyXTile, playerXTile, yTile, levelData);
		}
	}

}