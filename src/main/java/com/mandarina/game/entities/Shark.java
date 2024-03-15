package com.mandarina.game.entities;

import static com.mandarina.utilz.SmallerThanTile.CanMoveHere;
import static com.mandarina.utilz.SmallerThanTile.IsEntityOnFloor;
import static com.mandarina.utilz.SmallerThanTile.IsFloor;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Shark extends Enemy {

	public Shark(Point2D spawn) {
		super(spawn, EntityCts.SHARK);
		initDraw(SharkCts.SPRITE_WIDTH, SharkCts.SPRITE_HEIGHT, SharkCts.DRAW_OFFSET_X, SharkCts.DRAW_OFFSET_Y);
		initHitbox(SharkCts.HITBOX_WIDTH, SharkCts.HITBOX_HEIGHT);
		initAttackBox(SharkCts.ATTACKBOX_WIDTH, SharkCts.ATTACKBOX_HEIGHT, SharkCts.ATTACKBOX_OFFSET_X,
				SharkCts.ATTACKBOX_OFFSET_Y);
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
				if (IsEntityOnFloor(hitbox, levelData))
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
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(playing);
				}

				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 2);
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
	protected int getMaxHealth() {
		return SharkCts.HEALTH;
	}

	@Override
	public void resetEnemy() {
		super.resetEnemy();
		initHitbox(SharkCts.HITBOX_WIDTH, SharkCts.HITBOX_HEIGHT);
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance * 2;
	}

	private void attackMove(Playing playing) {
		var levelData = playing.getLevelData();
		double xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				levelData)) {
			if (IsFloor(hitbox, xSpeed * 4, levelData)) {
				hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(),
						hitbox.getHeight());
				return;
			}
		}
		newState(EnemyState.IDLE);
		playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-SharkCts.DAMAGE, this);
		else {
			return;
		}
		attackChecked = true;
	}

	private static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 8;
		case RUNNING:
			return 6;
		case ATTACK:
			return 8;
		case HIT:
			return 4;
		case DEAD:
			return 5;
		}
		return 0;
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(SharkCts.ATLAS_SIZE_X, SharkCts.ATLAS_SIZE_Y, SharkCts.SPRITE_WIDTH,
				SharkCts.SPRITE_HEIGHT, LoadSave.GetAtlas(SharkCts.ATLAS_IMAGE));
	}

	public void scale() {
		super.scale();
		initDraw(SharkCts.SPRITE_WIDTH, SharkCts.SPRITE_HEIGHT, SharkCts.DRAW_OFFSET_X, SharkCts.DRAW_OFFSET_Y);
		initHitbox(SharkCts.HITBOX_WIDTH, SharkCts.HITBOX_HEIGHT);
		initAttackBox(SharkCts.ATTACKBOX_WIDTH, SharkCts.ATTACKBOX_HEIGHT, SharkCts.ATTACKBOX_OFFSET_X,
				SharkCts.ATTACKBOX_OFFSET_Y);
	}
}
