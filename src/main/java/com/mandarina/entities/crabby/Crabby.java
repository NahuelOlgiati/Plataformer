package com.mandarina.entities.crabby;

import static com.mandarina.utilz.HelpMethods.IsFloor;

import javafx.geometry.Rectangle2D;

import com.mandarina.constants.DialogueCts;
import com.mandarina.entities.Enemy;
import com.mandarina.entities.EnemyState;
import com.mandarina.entities.player.Player;
import com.mandarina.gamestates.Playing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Crabby extends Enemy {

	public Crabby(float x, float y) {
		super(x, y, CrabbySprite.WIDTH.scaled(), CrabbySprite.HEIGHT.scaled(), CrabbyAtlas.GreenValue());
		initHitbox(CrabbyHitbox.width(), CrabbyHitbox.height());
		initAttackBox(CrabbyAttackHitbox.width(), CrabbyAttackHitbox.height(), CrabbyAttackHitbox.attackBoxOffsetX());
	}

	@Override
	protected void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			inAirChecks(lvlData, playing);
		} else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, lvlData))
					newState(EnemyState.RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(EnemyState.ATTACK);
				}
				move(lvlData);

				if (inAir)
					playing.addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);

				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}
	
	@Override
	protected void draw(GraphicsContext g, int xLvlOffset, Image[][] animations) {
		draw(g, xLvlOffset, animations, CrabbySprite.WIDTH.scaled(), CrabbySprite.HEIGHT.scaled(),
				CrabbySprite.DRAWOFFSET_X.scaled(), CrabbySprite.DRAWOFFSET_Y.scaled());
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