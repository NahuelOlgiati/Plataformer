package com.mandarina.utilz;

import com.mandarina.game.levels.LevelData;
import com.mandarina.game.objects.Projectile;
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

	private static boolean IsSolid(double x, double y, LevelData levelData) {
		double maxHeight = levelData.getLevel().getHeight() * AppStage.GetTileSize();
		if (y < 0 || y >= maxHeight)
			return true;

		double maxWidth = levelData.getLevel().getWidth() * AppStage.GetTileSize();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x);
		int yIndex = AppStage.GetTilesIn(y);

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	private static boolean IsSolid(double x, double y, int xTiles, int yTiles, LevelData levelData) {
		double maxHeight = levelData.getLevel().getHeight() * AppStage.GetTileSize();
		if (y < 0 || y >= maxHeight)
			return true;

		double maxWidth = levelData.getLevel().getWidth() * AppStage.GetTileSize();
		if (x < 0 || x >= maxWidth)
			return true;

		int xIndex = AppStage.GetTilesIn(x) + xTiles;
		int yIndex = AppStage.GetTilesIn(y) + yTiles;

		return IsTileSolid(xIndex, yIndex, levelData);
	}

	public static boolean IsProjectileHittingLevel(Projectile p, LevelData levelData) {
		return IsSolid(p.getHitbox().getMinX() + p.getHitbox().getWidth() / 2,
				p.getHitbox().getMinY() + p.getHitbox().getHeight() / 2, levelData);
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

	public static double GetEntityXPosNextToWall(Rectangle2D hitbox, double xSpeed) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinX());
		if (xSpeed > 0) {
			// Right
			double tileXPos = currentTile * AppStage.GetTileSize();
			double xOffset = AppStage.GetTileSize() - hitbox.getWidth();
			return tileXPos + xOffset - 1;
		} else {
			// Left
			return currentTile * AppStage.GetTileSize();
		}
	}

	public static double GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D hitbox, double airSpeed) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinY());
		if (airSpeed > 0) {
			// Falling - touching floor
			double tileYPos = currentTile * AppStage.GetTileSize();
			double yOffset = AppStage.GetTileSize() - hitbox.getHeight();
			return tileYPos + yOffset - 1;
		} else {
			// Jumping
			return currentTile * AppStage.GetTileSize();
		}
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

	public static boolean CanCannonSeePlayer(LevelData levelData, Rectangle2D firstHitbox, Rectangle2D secondHitbox,
			int yTile) {
		int firstXTile = AppStage.GetTilesIn(firstHitbox.getMinX());
		int secondXTile = AppStage.GetTilesIn(secondHitbox.getMinX());

		if (firstXTile > secondXTile) {
			return IsAllTilesClear(secondXTile, firstXTile, yTile, levelData);
		} else {
			return IsAllTilesClear(firstXTile, secondXTile, yTile, levelData);
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

	public static boolean IsSightClear(LevelData levelData, Rectangle2D enemyBox, Rectangle2D playerBox, int yTile) {
		int firstXTile = AppStage.GetTilesIn(enemyBox.getMinX());

		int secondXTile;
		if (IsSolid(playerBox.getMinX(), playerBox.getMinY() + playerBox.getHeight() + 1, levelData))
			secondXTile = AppStage.GetTilesIn(playerBox.getMinX());
		else
			secondXTile = AppStage.GetTilesIn(playerBox.getMinX() + playerBox.getWidth());

		if (firstXTile > secondXTile) {
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, levelData);
		} else {
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, levelData);
		}
	}

}