package com.mandarina.game.gamestates;

import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.ui.PauseButton;
import com.mandarina.game.ui.URMButtonCts;
import com.mandarina.game.ui.UrmButton;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GameOptions {

	private Image backgroundImg, optionsBackgroundImg;
	private float bgX, bgY, bgW, bgH;
	private UrmButton menuB;

	private Game game;

	public GameOptions(Game game) {
		this.game = game;
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
		optionsBackgroundImg = LoadSave.GetSprite(LoadSave.OPTIONS_MENU);
		loadImgs();
		loadButton();
	}

	private void loadButton() {
		int menuX = AppStage.Scale(387);
		int menuY = AppStage.Scale(325);

		menuB = new UrmButton(menuX, menuY, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 2);
	}

	private void loadImgs() {
		bgW = AppStage.Scale(optionsBackgroundImg.getWidth());
		bgH = AppStage.Scale(optionsBackgroundImg.getHeight());
		bgX = AppStage.GetGameWidth() / 2 - bgW / 2;
		bgY = AppStage.Scale(33);
	}

	public void update() {
		menuB.update();
		this.game.getAudioOptions().update();
	}

	public void draw(GameDrawer g) {
		g.drawImage(backgroundImg, 0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH);

		menuB.draw(g);
		this.game.getAudioOptions().draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		this.game.getAudioOptions().mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else
			this.game.getAudioOptions().mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				GameState.setState(GameState.MENU);
		} else
			this.game.getAudioOptions().mouseReleased(e);
		menuB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else
			this.game.getAudioOptions().mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void scale() {
		loadButton();
		loadImgs();
	}

}