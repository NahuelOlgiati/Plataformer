package com.mandarina.objects;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.ObjectCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Spike extends GameObject {

	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = (int) (GameCts.SCALE * 16);
		hitbox = new Rectangle2D(x + xDrawOffset, y + yDrawOffset, 32, 16);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image image) {
		g.drawImage(image, (int) (getHitbox().getMinX() - lvlOffsetX),
				(int) (getHitbox().getMinY() - getyDrawOffset() - lvlOffsetY), ObjectCts.SPIKE_WIDTH,
				ObjectCts.SPIKE_HEIGHT);
	}
	
	public static Image load() {
		return LoadSave.GetAtlas(LoadSave.TRAP);
	}
}
