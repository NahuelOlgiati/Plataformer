package com.mandarina.game.leveldata;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Grass extends GameData {

	private int type;

	public Grass(Point2D spawn, int type) {
		super(spawn);
		this.type = type;
	}

	@Override
	protected void init() {
		this.x = spawn.getX() * AppStage.GetTileSize();
		this.y = spawn.getY() * AppStage.GetTileSize() - AppStage.GetTileSize();
	}

	public void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[] grassImgs) {
		g.drawImage(grassImgs[getType()], x - lvlOffsetX, y - lvlOffsetY, AppStage.GetTileSize(),
				AppStage.GetTileSize());
	}

	public int getType() {
		return type;
	}

	public static Image[] load() {
		Image grassTemp = LoadSave.GetAtlas(LoadSave.GRASS);
		Image[] grassImgs = new Image[2];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = LoadSave.GetSubimage(grassTemp, i, 0, GameCts.TILES_DEFAULT_SIZE,
					GameCts.TILES_DEFAULT_SIZE);

		return grassImgs;
	}
}
