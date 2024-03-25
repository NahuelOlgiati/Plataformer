package com.mandarina.game.leveldata;

import com.mandarina.main.AppStage;

import com.mandarina.utilz.Point;

public class GameData {

	protected Point spawn;
	protected float x, y;

	public GameData(Point spawn) {
		this.spawn = spawn;
		init();
	}

	protected void init() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	public Point getSpawn() {
		return spawn;
	}

	public void scale() {
		init();
	}

}
