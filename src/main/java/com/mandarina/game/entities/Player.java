package com.mandarina.game.entities;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.GetEntityXPosNextToWall;
import static com.mandarina.utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static com.mandarina.utilz.HelpMethods.IsEntityInWater;
import static com.mandarina.utilz.HelpMethods.IsEntityOnFloor;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.audio.AudioPlayer;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Player extends Entity {

	private Image[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;

	private double jumpSpeed;
	private double fallSpeedAfterCollision;

	private Playing playing;
	private PlayerState state;

	private int tileY = 0;

	private boolean powerAttackActive;
	private int powerAttackTick;

	public Player(Point2D spawn) {
		super(spawn);
		this.state = PlayerState.IDLE;
		this.walkDir = DirectionCts.RIGHT;
		this.maxHealth = PlayerCts.HEALTH;
		this.currentHealth = maxHealth;
		this.animations = load();
		this.walkSpeed = AppStage.Scale(1.0);
		this.jumpSpeed = AppStage.Scale(-2.25);
		this.fallSpeedAfterCollision = AppStage.Scale(0.5);
		initSize(PlayerCts.SPRITE_WIDTH, PlayerCts.SPRITE_HEIGHT);
		initHitbox(PlayerCts.HITBOX_WIDTH, PlayerCts.HITBOX_HEIGHT);
		initAttackBox(PlayerCts.ATTACK_HITBOX_WIDTH, PlayerCts.ATTACK_HITBOX_HEIGHT, PlayerCts.ATTACK_HITBOX_OFFSET_X);
	}

	public void setPlaying(Playing playing) {
		this.playing = playing;
	}

	@Override
	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		super.initAttackBox(w, h, attackBoxOffsetX);
		resetAttackBox();
	}

	public void update() {
		LevelData levelData = playing.getLevelManager().getCurrentLevel().getLevelData();
		if (currentHealth <= 0) {
			updateNoHealth(levelData);
			return;
		}

		updateWalkDir();
		updateAttackBoxFlip();

		if (PlayerState.HIT.equals(state)) {
			if (aniIndex <= GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, levelData, 1.25);
			updatePushBackDrawOffset();
		} else
			updatePos();

		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.getMinY() / AppStage.GetTileSize());
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}

		if (attacking || powerAttackActive)
			checkAttack();

		updateAnimationTick();
		setAnimation();
	}

	private void updateNoHealth(LevelData levelData) {
		if (!PlayerState.DEAD.equals(state)) {
			state = PlayerState.DEAD;
			aniTick = 0;
			aniIndex = 0;
			playing.setPlayerDying(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

			// Check if player died in air
			if (!IsEntityOnFloor(hitbox, levelData)) {
				inAir = true;
				airSpeed = 0;
			}
		} else if (aniIndex == GetSpriteAmount(PlayerState.DEAD) - 1 && aniTick >= GameCts.ANI_SPEED - 1) {
			playing.setGameOver(true);
			playing.getGame().getAudioPlayer().stopSong();
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
		} else {
			updateAnimationTick();

			// Fall if in air
			if (inAir) {
				if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(), hitbox.getHeight(),
						levelData)) {
					hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(),
							hitbox.getHeight());
					airSpeed += AppStage.Scale(GameCts.GRAVITY_DEFAULT);
				} else {
					inAir = false;
				}
			}

		}
	}

	private void checkInsideWater() {
		if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;

		if (powerAttackActive)
			attackChecked = false;

		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateWalkDir() {
		if (right && left) {
			if (walkDir == DirectionCts.RIGHT) {
				changeWalkDir(DirectionCts.RIGHT);
			} else {
				changeWalkDir(DirectionCts.LEFT);
			}

		} else if (right || (powerAttackActive && walkDir == DirectionCts.RIGHT))
			changeWalkDir(DirectionCts.RIGHT);
		else if (left || (powerAttackActive && walkDir == DirectionCts.LEFT))
			changeWalkDir(DirectionCts.LEFT);
	}

	public void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		int spriteWidth = AppStage.Scale(PlayerCts.SPRITE_WIDTH);
		int spriteHeight = AppStage.Scale(PlayerCts.SPRITE_HEIGHT);
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), spriteWidth, spriteHeight,
				AppStage.Scale(PlayerCts.DRAW_OFFSET_X), AppStage.Scale(PlayerCts.DRAW_OFFSET_Y));
	}

	@Override
	public int flipX() {
		if (walkDir == DirectionCts.RIGHT)
			return 0;
		else
			return width;
	}

	@Override
	public int flipW() {
		if (walkDir == DirectionCts.RIGHT)
			return 1;
		else
			return -1;
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				if (PlayerState.HIT.equals(state)) {
					newState(PlayerState.IDLE);
					airSpeed = 0f;
					LevelData levelData = playing.getLevelData();
					if (!IsFloor(hitbox, 0, levelData))
						inAir = true;
				}
			}
		}
	}

	private void setAnimation() {
		PlayerState startAni = state;

		if (PlayerState.HIT.equals(state))
			return;

		if (moving)
			state = PlayerState.RUNNING;
		else
			state = PlayerState.IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = PlayerState.JUMP;
			else
				state = PlayerState.FALLING;
		}

		if (powerAttackActive) {
			state = PlayerState.ATTACK;
			aniIndex = 1;
			aniTick = 0;
			return;
		}

		if (attacking) {
			state = PlayerState.ATTACK;
			if (!PlayerState.ATTACK.equals(startAni)) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (!state.equals(startAni)) {
			resetAniTick();
		}
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;

		double xSpeed = 0;

		if (left && !right) {
			xSpeed -= walkSpeed;
		}
		if (right && !left) {
			xSpeed += walkSpeed;
		}

		if (powerAttackActive) {
			if ((!left && !right) || (left && right)) {
				if (walkDir == DirectionCts.LEFT)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			xSpeed *= 3;
		}

		LevelData levelData = playing.getLevelData();
		if (!inAir)
			if (!IsEntityOnFloor(hitbox, levelData))
				inAir = true;

		if (inAir && !powerAttackActive) {
			if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(), hitbox.getHeight(),
					levelData)) {
				hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(),
						hitbox.getHeight());
				airSpeed += AppStage.Scale(GameCts.GRAVITY_DEFAULT);
				updateXPos(xSpeed);
			} else {
				hitbox = new Rectangle2D(hitbox.getMinX(), GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed),
						hitbox.getWidth(), hitbox.getHeight());
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(double xSpeed) {
		LevelData levelData = playing.getLevelData();
		if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				levelData)) {
			hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(),
					hitbox.getHeight());
		} else {
			hitbox = new Rectangle2D(GetEntityXPosNextToWall(hitbox, xSpeed), hitbox.getMinY(), hitbox.getWidth(),
					hitbox.getHeight());
			if (powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}

	public void changeHealth(int value) {
		if (value < 0) {
			if (PlayerState.HIT.equals(state))
				return;
			else
				newState(PlayerState.HIT);
		}

		currentHealth += value;
		currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
	}

	public void changeHealth(int value, Enemy e) {
		if (PlayerState.HIT.equals(state))
			return;

		changeHealth(value);
		pushBackOffsetDir = DirectionCts.UP;
		pushDrawOffset = 0;

		if (e.getHitbox().getMinX() < hitbox.getMinX())
			pushBackDir = DirectionCts.RIGHT;
		else
			pushBackDir = DirectionCts.LEFT;
	}

	public void kill() {
		currentHealth = 0;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = true;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = PlayerState.IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		playing.getStatusBar().resetPower();

		toSpawn();
		hitbox = new Rectangle2D(x, y, hitbox.getWidth(), hitbox.getHeight());
		resetAttackBox();

		LevelData levelData = playing.getLevelData();
		if (!IsEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	private void resetAttackBox() {
		if (walkDir == DirectionCts.RIGHT)
			changeWalkDir(DirectionCts.RIGHT);
		else
			changeWalkDir(DirectionCts.LEFT);
	}

	public int getTileY() {
		return tileY;
	}

	public void powerAttack() {
		if (powerAttackActive)
			return;
		if (playing.getStatusBar().getPowerValue() >= 60) {
			powerAttackActive = true;
			playing.getStatusBar().changePower(-60);
		}
	}

	protected void newState(PlayerState state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	private static int GetSpriteAmount(PlayerState state) {
		switch (state) {
		case DEAD:
			return 6;
		case RUNNING:
			return 8;
		case IDLE:
			return 4;
		case HIT:
			return 6;
		case JUMP:
			return 8;
		case ATTACK:
			return 7;
		case FALLING:
			return 4;
		default:
			return 1;
		}
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(PlayerCts.ATLAS_SIZE_X, PlayerCts.ATLAS_SIZE_Y, PlayerCts.SPRITE_WIDTH,
				PlayerCts.SPRITE_HEIGHT, LoadSave.GetAtlas(PlayerCts.ATLAS_IMAGE));
	}

	public void scale() {
		super.scale();
		this.walkSpeed = AppStage.Scale(1.0);
		this.jumpSpeed = AppStage.Scale(-2.25);
		this.fallSpeedAfterCollision = AppStage.Scale(0.5);
		initSize(PlayerCts.SPRITE_WIDTH, PlayerCts.SPRITE_HEIGHT);
		initHitbox(PlayerCts.HITBOX_WIDTH, PlayerCts.HITBOX_HEIGHT);
		initAttackBox(PlayerCts.ATTACK_HITBOX_WIDTH, PlayerCts.ATTACK_HITBOX_HEIGHT, PlayerCts.ATTACK_HITBOX_OFFSET_X);
	}
}