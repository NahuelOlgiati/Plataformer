package com.mandarina.game.levels;

import com.mandarina.game.constants.GameCts;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.PNGMetadata;

public class Level {

	private LvlBuilderImage img;
	private PNGMetadata metadata;

	private LevelData levelData;
	private LevelEntities levelEntities;
	private LevelObjects levelObjects;

	private int lvlTilesWide;
	private int maxTilesOffsetX;
	private int maxLvlOffsetX;

	private int lvlTilesHeight;
	private int maxTilesOffsetY;
	private int maxLvlOffsetY;

	public Level(LvlBuilderImage img) {
		this.img = img;
		this.levelData = new LevelData(img);
		this.levelEntities = new LevelEntities(img);
		this.levelObjects = new LevelObjects(img);
		loadMetadata();
		calcLvlOffsets();
	}

	private void loadMetadata() {
		this.metadata = new PNGMetadata();
		this.metadata.load(img);
	}

	private void calcLvlOffsets() {
		lvlTilesWide = (int) img.getWidth();
		maxTilesOffsetX = lvlTilesWide - GameCts.TILES_IN_WIDTH;
		maxLvlOffsetX = GameCts.TILES_SIZE * maxTilesOffsetX;

		lvlTilesHeight = (int) img.getHeight();
		maxTilesOffsetY = lvlTilesHeight - GameCts.TILES_IN_HEIGHT;
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
