package com.mandarina.objects;

import static com.mandarina.utilz.HelpMethods.CanCannonSeePlayer;
import static com.mandarina.utilz.HelpMethods.IsProjectileHittingLevel;

import java.util.ArrayList;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.ObjectCts;
import com.mandarina.constants.ProjectileCts;
import com.mandarina.entities.Enemy;
import com.mandarina.entities.player.Player;
import com.mandarina.gamestates.Playing;
import com.mandarina.levels.Level;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ObjectManager {

	private Playing playing;
	private Image[][] potionImgs, containerImgs;
	private Image[] cannonImgs, grassImgs;
	private Image[][] treeImgs;
	private Image spikeImg, cannonBallImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Projectile> projectiles = new ArrayList<>();

	private Level currentLevel;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		currentLevel = playing.getLevelManager().getCurrentLevel();
		loadImgs();
	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : currentLevel.getSpikes())
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkSpikesTouched(Enemy e) {
		for (Spike s : currentLevel.getSpikes())
			if (s.getHitbox().intersects(e.getHitbox()))
				e.hurt(200);
	}

	public void checkObjectTouched(Rectangle2D hitbox) {
		for (Potion p : potions)
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}

	public void applyEffectToPlayer(Potion p) {
		if (p.getObjType() == ObjectCts.RED_POTION)
			playing.getPlayer().changeHealth(ObjectCts.RED_POTION_VALUE);
		else
			playing.getStatusBar().changePower(ObjectCts.BLUE_POTION_VALUE);
	}

	public void checkObjectHit(Rectangle2D attackbox) {
		for (GameContainer gc : containers)
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if (gc.getObjType() == ObjectCts.BARREL)
						type = 1;
					potions.add(new Potion((int) (gc.getHitbox().getMinX() + gc.getHitbox().getWidth() / 2),
							(int) (gc.getHitbox().getMinY() - gc.getHitbox().getHeight() / 2), type));
					return;
				}
			}
	}

	public void loadObjects(Level newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		projectiles.clear();
	}

	private void loadImgs() {
		potionImgs = Potion.load();
		containerImgs = GameContainer.load();
		spikeImg = Spike.load();
		cannonImgs = Cannon.load();
		cannonBallImg = CannonBall.load();
		treeImgs = Tree.load();
		grassImgs = Grass.load();
	}

	public void update(int[][] lvlData, Player player) {
		updateBackgroundTrees();
		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (GameContainer gc : containers)
			if (gc.isActive())
				gc.update();

		updateCannons(lvlData, player);
		updateProjectiles(lvlData, player);

	}

	private void updateBackgroundTrees() {
		for (BackgroundTree bt : currentLevel.getTrees())
			bt.update();
	}

	private void updateProjectiles(int[][] lvlData, Player player) {
		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.updatePos();
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					p.setActive(false);
				} else if (IsProjectileHittingLevel(p, lvlData))
					p.setActive(false);
			}
	}

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().getMinX() - c.getHitbox().getMinX());
		return absValue <= GameCts.TILES_SIZE * 5;
	}

	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
		if (c.getObjType() == ObjectCts.CANNON_LEFT) {
			return c.getHitbox().getMinX() > player.getHitbox().getMinX();
		} else {
			return c.getHitbox().getMinX() < player.getHitbox().getMinX();
		}
	}

	private void updateCannons(int[][] lvlData, Player player) {
		for (Cannon c : currentLevel.getCannons()) {
			if (!c.doAnimation)
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRange(c, player))
						if (isPlayerInfrontOfCannon(c, player))
							if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
								c.setAnimation(true);

			c.update();
			if (c.getAniIndex() == 4 && c.getAniTick() == 0)
				shootCannon(c);
		}
	}

	private void shootCannon(Cannon c) {
		int dir = 1;
		if (c.getObjType() == ObjectCts.CANNON_LEFT)
			dir = -1;

		projectiles.add(new Projectile((int) c.getHitbox().getMinX(), (int) c.getHitbox().getMinY(), dir));
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		drawPotions(g, lvlOffsetX, lvlOffsetY);
		drawContainers(g, lvlOffsetX, lvlOffsetY);
		drawTraps(g, lvlOffsetX, lvlOffsetY);
		drawCannons(g, lvlOffsetX, lvlOffsetY);
		drawProjectiles(g, lvlOffsetX, lvlOffsetY);
		drawGrass(g, lvlOffsetX, lvlOffsetY);
		drawBackgroundTrees(g, lvlOffsetX, lvlOffsetY);
	}

	private void drawGrass(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Grass grass : currentLevel.getGrass())
			g.drawImage(grassImgs[grass.getType()], grass.getX() - lvlOffsetX, grass.getY() - lvlOffsetY,
					(int) (32 * GameCts.SCALE), (int) (32 * GameCts.SCALE));
	}

	private void drawBackgroundTrees(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (BackgroundTree bt : currentLevel.getTrees()) {

			int type = bt.getType();
			if (type == 9)
				type = 8;
			g.drawImage(treeImgs[type - 7][bt.getAniIndex()],
					bt.getX() - lvlOffsetX + ObjectCts.GetTreeOffsetX(bt.getType()),
					(int) (bt.getY() - lvlOffsetY + ObjectCts.GetTreeOffsetY(bt.getType())),
					ObjectCts.GetTreeWidth(bt.getType()), ObjectCts.GetTreeHeight(bt.getType()));
		}
	}

	private void drawProjectiles(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Projectile p : projectiles)
			if (p.isActive())
				g.drawImage(cannonBallImg, (int) (p.getHitbox().getMinX() - lvlOffsetX),
						(int) p.getHitbox().getMinY() - lvlOffsetY, ProjectileCts.CANNON_BALL_WIDTH,
						ProjectileCts.CANNON_BALL_HEIGHT);
	}

	private void drawCannons(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Cannon c : currentLevel.getCannons())
			c.draw(g, lvlOffsetX, lvlOffsetY, cannonImgs);
	}

	private void drawTraps(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Spike s : currentLevel.getSpikes())
			s.draw(g, lvlOffsetX, lvlOffsetY, spikeImg);
	}

	private void drawContainers(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == ObjectCts.BARREL)
					type = 1;
				g.drawImage(containerImgs[type][gc.getAniIndex()],
						(int) (gc.getHitbox().getMinX() - gc.getxDrawOffset() - lvlOffsetX),
						(int) (gc.getHitbox().getMinY() - gc.getyDrawOffset() - lvlOffsetY), ObjectCts.CONTAINER_WIDTH,
						ObjectCts.CONTAINER_HEIGHT);
			}
	}

	private void drawPotions(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (Potion p : potions)
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == ObjectCts.RED_POTION)
					type = 1;
				g.drawImage(potionImgs[type][p.getAniIndex()],
						(int) (p.getHitbox().getMinX() - p.getxDrawOffset() - lvlOffsetX),
						(int) (p.getHitbox().getMinY() - p.getyDrawOffset() - lvlOffsetY), ObjectCts.POTION_WIDTH,
						ObjectCts.POTION_HEIGHT);
			}
	}

	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : currentLevel.getCannons())
			c.reset();
	}
}
