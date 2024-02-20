package com.mandarina.game.objects;

import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import javafx.scene.image.Image;

public class Spike extends GameObject {

	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = (int) (GameCts.SCALE * 16);
		hitbox = new Rectangle2D(x + xDrawOffset, y + yDrawOffset, 32, 16);
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY, Image image) {
		g.drawImage(image, (int) (getHitbox().getMinX() - lvlOffsetX),
				(int) (getHitbox().getMinY() - getyDrawOffset() - lvlOffsetY), ObjectCts.SPIKE_WIDTH,
				ObjectCts.SPIKE_HEIGHT);
	}

	public static Image load() {
		return LoadSave.GetAtlas(LoadSave.TRAP);
	}
}
