package com.mandarina.game.objects;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.Point;

import javafx.scene.paint.Color;

public class GameObject {

	private Point spawn;
	protected float x, y;
	protected int objType;
	protected Box hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;

	public GameObject(Point spawn, int objType) {
		this.spawn = spawn;
		this.objType = objType;
		init();
	}

	private void init() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == ObjectCts.BARREL || objType == ObjectCts.BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == ObjectCts.CANNON_LEFT || objType == ObjectCts.CANNON_RIGHT)
					doAnimation = false;
			}
		}
	}

	private static int GetSpriteAmount(int object_type) {
		switch (object_type) {
		case ObjectCts.RED_POTION, ObjectCts.BLUE_POTION:
			return 7;
		case ObjectCts.BARREL, ObjectCts.BOX:
			return 8;
		case ObjectCts.CANNON_LEFT, ObjectCts.CANNON_RIGHT:
			return 7;
		}
		return 1;
	}

	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;

		if (objType == ObjectCts.BARREL || objType == ObjectCts.BOX || objType == ObjectCts.CANNON_LEFT
				|| objType == ObjectCts.CANNON_RIGHT)
			doAnimation = false;
		else
			doAnimation = true;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Box(x, y, AppStage.Scale(width), AppStage.Scale(height));
	}

	public void drawHitbox(GameDrawer g, Offset offset) {
		g.setStroke(Color.PINK);
		g.strokeRect(hitbox.getMinX() - offset.getX(), hitbox.getMinY() - offset.getY(), hitbox.getWidth(),
				hitbox.getHeight());
	}

	public int getObjType() {
		return objType;
	}

	public Point getSpawn() {
		return spawn;
	}

	public Box getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getAniTick() {
		return aniTick;
	}

	public void scale() {
		init();
	}

}
