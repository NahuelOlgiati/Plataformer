package com.mandarina.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.constants.EntityCts;
import com.mandarina.game.constants.GameCts;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.crabby.Crabby;
import com.mandarina.game.entities.crabby.CrabbyAtlas;
import com.mandarina.game.entities.pinkstar.Pinkstar;
import com.mandarina.game.entities.pinkstar.PinkstarAtlas;
import com.mandarina.game.entities.shark.Shark;
import com.mandarina.game.entities.shark.SharkAtlas;
import com.mandarina.game.entities.titan.Titan;
import com.mandarina.game.entities.titan.TitanAtlas;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelEntities {

	private int height;
	private int width;

	private Image[][] crabbySprite;
	private Image[][] pinkstarSprite;
	private Image[][] sharkSprite;
	private Image[][] titanSprite;

	private Enemy[] enemys;
	private Crabby[] crabs;
	private Pinkstar[] pinkstars;
	private Shark[] sharks;
	private Titan[] titans;
	private Point2D playerSpawn;

	public LevelEntities(LvlBuilderImage img) {
		this.height = (int) img.getHeight();
		this.width = (int) img.getWidth();
		this.crabbySprite = CrabbyAtlas.getAnimations();
		this.pinkstarSprite = PinkstarAtlas.getAnimations();
		this.sharkSprite = SharkAtlas.getAnimations();
		this.titanSprite = TitanAtlas.getAnimations();
		load(img);
	}

	public boolean update(Playing playing) {
		boolean isAnyActive = false;
		for (Enemy e : enemys) {
			if (e.isActive()) {
				e.update(playing);
				isAnyActive = true;
			}
		}
		return isAnyActive;
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Crabby c : crabs) {
			if (c.isActive()) {
				c.draw(g, lvlOffsetX, lvlOffsetY, crabbySprite);
			}
		}
		for (Pinkstar p : pinkstars) {
			if (p.isActive()) {
				p.draw(g, lvlOffsetX, lvlOffsetY, pinkstarSprite);
			}
		}
		for (Shark s : sharks) {
			if (s.isActive()) {
				s.draw(g, lvlOffsetX, lvlOffsetY, sharkSprite);
			}
		}
		for (Titan t : titans) {
			if (t.isActive()) {
				t.draw(g, lvlOffsetX, lvlOffsetY, titanSprite);
			}
		}
	}

	public void load(LvlBuilderImage img) {
		List<Enemy> enemys = new ArrayList<Enemy>();
		List<Crabby> crabs = new ArrayList<Crabby>();
		List<Pinkstar> pinkstars = new ArrayList<Pinkstar>();
		List<Shark> sharks = new ArrayList<Shark>();
		List<Titan> titans = new ArrayList<Titan>();
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = pixelReader.getColor(x, y);
				int green = (int) (c.getGreen() * 255);
				addGreen(green, x, y, enemys, crabs, pinkstars, sharks, titans);
			}
		}
		this.enemys = enemys.toArray(new Enemy[enemys.size()]);
		this.crabs = crabs.toArray(new Crabby[crabs.size()]);
		this.pinkstars = pinkstars.toArray(new Pinkstar[pinkstars.size()]);
		this.sharks = sharks.toArray(new Shark[sharks.size()]);
		this.titans = titans.toArray(new Titan[titans.size()]);
	}

	public void addGreen(int green, int x, int y, List<Enemy> enemys, List<Crabby> crabs, List<Pinkstar> pinkstars,
			List<Shark> sharks, List<Titan> titans) {
		if (green != GameCts.EMPTY_TILE_VALUE) {
			switch (green) {
			case EntityCts.CRABBY:
				Crabby c = new Crabby(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
				enemys.add(c);
				crabs.add(c);
				break;
			case EntityCts.PINKSTAR:
				Pinkstar p = new Pinkstar(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
				enemys.add(p);
				pinkstars.add(p);
				break;
			case EntityCts.SHARK:
				Shark s = new Shark(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
				enemys.add(s);
				sharks.add(s);
				break;
			case EntityCts.TITAN:
				Titan t = new Titan(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
				enemys.add(t);
				titans.add(t);
				break;
			case EntityCts.PLAYER:
				this.playerSpawn = new Point2D(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
				break;
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Enemy[] getEnemys() {
		return enemys;
	}

	public Crabby[] getCrabs() {
		return crabs;
	}

	public Pinkstar[] getPinkstars() {
		return pinkstars;
	}

	public Shark[] getSharks() {
		return sharks;
	}

	public Titan[] getTitans() {
		return titans;
	}

	public Point2D getPlayerSpawn() {
		return playerSpawn;
	}
}
