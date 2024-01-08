package com.mandarina.entities;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static com.mandarina.utilz.HelpMethods.IsEntityInWater;
import static com.mandarina.utilz.HelpMethods.IsEntityOnFloor;
import static com.mandarina.utilz.HelpMethods.IsFloor;
import static com.mandarina.utilz.HelpMethods.IsSightClear;

import com.mandarina.constants.DirectionCts;
import com.mandarina.constants.GameCts;
import com.mandarina.entities.player.Player;
import com.mandarina.gamestates.Playing;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Enemy extends Entity {
	protected EnemyState state;
	protected boolean firstUpdate = true;
	protected int tileY;
	protected float attackDistance = GameCts.TILES_SIZE;
	protected boolean active = true;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.state = EnemyState.IDLE;
		maxHealth = getMaxHealth();
		currentHealth = maxHealth;
		walkSpeed = GameCts.SCALE * 0.35f;
	}

	protected abstract void updateBehavior(int[][] lvlData, Playing playing);

	protected abstract void draw(GraphicsContext g, int xLvlOffset, Image[][] animations);

	protected abstract int getSpriteAmount(EnemyState state);

	protected abstract int getMaxHealth();

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackBox = new Rectangle2D(x, y, (int) (w * GameCts.SCALE), (int) (h * GameCts.SCALE));
		this.attackBoxOffsetX = (int) (GameCts.SCALE * attackBoxOffsetX);
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void inAirChecks(int[][] lvlData, Playing playing) {
		if (!EnemyState.HIT.equals(state) && !EnemyState.DEAD.equals(state)) {
			updateInAir(lvlData);
			playing.getObjectManager().checkSpikesTouched(this);
			if (IsEntityInWater(hitbox, lvlData))
				hurt(maxHealth);
		}
	}

	protected void updateInAir(int[][] lvlData) {
	    if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(), hitbox.getHeight(), lvlData)) {
	        hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(), hitbox.getHeight());
	        airSpeed += GameCts.GRAVITY;
	    } else {
	        inAir = false;
	        hitbox = new Rectangle2D(hitbox.getMinX(), GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed),
	                                hitbox.getWidth(), hitbox.getHeight());
	        tileY = (int) (hitbox.getMinY() / GameCts.TILES_SIZE);
	    }
	}

	protected void move(int[][] lvlData) {
	    float xSpeed = 0;

	    if (walkDir == DirectionCts.LEFT)
	        xSpeed = -walkSpeed;
	    else
	        xSpeed = walkSpeed;

	    if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), lvlData))
	        if (IsFloor(hitbox, xSpeed, lvlData)) {
	            hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight());
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

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().getMinY() / GameCts.TILES_SIZE);
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
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
	    hitbox = new Rectangle2D(x, y, hitbox.getWidth(), hitbox.getHeight());
	    firstUpdate = true;
	    currentHealth = maxHealth;
	    newState(EnemyState.IDLE);
	    active = true;
	    airSpeed = 0;
	    pushDrawOffset = 0;
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
}