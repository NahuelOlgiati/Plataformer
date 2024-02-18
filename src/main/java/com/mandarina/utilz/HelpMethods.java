package com.mandarina.utilz;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.objects.Projectile;

import javafx.geometry.Rectangle2D;

public class HelpMethods {

	public static boolean CanMoveHere(double x, double y, double width, double height, LevelData lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

	private static boolean IsSolid(double x, double y, LevelData lvlData) {
		int maxHeight = lvlData.getHeight() * GameCts.TILES_SIZE;
		if (y < 0 || y >= maxHeight)
			return true;

		int maxWidth = lvlData.getWidth() * GameCts.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;

		double xIndex = x / GameCts.TILES_SIZE;
		double yIndex = y / GameCts.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	public static boolean IsProjectileHittingLevel(Projectile p, LevelData lvlData) {
		return IsSolid(p.getHitbox().getMinX() + p.getHitbox().getWidth() / 2,
				p.getHitbox().getMinY() + p.getHitbox().getHeight() / 2, lvlData);
	}

//	public static boolean IsEntityInWater(Rectangle2D hitbox, LevelData lvlData) {
//		// Will only check if entity touches the top water. Can't reach bottom water if
//		// not
//		// touched top water.
//		if (GetTileValue(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight(), lvlData) != 48
//				|| GetTileValue(hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight(),
//						lvlData) != 48) {
//			return false;
//		}
//		return true;
//	}
//
//	private static int GetTileValue(double xPos, double yPos, LevelData lvlData) {
//		int xCord = (int) (xPos / GameCts.TILES_SIZE);
//		int yCord = (int) (yPos / GameCts.TILES_SIZE);
//		return lvlData.getAll()[yCord][xCord];
//	}

	public static boolean IsEntityInWater(Rectangle2D hitbox, LevelData lvlData) {
		return IsEntityInWater(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight(), lvlData) || IsEntityInWater(
				hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight(), lvlData);
	}

	private static boolean IsEntityInWater(double xPos, double yPos, LevelData lvlData) {
		int xCord = (int) (xPos / GameCts.TILES_SIZE);
		int yCord = (int) (yPos / GameCts.TILES_SIZE);
		return lvlData.getIsWater()[yCord][xCord];
	}

	public static boolean IsTileSolid(int xTile, int yTile, LevelData lvlData) {
		return lvlData.getIsSolid()[yTile][xTile];
	}

	public static float GetEntityXPosNextToWall(Rectangle2D hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.getMinX() / GameCts.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * GameCts.TILES_SIZE;
			int xOffset = (int) (GameCts.TILES_SIZE - hitbox.getWidth());
			return tileXPos + xOffset - 1;
		} else {
			// Left
			return currentTile * GameCts.TILES_SIZE;
		}
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.getMinY() / GameCts.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * GameCts.TILES_SIZE;
			int yOffset = (int) (GameCts.TILES_SIZE - hitbox.getHeight());
			return tileYPos + yOffset - 1;
		} else {
			// Jumping
			return currentTile * GameCts.TILES_SIZE;
		}
	}

	public static boolean IsEntityOnFloor(Rectangle2D hitbox, LevelData lvlData) {
		if (!IsSolid(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight() + 1, lvlData)
				|| !IsSolid(hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight() + 1, lvlData)) {
			return false;
		}
		return true;
	}

	public static boolean IsFloor(Rectangle2D hitbox, float xSpeed, LevelData lvlData) {
		if (xSpeed > 0) {
			return IsSolid(hitbox.getMinX() + hitbox.getWidth() + xSpeed, hitbox.getMinY() + hitbox.getHeight() + 1,
					lvlData);
		} else {
			return IsSolid(hitbox.getMinX() + xSpeed, hitbox.getMinY() + hitbox.getHeight() + 1, lvlData);
		}
	}

	public static boolean IsFloor(Rectangle2D hitbox, LevelData lvlData) {
		if (!IsSolid(hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY() + hitbox.getHeight() + 1, lvlData)
				|| !IsSolid(hitbox.getMinX(), hitbox.getMinY() + hitbox.getHeight() + 1, lvlData)) {
			return false;
		}
		return true;
	}

	public static boolean CanCannonSeePlayer(LevelData lvlData, Rectangle2D firstHitbox, Rectangle2D secondHitbox,
			int yTile) {
		int firstXTile = (int) (firstHitbox.getMinX() / GameCts.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.getMinX() / GameCts.TILES_SIZE);

		if (firstXTile > secondXTile) {
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		} else {
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
		}
	}

	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, LevelData lvlData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}

	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, LevelData lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	public static boolean IsSightClear(LevelData lvlData, Rectangle2D enemyBox, Rectangle2D playerBox, int yTile) {
		int firstXTile = (int) (enemyBox.getMinX() / GameCts.TILES_SIZE);

		int secondXTile;
		if (IsSolid(playerBox.getMinX(), playerBox.getMinY() + playerBox.getHeight() + 1, lvlData))
			secondXTile = (int) (playerBox.getMinX() / GameCts.TILES_SIZE);
		else
			secondXTile = (int) ((playerBox.getMinX() + playerBox.getWidth()) / GameCts.TILES_SIZE);

		if (firstXTile > secondXTile) {
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		} else {
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
		}
	}

}