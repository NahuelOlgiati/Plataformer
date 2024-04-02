package com.mandarina.game.leveldata;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;
import com.mandarina.utilz.HorizontalLine;
import com.mandarina.utilz.LoadSave;
import com.mandarina.utilz.Point;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Slide extends GameData {

	private static final float SPEED = 0.4f;
	private float hitboxWidth;
	private HorizontalLine hitline;
	private int type;

	private float min;
	private float max;

	private boolean horizontal;
	private boolean increasing;

	private float height;

	public Slide(Point spawn, int type) {
		super(spawn);
		this.type = type;
		initSlide();
	}

	private void initSlide() {
		switch (this.type) {
		case 50: {
			this.min = this.x;
			this.max = this.x + 2 * AppStage.GetTileSize();
			this.horizontal = true;
			this.increasing = true;
			break;
		}
		case 51: {
			this.min = this.x - 2 * AppStage.GetTileSize();
			this.max = this.x;
			this.horizontal = true;
			this.increasing = false;
			break;
		}
		case 52: {
			this.min = this.y - 2 * AppStage.GetTileSize();
			this.max = this.y;
			this.horizontal = false;
			this.increasing = false;
			break;
		}
		case 53: {
			this.min = this.y;
			this.max = this.y + 2 * AppStage.GetTileSize();
			this.horizontal = false;
			this.increasing = false;
			break;
		}
		}
		this.height = AppStage.Scale(6);
		initHitline(GameCts.TILES_DEFAULT_SIZE);
	}

	@Override
	protected void init() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize();
	}

	protected void initHitline(int width) {
		this.hitboxWidth = AppStage.Scale(width);
		this.hitline = new HorizontalLine(x, y, hitboxWidth);
	}

	public void draw(GameDrawer g, Offset offset, Image grassImgs) {
		g.drawImage(grassImgs, hitline.getMinX() - offset.getX(), hitline.getY() - offset.getY(),
				AppStage.GetTileSize(), this.height);
		drawHitbox(g, offset);
	}

	protected void drawHitbox(GameDrawer g, Offset offset) {
		g.setStroke(Color.PINK);
		g.strokeRect(hitline.getMinX() - offset.getX(), hitline.getY() - offset.getY(), hitline.getWidth(), height);
	}

	public void update() {
		if (this.horizontal) {
			float minX = hitline.getMinX();
			if (increasing) {
				if (minX < max) {
					hitline.setMinX(minX + SPEED);
				} else {
					increasing = false;
				}
			} else {
				if (minX > min) {
					hitline.setMinX(minX - SPEED);
				} else {
					increasing = true;
				}
			}
		} else {
			float y = hitline.getY();
			if (increasing) {
				if (y < max) {
					hitline.setY(y + SPEED);
				} else {
					increasing = false;
				}
			} else {
				if (y > min) {
					hitline.setY(y - SPEED);
				} else {
					increasing = true;
				}
			}
		}
	}

	public float getSpeed() {
		return increasing ? SPEED : -SPEED;
	}

	public int getType() {
		return type;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public HorizontalLine getHitline() {
		return hitline;
	}

	public Slide getSlide(Box hitbox) {
		if (hitbox.collideFromBelow(hitline)) {
			return this;
		} else {
			return null;
		}
	}

	public static Image load() {
		return LoadSave.GetAtlas(LoadSave.SLIDE);
	}

	@Override
	public void scale() {
		super.scale();
		initSlide();
	}
}
