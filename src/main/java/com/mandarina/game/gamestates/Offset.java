package com.mandarina.game.gamestates;

import com.mandarina.game.levels.Level;
import com.mandarina.game.main.GameCts;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.Point;

public class Offset {

	private Playing playing;

	private float lvlOffsetX;
	private float lvlOffsetY;

	private float leftBorder;
	private float rightBorder;
	private float bottomBorder;
	private float topBorder;

	private int cachedTileOffsetX;
	private int cachedMaxTileOffsetX;
	private float cachedLvlOffsetX;
	private float cachedMaxOffsetX;

	private int cachedTileOffsetY;
	private int cachedMaxTileOffsetY;
	private float cachedLvlOffsetY;
	private float cachedMaxOffsetY;

	private float cachedLvlOffsetHalfX;
	private float cachedMaxOffsetHalfX;
	private float cachedLvlOffsetHalfY;
	private float cachedMaxOffsetHalfY;

	public Offset(Playing playing) {
		this.playing = playing;
		reset();
	}

	public void reset() {
		this.cachedTileOffsetX = -1;
		this.cachedMaxTileOffsetX = -1;
		this.cachedLvlOffsetX = -1;
		this.cachedMaxOffsetX = -1;
		this.cachedTileOffsetY = -1;
		this.cachedMaxTileOffsetY = -1;
		this.cachedLvlOffsetY = -1;
		this.cachedMaxOffsetY = -1;
		this.leftBorder = 0.25f * AppStage.GetGameWidth();
		this.rightBorder = 0.75f * AppStage.GetGameWidth();
		this.bottomBorder = 0.25f * AppStage.GetGameHeight();
		this.topBorder = 0.75f * AppStage.GetGameHeight();
	}

	public void update() {
		Box playerHitbox = playing.getPlayer().getHitbox();
		Level currentLevel = playing.getCurrentLevel();
		checkCloseToBorderX(playerHitbox, currentLevel);
		checkCloseToBorderY(playerHitbox, currentLevel);
	}

	private void checkCloseToBorderX(Box playerHitbox, Level currentLevel) {
		float playerX = playerHitbox.getMinX();
		float diff = playerX - lvlOffsetX;

		if (diff > rightBorder)
			lvlOffsetX += diff - rightBorder;
		else if (diff < leftBorder)
			lvlOffsetX += diff - leftBorder;

		lvlOffsetX = Math.max(Math.min(lvlOffsetX, currentLevel.getMaxLvlOffsetX()), 0);
	}

	private void checkCloseToBorderY(Box playerHitbox, Level currentLevel) {
		float playerY = playerHitbox.getMinY();
		float diff = playerY - lvlOffsetY;

		if (diff > topBorder)
			lvlOffsetY += diff - topBorder;
		else if (diff < bottomBorder)
			lvlOffsetY += diff - bottomBorder;

		lvlOffsetY = Math.max(Math.min(lvlOffsetY, currentLevel.getMaxLvlOffsetY()), 0);
	}

	public float getX() {
		return lvlOffsetX;
	}

	public float getY() {
		return lvlOffsetY;
	}

	public boolean in(Point p) {
		return mustUpdateX(p) && mustUpdateY(p);
	}

	public boolean in(Box h) {
		return mustUpdateX(h) && mustUpdateY(h);
	}

	public boolean inHalf(Box h) {
		return mustUpdateHalfX(h) && mustUpdateHalfY(h);
	}

	private boolean mustUpdateX(Point spawn) {
		checkCachedLvlOffsetX();
		return checkTileRangeX((int) spawn.getX());
	}

	private boolean mustUpdateY(Point spawn) {
		checkCachedLvlOffsetY();
		return checkTileRangeY((int) spawn.getY());
	}

	private boolean mustUpdateX(Box hitbox) {
		checkCachedLvlOffsetX();
		return checkRangeX(hitbox);
	}

	private boolean mustUpdateY(Box hitbox) {
		checkCachedLvlOffsetY();
		return checkRangeY(hitbox);
	}

	private boolean mustUpdateHalfX(Box hitbox) {
		checkCachedLvlOffsetX();
		return checkRangeHalfX(hitbox);
	}

	private boolean mustUpdateHalfY(Box hitbox) {
		checkCachedLvlOffsetY();
		return checkRangeHalfY(hitbox);
	}

	private boolean checkTileRangeX(int spawnX) {
		return spawnX >= cachedTileOffsetX && spawnX <= cachedMaxTileOffsetX;
	}

	private boolean checkTileRangeY(int spawnY) {
		return spawnY >= cachedTileOffsetY && spawnY <= cachedMaxTileOffsetY;
	}

	private boolean checkRangeX(Box hitbox) {
		float minX = hitbox.getMinX();
		float maxX = hitbox.getMaxX();
		return (minX >= cachedLvlOffsetX && minX <= cachedMaxOffsetX) || //
				(maxX >= cachedLvlOffsetX && maxX <= cachedMaxOffsetX);
	}

	private boolean checkRangeY(Box hitbox) {
		float minY = hitbox.getMinY();
		float maxY = hitbox.getMaxY();
		return (minY >= cachedLvlOffsetY && minY <= cachedMaxOffsetY) || //
				(maxY >= cachedLvlOffsetY && maxY <= cachedMaxOffsetY);
	}

	private boolean checkRangeHalfX(Box hitbox) {
		float minX = hitbox.getMinX();
		float maxX = hitbox.getMaxX();
		return (minX >= cachedLvlOffsetHalfX && minX <= cachedMaxOffsetHalfX) || //
				(maxX >= cachedLvlOffsetHalfX && maxX <= cachedMaxOffsetHalfX);
	}

	private boolean checkRangeHalfY(Box hitbox) {
		float minY = hitbox.getMinY();
		float maxY = hitbox.getMaxY();
		return (minY >= cachedLvlOffsetHalfY && minY <= cachedMaxOffsetHalfY) || //
				(maxY >= cachedLvlOffsetHalfY && maxY <= cachedMaxOffsetHalfY);
	}

	private void checkCachedLvlOffsetX() {
		if (lvlOffsetX != cachedLvlOffsetX) {
			float half = AppStage.GetGameWidth() / 2;
			cachedLvlOffsetX = lvlOffsetX;
			cachedLvlOffsetHalfX = lvlOffsetX - half;
			cachedMaxOffsetX = lvlOffsetX + AppStage.GetGameWidth();
			cachedMaxOffsetHalfX = cachedMaxOffsetX + half;
			cachedTileOffsetX = AppStage.GetTilesIn(lvlOffsetX);
			cachedMaxTileOffsetX = cachedTileOffsetX + GameCts.TILES_IN_WIDTH;
		}
	}

	private void checkCachedLvlOffsetY() {
		if (lvlOffsetY != cachedLvlOffsetY) {
			float half = AppStage.GetGameHeight() / 2;
			cachedLvlOffsetY = lvlOffsetY;
			cachedLvlOffsetHalfY = lvlOffsetY - half;
			cachedMaxOffsetY = lvlOffsetY + AppStage.GetGameHeight();
			cachedMaxOffsetHalfY = cachedMaxOffsetY + half;
			cachedTileOffsetY = AppStage.GetTilesIn(lvlOffsetY);
			cachedMaxTileOffsetY = cachedTileOffsetY + GameCts.TILES_IN_HEIGHT;
		}
	}

	public void scale() {
		reset();
	}
}