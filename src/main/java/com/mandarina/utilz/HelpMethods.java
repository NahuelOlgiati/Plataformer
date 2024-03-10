package com.mandarina.utilz;

import com.mandarina.game.levels.LevelData;
import com.mandarina.main.AppStage;

import javafx.geometry.Rectangle2D;

public class HelpMethods {

	public static boolean CanMoveHere(double x, double y, double width, double height, LevelData levelData) {
		if (!IsSolid(x, y, levelData))
			if (!IsSolid(x + width, y + height, levelData))
				if (!IsSolid(x + width, y, levelData))
					if (!IsSolid(x, y + height, levelData))
						return true;
		return false;
	}

	public static boolean IsSolid(double x, double y, LevelData levelData) {
		double maxHeight = levelData.getLevel().getHeight();
		if (y < 0 || y >= maxHeight)
			return true;

		double maxWidth = levelData.getLevel().getWidth();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x);
		int yIndex = AppStage.GetTilesIn(y);

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	private static boolean IsSolid(double x, double y, int xTiles, int yTiles, LevelData levelData) {
		double maxHeight = levelData.getLevel().getHeight();
		if (y < 0 || y >= maxHeight)
			return true;

		double maxWidth = levelData.getLevel().getWidth();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x) + xTiles;
		int yIndex = AppStage.GetTilesIn(y) + yTiles;

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	public static boolean IsEntityInWater(Rectangle2D hitbox, LevelData levelData) {
		return IsEntityInWater(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight(), levelData) || IsEntityInWater(
				hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight(), levelData);
	}

	private static boolean IsEntityInWater(double xPos, double yPos, LevelData levelData) {
		int xCord = AppStage.GetTilesIn(xPos);
		int yCord = AppStage.GetTilesIn(yPos);
		return levelData.getIsWater()[yCord][xCord];
	}

	public static boolean IsTileSolid(int xTile, int yTile, LevelData levelData) {
		return levelData.getIsSolid()[yTile][xTile];
	}

	public static double GetEntityMinXNextToWall(Rectangle2D hitbox, double xSpeed) {
		if (xSpeed > 0) {
			// Right
			return GetEntityMinXNextRightWall(hitbox);
		} else {
			// Left
			return GetEntityMinXNextLeftWall(hitbox);
		}
	}

	private static double GetEntityMinXNextRightWall(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinX() + hitbox.getWidth() / 2) + 1;
		return currentTile * AppStage.GetTileSize() - hitbox.getWidth() - 1;
	}

	private static double GetEntityMinXNextLeftWall(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinX() + hitbox.getWidth() / 2);
		return currentTile * AppStage.GetTileSize() + 1;
	}

	public static double GetEntityMinYNextToPlane(Rectangle2D hitbox, double airSpeed) {
		if (airSpeed > 0) {
			// Falling - touching floor
			return GetEntityMinYAboveFloor(hitbox);
		} else {
			// Jumping
			return GetEntityMinYUnderRoof(hitbox);
		}
	}

	private static double GetEntityMinYUnderRoof(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinY() + hitbox.getHeight() / 2);
		return currentTile * AppStage.GetTileSize() + 1;
	}

	private static double GetEntityMinYAboveFloor(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinY() + hitbox.getHeight() / 2) + 1;
		return currentTile * AppStage.GetTileSize() - hitbox.getHeight() - 1;
	}

	public static boolean IsEntityOnFloor(Rectangle2D hitbox, LevelData levelData) {
		if (IsSolid(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight(), 0, 1, levelData) || IsSolid(
				hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight(), 0, 1, levelData)) {
			return true;
		}
		return false;
	}

	public static boolean IsFloor(Rectangle2D hitbox, double xSpeed, LevelData levelData) {
		if (xSpeed > 0) {
			return IsSolid(hitbox.getMinX() + hitbox.getWidth() + xSpeed, hitbox.getMinY() + hitbox.getHeight(), 0, 1,
					levelData);
		} else {
			return IsSolid(hitbox.getMinX() + xSpeed, hitbox.getMinY() + hitbox.getHeight(), 0, 1, levelData);
		}
	}

	public static boolean IsFloor(Rectangle2D hitbox, LevelData levelData) {
		if (!IsSolid(hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight(), 0, 1, levelData)
				|| !IsSolid(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight(), 0, 1, levelData)) {
			return false;
		}
		return true;
	}

	public static boolean CanCannonSeePlayer(LevelData levelData, Rectangle2D playerBox, Rectangle2D cannonBox,
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

	public static boolean CanSeePlayer(LevelData levelData, Rectangle2D playerBox, int yTile) {
		return AppStage.GetTilesIn(playerBox.getMinY() + playerBox.getHeight() / 2) == yTile;
	}

	public static boolean IsSightClear(LevelData levelData, Rectangle2D enemyBox, Rectangle2D playerBox, int yTile) {
		int enemyXTile = AppStage.GetTilesIn(enemyBox.getMinX());

		int playerXTile;
		if (IsSolid(playerBox.getMinX(), playerBox.getMinY() + playerBox.getHeight(), 0, 1, levelData))
			playerXTile = AppStage.GetTilesIn(playerBox.getMinX());
		else
			playerXTile = AppStage.GetTilesIn(playerBox.getMinX() + playerBox.getWidth());

		if (enemyXTile > playerXTile) {
			return IsAllTilesWalkable(playerXTile, enemyXTile, yTile, levelData);
		} else {
			return IsAllTilesWalkable(enemyXTile, playerXTile, yTile, levelData);
		}
	}

}