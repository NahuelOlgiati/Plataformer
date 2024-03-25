package com.mandarina.game.entities;

import static com.mandarina.utilz.BiggerThanTile.CanMoveHere;
import static com.mandarina.utilz.BiggerThanTile.IsFloor;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.LoadSave;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;

public class Titan extends Enemy {

	public Titan(Point spawn) {
		super(spawn, TitanCts.HEALTH, EntityCts.TITAN);
		initTitan();
	}

	private void initTitan() {
		initAttackDistance(TitanCts.ATTACK_DISTANCE);
		initAttackWalkSpeed(TitanCts.WALK_SPEED);
		initDraw(TitanCts.SPRITE_WIDTH, TitanCts.SPRITE_HEIGHT, TitanCts.DRAW_OFFSET_X, TitanCts.DRAW_OFFSET_Y);
		initHitbox(TitanCts.HITBOX_WIDTH, TitanCts.HITBOX_HEIGHT);
		initAttackBox(TitanCts.ATTACKBOX_WIDTH, TitanCts.ATTACKBOX_HEIGHT, TitanCts.ATTACKBOX_OFFSET_X,
				TitanCts.ATTACKBOX_OFFSET_Y);
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
				if (IsFloor(hitbox, xSpeed, TitanCts.HITBOX_HORIZONTAL_CHECKS, levelData))
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
				else if (aniIndex >= TitanCts.ATTACK_ANI_IND) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(playing);
				}

				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 0.5f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	@Override
	public void draw(GameDrawer g, Offset offset, Image[][] animations) {
		draw(g, offset, animations, state.val());
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return GetSpriteAmount(state);
	}

	@Override
	public void resetEnemy() {
		super.resetEnemy();
		initHitbox(TitanCts.HITBOX_WIDTH, TitanCts.HITBOX_HEIGHT);
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance * 2;
	}

	private void checkPlayerHit(Box attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-TitanCts.DAMAGE, this);
		else {
			return;
		}
		attackChecked = true;
	}

	private void attackMove(Playing playing) {
		var levelData = playing.getLevelData();
		updateXSpeed();

		if (CanMoveHere(hitbox, xSpeed * 4, ySpeed, TitanCts.HITBOX_HORIZONTAL_CHECKS, TitanCts.HITBOX_VERTICAL_CHECKS,
				levelData)) {
			if (IsFloor(hitbox, xSpeed * 4, TitanCts.HITBOX_VERTICAL_CHECKS, levelData)) {
				hitbox.setMinX(hitbox.getMinX() + xSpeed * 4);
				return;
			}
		}

		newState(EnemyState.IDLE);
		playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
	}

	private static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 6;
		case RUNNING:
			return 6;
		case ATTACK:
			return 6;
		case HIT:
			return 6;
		case DEAD:
			return 6;
		}
		return 0;
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(TitanCts.ATLAS_SIZE_X, TitanCts.ATLAS_SIZE_Y, TitanCts.SPRITE_WIDTH,
				TitanCts.SPRITE_HEIGHT, LoadSave.GetAtlas(TitanCts.ATLAS_IMAGE));
	}

	@Override
	public void scale() {
		super.scale();
		initTitan();
	}
}
