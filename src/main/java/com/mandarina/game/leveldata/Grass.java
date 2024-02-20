package com.mandarina.game.leveldata;

import com.mandarina.utilz.LoadSave;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import javafx.scene.image.Image;

public class Grass {

	private int x, y, type;

	public Grass(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY, Image[] grassImgs) {
		g.drawImage(grassImgs[getType()], getX() - lvlOffsetX, getY() - lvlOffsetY, (int) (32 * GameCts.SCALE),
				(int) (32 * GameCts.SCALE));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;

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
