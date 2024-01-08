package com.mandarina.ui;

import com.mandarina.constants.GameCts;
import com.mandarina.constants.UICts;
import com.mandarina.gamestates.Gamestate;
import com.mandarina.gamestates.Playing;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
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
		play = new UrmButton(playX, y, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 0);
		menu = new UrmButton(menuX, y, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 2);

	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * GameCts.SCALE);
		imgH = (int) (img.getHeight() * GameCts.SCALE);
		imgX = GameCts.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * GameCts.SCALE);

	}

	public void draw(GraphicsContext g) {
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
				playing.setGamestate(Gamestate.MENU);
			}
		} else if (isIn(play, e))
			if (play.isMousePressed()) {
				playing.resetAll();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
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
