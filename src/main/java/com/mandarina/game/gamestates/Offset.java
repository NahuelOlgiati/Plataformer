package com.mandarina.game.gamestates;

import com.mandarina.game.levels.Level;
import com.mandarina.game.main.GameCts;
import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class Offset {

	private Playing playing;

	private double lvlOffsetX;
	private double lvlOffsetY;

	private double leftBorder;
	private double rightBorder;
	private double bottomBorder;
	private double topBorder;

	private int cachedTileOffsetX;
	private int cachedMaxTileOffsetX;
	private double cachedLvlOffsetX;
	private double cachedMaxOffsetX;

	private int cachedTileOffsetY;
	private int cachedMaxTileOffsetY;
	private double cachedLvlOffsetY;
	private double cachedMaxOffsetY;

	private double cachedLvlOffsetHalfX;
	private double cachedMaxOffsetHalfX;
	private double cachedLvlOffsetHalfY;
	private double cachedMaxOffsetHalfY;

	public Offset(Playing playing) {
		this.playing = playing;
		reset();
	}

	public void reset() {
		this.cachedTileOffsetX = -1;
		this.cachedMaxTileOffsetX = -1;
		this.cachedLvlOffsetX = Double.NaN;
		this.cachedMaxOffsetX = Double.NaN;
		this.cachedTileOffsetY = -1;
		this.cachedMaxTileOffsetY = -1;
		this.cachedLvlOffsetY = Double.NaN;
		this.cachedMaxOffsetY = Double.NaN;
		this.leftBorder = 0.25 * AppStage.GetGameWidth();
		this.rightBorder = 0.75 * AppStage.GetGameWidth();
		this.bottomBorder = 0.25 * AppStage.GetGameHeight();
		this.topBorder = 0.75 * AppStage.GetGameHeight();
	}

	public void update() {
		Rectangle2D playerHitbox = playing.getPlayer().getHitbox();
		Level currentLevel = playing.getCurrentLevel();
		checkCloseToBorderX(playerHitbox, currentLevel);
		checkCloseToBorderY(playerHitbox, currentLevel);
	}

	private void checkCloseToBorderX(Rectangle2D playerHitbox, Level currentLevel) {
		double playerX = playerHitbox.getMinX();
		double diff = playerX - lvlOffsetX;

		if (diff > rightBorder)
			lvlOffsetX += diff - rightBorder;
		else if (diff < leftBorder)
			lvlOffsetX += diff - leftBorder;

		lvlOffsetX = Math.max(Math.min(lvlOffsetX, currentLevel.getMaxLvlOffsetX()), 0);
	}

	private void checkCloseToBorderY(Rectangle2D playerHitbox, Level currentLevel) {
		double playerY = playerHitbox.getMinY();
		double diff = playerY - lvlOffsetY;

		if (diff > topBorder)
			lvlOffsetY += diff - topBorder;
		else if (diff < bottomBorder)
			lvlOffsetY += diff - bottomBorder;

		lvlOffsetY = Math.max(Math.min(lvlOffsetY, currentLevel.getMaxLvlOffsetY()), 0);
	}

	public double getX() {
		return lvlOffsetX;
	}

	public double getY() {
		return lvlOffsetY;
	}

	public boolean in(Point2D p) {
		return mustUpdateX(p) && mustUpdateY(p);
	}

	public boolean in(Rectangle2D h) {
		return mustUpdateX(h) && mustUpdateY(h);
	}

	public boolean inHalf(Rectangle2D h) {
		return mustUpdateHalfX(h) && mustUpdateHalfY(h);
	}

	private boolean mustUpdateX(Point2D spawn) {
		checkCachedLvlOffsetX();
		return checkTileRangeX((int) spawn.getX());
	}

	private boolean mustUpdateY(Point2D spawn) {
		checkCachedLvlOffsetY();
		return checkTileRangeY((int) spawn.getY());
	}

	private boolean mustUpdateX(Rectangle2D hitbox) {
		checkCachedLvlOffsetX();
		return checkRangeX(hitbox);
	}

	private boolean mustUpdateY(Rectangle2D hitbox) {
		checkCachedLvlOffsetY();
		return checkRangeY(hitbox);
	}

	private boolean mustUpdateHalfX(Rectangle2D hitbox) {
		checkCachedLvlOffsetX();
		return checkRangeHalfX(hitbox);
	}

	private boolean mustUpdateHalfY(Rectangle2D hitbox) {
		checkCachedLvlOffsetY();
		return checkRangeHalfY(hitbox);
	}

	private boolean checkTileRangeX(int spawnX) {
		return spawnX >= cachedTileOffsetX && spawnX <= cachedMaxTileOffsetX;
	}

	private boolean checkTileRangeY(int spawnY) {
		return spawnY >= cachedTileOffsetY && spawnY <= cachedMaxTileOffsetY;
	}

	private boolean checkRangeX(Rectangle2D hitbox) {
		double minX = hitbox.getMinX();
		double maxX = hitbox.getMaxX();
		return (minX >= cachedLvlOffsetX && minX <= cachedMaxOffsetX) || //
				(maxX >= cachedLvlOffsetX && maxX <= cachedMaxOffsetX);
	}

	private boolean checkRangeY(Rectangle2D hitbox) {
		double minY = hitbox.getMinY();
		double maxY = hitbox.getMaxY();
		return (minY >= cachedLvlOffsetY && minY <= cachedMaxOffsetY) || //
				(maxY >= cachedLvlOffsetY && maxY <= cachedMaxOffsetY);
	}

	private boolean checkRangeHalfX(Rectangle2D hitbox) {
		double minX = hitbox.getMinX();
		double maxX = hitbox.getMaxX();
		return (minX >= cachedLvlOffsetHalfX && minX <= cachedMaxOffsetHalfX) || //
				(maxX >= cachedLvlOffsetHalfX && maxX <= cachedMaxOffsetHalfX);
	}

	private boolean checkRangeHalfY(Rectangle2D hitbox) {
		double minY = hitbox.getMinY();
		double maxY = hitbox.getMaxY();
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