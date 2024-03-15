package com.mandarina.game.entities;

import static com.mandarina.utilz.BiggerThanTile.CanMoveHere;
import static com.mandarina.utilz.BiggerThanTile.IsFloor;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Longleg extends Enemy {

	public Longleg(Point2D spawn) {
		super(spawn, LonglegCts.HEALTH, EntityCts.LONGLEG);
		initLongleg();
	}

	private void initLongleg() {
		initAttackDistance(LonglegCts.ATTACK_DISTANCE);
		initAttackWalkSpeed(LonglegCts.WALK_SPEED);
		initDraw(LonglegCts.SPRITE_WIDTH, LonglegCts.SPRITE_HEIGHT, LonglegCts.DRAW_OFFSET_X, LonglegCts.DRAW_OFFSET_Y);
		initHitbox(LonglegCts.HITBOX_WIDTH, LonglegCts.HITBOX_HEIGHT);
		initAttackBox(LonglegCts.ATTACKBOX_WIDTH, LonglegCts.ATTACKBOX_HEIGHT, LonglegCts.ATTACKBOX_OFFSET_X,
				LonglegCts.ATTACKBOX_OFFSET_Y);
	}

	@Override
	public void update(Playing playing) {
		super.update(playing);
		updateAttackBoxFlip();
	}

	@Override
	protected void updateBehavior(Playing playing) {
		var levelData = playing.getLevelData();
		if (firstUpdate)
			firstUpdateCheck(levelData);

		if (inAir)
			inAirChecks(playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, xSpeed, LonglegCts.HITBOX_HORIZONTAL_CHECKS, levelData))
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
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == LonglegCts.ATTACK_ANI_IND) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(playing);
				}

				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 0.5);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	@Override
	public void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val());
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return GetSpriteAmount(state);
	}

	@Override
	public void resetEnemy() {
		super.resetEnemy();
		initHitbox(LonglegCts.HITBOX_WIDTH, LonglegCts.HITBOX_HEIGHT);
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance;
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-LonglegCts.DAMAGE, this);
		else {
			return;
		}
		attackChecked = true;
	}

	private void attackMove(Playing playing) {
		var levelData = playing.getLevelData();
		updateXSpeed();

		if (CanMoveHere(hitbox, xSpeed, ySpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, PlayerCts.HITBOX_VERTICAL_CHECKS,
				levelData)) {
			if (IsFloor(hitbox, xSpeed * 4, PlayerCts.HITBOX_VERTICAL_CHECKS, levelData)) {
				hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(),
						hitbox.getHeight());
				return;
			}
		}

		newState(EnemyState.IDLE);
		playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
	}

	private static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 4;
		case RUNNING:
			return 8;
		case ATTACK:
			return 7;
		case HIT:
			return 7;
		case DEAD:
			return 7;
		}
		return 0;
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(LonglegCts.ATLAS_SIZE_X, LonglegCts.ATLAS_SIZE_Y, LonglegCts.SPRITE_WIDTH,
				LonglegCts.SPRITE_HEIGHT, LoadSave.GetAtlas(LonglegCts.ATLAS_IMAGE));
	}

	public void scale() {
		super.scale();
		initLongleg();
	}
}
