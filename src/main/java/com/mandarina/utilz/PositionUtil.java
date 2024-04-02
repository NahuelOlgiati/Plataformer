package com.mandarina.utilz;

import com.mandarina.game.geometry.Box;
import com.mandarina.game.main.AppStage;

public class PositionUtil {

	public static float GetEntityMinXNextToWall(Box hitbox, float xSpeed) {
		return GetEntityMinXNextToWall(hitbox, xSpeed, false);
	}

	public static float GetEntityMinXNextToWall(Box hitbox, float xSpeed, boolean aprox) {
		if (xSpeed > 0) {
			// Right
			return GetEntityMinXNextRightWall(hitbox, aprox);
		} else {
			// Left
			return GetEntityMinXNextLeftWall(hitbox, aprox);
		}
	}

	private static float GetEntityMinXNextRightWall(Box hitbox, boolean aprox) {
		int currentTile = AppStage.GetTilesIn(aprox ? hitbox.getMinX() + hitbox.getWidth() / 2 : hitbox.getMaxX());
		return (currentTile + 1) * AppStage.GetTileSize() - hitbox.getWidth() - 1;
	}

	private static float GetEntityMinXNextLeftWall(Box hitbox, boolean aprox) {
		int currentTile = AppStage.GetTilesIn(aprox ? hitbox.getMinX() + hitbox.getWidth() / 2 : hitbox.getMinX());
		return currentTile * AppStage.GetTileSize() + 1;
	}

	public static float GetEntityMinYNextToPlane(Box hitbox, float ySpeed) {
		return GetEntityMinYNextToPlane(hitbox, ySpeed, false);
	}

	public static float GetEntityMinYNextToPlane(Box hitbox, float ySpeed, boolean aprox) {
		if (ySpeed > 0) {
			// Falling
			return GetEntityMinYAboveFloor(hitbox, aprox);
		} else {
			// Jumping
			return GetEntityMinYUnderRoof(hitbox, aprox);
		}
	}

	private static float GetEntityMinYUnderRoof(Box hitbox, boolean aprox) {
		int currentTile = AppStage.GetTilesIn(aprox ? hitbox.getMinY() + hitbox.getHeight() / 2 : hitbox.getMinY());
		return currentTile * AppStage.GetTileSize() + 1;
	}

	private static float GetEntityMinYAboveFloor(Box hitbox, boolean aprox) {
		int currentTile = AppStage.GetTilesIn(aprox ? hitbox.getMinY() + hitbox.getHeight() / 2 : hitbox.getMaxY());
		return (currentTile + 1) * AppStage.GetTileSize() - hitbox.getHeight() - 1;
	}
}