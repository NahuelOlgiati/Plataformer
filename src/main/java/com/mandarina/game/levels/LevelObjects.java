package com.mandarina.game.levels;

import java.util.List;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.game.main.LayerManager;
import com.mandarina.game.objects.Cannon;
import com.mandarina.game.objects.Container;
import com.mandarina.game.objects.Dialogue;
import com.mandarina.game.objects.ObjectCts;
import com.mandarina.game.objects.Potion;
import com.mandarina.game.objects.Projectile;
import com.mandarina.game.objects.Spike;
import com.mandarina.game.objects.Tree;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LevelObjects implements LayerDrawer {

	private Level level;

	private Image[][] potionSprite, containerSprite, treeSprite;
	private Image[] cannonSprite;
	private Image spikeSprite, projectileSprite;
	private Image[] dialogueQuestionSprite;
	private Image[] dialogueExclamationSprite;

	private LayerManager<Potion> potion;
	private LayerManager<Container> container;
	private LayerManager<Spike> spike;
	private LayerManager<Cannon> cannon;
	private LayerManager<Tree> tree;

	public LevelObjects(Level level) {
		this.level = level;
		this.potionSprite = Potion.load();
		this.containerSprite = Container.load();
		this.spikeSprite = Spike.load();
		this.cannonSprite = Cannon.load();
		this.projectileSprite = Projectile.load();
		this.treeSprite = Tree.load();
		this.dialogueQuestionSprite = Dialogue.loadQuestions();
		this.dialogueExclamationSprite = Dialogue.loadExclamations();
		this.potion = new LayerManager<Potion>() {

			@Override
			public Class<Potion> getClazz() {
				return Potion.class;
			}

			@Override
			public void draw(Potion p, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				if (p.isActive()) {
					int type = 0;
					if (p.getObjType() == ObjectCts.RED_POTION)
						type = 1;
					g.drawImage(potionSprite[type][p.getAniIndex()],
							(int) (p.getHitbox().getMinX() - p.getxDrawOffset() - lvlOffsetX),
							(int) (p.getHitbox().getMinY() - p.getyDrawOffset() - lvlOffsetY),
							AppStage.Scale(ObjectCts.POTION_WIDTH_DEFAULT),
							AppStage.Scale(ObjectCts.POTION_HEIGHT_DEFAULT));
				}
			}
		};
		this.container = new LayerManager<Container>() {

			@Override
			public Class<Container> getClazz() {
				return Container.class;
			}

			@Override
			public void draw(Container c, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				if (c.isActive()) {
					int type = 0;
					if (c.getObjType() == ObjectCts.BARREL)
						type = 1;
					g.drawImage(containerSprite[type][c.getAniIndex()],
							(int) (c.getHitbox().getMinX() - c.getxDrawOffset() - lvlOffsetX),
							(int) (c.getHitbox().getMinY() - c.getyDrawOffset() - lvlOffsetY),
							AppStage.Scale(ObjectCts.CONTAINER_WIDTH_DEFAULT),
							AppStage.Scale(ObjectCts.CONTAINER_HEIGHT_DEFAULT));
				}
			}
		};
		this.spike = new LayerManager<Spike>() {

			@Override
			public Class<Spike> getClazz() {
				return Spike.class;
			}

			@Override
			public void draw(Spike s, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				s.draw(g, lvlOffsetX, lvlOffsetY, spikeSprite);
			}
		};
		this.cannon = new LayerManager<Cannon>() {

			@Override
			public Class<Cannon> getClazz() {
				return Cannon.class;
			}

			@Override
			public void draw(Cannon c, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				c.draw(g, lvlOffsetX, lvlOffsetY, cannonSprite);
			}
		};
		this.tree = new LayerManager<Tree>(2) {

			@Override
			public Class<Tree> getClazz() {
				return Tree.class;
			}

			@Override
			public void draw(Tree t, GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
				t.draw(g, lvlOffsetX, lvlOffsetY, treeSprite);
			}
		};
		load(level.getImg());
	}

	@Override
	public void drawL1(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.potion.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.container.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.spike.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.cannon.drawL1(g, lvlOffsetX, lvlOffsetY);
		this.tree.drawL1(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL2(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.potion.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.container.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.spike.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.cannon.drawL2(g, lvlOffsetX, lvlOffsetY);
		this.tree.drawL2(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL3(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.potion.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.container.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.spike.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.cannon.drawL3(g, lvlOffsetX, lvlOffsetY);
		this.tree.drawL3(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL4(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		this.potion.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.container.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.spike.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.cannon.drawL4(g, lvlOffsetX, lvlOffsetY);
		this.tree.drawL4(g, lvlOffsetX, lvlOffsetY);
	}

	public void drawProjectiles(GameDrawer g, double lvlOffsetX, double lvlOffsetY, List<Projectile> projectiles) {
		for (Projectile p : projectiles)
			if (p.isActive())
				p.draw(g, lvlOffsetX, lvlOffsetY, projectileSprite);
	}

	public void drawDialogues(GameDrawer g, double lvlOffsetX, double lvlOffsetY, List<Dialogue> dialogues) {
		for (Dialogue d : dialogues)
			if (d.isActive()) {
				d.draw(g, lvlOffsetX, lvlOffsetY, dialogueQuestionSprite, dialogueExclamationSprite);
			}
	}

	public void load(LvlBuilderImage img) {
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < this.level.getHeight(); y++) {
			for (int x = 0; x < this.level.getWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int blue = (int) (c.getBlue() * 255);
				addBlue(blue, x, y);
			}
		}
		this.potion.consolidate();
		this.container.consolidate();
		this.spike.consolidate();
		this.cannon.consolidate();
		this.tree.consolidate();
	}

	public void addBlue(int blue, int x, int y) {
		if (blue != GameCts.EMPTY_TILE_VALUE) {
			Point2D spawn = new Point2D(x, y);
			switch (blue) {
			case ObjectCts.RED_POTION, ObjectCts.BLUE_POTION -> potion.add(new Potion(spawn, blue));
			case ObjectCts.BOX, ObjectCts.BARREL -> container.add(new Container(spawn, blue));
			case ObjectCts.SPIKE -> spike.add(new Spike(spawn, ObjectCts.SPIKE));
			case ObjectCts.CANNON_LEFT, ObjectCts.CANNON_RIGHT -> cannon.add(new Cannon(spawn, blue));
			case ObjectCts.TREE_UP, ObjectCts.TREE_RIGHT, ObjectCts.TREE_LEFT -> tree.add(new Tree(spawn, blue));
			}
		}
	}

	public LayerManager<Potion> getPotion() {
		return potion;
	}

	public LayerManager<Container> getContainer() {
		return container;
	}

	public LayerManager<Spike> getSpike() {
		return spike;
	}

	public LayerManager<Cannon> getCannon() {
		return cannon;
	}

	public LayerManager<Tree> getTree() {
		return tree;
	}

	public void scale() {
		for (Container c : this.container.getItems()) {
			c.scale();
		}
		for (Spike s : this.spike.getItems()) {
			s.scale();
		}
		for (Cannon c : this.cannon.getItems()) {
			c.scale();
		}
		for (Tree t : this.tree.getItems()) {
			t.scale();
		}
	}
}
