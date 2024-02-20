package com.mandarina.game.ui;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameOverOverlay {

	private Playing playing;
	private Image img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	private void createButtons() {
		int menuX = (int) (335 * GameCts.SCALE);
		int playX = (int) (440 * GameCts.SCALE);
		int y = (int) (195 * GameCts.SCALE);
		play = new UrmButton(playX, y, URMButtonCts.URM_SIZE, URMButtonCts.URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URMButtonCts.URM_SIZE, URMButtonCts.URM_SIZE, 2);

	}

	private void createImg() {
		img = LoadSave.GetSprite(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * GameCts.SCALE);
		imgH = (int) (img.getHeight() * GameCts.SCALE);
		imgX = GameCts.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * GameCts.SCALE);

	}

	public void draw(GameDrawer g) {
		g.setFill(new Color(0, 0, 0, 0.5));
		g.fillRect(0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);

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

}
