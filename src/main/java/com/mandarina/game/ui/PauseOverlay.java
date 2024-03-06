package com.mandarina.game.ui;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class PauseOverlay {

	private Playing playing;
	private Image backgroundImg;
	private double bgX, bgY, bgW, bgH;
	private UrmButton menuB, replayB, unpauseB;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		backgroundImg = LoadSave.GetSprite(LoadSave.PAUSE_BACKGROUND);
		loadBackground();
		createUrmButtons();
	}

	private void createUrmButtons() {
		int menuX = AppStage.Scale(313);
		int replayX = AppStage.Scale(387);
		int unpauseX = AppStage.Scale(462);
		int bY = AppStage.Scale(325);

		menuB = new UrmButton(menuX, bY, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 2);
		replayB = new UrmButton(replayX, bY, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 1);
		unpauseB = new UrmButton(unpauseX, bY, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 0);
	}

	private void loadBackground() {
		bgW = AppStage.Scale(backgroundImg.getWidth());
		bgH = AppStage.Scale(backgroundImg.getHeight());
		bgX = AppStage.GetGameWidth() / 2 - bgW / 2;
		bgY = AppStage.Scale(25);
	}

	public void update() {
		menuB.update();
		replayB.update();
		unpauseB.update();

		playing.getGame().getAudioOptions().update();
	}

	public void draw(GameDrawer g) {
		// Background
		g.drawImage(backgroundImg, bgX, bgY, bgW, bgH);

		// UrmButtons
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);

		playing.getGame().getAudioOptions().draw(g);

	}

	public void mouseDragged(MouseEvent e) {
		playing.getGame().getAudioOptions().mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else
			playing.getGame().getAudioOptions().mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				playing.resetAll();
				GameState.setState(GameState.MENU);
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
			playing.getGame().getAudioOptions().mouseReleased(e);

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
			playing.getGame().getAudioOptions().mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void scale() {
		loadBackground();
		createUrmButtons();
	}

}
