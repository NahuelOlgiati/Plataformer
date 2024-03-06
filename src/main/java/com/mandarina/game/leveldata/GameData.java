package com.mandarina.game.leveldata;

import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;

public class GameData {

	protected Point2D spawn;
	protected double x, y;

	public GameData(Point2D spawn) {
		this.spawn = spawn;
		init();
	}

	protected void init() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	public void scale() {
		init();
	}

}
