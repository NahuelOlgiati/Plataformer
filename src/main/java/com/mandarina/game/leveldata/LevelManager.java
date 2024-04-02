
package com.mandarina.game.leveldata;

import java.io.File;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.geometry.Box;
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
	public void drawL1(GameDrawer g, Offset offset) {
		this.background.draw(g, offset);
		this.backgroundCloud.draw(g, offset);
		getCurrentLevel().getLevelData().drawL1(g, offset);
	}

	@Override
	public void drawL2(GameDrawer g, Offset offset) {
		getCurrentLevel().getLevelData().drawL2(g, offset);
	}

	@Override
	public void drawL3(GameDrawer g, Offset offset) {
		getCurrentLevel().getLevelData().drawL3(g, offset);
	}

	@Override
	public void drawL4(GameDrawer g, Offset offset) {
		getCurrentLevel().getLevelData().drawL4(g, offset);
	}

	public void update(Offset offset) {
		getCurrentLevel().getLevelData().update(offset);
	}

	public Slide checkOverSlider(Box hitbox) {
		return getCurrentLevel().getLevelData().getSlide(hitbox);
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
