package com.mandarina.game.gamestates;

import java.util.ArrayList;
import java.util.List;

import com.mandarina.game.entities.Crabby;
import com.mandarina.game.entities.Pinkstar;
import com.mandarina.game.entities.Shark;
import com.mandarina.game.entities.Titan;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Credits {
	private Image backgroundImg, creditsImg;
	private double bgX, bgY, bgW, bgH;
	private double bgYdouble;

	private List<ShowEntity> entitiesList;

	public Credits() {
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
		creditsImg = LoadSave.GetSprite(LoadSave.CREDITS);
		bgW = AppStage.Scale(creditsImg.getWidth());
		bgH = AppStage.Scale(creditsImg.getHeight());
		bgX = AppStage.GetGameWidth() / 2 - bgW / 2;
		bgY = AppStage.GetGameHeight();
		loadEntities();
	}

	private void loadEntities() {
		entitiesList = new ArrayList<ShowEntity>();
		entitiesList.add(new ShowEntity(Titan.load()[2], (int) (AppStage.GetGameWidth() * 0.05),
				(int) (AppStage.GetGameHeight() * 0.45)));
		entitiesList.add(new ShowEntity(Crabby.load()[1], (int) (AppStage.GetGameWidth() * 0.2),
				(int) (AppStage.GetGameHeight() * 0.75)));
		entitiesList.add(new ShowEntity(Shark.load()[3], (int) (AppStage.GetGameWidth() * 0.7),
				(int) (AppStage.GetGameHeight() * 0.75)));
		entitiesList.add(new ShowEntity(Pinkstar.load()[3], (int) (AppStage.GetGameWidth() * 0.8),
				(int) (AppStage.GetGameHeight() * 0.8)));
	}

	public void update() {
		bgYdouble -= 0.2f;
		for (ShowEntity se : entitiesList)
			se.update();
	}

	public void draw(GameDrawer g) {
		g.drawImage(backgroundImg, 0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
		g.drawImage(creditsImg, bgX, bgY + bgYdouble, bgW, bgH);

		for (ShowEntity se : entitiesList)
			se.draw(g);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getCode() == KeyCode.ESCAPE) {
			bgYdouble = 0;
			GameState.setState(GameState.MENU);
		}
	}

	private class ShowEntity {
		private Image[] idleAnimation;
		private int x, y, aniIndex, aniTick;

		public ShowEntity(Image[] idleAnimation, int x, int y) {
			this.idleAnimation = idleAnimation;
			this.x = x;
			this.y = y;
		}

		public void draw(GameDrawer g) {
			g.drawImage(idleAnimation[aniIndex], x, y, (int) (idleAnimation[aniIndex].getWidth() * 4),
					(int) (idleAnimation[aniIndex].getHeight() * 4));
		}

		public void update() {
			aniTick++;
			if (aniTick >= 25) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= idleAnimation.length)
					aniIndex = 0;
			}

		}
	}

	public void scale() {
		bgW = AppStage.Scale(creditsImg.getWidth());
		bgH = AppStage.Scale(creditsImg.getHeight());
		bgX = AppStage.GetGameWidth() / 2 - bgW / 2;
		bgY = AppStage.GetGameHeight();
		loadEntities();
	}

}
