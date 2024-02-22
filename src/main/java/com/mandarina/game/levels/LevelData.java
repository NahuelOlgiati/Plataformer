package com.mandarina.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.leveldata.Grass;
import com.mandarina.game.leveldata.Tile;
import com.mandarina.game.leveldata.Water;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.PNGMetadata;
import com.mandarina.lvlbuilder.RGB;
import com.mandarina.lvlbuilder.TileFeature;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class LevelData implements LayerDrawer {

	private int height;
	private int width;
	private boolean[][] isSolid;
	private boolean[][] isWater;

	private Image[] tileSprite;
	private Image[] waterSprite;
	private Image[] grassSprite;

	private Tile[] tile;
	private Tile[] tileL2;
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

	@Override
	public void drawL1(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
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

	@Override
	public void drawL2(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		for (Tile t : tileL2) {
			t.draw(g, lvlOffsetX, lvlOffsetY, tileSprite);
		}
	}

	public void load(LvlBuilderImage img) {
		PNGMetadata pngMetadata = new PNGMetadata(img);
		List<Pair<Integer, Integer>> traversable = TileFeature.TRAVERSABLE.get(pngMetadata, RGB.RED);
		List<Tile> tile = new ArrayList<Tile>();
		List<Tile> tileL2 = new ArrayList<Tile>();
		List<Water> water = new ArrayList<Water>();
		List<Grass> grass = new ArrayList<Grass>();
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				addRed(red, x, y, traversable, tile, tileL2, water, grass);
			}
		}
		this.tile = tile.toArray(new Tile[tile.size()]);
		this.tileL2 = tileL2.toArray(new Tile[tileL2.size()]);
		this.water = water.toArray(new Water[water.size()]);
		this.grass = grass.toArray(new Grass[grass.size()]);
	}

	private void addRed(int red, int x, int y, List<Pair<Integer, Integer>> traversable, List<Tile> tile,
			List<Tile> tileL2, List<Water> water, List<Grass> grass) {
		if (red != GameCts.EMPTY_TILE_VALUE) {
			switch (red) {
			case 48, 49:
				isWater[y][x] = true;
				water.add(new Water(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red));
				break;
			default:
				boolean isTraversable = traversable.contains(new Pair<Integer, Integer>(x, y));
				isSolid[y][x] = !isTraversable;
				Tile t = new Tile(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red);
				if (isTraversable) {
					tileL2.add(t);
				} else {
					tile.add(t);
				}
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
