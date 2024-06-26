package com.mandarina.game.leveldata;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.geometry.Point;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Tile extends GameData {

	private int aniIndex;

	public Tile(Point spawn, int aniIndex) {
		super(spawn);
		this.aniIndex = aniIndex;
	}

	public void draw(GameDrawer g, Offset offset, Image[] imgs) {
		g.drawImage(imgs[aniIndex], x - offset.getX(), y - offset.getY(), AppStage.GetTileSize(),
				AppStage.GetTileSize());

//		g.setStroke(Color.PINK);
//		g.strokeRect(x - lvlOffsetX, y - lvlOffsetY, AppStage.GetTileSize(), AppStage.GetTileSize());
	}

	public static Image[] load() {
		return LoadSave.GetAnimations(46, 32, 32, LoadSave.GetAtlas(Catalog.OUTSIDE));
	}
}
