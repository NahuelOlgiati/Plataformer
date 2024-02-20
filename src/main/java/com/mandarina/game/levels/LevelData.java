package com.mandarina.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.leveldata.Grass;
import com.mandarina.game.leveldata.Tile;
import com.mandarina.game.leveldata.Water;
import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelData {

	private int height;
	private int width;
	private boolean[][] isSolid;
	private boolean[][] isWater;

	private Image[] tileSprite;
	private Image[] waterSprite;
	private Image[] grassSprite;

	private Tile[] tile;
	private Water[] water;
	private Grass[] grass;

	public LevelData(LvlBuilderImage img) {
		this.height = (int) img.getHeight();
		this.width = (int) img.getWidth();
		this.isSolid = new boolean[height][width];
		this.isWater = new boolean[height][width];
		this.tileSprite = Tile.load();
		this.waterSprite = Water.load();
		this.grassSprite = Grass.load();
		load(img);
	}

	public void update() {
		for (Water w : water) {
			w.update();
		}
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Tile t : tile) {
			t.draw(g, lvlOffsetX, lvlOffsetY, tileSprite);
		}
		for (Water w : water) {
			w.draw(g, lvlOffsetX, lvlOffsetY, waterSprite);
		}
		for (Grass s : grass) {
			s.draw(g, lvlOffsetX, lvlOffsetY, grassSprite);
		}
	}

	public void load(LvlBuilderImage img) {
		List<Tile> tile = new ArrayList<Tile>();
		List<Water> water = new ArrayList<Water>();
		List<Grass> grass = new ArrayList<Grass>();
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				addRed(red, x, y, tile, water, grass);
			}
		}
		this.tile = tile.toArray(new Tile[tile.size()]);
		this.water = water.toArray(new Water[water.size()]);
		this.grass = grass.toArray(new Grass[grass.size()]);
	}

	public void addRed(int red, int x, int y, List<Tile> tile, List<Water> water, List<Grass> grass) {
		if (red != GameCts.EMPTY_TILE_VALUE) {
			switch (red) {
			case 48, 49:
				isWater[y][x] = true;
				water.add(new Water(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red));
				break;
			default:
				isSolid[y][x] = true;
				tile.add(new Tile(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red));
				addGrass(red, x, y, grass);
				break;
			}
		}
	}

	private void addGrass(int red, int x, int y, List<Grass> grass) {
		switch (red) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> grass.add(
				new Grass(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE - GameCts.TILES_SIZE, getRndGrassType(x)));
		}
	}

	private int getRndGrassType(int xPos) {
		return xPos % 2;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean[][] getIsSolid() {
		return isSolid;
	}

	public boolean[][] getIsWater() {
		return isWater;
	}
}
