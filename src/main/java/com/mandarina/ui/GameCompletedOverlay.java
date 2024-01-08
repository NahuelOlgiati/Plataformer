package com.mandarina.ui;

import com.mandarina.constants.GameCts;
import com.mandarina.gamestates.Gamestate;
import com.mandarina.gamestates.Playing;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameCompletedOverlay {
	private Playing playing;
	private Image img;
	private MenuButton quit, credit;
	private int imgX, imgY, imgW, imgH;

	public GameCompletedOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	private void createButtons() {
		quit = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (270 * GameCts.SCALE), 2, Gamestate.MENU);
		credit = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (200 * GameCts.SCALE), 3, Gamestate.CREDITS);
	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
		imgW = (int) (img.getWidth() * GameCts.SCALE);
		imgH = (int) (img.getHeight() * GameCts.SCALE);
		imgX = GameCts.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * GameCts.SCALE);

	}

	public void draw(GraphicsContext g) {
		g.setFill(new Color(0, 0, 0, 0.5));
		g.fillRect(0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH);

		credit.draw(g);
		quit.draw(g);
	}

	public void update() {
		credit.update();
		quit.update();
	}

	private boolean isIn(MenuButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		credit.setMouseOver(false);
		quit.setMouseOver(false);

		if (isIn(quit, e))
			quit.setMouseOver(true);
		else if (isIn(credit, e))
			credit.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(quit, e)) {
			if (quit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				playing.setGamestate(Gamestate.MENU);

			}
		} else if (isIn(credit, e))
			if (credit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				playing.setGamestate(Gamestate.CREDITS);
			}

		quit.resetBools();
		credit.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(quit, e))
			quit.setMousePressed(true);
		else if (isIn(credit, e))
			credit.setMousePressed(true);
	}
}
