package com.mandarina.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.entities.Crabby;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.EntityCts;
import com.mandarina.game.entities.Longleg;
import com.mandarina.game.entities.Pinkstar;
import com.mandarina.game.entities.Player;
import com.mandarina.game.entities.Shark;
import com.mandarina.game.entities.Titan;
import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelEntities implements LayerDrawer {

	private Level level;

	private Image[][] crabbySprite;
	private Image[][] pinkstarSprite;
	private Image[][] sharkSprite;
	private Image[][] titanSprite;
	private Image[][] longlegSprite;

	private Enemy[] enemys;
	private Crabby[] crabs;
	private Pinkstar[] pinkstars;
	private Shark[] sharks;
	private Titan[] titans;
	private Longleg[] longlegs;
	private Player player;

	public LevelEntities(Level level) {
		this.level = level;
		this.crabbySprite = Crabby.load();
		this.pinkstarSprite = Pinkstar.load();
		this.sharkSprite = Shark.load();
		this.titanSprite = Titan.load();
		this.longlegSprite = Longleg.load();
		load(level.getImg());
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

	@Override
	public void drawL1(GameDrawer g, Offset offset) {
		for (Crabby c : crabs) {
			if (c.isActive()) {
				c.draw(g, offset, crabbySprite);
			}
		}
		for (Pinkstar p : pinkstars) {
			if (p.isActive()) {
				p.draw(g, offset, pinkstarSprite);
			}
		}
		for (Shark s : sharks) {
			if (s.isActive()) {
				s.draw(g, offset, sharkSprite);
			}
		}
		for (Titan t : titans) {
			if (t.isActive()) {
				t.draw(g, offset, titanSprite);
			}
		}
		for (Longleg l : longlegs) {
			if (l.isActive()) {
				l.draw(g, offset, longlegSprite);
			}
		}
	}

	@Override
	public void drawL2(GameDrawer g, Offset offset) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawL3(GameDrawer g, Offset offset) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawL4(GameDrawer g, Offset offset) {
		// TODO Auto-generated method stub
	}

	public void load(LvlBuilderImage img) {
		List<Enemy> enemys = new ArrayList<>();
		List<Crabby> crabs = new ArrayList<>();
		List<Pinkstar> pinkstars = new ArrayList<>();
		List<Shark> sharks = new ArrayList<>();
		List<Titan> titans = new ArrayList<>();
		List<Longleg> longlegs = new ArrayList<>();
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < this.level.getImgHeight(); y++) {
			for (int x = 0; x < this.level.getImgWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int green = (int) (c.getGreen() * 255);
				addGreen(green, new Point2D(x, y), enemys, crabs, pinkstars, sharks, titans, longlegs);
			}
		}
		this.enemys = enemys.toArray(new Enemy[enemys.size()]);
		this.crabs = crabs.toArray(new Crabby[crabs.size()]);
		this.pinkstars = pinkstars.toArray(new Pinkstar[pinkstars.size()]);
		this.sharks = sharks.toArray(new Shark[sharks.size()]);
		this.titans = titans.toArray(new Titan[titans.size()]);
		this.longlegs = longlegs.toArray(new Longleg[longlegs.size()]);
	}

	public void addGreen(int green, Point2D spawn, List<Enemy> enemys, List<Crabby> crabs, List<Pinkstar> pinkstars,
			List<Shark> sharks, List<Titan> titans, List<Longleg> longlegs) {
		if (green != GameCts.EMPTY_TILE_VALUE) {
			switch (green) {
			case EntityCts.CRABBY:
				Crabby c = new Crabby(spawn);
				enemys.add(c);
				crabs.add(c);
				break;
			case EntityCts.PINKSTAR:
				Pinkstar p = new Pinkstar(spawn);
				enemys.add(p);
				pinkstars.add(p);
				break;
			case EntityCts.SHARK:
				Shark s = new Shark(spawn);
				enemys.add(s);
				sharks.add(s);
				break;
			case EntityCts.TITAN:
				Titan t = new Titan(spawn);
				enemys.add(t);
				titans.add(t);
				break;
			case EntityCts.LONGLEG:
				Longleg l = new Longleg(spawn);
				enemys.add(l);
				longlegs.add(l);
				break;
			case EntityCts.PLAYER:
				this.player = new Player(spawn);
				break;
			}
		}
	}

	public void resetAllEnemies() {
		for (Enemy e : enemys) {
			e.resetEnemy();
		}
//		for (Crabby c : crabs) {
//			c.resetEnemy();
//		}
//		for (Pinkstar p : pinkstars) {
//			p.resetEnemy();
//		}
//		for (Shark s : sharks) {
//			s.resetEnemy();
//		}
//		for (Titan t : titans) {
//			t.resetEnemy();
//		}
//		for (Longleg l : longlegs) {
//			l.resetEnemy();
//		}
	}

	public Enemy[] getEnemys() {
		return enemys;
	}

	public Player getPlayer() {
		return player;
	}

	public void scale() {
		for (Enemy e : enemys) {
			if (e.isActive()) {
				e.scale();
			}
		}
	}
}
