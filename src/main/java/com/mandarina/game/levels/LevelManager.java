
package com.mandarina.game.levels;

import java.util.ArrayList;

import com.mandarina.game.main.Game;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;

public class LevelManager {

	private Game game;
	private ArrayList<Level> levels;
	private Level customLevel;
	private int lvlIndex = 0;

	public LevelManager(Game game) {
		this.game = game;
		levels = new ArrayList<>();
		buildAllLevels();
	}

	public void loadNextLevel() {
		loadLevel(levels.get(lvlIndex));
	}

	public void loadCustomLevel(Level level) {
		setLevelIndex(-1);
		this.customLevel = level;
		loadLevel(getCurrentLevel());
	}

	public void loadLevel(Level level) {
		game.getPlaying().getEnemyManager().loadEnemies(level);
		game.getPlaying().setMaxLvlOffsetX(level.getLvlOffsetX());
		game.getPlaying().setMaxLvlOffsetY(level.getLvlOffsetY());
		game.getPlaying().getObjectManager().loadObjects(level);
	}

	private void buildAllLevels() {
		LvlBuilderImage[] allLevels = LoadSave.GetAllLevels();
		for (LvlBuilderImage img : allLevels)
			levels.add(new Level(img));
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		getCurrentLevel().getLevelData().draw(g, lvlOffsetX, lvlOffsetY);
	}

	public void update() {
		getCurrentLevel().getLevelData().update();
	}

	public Level getCurrentLevel() {
		return lvlIndex == -1 ? customLevel : levels.get(lvlIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}

	public int getLevelIndex() {
		return lvlIndex;
	}

	public void setLevelIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}
}
