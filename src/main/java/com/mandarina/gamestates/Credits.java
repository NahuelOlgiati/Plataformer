package com.mandarina.gamestates;

import java.util.ArrayList;

import com.mandarina.constants.GameCts;
import com.mandarina.entities.crabby.CrabbyAtlas;
import com.mandarina.entities.pinkstar.PinkstarAtlas;
import com.mandarina.entities.shark.SharkAtlas;
import com.mandarina.entities.titan.TitanAtlas;
import com.mandarina.main.Game;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Credits extends State implements Statemethods {
	private Image backgroundImg, creditsImg;
	private int bgX, bgY, bgW, bgH;
	private float bgYfloat;

	private ArrayList<ShowEntity> entitiesList;

	public Credits(Game game) {
		super(game);
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
		entitiesList.add(new ShowEntity(TitanAtlas.getAnimations()[1], (int) (GameCts.GAME_WIDTH * 0.05),
				(int) (GameCts.GAME_HEIGHT * 0.8)));
		entitiesList.add(new ShowEntity(CrabbyAtlas.getAnimations()[1], (int) (GameCts.GAME_WIDTH * 0.15),
				(int) (GameCts.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(SharkAtlas.getAnimations()[3], (int) (GameCts.GAME_WIDTH * 0.7),
				(int) (GameCts.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(PinkstarAtlas.getAnimations()[3], (int) (GameCts.GAME_WIDTH * 0.8),
				(int) (GameCts.GAME_HEIGHT * 0.8)));
	}

	@Override
	public void update() {
		bgYfloat -= 0.2f;
		for (ShowEntity se : entitiesList)
			se.update();
	}

	@Override
	public void draw(GraphicsContext g) {
		g.drawImage(backgroundImg, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		g.drawImage(creditsImg, bgX, (int) (bgY + bgYfloat), bgW, bgH);

		for (ShowEntity se : entitiesList)
			se.draw(g);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getCode() == KeyCode.ESCAPE) {
			bgYfloat = 0;
			setGamestate(Gamestate.MENU);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
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
