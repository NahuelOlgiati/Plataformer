package com.mandarina.game.levels;

import java.util.List;

import com.mandarina.game.leveldata.GameData;
import com.mandarina.game.leveldata.Grass;
import com.mandarina.game.leveldata.Tile;
import com.mandarina.game.leveldata.Water;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.game.main.LayerManager;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.RGB;
import com.mandarina.lvlbuilder.feature.PNGMetadata;
import com.mandarina.lvlbuilder.feature.TileFeature;
import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class LevelData implements LayerDrawer {

	private Level level;

	private boolean[][] isSolid;
	private boolean[][] isWater;

	private Image[] tileSprite;
	private Image[] waterSprite;
	private Image[] grassSprite;

	private LayerManager<Tile> tile;
	private LayerManager<Water> water;
	private LayerManager<Grass> grass;

	private static int cachedTileOffsetX = -1;
	private static int cachedMaxTileOffsetX = -1;
	private static double cachedLvlOffsetX = Double.NaN;

	private static int cachedTileOffsetY = -1;
	private static int cachedMaxTileOffsetY = -1;
	private static double cachedLvlOffsetY = Double.NaN;

	public LevelData(Level level) {
		this.level = level;
		int height = (int) level.getImg().getHeight();
		int width = (int) level.getImg().getWidth();
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
			public void draw(Tile t, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				if (mustDrawX(t, lvlOffsetX) && mustDrawY(t, lvlOffsetY)) {
					t.draw(g, lvlOffsetX, lvlOffsetY, tileSprite);
				}
			}
		};
		this.water = new LayerManager<Water>() {

			@Override
			public Class<Water> getClazz() {
				return Water.class;
			}

			@Override
			public void draw(Water w, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				if (mustDrawX(w, lvlOffsetX) && mustDrawY(w, lvlOffsetY)) {
					w.draw(g, lvlOffsetX, lvlOffsetY, waterSprite);
				}
			}
		};
		this.grass = new LayerManager<Grass>() {

			@Override
			public Class<Grass> getClazz() {
				return Grass.class;
			}

			@Override
			public void draw(Grass r, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				if (mustDrawX(r, lvlOffsetX) && mustDrawY(r, lvlOffsetY)) {
					r.draw(g, lvlOffsetX, lvlOffsetY, grassSprite);
				}
			}
		};
		load(level.getImg(), level.getPm());
	}

	private static boolean mustDrawX(GameData d, double lvlOffsetX) {
		if (lvlOffsetX != cachedLvlOffsetX) {
			cachedLvlOffsetX = lvlOffsetX;
			cachedTileOffsetX = AppStage.GetTilesIn(lvlOffsetX);
			cachedMaxTileOffsetX = cachedTileOffsetX + GameCts.TILES_IN_WIDTH;
		}

		double spawnX = d.getSpawn().getX();
		return spawnX >= cachedTileOffsetX && spawnX <= cachedMaxTileOffsetX;
	}

	private static boolean mustDrawY(GameData d, double lvlOffsetY) {
		if (lvlOffsetY != cachedLvlOffsetY) {
			cachedLvlOffsetY = lvlOffsetY;
			cachedTileOffsetY = AppStage.GetTilesIn(lvlOffsetY);
			cachedMaxTileOffsetY = cachedTileOffsetY + GameCts.TILES_IN_HEIGHT;
		}

		double spawnY = d.getSpawn().getY();
		return spawnY >= cachedTileOffsetY && spawnY <= cachedMaxTileOffsetY;
	}

	public void update() {
		for (Water w : water.getItems()) {
			w.update();
		}
	}

	@Override
	public void drawL1(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.tile.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL1(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL2(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.tile.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL2(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL3(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.tile.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL3(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL4(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.tile.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.water.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.grass.drawL4(g, lvlOffsetX, lvlOffsetY);
	}

	@SuppressWarnings("unchecked")
	public void load(LvlBuilderImage img, PNGMetadata pngMetadata) {
		List<Pair<Integer, Integer>> nosolid = (List<Pair<Integer, Integer>>) TileFeature.NOSOLID.getManager()
				.get(pngMetadata, RGB.RED);
		List<Pair<Integer, Integer>> layer1 = (List<Pair<Integer, Integer>>) TileFeature.LAYER1.getManager()
				.get(pngMetadata, RGB.RED);
		List<Pair<Integer, Integer>> layer2 = (List<Pair<Integer, Integer>>) TileFeature.LAYER2.getManager()
				.get(pngMetadata, RGB.RED);
		List<Pair<Integer, Integer>> layer3 = (List<Pair<Integer, Integer>>) TileFeature.LAYER3.getManager()
				.get(pngMetadata, RGB.RED);
		List<Pair<Integer, Integer>> layer4 = (List<Pair<Integer, Integer>>) TileFeature.LAYER4.getManager()
				.get(pngMetadata, RGB.RED);
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < this.level.getImgHeight(); y++) {
			for (int x = 0; x < this.level.getImgWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				addRed(red, new Point2D(x, y), nosolid, layer1, layer2, layer3, layer4);
			}
		}
		this.tile.consolidate();
		this.water.consolidate();
		this.grass.consolidate();
	}

	private void addRed(int red, Point2D spawn, List<Pair<Integer, Integer>> nosolid,
			List<Pair<Integer, Integer>> layer1, List<Pair<Integer, Integer>> layer2,
			List<Pair<Integer, Integer>> layer3, List<Pair<Integer, Integer>> layer4) {

		// TODO
		Pair<Integer, Integer> p = new Pair<Integer, Integer>((int) spawn.getX(), (int) spawn.getY());
		if (red != GameCts.EMPTY_TILE_VALUE) {
			switch (red) {
			case 48, 49:
				isWater[(int) spawn.getY()][(int) spawn.getX()] = true;
				this.water.add(new Water(spawn, red));
				break;
			default:
				boolean isNosolid = nosolid == null ? false : nosolid.contains(p);
				isSolid[(int) spawn.getY()][(int) spawn.getX()] = !isNosolid;
				Tile t = new Tile(spawn, red);

				boolean isLayer1 = layer1 == null ? false : layer1.contains(p);
				boolean isLayer2 = layer2 == null ? false : layer2.contains(p);
				boolean isLayer3 = layer3 == null ? false : layer3.contains(p);
				boolean isLayer4 = layer4 == null ? false : layer4.contains(p);

				if (!isLayer1 && !isLayer2 && !isLayer3 && !isLayer4) {
					tile.add(t);
				} else if (isLayer1) {
					tile.add(t);
				} else if (isLayer2) {
					tile.add(t, 2);
				} else if (isLayer3) {
					tile.add(t, 3);
				} else if (isLayer4) {
					tile.add(t, 4);
				}
				addGrass(red, spawn);
				break;
			}
		}
	}

	private void addGrass(int red, Point2D spawn) {
		switch (red) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
			this.grass.add(new Grass(spawn, getRndGrassType(spawn.getX())));
		}
	}

	private int getRndGrassType(double d) {
		return (int) (d % 2);
	}

	public Level getLevel() {
		return level;
	}

	public boolean[][] getIsSolid() {
		return isSolid;
	}

	public boolean[][] getIsWater() {
		return isWater;
	}

	public void scale() {
		for (Tile t : this.tile.getItems()) {
			t.scale();
		}
		for (Water w : this.water.getItems()) {
			w.scale();
		}
		for (Grass g : this.grass.getItems()) {
			g.scale();
		}
	}
}
