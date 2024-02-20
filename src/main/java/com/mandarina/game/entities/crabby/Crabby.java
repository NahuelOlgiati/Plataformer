package com.mandarina.game.entities.crabby;

import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.constants.DialogueCts;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.game.entities.player.Player;
import com.mandarina.game.gamestates.Playing;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Crabby extends Enemy {

	public Crabby(float x, float y) {
		super(x, y, CrabbySprite.WIDTH.scaled(), CrabbySprite.HEIGHT.scaled(), CrabbyAtlas.GreenValue());
		initHitbox(CrabbyHitbox.width(), CrabbyHitbox.height());
		initAttackBox(CrabbyAttackHitbox.width(), CrabbyAttackHitbox.height(), CrabbyAttackHitbox.attackBoxOffsetX());
	}

	@Override
	public void update(Playing playing) {
		super.update(playing);
		updateAttackBox();
	}

	@Override
	protected void updateBehavior(Playing playing) {
		var levelData = playing.getLevelData();
		if (firstUpdate)
			firstUpdateCheck(levelData);

		if (inAir) {
			inAirChecks(playing);
		} else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, levelData))
					newState(EnemyState.RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(levelData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(EnemyState.ATTACK);
				}
				move(levelData);

				if (inAir)
					playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(),
							DialogueCts.EXCLAMATION);

				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	@Override
	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), CrabbySprite.WIDTH.scaled(),
				CrabbySprite.HEIGHT.scaled(), CrabbySprite.DRAWOFFSET_X.scaled(), CrabbySprite.DRAWOFFSET_Y.scaled());
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return CrabbyAtlas.GetSpriteAmount(state);
	}

	@Override
	protected int getMaxHealth() {
		return CrabbyStats.GetMaxHealth();
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance;
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-CrabbyStats.GetEnemyDmg(), this);
		attackChecked = true;
	}
}