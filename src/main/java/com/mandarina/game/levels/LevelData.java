package com.mandarina.game.levels;

import java.util.List;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.leveldata.Grass;
import com.mandarina.game.leveldata.Slide;
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
import com.mandarina.utilz.Box;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelData implements LayerDrawer {

	private Level level;

	private boolean[][] isSolid;
	private boolean[][] isWater;

	private Image[] tileSprite;
	private Image[] waterSprite;
	private Image[] grassSprite;
	private Image slideSprite;

	private LayerManager<Tile> tile;
	private LayerManager<Water> water;
	private LayerManager<Grass> grass;
	private LayerManager<Slide> slide;

	public LevelData(Level level) {
		this.level = level;
		int height = (int) level.getImg().getHeight();
		int width = (int) level.getImg().getWidth();
		this.isSolid = new boolean[height][width];
		this.isWater = new boolean[height][width];
		this.tileSprite = Tile.load();
		this.waterSprite = Water.load();
		this.grassSprite = Grass.load();
		this.slideSprite = Slide.load();

		this.tile = new LayerManager<>() {

			@Override
			public Class<Tile> getClazz() {
				return Tile.class;
			}

			@Override
			public void draw(Tile t, GameDrawer g, Offset offset) {
				if (offset.in(t.getSpawn())) {
					t.draw(g, offset, tileSprite);
				}
			}
		};
		this.water = new LayerManager<>() {

			@Override
			public Class<Water> getClazz() {
				return Water.class;
			}

			@Override
			public void draw(Water w, GameDrawer g, Offset offset) {
				if (offset.in(w.getSpawn())) {
					w.draw(g, offset, waterSprite);
				}
			}
		};
		this.grass = new LayerManager<>() {

			@Override
			public Class<Grass> getClazz() {
				return Grass.class;
			}

			@Override
			public void draw(Grass r, GameDrawer g, Offset offset) {
				if (offset.in(r.getSpawn())) {
					r.draw(g, offset, grassSprite);
				}
			}
		};
		this.slide = new LayerManager<>() {

			@Override
			public Class<Slide> getClazz() {
				return Slide.class;
			}

			@Override
			public void draw(Slide s, GameDrawer g, Offset offset) {
				if (offset.in(s.getSpawn())) {
					s.draw(g, offset, slideSprite);
				}
			}
		};
		load(level.getImg(), level.getPm());
	}

	public void update(Offset offset) {
		for (Water w : water.getItems()) {
			if (offset.in(w.getSpawn()))
				w.update();
		}
		for (Slide s : slide.getItems()) {
			s.update();
		}
	}

	@Override
	public void drawL1(GameDrawer g, Offset offset) {
		this.tile.drawL1(g, offset);
		this.water.drawL1(g, offset);
		this.grass.drawL1(g, offset);
		this.slide.drawL1(g, offset);
	}

	@Override
	public void drawL2(GameDrawer g, Offset offset) {
		this.tile.drawL2(g, offset);
		this.water.drawL2(g, offset);
		this.grass.drawL2(g, offset);
		this.slide.drawL2(g, offset);
	}

	@Override
	public void drawL3(GameDrawer g, Offset offset) {
		this.tile.drawL3(g, offset);
		this.water.drawL3(g, offset);
		this.grass.drawL3(g, offset);
		this.slide.drawL3(g, offset);
	}

	@Override
	public void drawL4(GameDrawer g, Offset offset) {
		this.tile.drawL4(g, offset);
		this.water.drawL4(g, offset);
		this.grass.drawL4(g, offset);
		this.slide.drawL4(g, offset);
	}

	@SuppressWarnings("unchecked")
	public void load(LvlBuilderImage img, PNGMetadata pngMetadata) {
		List<Point> nosolid = (List<Point>) TileFeature.NOSOLID.getManager().get(pngMetadata, RGB.RED);
		List<Point> layer1 = (List<Point>) TileFeature.LAYER1.getManager().get(pngMetadata, RGB.RED);
		List<Point> layer2 = (List<Point>) TileFeature.LAYER2.getManager().get(pngMetadata, RGB.RED);
		List<Point> layer3 = (List<Point>) TileFeature.LAYER3.getManager().get(pngMetadata, RGB.RED);
		List<Point> layer4 = (List<Point>) TileFeature.LAYER4.getManager().get(pngMetadata, RGB.RED);
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < this.level.getImgHeight(); y++) {
			for (int x = 0; x < this.level.getImgWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				addRed(red, new Point(x, y), nosolid, layer1, layer2, layer3, layer4);
			}
		}
		this.tile.consolidate();
		this.water.consolidate();
		this.grass.consolidate();
		this.slide.consolidate();
	}

	private void addRed(int red, Point spawn, List<Point> nosolid, List<Point> layer1, List<Point> layer2,
			List<Point> layer3, List<Point> layer4) {
		if (red != GameCts.EMPTY_TILE_VALUE) {
			switch (red) {
			case 48, 49:
				isWater[spawn.getY()][spawn.getX()] = true;
				this.water.add(new Water(spawn, red));
				break;
			case 50, 51, 52, 53:
				this.slide.add(new Slide(spawn, red));
				break;
			default:
				boolean isNosolid = nosolid == null ? false : nosolid.contains(spawn);
				isSolid[spawn.getY()][spawn.getX()] = !isNosolid;
				Tile t = new Tile(spawn, red);

				boolean isLayer1 = layer1 == null ? false : layer1.contains(spawn);
				boolean isLayer2 = layer2 == null ? false : layer2.contains(spawn);
				boolean isLayer3 = layer3 == null ? false : layer3.contains(spawn);
				boolean isLayer4 = layer4 == null ? false : layer4.contains(spawn);

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

	private void addGrass(int red, Point spawn) {
		switch (red) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
			this.grass.add(new Grass(spawn, getRndGrassType(spawn.getX())));
		}
	}

	private int getRndGrassType(float d) {
		return (int) (d % 2);
	}

	public Slide getSlide(Box hitbox) {
		for (Slide s : this.slide.getItems()) {
			Slide g = s.getSlide(hitbox);
			if (g != null) {
				return g;
			}
		}
		return null;
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
		for (Slide s : this.slide.getItems()) {
			s.scale();
		}
	}
}
