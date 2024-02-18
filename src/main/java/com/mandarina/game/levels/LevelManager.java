
package com.mandarina.game.levels;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;

public class LevelManager {

	private Playing playing;
	private Level currentLevel;
	private int lvlIndex;
	private int numOfLevels;

	public LevelManager(Playing playing) {
		this.playing = playing;
		this.lvlIndex = 0;
		this.numOfLevels = LoadSave.GetNumOfLevels();
	}

	public void loadNextLevel() {
		lvlIndex++;
		LvlBuilderImage lvlImg = LoadSave.GetLevel(lvlIndex);
		Level level = new Level(lvlImg);
		this.currentLevel = level;
		loadLevel(level);
	}

	public void loadCustomLevel(Level level) {
		this.lvlIndex = 0;
		this.currentLevel = level;
		loadLevel(level);
	}

	public void loadLevel(Level level) {
		playing.getObjectManager().loadObjects(level);
		playing.getEnemyManager().loadEnemies(level);
		playing.setMaxLvlOffsetX(level.getLvlOffsetX());
		playing.setMaxLvlOffsetY(level.getLvlOffsetY());
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		getCurrentLevel().getLevelData().draw(g, lvlOffsetX, lvlOffsetY);
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

	public void reset() {
		this.currentLevel = null;
		this.lvlIndex = 0;
		loadNextLevel();
	}
}
