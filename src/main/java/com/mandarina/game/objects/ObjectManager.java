package com.mandarina.game.objects;

import static com.mandarina.utilz.HelpMethods.CanCannonSeePlayer;
import static com.mandarina.utilz.HelpMethods.IsProjectileHittingLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.Player;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelData;

import javafx.geometry.Rectangle2D;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;

public class ObjectManager {

	private Playing playing;
	private Level currentLevel;

	private List<Potion> potions;
	private List<Projectile> projectiles;
	private List<Dialogue> dialogues = new ArrayList<Dialogue>();

	public ObjectManager(Playing playing) {
		this.playing = playing;
	}

	public void loadObjects(Level level) {
		this.currentLevel = level;
		this.potions = new ArrayList<Potion>(Arrays.asList(level.getLevelObjects().getPotions()));
		this.projectiles = new ArrayList<Projectile>();
		loadDialogues();
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		currentLevel.getLevelObjects().draw(g, lvlOffsetX, lvlOffsetY, this.potions, this.projectiles, this.dialogues);
	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : currentLevel.getLevelObjects().getSpikes())
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkSpikesTouched(Enemy e) {
		for (Spike s : currentLevel.getLevelObjects().getSpikes())
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
		for (Container c : currentLevel.getLevelObjects().getContainers())
			if (c.isActive() && !c.doAnimation) {
				if (c.getHitbox().intersects(attackbox)) {
					c.setAnimation(true);
					int type = 0;
					if (c.getObjType() == ObjectCts.BARREL)
						type = 1;
					potions.add(new Potion((int) (c.getHitbox().getMinX() + c.getHitbox().getWidth() / 2),
							(int) (c.getHitbox().getMinY() - c.getHitbox().getHeight() / 2), type));
					return;
				}
			}
	}

	public void update() {
		updateBackgroundTrees();
		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (Container c : currentLevel.getLevelObjects().getContainers())
			if (c.isActive())
				c.update();

		updateCannons(currentLevel.getLevelData(), playing.getPlayer());
		updateProjectiles(currentLevel.getLevelData(), playing.getPlayer());

		for (Dialogue d : dialogues)
			if (d.isActive())
				d.update();

	}

	private void updateBackgroundTrees() {
		for (Tree bt : currentLevel.getLevelObjects().getTrees())
			bt.update();
	}

	private void updateProjectiles(LevelData levelData, Player player) {
		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.updatePos();
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					p.setActive(false);
				} else if (IsProjectileHittingLevel(p, levelData))
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

	private void updateCannons(LevelData levelData, Player player) {
		for (Cannon c : currentLevel.getLevelObjects().getCannons()) {
			if (!c.doAnimation)
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRange(c, player))
						if (isPlayerInfrontOfCannon(c, player))
							if (CanCannonSeePlayer(levelData, player.getHitbox(), c.getHitbox(), c.getTileY()))
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

	private void loadDialogues() {
		dialogues.clear();
		for (int i = 0; i < 10; i++)
			dialogues.add(new Dialogue(0, 0, DialogueCts.EXCLAMATION));
		for (int i = 0; i < 10; i++)
			dialogues.add(new Dialogue(0, 0, DialogueCts.QUESTION));

		for (Dialogue d : dialogues)
			d.deactive();
	}

	public void addDialogue(int x, int y, int type) {
		dialogues.add(new Dialogue(x, y - (int) (GameCts.SCALE * 15), type));
		for (Dialogue d : dialogues)
			if (!d.isActive())
				if (d.getType() == type) {
					d.reset(x, -(int) (GameCts.SCALE * 15));
					return;
				}
	}

	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (Container c : currentLevel.getLevelObjects().getContainers())
			c.reset();
		for (Cannon c : currentLevel.getLevelObjects().getCannons())
			c.reset();
		loadDialogues();
	}
}
