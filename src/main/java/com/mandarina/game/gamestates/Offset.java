package com.mandarina.game.gamestates;

import com.mandarina.main.AppStage;

public class Offset {

	private Playing playing;

	private double lvlOffsetX;
	private double lvlOffsetY;

	private double leftBorder;
	private double rightBorder;
	private double bottomBorder;
	private double topBorder;

	public Offset(Playing playing) {
		this.playing = playing;
		this.leftBorder = 0.25 * AppStage.GetGameWidth();
		this.rightBorder = 0.75 * AppStage.GetGameWidth();
		this.bottomBorder = 0.25 * AppStage.GetGameHeight();
		this.topBorder = 0.75 * AppStage.GetGameHeight();
	}

	public void update() {
		checkCloseToBorderX();
		checkCloseToBorderY();
	}

	private void checkCloseToBorderX() {
		double playerX = playing.getPlayer().getHitbox().getMinX();
		double diff = playerX - lvlOffsetX;

		if (diff > rightBorder)
			lvlOffsetX += diff - rightBorder;
		else if (diff < leftBorder)
			lvlOffsetX += diff - leftBorder;

		lvlOffsetX = Math.max(Math.min(lvlOffsetX, playing.getCurrentLevel().getMaxLvlOffsetX()), 0);
	}

	private void checkCloseToBorderY() {
		double playerY = playing.getPlayer().getHitbox().getMinY();
		double diff = playerY - lvlOffsetY;

		if (diff > topBorder)
			lvlOffsetY += diff - topBorder;
		else if (diff < bottomBorder)
			lvlOffsetY += diff - bottomBorder;

		lvlOffsetY = Math.max(Math.min(lvlOffsetY, playing.getCurrentLevel().getMaxLvlOffsetY()), 0);
	}

	public double getX() {
		return lvlOffsetX;
	}

	public double getY() {
		return lvlOffsetY;
	}

	public void scale() {
		this.leftBorder = 0.25 * AppStage.GetGameWidth();
		this.rightBorder = 0.75 * AppStage.GetGameWidth();
		this.bottomBorder = 0.25 * AppStage.GetGameHeight();
		this.topBorder = 0.75 * AppStage.GetGameHeight();
	}
}