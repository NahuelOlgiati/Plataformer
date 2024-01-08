package com.mandarina.entities;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;

import com.mandarina.constants.DirectionCts;
import com.mandarina.constants.GameCts;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D hitbox;
	protected int aniTick, aniIndex;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D attackBox;
	protected float walkSpeed;
	protected int walkDir = DirectionCts.LEFT;

	protected int pushBackDir;
	protected float pushDrawOffset;
	protected int pushBackOffsetDir = DirectionCts.UP;

	protected boolean attackChecked;
	protected int attackBoxOffsetX;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void draw(GraphicsContext g, int xLvlOffset, Image[][] animations, int row, int spriteW, int spriteH,
			int offsetX, int offsetY) {
		g.drawImage(animations[row][getAniIndex()], (int) (hitbox.getMinX() - xLvlOffset - offsetX + flipX()),
				(int) hitbox.getMinY() - offsetY + (int) pushDrawOffset, spriteW * flipW(), spriteH);

		// For Debug
		drawHitbox(g, xLvlOffset);
		drawAttackBox(g, xLvlOffset);
	}

	protected void updateAttackBox() {
		attackBox = new Rectangle2D(hitbox.getMinX() - attackBoxOffsetX, hitbox.getMinY(), attackBox.getWidth(),
				attackBox.getHeight());
	}

	protected void updateAttackBoxFlip() {
		if (walkDir == DirectionCts.RIGHT) {
			attackBox = new Rectangle2D(hitbox.getMinX() + hitbox.getWidth(), hitbox.getMinY(), attackBox.getWidth(),
					attackBox.getHeight());
		} else {
			attackBox = new Rectangle2D(hitbox.getMinX() - attackBoxOffsetX, hitbox.getMinY(), attackBox.getWidth(),
					attackBox.getHeight());
		}
	}

	protected void changeWalkDir() {
		if (walkDir == DirectionCts.LEFT)
			changeWalkDir(DirectionCts.RIGHT);
		else
			changeWalkDir(DirectionCts.LEFT);
	}

	protected void changeWalkDir(int direction) {
		walkDir = direction;
	}

	public int flipX() {
		if (walkDir == DirectionCts.RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == DirectionCts.RIGHT)
			return -1;
		else
			return 1;
	}

	protected void updatePushBackDrawOffset() {
		float speed = 0.95f;
		float limit = -30f;

		if (pushBackOffsetDir == DirectionCts.UP) {
			pushDrawOffset -= speed;
			if (pushDrawOffset <= limit)
				pushBackOffsetDir = DirectionCts.DOWN;
		} else {
			pushDrawOffset += speed;
			if (pushDrawOffset >= 0)
				pushDrawOffset = 0;
		}
	}

	protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * speedMulti, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				lvlData))
			hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * speedMulti, hitbox.getMinY(), hitbox.getWidth(),
					hitbox.getHeight());
	}

	protected void drawAttackBox(GraphicsContext g, int xLvlOffset) {
		g.setStroke(Color.RED);
		g.strokeRect(attackBox.getMinX() - xLvlOffset, attackBox.getMinY(), attackBox.getWidth(),
				attackBox.getHeight());
	}

	protected void drawHitbox(GraphicsContext g, int xLvlOffset) {
		g.setStroke(Color.PINK);
		g.strokeRect((int) (hitbox.getMinX() - xLvlOffset), (int) hitbox.getMinY(), (int) hitbox.getWidth(),
				(int) hitbox.getHeight());
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D(x, y, (int) (width * GameCts.SCALE), (int) (height * GameCts.SCALE));
	}

	public Rectangle2D getHitbox() {
		return hitbox;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}
}