package com.mandarina.game.leveldata;

import com.mandarina.utilz.LoadSave;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import javafx.scene.image.Image;

public class Tile {

	private int x, y, aniIndex;

	public Tile(int x, int y, int aniIndex) {
		this.x = x;
		this.y = y;
		this.aniIndex = aniIndex;
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY, Image[] imgs) {
		g.drawImage(imgs[aniIndex], x - lvlOffsetX, y - lvlOffsetY, GameCts.TILES_SIZE, GameCts.TILES_SIZE);
	}

	public static Image[] load() {
		return LoadSave.GetAnimations(46, 32, 32, LoadSave.GetAtlas(LoadSave.OUTSIDE));
	}
}