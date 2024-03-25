package com.mandarina.game.entities;

import static com.mandarina.utilz.PositionUtil.GetEntityMinYNextToPlane;
import static com.mandarina.utilz.SmallerThanTile.CanMoveHere;
import static com.mandarina.utilz.SmallerThanTile.IsEntityInWater;
import static com.mandarina.utilz.SmallerThanTile.IsEntityOnFloor;
import static com.mandarina.utilz.SmallerThanTile.IsFloor;
import static com.mandarina.utilz.SmallerThanTile.IsSightClear;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;

public abstract class Enemy extends Entity {
	protected EnemyState state;
	protected boolean firstUpdate = true;
	protected int enemyType;
	protected boolean active = true;

	protected float attackDistance;

	public Enemy(Point spawn, int health, int enemyType) {
		super(spawn, health);
		this.enemyType = enemyType;
		this.state = EnemyState.IDLE;
	}

	protected void initAttackDistance(int attackDistance) {
		this.attackDistance = AppStage.Scale(attackDistance);
	}

	protected abstract void updateBehavior(Playing playing);

	protected abstract void draw(GameDrawer g, Offset offset, Image[][] animations);

	protected abstract int getSpriteAmount(EnemyState state);

	public void update(Playing playing) {
		updateBehavior(playing);
		updateAnimationTick();
	}

	protected void firstUpdateCheck(LevelData levelData) {
		if (!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
		firstUpdate = false;
	}

	protected void inAirChecks(Playing playing) {
		var levelData = playing.getLevelData();
		if (!EnemyState.HIT.equals(state) && !EnemyState.DEAD.equals(state)) {
			updateInAir(levelData);
			playing.getObjectManager().checkSpikesTouched(this);
			if (IsEntityInWater(hitbox, levelData))
				hurt(maxHealth);
		}
	}

	protected void updateInAir(LevelData levelData) {
		if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + ySpeed, hitbox.getWidth(), hitbox.getHeight(),
				levelData)) {
			hitbox.setMinY(hitbox.getMinY() + ySpeed);
			ySpeed += AppStage.Scale(GameCts.GRAVITY_DEFAULT);
		} else {
			inAir = false;
			hitbox.setMinY(GetEntityMinYNextToPlane(hitbox, ySpeed));
			updateTileY();
		}
	}

	protected void move(LevelData levelData) {
		float xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), levelData))
			if (IsFloor(hitbox, xSpeed, levelData)) {
				hitbox.setMinX(hitbox.getMinX() + xSpeed);
				return;
			}

		changeWalkDir();
	}

	protected void turnTowardsPlayer(Player player) {
		if (player.getHitbox().getMinX() > hitbox.getMinX()) {
			walkDir = DirectionCts.RIGHT;
		} else {
			walkDir = DirectionCts.LEFT;
		}
	}

	protected boolean canSeePlayer(LevelData levelData, Player player) {
		if (player.getTileY() == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(levelData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}

	protected boolean isPlayerInRange(Player player) {
		int absValue = getCurrentPlayerDistance(player);
		return absValue <= attackDistance * 5;
	}

	protected int getCurrentPlayerDistance(Player player) {
		return (int) Math.abs(player.getHitbox().getMinX() - hitbox.getMinX());
	}

	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(EnemyState.DEAD);
		else {
			newState(EnemyState.HIT);
			if (walkDir == DirectionCts.LEFT)
				pushBackDir = DirectionCts.RIGHT;
			else
				pushBackDir = DirectionCts.LEFT;
			pushBackOffsetDir = DirectionCts.UP;
			pushDrawOffset = 0;
		}
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(state)) {
				aniIndex = 0;
				switch (state) {
				case ATTACK, HIT -> state = EnemyState.IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}

	public void resetEnemy() {
		this.inAir = true;
		this.walkDir = DirectionCts.LEFT;
		this.pushBackOffsetDir = DirectionCts.UP;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(EnemyState.IDLE);
		active = true;
		ySpeed = 0;
		pushDrawOffset = 0;
		toSpawn();
	}

	public boolean isActive() {
		return active;
	}

	public EnemyState getState() {
		return state;
	}

	protected void newState(EnemyState state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}

	@Override
	public void scale() {
		super.scale();
		walkSpeed = AppStage.Scale(0.35f);
	}
}