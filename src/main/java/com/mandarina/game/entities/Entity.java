package com.mandarina.game.entities;

import static com.mandarina.utilz.SmallerThanTile.CanMoveHere;

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
	protected double drawWidth, drawHeight, drawOffsetX, drawOffsetY;

	protected int aniTick, aniIndex;

	protected int maxHealth;
	protected int currentHealth;

	protected double walkSpeed;
	protected int walkDir;

	protected double airSpeed;
	protected boolean inAir;

	protected int tileY;

	protected Rectangle2D hitbox;

	protected Rectangle2D attackBox;
	protected boolean attackChecked;
	protected int attackBoxOffsetX;
	protected int attackBoxOffsetY;

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

	protected void initDraw(int width, int height, int offsetX, int offsetY) {
		this.drawWidth = AppStage.Scale(width);
		this.drawHeight = AppStage.Scale(height);
		this.drawOffsetX = AppStage.Scale(offsetX);
		this.drawOffsetY = AppStage.Scale(offsetY);
	}

	protected void initHitbox(int width, int height) {
		int heightScaled = AppStage.Scale(height);
		int widthScale = AppStage.Scale(width);
		hitbox = new Rectangle2D(x, y - heightScaled, widthScale, heightScaled);
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX, int attackBoxOffsetY) {
		attackBox = new Rectangle2D(x, y, AppStage.Scale(w), AppStage.Scale(h));
		this.attackBoxOffsetX = AppStage.Scale(attackBoxOffsetX);
		this.attackBoxOffsetY = AppStage.Scale(attackBoxOffsetY);
	}

	protected void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[][] animations, int row) {
		g.drawImage(animations[row][getAniIndex()], hitbox.getMinX() - lvlOffsetX - drawOffsetX + flipX(),
				hitbox.getMinY() - lvlOffsetY - drawOffsetY + pushDrawOffset, drawWidth * flipW(), drawHeight);

		// For Debug
		drawHitbox(g, lvlOffsetX, lvlOffsetY);
		drawAttackBox(g, lvlOffsetX, lvlOffsetY);
	}

	protected void updateAttackBox() {
		attackBox = new Rectangle2D(hitbox.getMinX() - attackBoxOffsetX, hitbox.getMinY() - attackBoxOffsetY,
				attackBox.getWidth(), attackBox.getHeight());
	}

	protected void updateAttackBoxFlip() {
		if (walkDir == DirectionCts.RIGHT) {
			attackBox = new Rectangle2D(hitbox.getMinX() - attackBoxOffsetX * flipW(),
					hitbox.getMinY() - attackBoxOffsetY, attackBox.getWidth(), attackBox.getHeight());
		} else {
			attackBox = new Rectangle2D(
					hitbox.getMinX() + hitbox.getWidth() - attackBox.getWidth() - attackBoxOffsetX * flipW(),
					hitbox.getMinY() - attackBoxOffsetY, attackBox.getWidth(), attackBox.getHeight());
		}
	}

	protected void updateTileY() {
		tileY = AppStage.GetTilesIn(hitbox.getMaxY());
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

	public double flipX() {
		if (walkDir == DirectionCts.RIGHT)
			return drawWidth;
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

	public int getTileY() {
		return tileY;
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