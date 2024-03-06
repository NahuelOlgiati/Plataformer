package com.mandarina.game.levels;

import com.mandarina.game.main.GameCts;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.feature.PNGMetadata;
import com.mandarina.main.AppStage;

public class Level {

	private LvlBuilderImage img;
	private PNGMetadata pm;

	private int height;
	private int width;

	private LevelData levelData;
	private LevelEntities levelEntities;
	private LevelObjects levelObjects;

	private double maxLvlOffsetX;
	private double maxLvlOffsetY;

	public Level(LvlBuilderImage img) {
		this.img = img;
		this.pm = new PNGMetadata(img);
		this.height = (int) img.getHeight();
		this.width = (int) img.getWidth();
		this.levelData = new LevelData(this);
		this.levelEntities = new LevelEntities(this);
		this.levelObjects = new LevelObjects(this);
		calcLvlOffsets();
	}

	private void calcLvlOffsets() {
		int lvlTilesWide = (int) img.getWidth();
		int maxTilesOffsetX = lvlTilesWide - GameCts.TILES_IN_WIDTH;
		maxLvlOffsetX = AppStage.GetTileSize() * maxTilesOffsetX;

		int lvlTilesHeight = (int) img.getHeight();
		int maxTilesOffsetY = lvlTilesHeight - GameCts.TILES_IN_HEIGHT;
		maxLvlOffsetY = AppStage.GetTileSize() * maxTilesOffsetY;
	}

	public LvlBuilderImage getImg() {
		return img;
	}

	public PNGMetadata getPm() {
		return pm;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
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

	public double getMaxLvlOffsetX() {
		return maxLvlOffsetX;
	}

	public double getMaxLvlOffsetY() {
		return maxLvlOffsetY;
	}

	public void scale() {
		calcLvlOffsets();
	}
}
