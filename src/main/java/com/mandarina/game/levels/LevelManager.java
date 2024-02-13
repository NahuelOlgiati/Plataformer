
package com.mandarina.game.levels;

import java.util.ArrayList;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.main.Game;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LevelManager {

	private Game game;
	private Image[] levelSprite;
	private Image[] waterSprite;
	private ArrayList<Level> levels;
	private Level customLevel;
	private int lvlIndex = 0, aniTick, aniIndex;

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		createWater();
		levels = new ArrayList<>();
		buildAllLevels();
	}

	private void createWater() {
		waterSprite = new Image[5];
		Image img = LoadSave.GetAtlas(LoadSave.WATER);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = LoadSave.GetSubimage(img, i, 0, GameCts.TILES_DEFAULT_SIZE, GameCts.TILES_DEFAULT_SIZE);
		waterSprite[4] = LoadSave.GetSprite(LoadSave.WATER_BOTTOM);
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
		game.getPlaying().getPlayer().loadLvlData(level.getLevelData());
		game.getPlaying().setMaxLvlOffsetX(level.getLvlOffsetX());
		game.getPlaying().setMaxLvlOffsetY(level.getLvlOffsetY());
		game.getPlaying().getObjectManager().loadObjects(level);
	}

	private void buildAllLevels() {
		Image[] allLevels = LoadSave.GetAllLevels();
		for (Image img : allLevels)
			levels.add(new Level(img));
	}

	private void importOutsideSprites() {
		levelSprite = LoadSave.GetAnimations(46, 32, 32, LoadSave.GetAtlas(LoadSave.OUTSIDE));
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		Level level = getCurrentLevel();
		int[][] levelData = level.getLevelData();
		for (int j = 0; j < levelData.length; j++) {
			for (int i = 0; i < levelData[0].length; i++) {
				int index = level.getSpriteIndex(i, j);
				int x = GameCts.TILES_SIZE * i - lvlOffsetX;
				int y = GameCts.TILES_SIZE * j - lvlOffsetY;
				if (index != GameCts.EMPTY_TILE_VALUE) {
					if (index == 48)
						g.drawImage(waterSprite[aniIndex], x, y, GameCts.TILES_SIZE, GameCts.TILES_SIZE);
					else if (index == 49)
						g.drawImage(waterSprite[4], x, y, GameCts.TILES_SIZE, GameCts.TILES_SIZE);
					else
						g.drawImage(levelSprite[index], x, y, GameCts.TILES_SIZE, GameCts.TILES_SIZE);
				}
			}
		}
	}

	public void update() {
		updateWaterAnimation();
	}

	private void updateWaterAnimation() {
		aniTick++;
		if (aniTick >= 40) {
			aniTick = 0;
			aniIndex++;

			if (aniIndex >= 4)
				aniIndex = 0;
		}
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
