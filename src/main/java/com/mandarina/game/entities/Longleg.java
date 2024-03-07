package com.mandarina.game.entities;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.IsEntityOnFloor;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Longleg extends Enemy {

	public Longleg(Point2D spawn) {
		super(spawn, EntityCts.LONGLEG);
		initDraw(LonglegCts.SPRITE_WIDTH, LonglegCts.SPRITE_HEIGHT, LonglegCts.DRAW_OFFSET_X, LonglegCts.DRAW_OFFSET_Y);
		initHitbox(LonglegCts.HITBOX_WIDTH, LonglegCts.HITBOX_WIDTH);
		initAttackBox(LonglegCts.ATTACK_HITBOX_WIDTH, LonglegCts.ATTACK_HITBOX_HEIGHT,
				LonglegCts.ATTACK_HITBOX_OFFSET_X);
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
	protected int getMaxHealth() {
		return LonglegCts.HEALTH;
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance * 2;
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
		return LoadSave.GetAnimations(LonglegCts.ATLAS_SIZE_X, LonglegCts.ATLAS_SIZE_Y, LonglegCts.SPRITE_WIDTH,
				LonglegCts.SPRITE_HEIGHT, LoadSave.GetAtlas(LonglegCts.ATLAS_IMAGE));
	}

	public void scale() {
		super.scale();
		initDraw(LonglegCts.SPRITE_WIDTH, LonglegCts.SPRITE_HEIGHT, LonglegCts.DRAW_OFFSET_X, LonglegCts.DRAW_OFFSET_Y);
		initHitbox(LonglegCts.HITBOX_WIDTH, LonglegCts.HITBOX_WIDTH);
		initAttackBox(LonglegCts.ATTACK_HITBOX_WIDTH, LonglegCts.ATTACK_HITBOX_HEIGHT,
				LonglegCts.ATTACK_HITBOX_OFFSET_X);
	}
}
