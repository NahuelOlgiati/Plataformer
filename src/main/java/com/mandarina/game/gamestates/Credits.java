package com.mandarina.game.gamestates;

import java.util.ArrayList;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.entities.crabby.CrabbyAtlas;
import com.mandarina.game.entities.pinkstar.PinkstarAtlas;
import com.mandarina.game.entities.shark.SharkAtlas;
import com.mandarina.game.entities.titan.TitanAtlas;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Credits {
	private Image backgroundImg, creditsImg;
	private int bgX, bgY, bgW, bgH;
	private float bgYfloat;

	private ArrayList<ShowEntity> entitiesList;

	public Credits() {
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
		creditsImg = LoadSave.GetSprite(LoadSave.CREDITS);
		bgW = (int) (creditsImg.getWidth() * GameCts.SCALE);
		bgH = (int) (creditsImg.getHeight() * GameCts.SCALE);
		bgX = GameCts.GAME_WIDTH / 2 - bgW / 2;
		bgY = GameCts.GAME_HEIGHT;
		loadEntities();
	}

	private void loadEntities() {
		entitiesList = new ArrayList<>();
		entitiesList.add(new ShowEntity(TitanAtlas.getAnimations()[2], (int) (GameCts.GAME_WIDTH * 0.05),
				(int) (GameCts.GAME_HEIGHT * 0.45)));
		entitiesList.add(new ShowEntity(CrabbyAtlas.getAnimations()[1], (int) (GameCts.GAME_WIDTH * 0.2),
				(int) (GameCts.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(SharkAtlas.getAnimations()[3], (int) (GameCts.GAME_WIDTH * 0.7),
				(int) (GameCts.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(PinkstarAtlas.getAnimations()[3], (int) (GameCts.GAME_WIDTH * 0.8),
				(int) (GameCts.GAME_HEIGHT * 0.8)));
	}

	public void update() {
		bgYfloat -= 0.2f;
		for (ShowEntity se : entitiesList)
			se.update();
	}

	public void draw(GraphicsContext g) {
		g.drawImage(backgroundImg, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		g.drawImage(creditsImg, bgX, (int) (bgY + bgYfloat), bgW, bgH);

		for (ShowEntity se : entitiesList)
			se.draw(g);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getCode() == KeyCode.ESCAPE) {
			bgYfloat = 0;
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

		public void draw(GraphicsContext g) {
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

}
