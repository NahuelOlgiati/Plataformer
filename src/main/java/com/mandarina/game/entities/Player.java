package com.mandarina.game.entities;

import static com.mandarina.utilz.BiggerThanTile.CanMoveHere;
import static com.mandarina.utilz.BiggerThanTile.IsFloor;
import static com.mandarina.utilz.PositionUtil.GetEntityMinXNextToWall;
import static com.mandarina.utilz.PositionUtil.GetEntityMinYNextToPlane;
import static com.mandarina.utilz.SmallerThanTile.IsEntityInWater;
import static com.mandarina.utilz.SmallerThanTile.IsEntityOnFloor;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.geometry.Box;
import com.mandarina.game.geometry.Point;
import com.mandarina.game.leveldata.Slide;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameAudio;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Player extends Entity {

	private Image[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump, duck, tryStandUp;

	private float xSpeed;
	private float jumpSpeed;
	private float fallSpeedAfterCollision;

	private Playing playing;
	private PlayerState state;

	private boolean powerAttackActive;
	private int powerAttackTick;

	private float duckHeight;
	private float diffHeight;

	private Slide slide;

	public Player(Point spawn) {
		super(spawn, PlayerCts.HEALTH);
		this.state = PlayerState.IDLE;
		this.walkDir = DirectionCts.RIGHT;
		this.animations = load();
		initPlayer();
	}

	private void initPlayer() {
		this.jumpSpeed = -AppStage.Scale(PlayerCts.JUMP_SPEED);
		this.fallSpeedAfterCollision = AppStage.Scale(PlayerCts.FALL_SPEED_AFTER_COLLISION);
		initAttackWalkSpeed(PlayerCts.WALK_SPEED);
		initDraw(PlayerCts.SPRITE_WIDTH, PlayerCts.SPRITE_HEIGHT, PlayerCts.DRAW_OFFSET_X, PlayerCts.DRAW_OFFSET_Y);
		initHitbox(PlayerCts.HITBOX_WIDTH, PlayerCts.HITBOX_HEIGHT);
		initAttackBox(PlayerCts.ATTACKBOX_WIDTH, PlayerCts.ATTACKBOX_HEIGHT, PlayerCts.ATTACKBOX_OFFSET_X,
				PlayerCts.ATTACKBOX_OFFSET_Y);
		this.duckHeight = AppStage.Scale(PlayerCts.HITBOX_DUCK_HEIGHT);
		this.diffHeight = AppStage.Scale(PlayerCts.HITBOX_DIFF_HEIGHT);
	}

	public void setPlaying(Playing playing) {
		this.playing = playing;
	}

	@Override
	protected void initAttackBox(int w, int h, int attackBoxOffsetX, int attackBoxOffsetY) {
		super.initAttackBox(w, h, attackBoxOffsetX, attackBoxOffsetY);
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

		checkOverSlider();

		if (PlayerState.HIT.equals(state)) {
			if (aniIndex <= GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, levelData, 1.25f);
			updatePushBackDrawOffset();
		} else
			updatePos();

		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
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
			playing.getGame().getAudioPlayer().playEffect(GameAudio.DIE);

			// Check if player died in air
			if (!IsEntityOnFloor(hitbox, levelData)) {
				inAir = true;
				ySpeed = 0;
			}
		} else if (aniIndex == GetSpriteAmount(PlayerState.DEAD) - 1 && aniTick >= GameCts.ANI_SPEED - 1) {
			playing.setGameOver(true);
			playing.getGame().getAudioPlayer().stopSong();
			playing.getGame().getAudioPlayer().playEffect(GameAudio.GAMEOVER);
		} else {
			updateAnimationTick();

			// Fall if in air
			if (inAir) {
				if (CanMoveHere(hitbox, 0, ySpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData)) {
					hitbox.setMinY(hitbox.getMinY() + ySpeed);
					ySpeed += AppStage.Scale(GameCts.GRAVITY_DEFAULT);
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

	private void checkOverSlider() {
		Slide checkOverSlider = playing.checkOverSlider(hitbox);
		if (checkOverSlider == null && onSlide()) {
			inAir = true;
		}
		if (checkOverSlider != null && !onSlide()) {
			jump = false;
		}
		this.slide = checkOverSlider;
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

	public void draw(GameDrawer g, Offset offset) {
		draw(g, offset, animations, state.val());
	}

	@Override
	public float flipX() {
		if (walkDir == DirectionCts.RIGHT)
			return 0;
		else
			return drawWidth;
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
					ySpeed = 0f;
					LevelData levelData = playing.getLevelData();
					if (!IsFloor(hitbox, xSpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, levelData))
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

		if (duck) {
			state = PlayerState.DUCK;
			if (!moving) {
				aniIndex = 1;
				aniTick = 0;
			}
			return;
		}

		if (inAir) {
			if (ySpeed < 0)
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

		if (onSlide())
			inAir = false;

		updateXSpeed();

		if (jump)
			jump();

		LevelData levelData = playing.getLevelData();

		if (tryStandUp) {
			tryStandUp(levelData);
		}

		if (!inAir && !onSlide())
			if (!IsEntityOnFloor(hitbox, levelData))
				inAir = true;

		if (inAir && !powerAttackActive) {
			updatePosOnAir();
			updateTileY();
		} else {

			if (onVerticalSlide()) {
				hitbox.setMinY(slide.getHitline().getY() - hitbox.getHeight());
				updateTileY();
			}

			updateXPos();
		}

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;

		moving = true;
	}

	private void tryStandUp(LevelData levelData) {
		Box clone = getDuckHitbox();
		if (CanMoveHere(clone, xSpeed, ySpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData)) {
			hitbox = clone;
			this.tryStandUp = false;
			this.duck = false;
		}
	}

	@Override
	protected void updateXSpeed() {
		xSpeed = 0;

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

		if (onHorizontalSlide()) {
			ySpeed = 0;
			xSpeed = xSpeed + slide.getSpeed();
		}
	}

	private void updatePosOnAir() {
		LevelData levelData = playing.getLevelData();
		boolean canMoveY = CanMoveHere(hitbox, 0, ySpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData);
		boolean canMoveX = CanMoveHere(hitbox, xSpeed, 0, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData);

		if (canMoveY && canMoveX) {
			hitbox.setMinXY(hitbox.getMinX() + xSpeed, hitbox.getMinY() + ySpeed);
			updateYSpeed();
			return;
		}

		float minXNextToWall = GetEntityMinXNextToWall(hitbox, xSpeed);
		if (canMoveY) {
			hitbox.setMinXY(minXNextToWall, hitbox.getMinY() + ySpeed);
			updateYSpeed();
			resetPowerAttack();
			return;
		}

		float minYNextToPlane = GetEntityMinYNextToPlane(hitbox, ySpeed);
		if (canMoveX) {
			hitbox.setMinXY(hitbox.getMinX() + xSpeed, minYNextToPlane);
			updateYSpeedAfterCollision(levelData);
			resetPowerAttack();
			return;
		}

		hitbox.setMinXY(GetEntityMinXNextToWall(hitbox, xSpeed, true), GetEntityMinYNextToPlane(hitbox, ySpeed, true));
		updateYSpeedAfterCollision(levelData);
		resetPowerAttack();
	}

	private void updateYSpeed() {
		ySpeed += AppStage.Scale(GameCts.GRAVITY_DEFAULT);
	}

	private void updateYSpeedAfterCollision(LevelData levelData) {
		if (ySpeed > 0 || IsFloor(hitbox, xSpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, levelData)) {
			resetInAir();
		} else {
			ySpeed = fallSpeedAfterCollision;
		}
	}

	private void updateXPos() {
		LevelData levelData = playing.getLevelData();
		if (CanMoveHere(hitbox, xSpeed, 0, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData)) {
			hitbox.setMinX(hitbox.getMinX() + xSpeed);
		} else {
			hitbox.setMinX(GetEntityMinXNextToWall(hitbox, xSpeed, true));
			resetPowerAttack();
		}
	}

	private boolean onSlide() {
		return slide != null;
	}

	private boolean onHorizontalSlide() {
		return slide != null && slide.isHorizontal();
	}

	private boolean onVerticalSlide() {
		return slide != null && !slide.isHorizontal();
	}

	public int getvCheck() {
		return duck ? 0 : PlayerCts.HITBOX_VERTICAL_CHECKS;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(GameAudio.JUMP);
		inAir = true;
		ySpeed = jumpSpeed;
	}

	private void resetPowerAttack() {
		if (powerAttackActive) {
			powerAttackActive = false;
			powerAttackTick = 0;
		}
	}

	private void resetInAir() {
		inAir = false;
		ySpeed = 0;
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

	public boolean isDuck() {
		return duck;
	}

	public void setDuck(boolean duck) {
		if (duck && duck != this.duck) {
			hitbox.set(hitbox.getMinX(), hitbox.getMinY() + diffHeight, hitbox.getWidth(), duckHeight);
			this.duck = duck;
		}
		if (!duck && duck != this.duck) {
			LevelData levelData = playing.getLevelData();
			Box clone = getDuckHitbox();
			if (CanMoveHere(clone, xSpeed, ySpeed, PlayerCts.HITBOX_HORIZONTAL_CHECKS, getvCheck(), levelData)) {
				hitbox = clone;
				this.duck = duck;
			} else {
				this.tryStandUp = true;
			}
		}
	}

	private Box getDuckHitbox() {
		Box clone = hitbox.clone();
		clone.set(hitbox.getMinX(), hitbox.getMinY() - diffHeight, hitbox.getWidth(), hitboxHeight);
		return clone;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = true;
		attacking = false;
		moving = false;
		ySpeed = 0f;
		state = PlayerState.IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		playing.getStatusBar().resetPower();

		toSpawn();
		initHitbox(PlayerCts.HITBOX_WIDTH, PlayerCts.HITBOX_HEIGHT);
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

	public float getMaxHealth() {
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
			return 4; // TODO
		case FALLING:
			return 4;
		case DUCK:
			return 4;
		default:
			return 1;
		}
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(PlayerCts.ATLAS_SIZE_X, PlayerCts.ATLAS_SIZE_Y, PlayerCts.SPRITE_WIDTH,
				PlayerCts.SPRITE_HEIGHT, LoadSave.GetAtlas(PlayerCts.ATLAS_IMAGE));
	}

	@Override
	public void scale() {
		super.scale();
		initPlayer();
	}
}