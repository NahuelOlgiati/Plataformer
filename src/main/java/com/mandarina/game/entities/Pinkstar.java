package com.mandarina.game.entities;

import static com.mandarina.utilz.SmallerThanTile.CanMoveHere;
import static com.mandarina.utilz.SmallerThanTile.IsEntityOnFloor;
import static com.mandarina.utilz.SmallerThanTile.IsFloor;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Pinkstar extends Enemy {

	private boolean preRoll = true;
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;

	public Pinkstar(Point2D spawn) {
		super(spawn, PinkstarCts.HEALTH, EntityCts.PINKSTAR);
		initPinkstar();
	}

	private void initPinkstar() {
		initAttackDistance(PinkstarCts.ATTACK_DISTANCE);
		initAttackWalkSpeed(PinkstarCts.WALK_SPEED);
		initDraw(PinkstarCts.SPRITE_WIDTH, PinkstarCts.SPRITE_HEIGHT, PinkstarCts.DRAW_OFFSET_X,
				PinkstarCts.DRAW_OFFSET_Y);
		initHitbox(PinkstarCts.HITBOX_WIDTH, PinkstarCts.HITBOX_HEIGHT);
		initAttackBox(PinkstarCts.ATTACKBOX_WIDTH, PinkstarCts.ATTACKBOX_HEIGHT, PinkstarCts.ATTACKBOX_OFFSET_X,
				PinkstarCts.ATTACKBOX_OFFSET_Y);
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

		if (inAir)
			inAirChecks(playing);
		else {
			switch (state) {
			case IDLE:
				preRoll = true;
				if (tickAfterRollInIdle >= 120) {
					if (IsEntityOnFloor(hitbox, levelData))
						newState(EnemyState.RUNNING);
					else
						inAir = true;
					tickAfterRollInIdle = 0;
					tickSinceLastDmgToPlayer = 60;
				} else
					tickAfterRollInIdle++;
				break;
			case RUNNING:
				if (canSeePlayer(levelData, playing.getPlayer())) {
					newState(EnemyState.ATTACK);
					setWalkDir(playing.getPlayer());
				}
				move(playing);
				break;
			case ATTACK:
				if (preRoll) {
					if (aniIndex >= PinkstarCts.ATTACK_ANI_IND)
						preRoll = false;
				} else {
					move(playing);
					checkPlayerHit(playing.getPlayer());
					checkRollOver(playing);
				}
				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 2);
				updatePushBackDrawOffset();
				tickAfterRollInIdle = 120;

				break;
			}
		}
	}

	@Override
	public void draw(GameDrawer g, Offset offset, Image[][] animations) {
		draw(g, offset, animations, state.val());
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
		return GetSpriteAmount(state);
	}

	@Override
	public void resetEnemy() {
		super.resetEnemy();
		initHitbox(PinkstarCts.HITBOX_WIDTH, PinkstarCts.HITBOX_HEIGHT);
	}

	private void checkPlayerHit(Player player) {
		if (attackBox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-PinkstarCts.DAMAGE, this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	private void setWalkDir(Player player) {
		if (player.getHitbox().getMinX() > hitbox.getMinX())
			walkDir = DirectionCts.RIGHT;
		else
			walkDir = DirectionCts.LEFT;

	}

	private void move(Playing playing) {
		var levelData = playing.getLevelData();
		updateXSpeed();

		if (EnemyState.ATTACK.equals(state))
			xSpeed *= 2;

		if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), levelData))
			if (IsFloor(hitbox, xSpeed, levelData)) {
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
		playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.QUESTION);
	}

	private static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 8;
		case RUNNING:
			return 6;
		case ATTACK:
			return 7;
		case HIT:
			return 4;
		case DEAD:
			return 5;
		}
		return 0;
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(PinkstarCts.ATLAS_SIZE_X, PinkstarCts.ATLAS_SIZE_Y, PinkstarCts.SPRITE_WIDTH,
				PinkstarCts.SPRITE_HEIGHT, LoadSave.GetAtlas(PinkstarCts.ATLAS_IMAGE));
	}

	@Override
	public void scale() {
		super.scale();
		initPinkstar();
	}
}
