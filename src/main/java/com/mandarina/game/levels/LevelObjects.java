package com.mandarina.game.levels;

import java.util.List;

import com.mandarina.game.gamestates.Offset;
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
import com.mandarina.utilz.Point;

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
		this.potion = new LayerManager<>() {

			@Override
			public Class<Potion> getClazz() {
				return Potion.class;
			}

			@Override
			public void draw(Potion p, GameDrawer g, Offset offset) {
				if (p.isActive()) {
					int type = 0;
					if (p.getObjType() == ObjectCts.RED_POTION)
						type = 1;
					g.drawImage(potionSprite[type][p.getAniIndex()],
							(int) (p.getHitbox().getMinX() - p.getxDrawOffset() - offset.getX()),
							(int) (p.getHitbox().getMinY() - p.getyDrawOffset() - offset.getY()),
							AppStage.Scale(ObjectCts.POTION_WIDTH_DEFAULT),
							AppStage.Scale(ObjectCts.POTION_HEIGHT_DEFAULT));
				}
			}
		};
		this.container = new LayerManager<>() {

			@Override
			public Class<Container> getClazz() {
				return Container.class;
			}

			@Override
			public void draw(Container c, GameDrawer g, Offset offset) {
				if (c.isActive()) {
					int type = 0;
					if (c.getObjType() == ObjectCts.BARREL)
						type = 1;
					g.drawImage(containerSprite[type][c.getAniIndex()],
							(int) (c.getHitbox().getMinX() - c.getxDrawOffset() - offset.getX()),
							(int) (c.getHitbox().getMinY() - c.getyDrawOffset() - offset.getY()),
							AppStage.Scale(ObjectCts.CONTAINER_WIDTH_DEFAULT),
							AppStage.Scale(ObjectCts.CONTAINER_HEIGHT_DEFAULT));
				}
			}
		};
		this.spike = new LayerManager<>() {

			@Override
			public Class<Spike> getClazz() {
				return Spike.class;
			}

			@Override
			public void draw(Spike s, GameDrawer g, Offset offset) {
				s.draw(g, offset, spikeSprite);
			}
		};
		this.cannon = new LayerManager<>() {

			@Override
			public Class<Cannon> getClazz() {
				return Cannon.class;
			}

			@Override
			public void draw(Cannon c, GameDrawer g, Offset offset) {
				c.draw(g, offset, cannonSprite);
			}
		};
		this.tree = new LayerManager<Tree>(2) {

			@Override
			public Class<Tree> getClazz() {
				return Tree.class;
			}

			@Override
			public void draw(Tree t, GameDrawer g, Offset offset) {
				t.draw(g, offset, treeSprite);
			}
		};
		load(level.getImg());
	}

	@Override
	public void drawL1(GameDrawer g, Offset offset) {
		this.potion.drawL1(g, offset);
		this.container.drawL1(g, offset);
		this.spike.drawL1(g, offset);
		this.cannon.drawL1(g, offset);
		this.tree.drawL1(g, offset);
	}

	@Override
	public void drawL2(GameDrawer g, Offset offset) {
		this.potion.drawL2(g, offset);
		this.container.drawL2(g, offset);
		this.spike.drawL2(g, offset);
		this.cannon.drawL2(g, offset);
		this.tree.drawL2(g, offset);
	}

	@Override
	public void drawL3(GameDrawer g, Offset offset) {
		this.potion.drawL3(g, offset);
		this.container.drawL3(g, offset);
		this.spike.drawL3(g, offset);
		this.cannon.drawL3(g, offset);
		this.tree.drawL3(g, offset);
	}

	@Override
	public void drawL4(GameDrawer g, Offset offset) {
		this.potion.drawL4(g, offset);
		this.container.drawL4(g, offset);
		this.spike.drawL4(g, offset);
		this.cannon.drawL4(g, offset);
		this.tree.drawL4(g, offset);
	}

	public void drawProjectiles(GameDrawer g, Offset offset, List<Projectile> projectiles) {
		for (Projectile p : projectiles)
			if (p.isActive())
				p.draw(g, offset, projectileSprite);
	}

	public void drawDialogues(GameDrawer g, Offset offset, List<Dialogue> dialogues) {
		for (Dialogue d : dialogues)
			if (d.isActive()) {
				d.draw(g, offset, dialogueQuestionSprite, dialogueExclamationSprite);
			}
	}

	public void load(LvlBuilderImage img) {
		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < this.level.getImgHeight(); y++) {
			for (int x = 0; x < this.level.getImgWidth(); x++) {
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
			Point spawn = new Point(x, y);
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
