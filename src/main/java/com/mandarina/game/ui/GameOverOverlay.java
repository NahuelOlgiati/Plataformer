package com.mandarina.game.ui;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameOverOverlay {

	private Playing playing;
	private Image img;
	private double imgX, imgY, imgW, imgH;
	private UrmButton menu, play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		img = LoadSave.GetSprite(LoadSave.DEATH_SCREEN);
		createImg();
		createButtons();
	}

	private void createButtons() {
		int menuX = AppStage.Scale(335);
		int playX = AppStage.Scale(440);
		int y = AppStage.Scale(195);
		play = new UrmButton(playX, y, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 0);
		menu = new UrmButton(menuX, y, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 2);

	}

	private void createImg() {
		imgW = AppStage.Scale(img.getWidth());
		imgH = AppStage.Scale(img.getHeight());
		imgX = AppStage.GetGameWidth() / 2 - imgW / 2;
		imgY = AppStage.Scale(100);
	}

	public void draw(GameDrawer g) {
		g.setFill(new Color(0, 0, 0, 0.5));
		g.fillRect(0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());

		g.drawImage(img, imgX, imgY, imgW, imgH);

		menu.draw(g);
		play.draw(g);
	}

	public void update() {
		menu.update();
		play.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(play, e))
			play.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				GameState.setState(GameState.MENU);
			}
		} else if (isIn(play, e))
			if (play.isMousePressed()) {
				playing.resetAll();
				playing.setLevelSong();
			}

		menu.resetBools();
		play.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(play, e))
			play.setMousePressed(true);
	}

	public void scale() {
		createImg();
		createButtons();
	}

}
