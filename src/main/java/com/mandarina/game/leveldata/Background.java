package com.mandarina.game.leveldata;

import com.mandarina.game.constants.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {

	Image backgroundSprite;

	public Background() {
		this.backgroundSprite = LoadSave.GetSprite(LoadSave.PLAYING_BG_IMG);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		g.drawImage(backgroundSprite, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
	}

}
