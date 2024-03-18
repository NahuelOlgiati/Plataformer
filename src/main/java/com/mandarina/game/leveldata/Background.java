package com.mandarina.game.leveldata;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.ImageUtil;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Background {

	Image backgroundSprite;

	public Background() {
		this.backgroundSprite = LoadSave.GetSprite(LoadSave.PLAYING_BG_IMG);
		this.backgroundSprite = ImageUtil.darkerBackground(backgroundSprite, 1);
	}

	public void draw(GameDrawer g, Offset offset) {
		g.drawImage(backgroundSprite, 0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
	}

}
