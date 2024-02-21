
package com.mandarina.game.leveldata;

import java.util.Random;

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
	private int numOfLevels;

	private Background background;
	private BackgroundCloud backgroundCloud;

	private Random rnd = new Random();
	private Rain rain;
	private boolean drawRain;

	public LevelManager(Playing playing) {
		this.playing = playing;
		this.lvlIndex = 0;
		this.numOfLevels = LoadSave.GetNumOfLevels();

		this.background = new Background();
		this.backgroundCloud = new BackgroundCloud();
		this.rain = null;
		this.drawRain = false;
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
		this.drawRain = rnd.nextFloat() >= 0.8f; // 20%
		if (drawRain) {
			rain = new Rain();
		} else {
			rain = null;
		}
		playing.getObjectManager().loadObjects(level);
		playing.getEnemyManager().loadEnemies(level);
		playing.setMaxLvlOffsetX(level.getLvlOffsetX());
		playing.setMaxLvlOffsetY(level.getLvlOffsetY());
	}

	@Override
	public void drawL1(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		this.background.draw(g, lvlOffsetX, lvlOffsetY);
		this.backgroundCloud.draw(g, lvlOffsetX, lvlOffsetY);
		if (drawRain)
			this.rain.draw(g, lvlOffsetX, lvlOffsetY);

		getCurrentLevel().getLevelData().drawL1(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL2(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		getCurrentLevel().getLevelData().drawL2(g, lvlOffsetX, lvlOffsetY);
	}

	public void update() {
		if (drawRain)
			this.rain.update(playing.getLvlOffsetX(), playing.getLvlOffsetY());

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
