package com.mandarina.game.objects;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.constants.ObjectCts;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= ObjectCts.GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == ObjectCts.BARREL || objType == ObjectCts.BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == ObjectCts.CANNON_LEFT || objType == ObjectCts.CANNON_RIGHT)
					doAnimation = false;
			}
		}
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
		hitbox = new Rectangle2D(x, y, (int) (width * GameCts.SCALE), (int) (height * GameCts.SCALE));
	}

	public void drawHitbox(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		g.setStroke(Color.PINK);
		g.strokeRect(hitbox.getMinX() - lvlOffsetX, hitbox.getMinY() - lvlOffsetY, hitbox.getWidth(),
				hitbox.getHeight());
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D getHitbox() {
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

}
