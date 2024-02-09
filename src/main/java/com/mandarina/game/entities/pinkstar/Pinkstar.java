package com.mandarina.game.entities.pinkstar;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.constants.DialogueCts;
import com.mandarina.game.constants.DirectionCts;
import com.mandarina.game.constants.GameCts;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.game.entities.player.Player;
import com.mandarina.game.gamestates.Playing;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pinkstar extends Enemy {

	private boolean preRoll = true;
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;

	public Pinkstar(float x, float y) {
		super(x, y, PinkstarSprite.WIDTH.scaled(), PinkstarSprite.HEIGHT.scaled(), PinkstarAtlas.GreenValue());
		initHitbox(PinkstarHitbox.width(), PinkstarHitbox.height());
		initAttackBox(PinkstarAttackHitbox.width(), PinkstarAttackHitbox.height(),
				PinkstarAttackHitbox.attackBoxOffsetX());
	}

	@Override
	public void update(int[][] lvlData, Playing playing) {
		super.update(lvlData, playing);
		updateAttackBox();
	}

	@Override
	protected void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				preRoll = true;
				if (tickAfterRollInIdle >= 120) {
					if (IsFloor(hitbox, lvlData))
						newState(EnemyState.RUNNING);
					else
						inAir = true;
					tickAfterRollInIdle = 0;
					tickSinceLastDmgToPlayer = 60;
				} else
					tickAfterRollInIdle++;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					newState(EnemyState.ATTACK);
					setWalkDir(playing.getPlayer());
				}
				move(lvlData, playing);
				break;
			case ATTACK:
				if (preRoll) {
					if (aniIndex >= 3)
						preRoll = false;
				} else {
					move(lvlData, playing);
					checkPlayerHit(playing.getPlayer());
					checkRollOver(playing);
				}
				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				tickAfterRollInIdle = 120;

				break;
			}
		}
	}

	@Override
	protected void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), PinkstarSprite.WIDTH.scaled(),
				PinkstarSprite.HEIGHT.scaled(), PinkstarSprite.DRAWOFFSET_X.scaled(),
				PinkstarSprite.DRAWOFFSET_Y.scaled());
	}

	@Override
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(state)) {
				if (EnemyState.ATTACK.equals(state))
					aniIndex = 3;
				else {
					aniIndex = 0;
					if (EnemyState.HIT.equals(state)) {
						state = EnemyState.IDLE;

					} else if (EnemyState.DEAD.equals(state))
						active = false;
				}
			}
		}
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return PinkstarAtlas.GetSpriteAmount(state);
	}

	@Override
	protected int getMaxHealth() {
		return PinkstarStats.GetMaxHealth();
	}

	private void checkPlayerHit(Player player) {
		if (attackBox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-PinkstarStats.GetEnemyDmg(), this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	private void setWalkDir(Player player) {
		if (player.getHitbox().getMinX() > hitbox.getMinX())
			walkDir = DirectionCts.RIGHT;
		else
			walkDir = DirectionCts.LEFT;

	}

	private void move(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (EnemyState.ATTACK.equals(state))
			xSpeed *= 2;

		if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(),
						hitbox.getHeight());
				return;
			}

		if (EnemyState.ATTACK.equals(state)) {
			rollOver(playing);
			rollDurationTick = 0;
		}

		changeWalkDir();

	}

	private void checkRollOver(Playing playing) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(playing);
			rollDurationTick = 0;
		}
	}

	private void rollOver(Playing playing) {
		newState(EnemyState.IDLE);
		playing.addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.QUESTION);
	}

}
