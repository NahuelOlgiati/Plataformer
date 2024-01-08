package com.mandarina.entities.player;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.GetEntityXPosNextToWall;
import static com.mandarina.utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static com.mandarina.utilz.HelpMethods.IsEntityInWater;
import static com.mandarina.utilz.HelpMethods.IsEntityOnFloor;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.audio.AudioPlayer;
import com.mandarina.constants.DirectionCts;
import com.mandarina.constants.GameCts;
import com.mandarina.entities.Enemy;
import com.mandarina.entities.Entity;
import com.mandarina.gamestates.Playing;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Entity {

	private Image[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;

	// Jumping / Gravity
	private float jumpSpeed = -2.25f * GameCts.SCALE;
	private float fallSpeedAfterCollision = 0.5f * GameCts.SCALE;

	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;
	private Playing playing;
	private PlayerState state;

	private int tileY = 0;

	private boolean powerAttackActive;
	private int powerAttackTick;

	public Player(float x, float y, Playing playing) {
		super(x, y, PlayerSprite.WIDTH.scaled(), PlayerSprite.HEIGHT.scaled());
		this.playing = playing;
		this.state = PlayerState.IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = GameCts.SCALE * 1.0f;
		loadAnimations();
		initHitbox(PlayerHitbox.width(), PlayerHitbox.height());
		initAttackBox(PlayerAttackHitbox.width(), PlayerAttackHitbox.height());
	}

	public void setSpawn(Point2D spawn) {
		this.x = (float) spawn.getX();
		this.y = (float) spawn.getY();
		hitbox = new Rectangle2D(x, y, hitbox.getWidth(), hitbox.getHeight());
	}

	private void initAttackBox(int width, int height) {
		attackBox = new Rectangle2D(x, y, (int) (width * GameCts.SCALE), (int) (height * GameCts.SCALE));
		resetAttackBox();
	}

	public void update() {
		if (currentHealth <= 0) {
			if (!PlayerState.DEAD.equals(state)) {
				state = PlayerState.DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

				// Check if player died in air
				if (!IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;
					airSpeed = 0;
				}
			} else if (aniIndex == PlayerAtlas.GetSpriteAmount(PlayerState.DEAD) - 1
					&& aniTick >= GameCts.ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else {
				updateAnimationTick();

				// Fall if in air
				if (inAir) {
					if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(),
							hitbox.getHeight(), lvlData)) {
						hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(),
								hitbox.getHeight());
						airSpeed += GameCts.GRAVITY;
					} else {
						inAir = false;
					}
				}

			}

			return;
		}

		updateAttackBox();

		if (PlayerState.HIT.equals(state)) {
			if (aniIndex <= PlayerAtlas.GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, lvlData, 1.25f);
			updatePushBackDrawOffset();
		} else
			updatePos();

		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.getMinY() / GameCts.TILES_SIZE);
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

	private void setAttackBoxOnRightSide() {
		attackBox = new Rectangle2D(hitbox.getMinX() + hitbox.getWidth() - (int) (GameCts.SCALE * 5), hitbox.getMinY(),
				attackBox.getWidth(), attackBox.getHeight());
	}

	private void setAttackBoxOnLeftSide() {
		attackBox = new Rectangle2D(hitbox.getMinX() - hitbox.getWidth() - (int) (GameCts.SCALE * 10), hitbox.getMinY(),
				attackBox.getWidth(), attackBox.getHeight());
	}

	private void updateAttackBox() {
		if (right && left) {
			if (flipW == 1) {
				setAttackBoxOnRightSide();
			} else {
				setAttackBoxOnLeftSide();
			}

		} else if (right || (powerAttackActive && flipW == 1))
			setAttackBoxOnRightSide();
		else if (left || (powerAttackActive && flipW == -1))
			setAttackBoxOnLeftSide();

		attackBox = new Rectangle2D(attackBox.getMinX(), hitbox.getMinY() + (GameCts.SCALE * 10), attackBox.getWidth(),
				attackBox.getHeight());
	}

	public void render(GraphicsContext g, int lvlOffset) {
		g.drawImage(animations[state.val()][aniIndex],
				(int) (hitbox.getMinX() - PlayerSprite.DRAWOFFSET_X.scaled()) - lvlOffset + flipX,
				(int) (hitbox.getMinY() - PlayerSprite.DRAWOFFSET_Y.scaled() + (int) pushDrawOffset), width * flipW,
				height);
//		drawHitbox(g, lvlOffset);
//		drawAttackBox(g, lvlOffset);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= PlayerAtlas.GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				if (PlayerState.HIT.equals(state)) {
					newState(PlayerState.IDLE);
					airSpeed = 0f;
					if (!IsFloor(hitbox, 0, lvlData))
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
		if (!state.equals(startAni))
			resetAniTick();
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

		float xSpeed = 0;

		if (left && !right) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right && !left) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (powerAttackActive) {
			if ((!left && !right) || (left && right)) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			xSpeed *= 3;
		}

		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir && !powerAttackActive) {
			if (CanMoveHere(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(), hitbox.getHeight(),
					lvlData)) {
				hitbox = new Rectangle2D(hitbox.getMinX(), hitbox.getMinY() + airSpeed, hitbox.getWidth(),
						hitbox.getHeight());
				airSpeed += GameCts.GRAVITY;
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

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.getMinX() + xSpeed, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), lvlData)) {
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

	private void loadAnimations() {
		animations = PlayerAtlas.getAnimations();
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
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
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = PlayerState.IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		playing.getStatusBar().resetPower();

		hitbox = new Rectangle2D(x, y, hitbox.getWidth(), hitbox.getHeight());
		resetAttackBox();

		if (!IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	private void resetAttackBox() {
		if (flipW == 1)
			setAttackBoxOnRightSide();
		else
			setAttackBoxOnLeftSide();
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

	public float getMaxHealth() {
		return maxHealth;
	}
}