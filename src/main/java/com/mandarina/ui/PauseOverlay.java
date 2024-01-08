package com.mandarina.ui;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.UICts;
import com.mandarina.gamestates.Gamestate;
import com.mandarina.gamestates.Playing;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class PauseOverlay {

	private Playing playing;
	private Image backgroundImg;
	private int bgX, bgY, bgW, bgH;
	private AudioOptions audioOptions;
	private UrmButton menuB, replayB, unpauseB;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		audioOptions = playing.getGame().getAudioOptions();
		createUrmButtons();
	}

	private void createUrmButtons() {
		int menuX = (int) (313 * GameCts.SCALE);
		int replayX = (int) (387 * GameCts.SCALE);
		int unpauseX = (int) (462 * GameCts.SCALE);
		int bY = (int) (325 * GameCts.SCALE);

		menuB = new UrmButton(menuX, bY, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 2);
		replayB = new UrmButton(replayX, bY, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 1);
		unpauseB = new UrmButton(unpauseX, bY, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 0);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (backgroundImg.getWidth() * GameCts.SCALE);
		bgH = (int) (backgroundImg.getHeight() * GameCts.SCALE);
		bgX = GameCts.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (25 * GameCts.SCALE);
	}

	public void update() {

		menuB.update();
		replayB.update();
		unpauseB.update();

		audioOptions.update();

	}

	public void draw(GraphicsContext g) {
		// Background
		g.drawImage(backgroundImg, bgX, bgY, bgW, bgH);

		// UrmButtons
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);

		audioOptions.draw(g);

	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else
			audioOptions.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				playing.resetAll();
				playing.setGamestate(Gamestate.MENU);
				playing.unpauseGame();
			}
		} else if (isIn(e, replayB)) {
			if (replayB.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}
		} else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		} else
			audioOptions.mouseReleased(e);

		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();

	}

	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
