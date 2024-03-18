package com.mandarina.game.objects;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Spike extends GameObject {

	public Spike(Point2D spawn, int objType) {
		super(spawn, objType);
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = AppStage.Scale(16);
		hitbox = new Rectangle2D(x + xDrawOffset, y + yDrawOffset, 32, 16);
	}

	public void draw(GameDrawer g, Offset offset, Image image) {
		g.drawImage(image, (int) (getHitbox().getMinX() - offset.getX()),
				(int) (getHitbox().getMinY() - getyDrawOffset() - offset.getY()),
				AppStage.Scale(ObjectCts.SPIKE_WIDTH_DEFAULT), AppStage.Scale(ObjectCts.SPIKE_HEIGHT_DEFAULT));
	}

	public static Image load() {
		return LoadSave.GetAtlas(LoadSave.TRAP);
	}

	@Override
	public void scale() {
		super.scale();
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = AppStage.Scale(16);
		hitbox = new Rectangle2D(x + xDrawOffset, y + yDrawOffset, 32, 16);
	}
}
