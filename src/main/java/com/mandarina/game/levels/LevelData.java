package com.mandarina.game.levels;

import java.util.List;

import com.mandarina.game.leveldata.Grass;
import com.mandarina.game.leveldata.Tile;
import com.mandarina.game.leveldata.Water;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.game.main.LayerManager;
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

	private LayerManager<Tile> tile;
	private LayerManager<Water> water;
	private LayerManager<Grass> grass;

	public LevelData(LvlBuilderImage img) {
		this.height = (int) img.getHeight();
		this.width = (int) img.getWidth();
		this.isSolid = new boolean[height][width];
		this.isWater = new boolean[height][width];
		this.tileSprite = Tile.load();
		this.waterSprite = Water.load();
		this.grassSprite = Grass.load();
		this.tile = new LayerManager<Tile>() {

			@Override
			public Class<Tile> getClazz() {
				return Tile.class;
			}

			@Override
			public void draw(Tile t, GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
				t.draw(g, lvlOffsetX, lvlOffsetY, tileSprite);

			}
		};
		this.water = new LayerManager<Water>() {

			@Override
			public Class<Water> getClazz() {
				return Water.class;
			}

			@Override
			public void draw(Water w, GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
				w.draw(g, lvlOffsetX, lvlOffsetY, waterSprite);
			}
		};
		this.grass = new LayerManager<Grass>() {

			@Override
			public Class<Grass> getClazz() {
				return Grass.class;
			}

			@Override
			public void draw(Grass r, GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
				r.draw(g, lvlOffsetX, lvlOffsetY, grassSprite);
			}
		};
		load(img);
	}

	public void update() {
		for (Water w : water.getItems()) {
			w.update();
		}
	}

	@Override
	public void drawL1(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		this.tile.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL1(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL2(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		this.tile.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL2(g, lvlOffsetX, lvlOffsetY);
	}

	@SuppressWarnings("unchecked")
	public void load(LvlBuilderImage img) {
		PNGMetadata pngMetadata = new PNGMetadata(img);
		List<Pair<Integer, Integer>> traversable = (List<Pair<Integer, Integer>>) TileFeature.TRAVERSABLE
				.get(pngMetadata, RGB.RED);
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				addRed(red, x, y, traversable);
			}
		}
		this.tile.consolidate();
		this.water.consolidate();
		this.grass.consolidate();
	}

	private void addRed(int red, int x, int y, List<Pair<Integer, Integer>> traversable) {
		if (red != GameCts.EMPTY_TILE_VALUE) {
			switch (red) {
			case 48, 49:
				isWater[y][x] = true;
				this.water.add(new Water(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red));
				break;
			default:
				boolean isTraversable = traversable == null ? false
						: traversable.contains(new Pair<Integer, Integer>(x, y));
				isSolid[y][x] = !isTraversable;
				Tile t = new Tile(GameCts.TILES_SIZE * x, GameCts.TILES_SIZE * y, red);
				if (isTraversable) {
					tile.add(t, 2);
				} else {
					tile.add(t);
				}
				addGrass(red, x, y);
				break;
			}
		}
	}

	private void addGrass(int red, int x, int y) {
		switch (red) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> this.grass.add(
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
