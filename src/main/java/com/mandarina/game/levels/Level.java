package com.mandarina.game.levels;

import com.mandarina.game.main.GameCts;
import com.mandarina.lvlbuilder.LvlBuilderImage;

public class Level {

	private LvlBuilderImage img;

	private LevelData levelData;
	private LevelEntities levelEntities;
	private LevelObjects levelObjects;

	private int maxLvlOffsetX;
	private int maxLvlOffsetY;

	public Level(LvlBuilderImage img) {
		this.img = img;
		this.levelData = new LevelData(img);
		this.levelEntities = new LevelEntities(img);
		this.levelObjects = new LevelObjects(img);
		calcLvlOffsets();
	}

	private void calcLvlOffsets() {
		int lvlTilesWide = (int) img.getWidth();
		int maxTilesOffsetX = lvlTilesWide - GameCts.TILES_IN_WIDTH;
		maxLvlOffsetX = GameCts.TILES_SIZE * maxTilesOffsetX;

		int lvlTilesHeight = (int) img.getHeight();
		int maxTilesOffsetY = lvlTilesHeight - GameCts.TILES_IN_HEIGHT;
		maxLvlOffsetY = GameCts.TILES_SIZE * maxTilesOffsetY;
	}

	public LevelData getLevelData() {
		return levelData;
	}

	public LevelEntities getLevelEntities() {
		return levelEntities;
	}

	public LevelObjects getLevelObjects() {
		return levelObjects;
	}

	public int getLvlOffsetX() {
		return maxLvlOffsetX;
	}

	public int getLvlOffsetY() {
		return maxLvlOffsetY;
	}
}
