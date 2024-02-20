package com.mandarina.game.gamestates;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.ui.AudioOptions;
import com.mandarina.game.ui.PauseButton;
import com.mandarina.game.ui.URMButtonCts;
import com.mandarina.game.ui.UrmButton;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GameOptions {

	private AudioOptions audioOptions;
	private Image backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuB;

	public GameOptions(AudioOptions audioOptions) {
		loadImgs();
		loadButton();
		this.audioOptions = audioOptions;
	}

	private void loadButton() {
		int menuX = (int) (387 * GameCts.SCALE);
		int menuY = (int) (325 * GameCts.SCALE);

		menuB = new UrmButton(menuX, menuY, URMButtonCts.URM_SIZE, URMButtonCts.URM_SIZE, 2);
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
		optionsBackgroundImg = LoadSave.GetSprite(LoadSave.OPTIONS_MENU);

		bgW = (int) (optionsBackgroundImg.getWidth() * GameCts.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * GameCts.SCALE);
		bgX = GameCts.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * GameCts.SCALE);
	}

	public void update() {
		menuB.update();
		audioOptions.update();
	}

	public void draw(GameDrawer g) {
		g.drawImage(backgroundImg, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH);

		menuB.draw(g);
		audioOptions.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else
			audioOptions.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				GameState.setState(GameState.MENU);
		} else
			audioOptions.mouseReleased(e);
		menuB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}