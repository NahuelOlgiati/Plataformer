package com.mandarina.game.ui;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.constants.UICts;
import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, next;
	private Image img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int) (330 * GameCts.SCALE);
		int nextX = (int) (445 * GameCts.SCALE);
		int y = (int) (195 * GameCts.SCALE);
		next = new UrmButton(nextX, y, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 0);
		menu = new UrmButton(menuX, y, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE, 2);
	}

	private void initImg() {
		img = LoadSave.GetSprite(LoadSave.COMPLETED_IMG);
		bgW = (int) (img.getWidth() * GameCts.SCALE);
		bgH = (int) (img.getHeight() * GameCts.SCALE);
		bgX = GameCts.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * GameCts.SCALE);
	}

	public void draw(GraphicsContext g) {
		g.setFill(new Color(0, 0, 0, 0.5));
		g.fillRect(0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgW, bgH);
		next.draw(g);
		menu.draw(g);
	}

	public void update() {
		next.update();
		menu.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(next, e))
			next.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				GameState.setState(GameState.MENU);
			}
		} else if (isIn(next, e))
			if (next.isMousePressed()) {
				playing.loadNextLevel();
				playing.setLevelSong();
			}

		menu.resetBools();
		next.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(next, e))
			next.setMousePressed(true);
	}

}
