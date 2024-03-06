package com.mandarina.game.entities;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;

import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Entity {

	private Point2D spawn;
	protected double x, y;
	protected int width, height;

	protected int aniTick, aniIndex;

	protected int maxHealth;
	protected int currentHealth;

	protected double walkSpeed;
	protected int walkDir;

	protected double airSpeed;
	protected boolean inAir;

	protected Rectangle2D hitbox;

	protected Rectangle2D attackBox;
	protected boolean attackChecked;
	protected int attackBoxOffsetX;

	protected int pushBackDir;
	protected double pushDrawOffset;
	protected int pushBackOffsetDir;

	public Entity(Point2D spawn) {
		this.spawn = spawn;
		this.inAir = true;
		this.walkDir = DirectionCts.LEFT;
		this.pushBackOffsetDir = DirectionCts.UP;
		toSpawn();
	}

	public void toSpawn() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	protected void initSize(int width, int height) {
		this.width = AppStage.Scale(width);
		this.height = AppStage.Scale(height);
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D(x, y, AppStage.Scale(width), AppStage.Scale(height));
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackBox = new Rectangle2D(x, y, AppStage.Scale(w), AppStage.Scale(h));
		this.attackBoxOffsetX = AppStage.Scale(attackBoxOffsetX);
	}

	protected void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[][] animations, int row, int spriteW,
			int spriteH, int offsetX, int offsetY) {
		g.drawImage(animations[row][getAniIndex()], hitbox.getMinX() - lvlOffsetX - offsetX + flipX(),
				hitbox.getMinY() - lvlOffsetY - offsetY + pushDrawOffset, spriteW * flipW(), spriteH);

		// For Debug
		drawHitbox(g, lvlOffsetX, lvlOffsetY);
		drawAttackBox(g, lvlOffsetX, lvlOffsetY);
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
		double speed = 0.95f;
		double limit = -30f;

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

	protected void pushBack(int pushBackDir, LevelData levelData, double speedMulti) {
		double xSpeed = 0;
		if (pushBackDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * speedMulti, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				levelData))
			hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * speedMulti, hitbox.getMinY(), hitbox.getWidth(),
					hitbox.getHeight());
	}

	protected void drawAttackBox(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		g.setStroke(Color.RED);
		g.strokeRect(attackBox.getMinX() - lvlOffsetX, attackBox.getMinY() - lvlOffsetY, attackBox.getWidth(),
				attackBox.getHeight());
	}

	protected void drawHitbox(GameDrawer g, double lvlOffsetX, double lvlOffsetY) {
		g.setStroke(Color.PINK);
		g.strokeRect(hitbox.getMinX() - lvlOffsetX, hitbox.getMinY() - lvlOffsetY, hitbox.getWidth(),
				hitbox.getHeight());
	}

	public Point2D getSpawn() {
		return spawn;
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

	public void scale() {
		this.x = hitbox.getMinX() / AppStage.GetPreviusTileSize() * AppStage.GetTileSize();
		this.y = hitbox.getMinY() / AppStage.GetPreviusTileSize() * AppStage.GetTileSize();
	}
}