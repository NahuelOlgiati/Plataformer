package com.mandarina.game.leveldata;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Water extends GameData {

	private int type, aniIndex, aniTick;

	public Water(Point2D spawn, int type) {
		super(spawn);
		this.type = type;
	}

	public void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[] imgs) {
		if (type == 48)
			g.drawImage(imgs[aniIndex], x - lvlOffsetX, y - lvlOffsetY, AppStage.GetTileSize(), AppStage.GetTileSize());
		else if (type == 49)
			g.drawImage(imgs[4], x - lvlOffsetX, y - lvlOffsetY, AppStage.GetTileSize(), AppStage.GetTileSize());
	}

	public void update() {
		if (type == 48) {
			aniTick++;
			if (aniTick >= 40) {
				aniTick = 0;
				aniIndex++;

				if (aniIndex >= 4)
					aniIndex = 0;
			}
		}
	}

	public static Image[] load() {
		Image[] waterSprite = new Image[5];
		Image img = LoadSave.GetAtlas(LoadSave.WATER);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = LoadSave.GetSubimage(img, i, 0, GameCts.TILES_DEFAULT_SIZE, GameCts.TILES_DEFAULT_SIZE);
		waterSprite[4] = LoadSave.GetSprite(LoadSave.WATER_BOTTOM);
		return waterSprite;
	}
}
