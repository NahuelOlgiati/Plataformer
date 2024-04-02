package com.mandarina.game.ui;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, next;
	private Image img;
	private float bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		img = LoadSave.GetSprite(Catalog.COMPLETED_IMG);
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = AppStage.Scale(330);
		int nextX = AppStage.Scale(445);
		int y = AppStage.Scale(195);
		next = new UrmButton(nextX, y, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 0);
		menu = new UrmButton(menuX, y, AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT), 2);
	}

	private void initImg() {
		bgW = AppStage.Scale(img.getWidth());
		bgH = AppStage.Scale(img.getHeight());
		bgX = AppStage.GetGameWidth() / 2 - bgW / 2;
		bgY = AppStage.Scale(75);
	}

	public void draw(GameDrawer g) {
		g.setFill(new Color(0, 0, 0, 0.5));
		g.fillRect(0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());

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
				GameState.setState(GameState.PLAYING);
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

	public void scale() {
		initImg();
		initButtons();
	}

}
