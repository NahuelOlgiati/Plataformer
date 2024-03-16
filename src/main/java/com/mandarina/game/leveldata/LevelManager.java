
package com.mandarina.game.leveldata;

import java.io.File;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.utilz.LoadSave;

public class LevelManager implements LayerDrawer {

	private Playing playing;
	private Level currentLevel;
	private int lvlIndex;
	private Integer numOfLevels;

	private Background background;
	private BackgroundCloud backgroundCloud;

	private File folderLvls;

//	private boolean offsetChange = false;
//	private double lastLvlOffsetX = -1;
//	private double lastLvlOffsetY = -1;
//	private WritableImage snapshot = null;

	public LevelManager(Playing playing) {
		this.playing = playing;
		this.background = new Background();
		this.backgroundCloud = new BackgroundCloud();
	}

	public void loadNextLevel() {
		lvlIndex++;
		Level level = getLevel();
		this.currentLevel = level;
		loadLevel(currentLevel);
	}

	public void loadCustomLevel(LvlBuilderImage image) {
		this.lvlIndex = 0;
		this.currentLevel = new Level(image);
		loadLevel(currentLevel);
	}

	public void loadCustomFolder(File folder) {
		this.folderLvls = folder;
		this.lvlIndex = 1;
		Level level = getLevel();
		this.currentLevel = level;
		loadLevel(currentLevel);
	}

	private Level getLevel() {
		if (this.folderLvls == null) {
			return new Level(LoadSave.GetLevel(lvlIndex));
		} else {
			return new Level(LoadSave.GetLevel(folderLvls, lvlIndex));
		}
	}

	public void loadLevel(Level level) {
		if (this.numOfLevels == null) {
			if (this.folderLvls == null) {
				this.numOfLevels = LoadSave.GetNumOfLevels();
			} else {
				this.numOfLevels = LoadSave.GetNumOfLevels(this.folderLvls);
			}
		}
		playing.getObjectManager().loadObjects(level);
		playing.getEnemyManager().loadEnemies(level);
	}

	@Override
	public void drawL1(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.background.draw(g, lvlOffsetX, lvlOffsetY);
		this.backgroundCloud.draw(g, lvlOffsetX, lvlOffsetY);
		getCurrentLevel().getLevelData().drawL1(g, lvlOffsetX, lvlOffsetY);
//		this.offsetChange = this.lastLvlOffsetX != lvlOffsetX || this.lastLvlOffsetY != lvlOffsetY;
//		if (offsetChange) {
//			this.background.draw(g, lvlOffsetX, lvlOffsetY);
//			this.backgroundCloud.draw(g, lvlOffsetX, lvlOffsetY);
//			getCurrentLevel().getLevelData().drawL1(g, lvlOffsetX, lvlOffsetY);
//			this.snapshot = g.getSnapshot();
//			this.lastLvlOffsetX = lvlOffsetX;
//			this.lastLvlOffsetY = lvlOffsetY;
//		} else {
//			g.drawImage(snapshot, 0, 0, snapshot.getWidth(), snapshot.getHeight());
//		}
	}

	@Override
	public void drawL2(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		getCurrentLevel().getLevelData().drawL2(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL3(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		getCurrentLevel().getLevelData().drawL3(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL4(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		getCurrentLevel().getLevelData().drawL4(g, lvlOffsetX, lvlOffsetY);
	}

	public void update() {
		getCurrentLevel().getLevelData().update();
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public int getNumOfLevels() {
		return numOfLevels;
	}

	public int getLevelIndex() {
		return lvlIndex;
	}

	public void resetLvlIndex() {
		this.lvlIndex = 0;
	}

	public void reset() {
		this.currentLevel = null;
		this.folderLvls = null;
		resetLvlIndex();
		loadNextLevel();
	}

	public void scale() {
		if (currentLevel != null) {
			getCurrentLevel().getLevelData().scale();
		}
		this.backgroundCloud.scale();
	}

}
