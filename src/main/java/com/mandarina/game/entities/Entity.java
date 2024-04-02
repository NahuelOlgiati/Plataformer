package com.mandarina.game.entities;

import static com.mandarina.utilz.SmallerThanTile.CanMoveHere;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.geometry.Box;
import com.mandarina.game.geometry.Point;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Entity {

	private Point spawn;
	protected float x, y;
	protected float hitboxWidth, hitboxHeight;
	protected float drawWidth, drawHeight, drawOffsetX, drawOffsetY;

	protected int aniTick, aniIndex;

	protected int maxHealth;
	protected int currentHealth;

	protected float walkSpeed;
	protected int walkDir;

	protected float xSpeed;
	protected float ySpeed;
	protected boolean inAir;

	protected int tileY;

	protected Box hitbox;

	protected Box attackBox;
	protected boolean attackChecked;
	protected int attackBoxOffsetX;
	protected int attackBoxOffsetY;

	protected int pushBackDir;
	protected float pushDrawOffset;
	protected int pushBackOffsetDir;

	public Entity(Point spawn, int health) {
		this.spawn = spawn;
		this.maxHealth = health;
		this.currentHealth = maxHealth;
		this.inAir = true;
		this.walkDir = DirectionCts.LEFT;
		this.pushBackOffsetDir = DirectionCts.UP;
		toSpawn();
	}

	public void toSpawn() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	protected void initAttackWalkSpeed(float walkSpeed) {
		this.walkSpeed = AppStage.Scale(walkSpeed);
	}

	protected void initDraw(int width, int height, int offsetX, int offsetY) {
		this.drawWidth = AppStage.Scale(width);
		this.drawHeight = AppStage.Scale(height);
		this.drawOffsetX = AppStage.Scale(offsetX);
		this.drawOffsetY = AppStage.Scale(offsetY);
	}

	protected void initHitbox(int width, int height) {
		this.hitboxWidth = AppStage.Scale(width);
		this.hitboxHeight = AppStage.Scale(height);
		hitbox = new Box(x, y - hitboxHeight, hitboxWidth, hitboxHeight);
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX, int attackBoxOffsetY) {
		attackBox = new Box(x, y, AppStage.Scale(w), AppStage.Scale(h));
		this.attackBoxOffsetX = AppStage.Scale(attackBoxOffsetX);
		this.attackBoxOffsetY = AppStage.Scale(attackBoxOffsetY);
	}

	protected void draw(GameDrawer g, Offset offset, Image[][] animations, int row) {
		g.drawImage(animations[row][getAniIndex()], hitbox.getMinX() - offset.getX() - drawOffsetX + flipX(),
				hitbox.getMinY() - offset.getY() - drawOffsetY + pushDrawOffset, drawWidth * flipW(), drawHeight);

		// For Debug
		drawHitbox(g, offset);
		drawAttackBox(g, offset);
	}

	protected void updateAttackBox() {
		attackBox.setMinXY(hitbox.getMinX() - attackBoxOffsetX, hitbox.getMinY() - attackBoxOffsetY);
	}

	protected void updateAttackBoxFlip() {
		if (walkDir == DirectionCts.RIGHT) {
			attackBox.setMinXY(hitbox.getMinX() - attackBoxOffsetX * flipW(), hitbox.getMinY() - attackBoxOffsetY);
		} else {
			attackBox.setMinXY(hitbox.getMinX() + hitbox.getWidth() - attackBox.getWidth() - attackBoxOffsetX * flipW(),
					hitbox.getMinY() - attackBoxOffsetY);
		}
	}

	protected void updateTileY() {
		tileY = AppStage.GetTilesIn(hitbox.getMaxY());
	}

	protected void updateXSpeed() {
		xSpeed = 0;
		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
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

	public float flipX() {
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

	protected void pushBack(int pushBackDir, LevelData levelData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * speedMulti, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				levelData))
			hitbox.setMinX(hitbox.getMinX() + xSpeed * speedMulti);
	}

	protected void drawAttackBox(GameDrawer g, Offset offset) {
		g.setStroke(Color.RED);
		g.strokeRect(attackBox.getMinX() - offset.getX(), attackBox.getMinY() - offset.getY(), attackBox.getWidth(),
				attackBox.getHeight());
	}

	protected void drawHitbox(GameDrawer g, Offset offset) {
		g.setStroke(Color.PINK);
		g.strokeRect(hitbox.getMinX() - offset.getX(), hitbox.getMinY() - offset.getY(), hitbox.getWidth(),
				hitbox.getHeight());
	}

	public Point getSpawn() {
		return spawn;
	}

	public Box getHitbox() {
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