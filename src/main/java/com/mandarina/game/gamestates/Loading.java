package com.mandarina.game.gamestates;

import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Loading {

	private Image backgroundImg;
	private double x, y, w, h;

	public Loading() {
		loadBackground();
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
//		w = AppStage.Scale(backgroundImg.getWidth());
//		h = AppStage.Scale(backgroundImg.getHeight());
//		x = AppStage.GetGameWidth() / 2 - w / 2;
//		y = AppStage.Scale(25);
	}

	public void draw(GameDrawer g) {
		System.out.println("HOLA");
		g.drawImage(backgroundImg, 0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
//		g.drawImage(backgroundImg, x, y, w, h);
	}

	public void scale() {
//		w = AppStage.Scale(backgroundImg.getWidth());
//		h = AppStage.Scale(backgroundImg.getHeight());
//		x = AppStage.GetGameWidth() / 2 - w / 2;
//		y = AppStage.Scale(25);
	}
}
