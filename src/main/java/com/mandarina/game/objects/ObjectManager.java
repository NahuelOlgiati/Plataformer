package com.mandarina.game.objects;

import static com.mandarina.utilz.SmallerThanTile.CanCannonSeePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.Player;
import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.GameAudio;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;
import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class ObjectManager implements LayerDrawer {

	private Playing playing;
	private Level currentLevel;

	private List<Potion> potions;
	private List<Projectile> projectiles;
	private List<Dialogue> dialogues = new ArrayList<>();

	public ObjectManager(Playing playing) {
		this.playing = playing;
	}

	public void loadObjects(Level level) {
		this.currentLevel = level;
		this.potions = new ArrayList<>(Arrays.asList(level.getLevelObjects().getPotion().getItems()));
		this.projectiles = new ArrayList<>();
		loadDialogues();
	}

	@Override
	public void drawL1(GameDrawer g, Offset offset) {
		currentLevel.getLevelObjects().drawL1(g, offset);
		currentLevel.getLevelObjects().drawProjectiles(g, offset, this.projectiles);
		currentLevel.getLevelObjects().drawDialogues(g, offset, this.dialogues);
	}

	@Override
	public void drawL2(GameDrawer g, Offset offset) {
		currentLevel.getLevelObjects().drawL2(g, offset);
	}

	@Override
	public void drawL3(GameDrawer g, Offset offset) {
		currentLevel.getLevelObjects().drawL3(g, offset);
	}

	@Override
	public void drawL4(GameDrawer g, Offset offset) {
		currentLevel.getLevelObjects().drawL4(g, offset);
	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : currentLevel.getLevelObjects().getSpike().getItems())
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkSpikesTouched(Enemy e) {
		for (Spike s : currentLevel.getLevelObjects().getSpike().getItems())
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
		for (Container c : currentLevel.getLevelObjects().getContainer().getItems())
			if (c.isActive() && !c.doAnimation) {
				if (c.getHitbox().intersects(attackbox)) {
					c.setAnimation(true);
					int type = 0;
					if (c.getObjType() == ObjectCts.BARREL)
						type = 1;
					Point2D p = new Point2D((int) (c.getHitbox().getMinX() + c.getHitbox().getWidth() / 2),
							(int) (c.getHitbox().getMinY() - c.getHitbox().getHeight() / 2));
					potions.add(new Potion(p, type));
					return;
				}
			}
	}

	public void update(Offset offset) {
		updateTrees(offset);
		updatePotions(offset);
		updateContainers(offset);
		updateCannons(currentLevel.getLevelData(), playing.getPlayer(), offset);
		updateProjectiles(currentLevel.getLevelData(), playing.getPlayer(), offset);
		updateDialogues(offset);
	}

	private void updateDialogues(Offset offset) {
		for (Dialogue d : dialogues)
			if (d.isActive())
				if (offset.in(d.getSpawn()))
					d.update();
	}

	private void updateContainers(Offset offset) {
		for (Container c : currentLevel.getLevelObjects().getContainer().getItems())
			if (c.isActive())
				if (offset.in(c.getSpawn()))
					c.update();
	}

	private void updatePotions(Offset offset) {
		for (Potion p : potions)
			if (p.isActive())
				if (offset.in(p.getSpawn()))
					p.update();
	}

	private void updateTrees(Offset offset) {
		for (Tree t : currentLevel.getLevelObjects().getTree().getItems())
			if (offset.in(t.getSpawn())) {
				t.update();
			}

	}

	private void updateProjectiles(LevelData levelData, Player player, Offset offset) {
		for (Projectile p : projectiles)
			if (p.isActive())
				if (offset.in(p.getHitbox())) {
					p.updatePos();
					if (p.getHitbox().intersects(player.getHitbox())) {
						player.changeHealth(-25);
						p.setActive(false);
					} else if (p.isProjectileHittingLevel(levelData))
						p.setActive(false);
				}
	}

	private void updateCannons(LevelData levelData, Player player, Offset offset) {
		for (Cannon c : currentLevel.getLevelObjects().getCannon().getItems()) {
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

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().getMinX() - c.getHitbox().getMinX());
		return absValue <= AppStage.GetTileSize() * 5;
	}

	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
		if (c.getObjType() == ObjectCts.CANNON_LEFT) {
			return c.getHitbox().getMinX() > player.getHitbox().getMinX();
		} else {
			return c.getHitbox().getMinX() < player.getHitbox().getMinX();
		}
	}

	private void shootCannon(Cannon c) {
		int dir = 1;
		if (c.getObjType() == ObjectCts.CANNON_LEFT)
			dir = -1;
		playing.getGame().getAudioPlayer().playEffect(GameAudio.WOMP);
		projectiles.add(new Projectile(c.getSpawn(), dir));
	}

	private void loadDialogues() {
		dialogues.clear();
		for (int i = 0; i < 10; i++)
			dialogues.add(new Dialogue(new Point2D(0, 0), DialogueCts.EXCLAMATION));
		for (int i = 0; i < 10; i++)
			dialogues.add(new Dialogue(new Point2D(0, 0), DialogueCts.QUESTION));

		for (Dialogue d : dialogues)
			d.deactive();
	}

	public void addDialogue(int x, int y, int type) {
		dialogues.add(new Dialogue(new Point2D(x, y - AppStage.Scale(15)), type));
		for (Dialogue d : dialogues)
			if (!d.isActive())
				if (d.getObjType() == type) {
					d.reset(x, -AppStage.Scale(15));
					return;
				}
	}

	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (Container c : currentLevel.getLevelObjects().getContainer().getItems())
			c.reset();
		for (Cannon c : currentLevel.getLevelObjects().getCannon().getItems())
			c.reset();
		loadDialogues();
	}

	public void scale() {
		if (currentLevel != null) {
			currentLevel.getLevelObjects().scale();
			for (Potion p : this.potions) {
				p.scale();
			}
			for (Projectile p : this.projectiles) {
				p.scale();
			}
			for (Dialogue d : this.dialogues) {
				d.scale();
			}
		}
	}
}
