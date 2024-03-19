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

	private int cachedTileOffsetX = -1;
	private int cachedMaxTileOffsetX = -1;
	private double cachedLvlOffsetX = Double.NaN;
	private double cachedMaxOffsetX = Double.NaN;

	private int cachedTileOffsetY = -1;
	private int cachedMaxTileOffsetY = -1;
	private double cachedLvlOffsetY = Double.NaN;
	private double cachedMaxOffsetY = Double.NaN;

	public Offset(Playing playing) {
		this.playing = playing;
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

	private boolean mustUpdateX(Point2D spawn) {
		checkCachedLvlOffsetX();
		return checkTileRangeX(spawn.getX());
	}

	private boolean mustUpdateY(Point2D spawn) {
		checkCachedLvlOffsetY();
		return checkTileRangeY(spawn.getY());
	}

	private boolean mustUpdateX(Rectangle2D hitbox) {
		checkCachedLvlOffsetX();
		return checkRangeX(hitbox);
	}

	private boolean mustUpdateY(Rectangle2D hitbox) {
		checkCachedLvlOffsetY();
		return checkRangeY(hitbox);
	}

	private boolean checkTileRangeX(double spawnX) {
		return spawnX >= cachedTileOffsetX && spawnX <= cachedMaxTileOffsetX;
	}

	private boolean checkTileRangeY(double spawnY) {
		return spawnY >= cachedTileOffsetY && spawnY <= cachedMaxTileOffsetY;
	}

	private boolean checkRangeX(Rectangle2D hitbox) {
		double minX = hitbox.getMinX();
		double maxX = hitbox.getMaxX();
		return (minX >= lvlOffsetX && minX <= cachedMaxOffsetX) || //
				(maxX >= lvlOffsetX && maxX <= cachedMaxOffsetX);
	}

	private boolean checkRangeY(Rectangle2D hitbox) {
		double minY = hitbox.getMinY();
		double maxY = hitbox.getMaxY();
		return (minY >= lvlOffsetY && minY <= cachedMaxOffsetY) || //
				(maxY >= lvlOffsetY && maxY <= cachedMaxOffsetY);
	}

	private void checkCachedLvlOffsetX() {
		if (lvlOffsetX != cachedLvlOffsetX) {
			cachedLvlOffsetX = lvlOffsetX;
			cachedMaxOffsetX = lvlOffsetX + AppStage.GetGameWidth();
			cachedTileOffsetX = AppStage.GetTilesIn(lvlOffsetX);
			cachedMaxTileOffsetX = cachedTileOffsetX + GameCts.TILES_IN_WIDTH;
		}
	}

	private void checkCachedLvlOffsetY() {
		if (lvlOffsetY != cachedLvlOffsetY) {
			cachedLvlOffsetY = lvlOffsetY;
			cachedMaxOffsetY = lvlOffsetY + AppStage.GetGameHeight();
			cachedTileOffsetY = AppStage.GetTilesIn(lvlOffsetY);
			cachedMaxTileOffsetY = cachedTileOffsetY + GameCts.TILES_IN_HEIGHT;
		}
	}

	public void scale() {
		this.leftBorder = 0.25 * AppStage.GetGameWidth();
		this.rightBorder = 0.75 * AppStage.GetGameWidth();
		this.bottomBorder = 0.25 * AppStage.GetGameHeight();
		this.topBorder = 0.75 * AppStage.GetGameHeight();
	}
}