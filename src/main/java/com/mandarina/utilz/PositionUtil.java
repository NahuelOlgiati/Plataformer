package com.mandarina.utilz;

import com.mandarina.main.AppStage;

import javafx.geometry.Rectangle2D;

public class PositionUtil {

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
		int currentTile = AppStage.GetTilesIn(hitbox.getMaxX()) + 1;
		return currentTile * AppStage.GetTileSize() - hitbox.getWidth() - 1;
	}

	private static double GetEntityMinXNextLeftWall(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinX());
		return currentTile * AppStage.GetTileSize() + 1;
	}

	public static double GetEntityMinYNextToPlane(Rectangle2D hitbox, double airSpeed) {
		if (airSpeed > 0) {
			// Falling
			return GetEntityMinYAboveFloor(hitbox);
		} else {
			// Jumping
			return GetEntityMinYUnderRoof(hitbox);
		}
	}

	private static double GetEntityMinYUnderRoof(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMinY());
		return currentTile * AppStage.GetTileSize() + 1;
	}

	private static double GetEntityMinYAboveFloor(Rectangle2D hitbox) {
		int currentTile = AppStage.GetTilesIn(hitbox.getMaxY()) + 1;
		return currentTile * AppStage.GetTileSize() - hitbox.getHeight() - 1;
	}
}