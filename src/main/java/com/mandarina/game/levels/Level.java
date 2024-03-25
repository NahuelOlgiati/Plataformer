package com.mandarina.game.levels;

import com.mandarina.game.main.GameCts;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.feature.PNGMetadata;
import com.mandarina.main.AppStage;

public class Level {

	private LvlBuilderImage img;
	private PNGMetadata pm;

	private int imgHeight;
	private int imgWidth;

	private float height;
	private float width;

	private LevelData levelData;
	private LevelEntities levelEntities;
	private LevelObjects levelObjects;

	private float maxLvlOffsetX;
	private float maxLvlOffsetY;

	public Level(LvlBuilderImage img) {
		this.img = img;
		this.pm = new PNGMetadata(img);
		this.imgHeight = (int) img.getHeight();
		this.imgWidth = (int) img.getWidth();
		this.height = this.imgHeight * AppStage.GetTileSize();
		this.width = this.imgWidth * AppStage.GetTileSize();
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

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public LevelData getLevelData() {
		return levelData;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public LevelEntities getLevelEntities() {
		return levelEntities;
	}

	public LevelObjects getLevelObjects() {
		return levelObjects;
	}

	public float getMaxLvlOffsetX() {
		return maxLvlOffsetX;
	}

	public float getMaxLvlOffsetY() {
		return maxLvlOffsetY;
	}

	public void scale() {
		this.height = this.imgHeight * AppStage.GetTileSize();
		this.width = this.imgWidth * AppStage.GetTileSize();
		calcLvlOffsets();
	}
}
