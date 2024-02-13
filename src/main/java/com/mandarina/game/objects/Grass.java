package com.mandarina.game.objects;

import com.mandarina.game.constants.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Grass {

	private int x, y, type;

	public Grass(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
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
