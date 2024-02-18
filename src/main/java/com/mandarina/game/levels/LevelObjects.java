package com.mandarina.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.constants.ObjectCts;
import com.mandarina.game.constants.ProjectileCts;
import com.mandarina.game.objects.Cannon;
import com.mandarina.game.objects.CannonBall;
import com.mandarina.game.objects.Container;
import com.mandarina.game.objects.Potion;
import com.mandarina.game.objects.Projectile;
import com.mandarina.game.objects.Spike;
import com.mandarina.game.objects.Tree;
import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelObjects {

	private int height;
	private int width;

	private Image[][] potionSprite, containerSprite, treeSprite;
	private Image[] cannonSprite;
	private Image spikeSprite, cannonBallSprite;

	private Potion[] potions;
	private Container[] containers;
	private Spike[] spikes;
	private Cannon[] cannons;
	private Tree[] trees;

	public LevelObjects(LvlBuilderImage img) {
		this.height = (int) img.getHeight();
		this.width = (int) img.getWidth();
		this.potionSprite = Potion.load();
		this.containerSprite = Container.load();
		this.spikeSprite = Spike.load();
		this.cannonSprite = Cannon.load();
		this.cannonBallSprite = CannonBall.load();
		this.treeSprite = Tree.load();
		load(img);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, List<Potion> potions,
			List<Projectile> projectiles) {
		drawPotions(g, lvlOffsetX, lvlOffsetY, potions);
		drawContainers(g, lvlOffsetX, lvlOffsetY);
		drawTraps(g, lvlOffsetX, lvlOffsetY);
		drawCannons(g, lvlOffsetX, lvlOffsetY);
		drawProjectiles(g, lvlOffsetX, lvlOffsetY, projectiles);
		drawBackgroundTrees(g, lvlOffsetX, lvlOffsetY);
	}

	private void drawBackgroundTrees(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Tree bt : getTrees())
			bt.draw(g, lvlOffsetX, lvlOffsetY, treeSprite);
	}

	private void drawProjectiles(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, List<Projectile> projectiles) {
		for (Projectile p : projectiles)
			if (p.isActive())
				g.drawImage(cannonBallSprite, (int) (p.getHitbox().getMinX() - lvlOffsetX),
						(int) p.getHitbox().getMinY() - lvlOffsetY, ProjectileCts.CANNON_BALL_WIDTH,
						ProjectileCts.CANNON_BALL_HEIGHT);
	}

	private void drawCannons(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Cannon c : getCannons())
			c.draw(g, lvlOffsetX, lvlOffsetY, cannonSprite);
	}

	private void drawTraps(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Spike s : getSpikes())
			s.draw(g, lvlOffsetX, lvlOffsetY, spikeSprite);
	}

	private void drawContainers(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Container gc : containers)
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == ObjectCts.BARREL)
					type = 1;
				g.drawImage(containerSprite[type][gc.getAniIndex()],
						(int) (gc.getHitbox().getMinX() - gc.getxDrawOffset() - lvlOffsetX),
						(int) (gc.getHitbox().getMinY() - gc.getyDrawOffset() - lvlOffsetY), ObjectCts.CONTAINER_WIDTH,
						ObjectCts.CONTAINER_HEIGHT);
			}
	}

	private void drawPotions(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, List<Potion> potions) {
		for (Potion p : potions)
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == ObjectCts.RED_POTION)
					type = 1;
				g.drawImage(potionSprite[type][p.getAniIndex()],
						(int) (p.getHitbox().getMinX() - p.getxDrawOffset() - lvlOffsetX),
						(int) (p.getHitbox().getMinY() - p.getyDrawOffset() - lvlOffsetY), ObjectCts.POTION_WIDTH,
						ObjectCts.POTION_HEIGHT);
			}
	}

	public void load(LvlBuilderImage img) {
		List<Potion> potions = new ArrayList<Potion>();
		List<Container> containers = new ArrayList<Container>();
		List<Spike> spikes = new ArrayList<Spike>();
		List<Cannon> cannons = new ArrayList<Cannon>();
		List<Tree> trees = new ArrayList<Tree>();
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = pixelReader.getColor(x, y);
				int blue = (int) (c.getBlue() * 255);
				addBlue(blue, x, y, potions, containers, spikes, cannons, trees);
			}
		}
		this.potions = potions.toArray(new Potion[potions.size()]);
		this.containers = containers.toArray(new Container[containers.size()]);
		this.spikes = spikes.toArray(new Spike[spikes.size()]);
		this.cannons = cannons.toArray(new Cannon[cannons.size()]);
		this.trees = trees.toArray(new Tree[trees.size()]);
	}

	public void addBlue(int blue, int x, int y, List<Potion> potions, List<Container> containers, List<Spike> spikes,
			List<Cannon> cannons, List<Tree> trees) {
		if (blue != GameCts.EMPTY_TILE_VALUE) {
			switch (blue) {
			case ObjectCts.RED_POTION, ObjectCts.BLUE_POTION ->
				potions.add(new Potion(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blue));
			case ObjectCts.BOX, ObjectCts.BARREL ->
				containers.add(new Container(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blue));
			case ObjectCts.SPIKE ->
				spikes.add(new Spike(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, ObjectCts.SPIKE));
			case ObjectCts.CANNON_LEFT, ObjectCts.CANNON_RIGHT ->
				cannons.add(new Cannon(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blue));
			case ObjectCts.TREE_UP, ObjectCts.TREE_TWO, ObjectCts.TREE_THREE ->
				trees.add(new Tree(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blue));
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Potion[] getPotions() {
		return potions;
	}

	public Container[] getContainers() {
		return containers;
	}

	public Spike[] getSpikes() {
		return spikes;
	}

	public Cannon[] getCannons() {
		return cannons;
	}

	public Tree[] getTrees() {
		return trees;
	}
}
