
package com.mandarina.levels;

import java.util.ArrayList;

import com.mandarina.constants.GameCts;
import com.mandarina.main.Game;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LevelManager {

	private Game game;
	private Image[] levelSprite;
	private Image[] waterSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0, aniTick, aniIndex;

	public LevelManager(Game game, Image image) {
		this.game = game;
		importOutsideSprites();
		createWater();
		levels = new ArrayList<>();
		if (image != null) {
			buildLevel(image);
		} else {
			buildAllLevels();
		}
	}

	private void createWater() {
		waterSprite = new Image[5];
		Image img = LoadSave.GetAtlas(LoadSave.WATER);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = new WritableImage(img.getPixelReader(), i * 32, 0, 32, 32);
		waterSprite[4] = LoadSave.GetSprite(LoadSave.WATER_BOTTOM);
	}

	public void loadNextLevel() {
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
		game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffsetX());
		game.getPlaying().setMaxLvlOffsetY(newLevel.getLvlOffsetY());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	private void buildAllLevels() {
		Image[] allLevels = LoadSave.GetAllLevels();
		for (Image img : allLevels)
			buildLevel(img);
	}

	private void buildLevel(Image img) {
		levels.add(new Level(img));
	}

	private void importOutsideSprites() {
		levelSprite = LoadSave.GetAnimations(46, 32, 32, LoadSave.GetAtlas(LoadSave.OUTSIDE));
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		Level level = levels.get(lvlIndex);
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
		return levels.get(lvlIndex);
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
