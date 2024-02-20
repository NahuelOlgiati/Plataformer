package com.mandarina.game.gamestates;

import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.ui.MenuButton;
import com.mandarina.utilz.LoadSave;

import com.mandarina.game.main.GameDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Menu {

	private Game game;
	private MenuButton[] buttons = new MenuButton[5];
	private Image backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		this.game = game;
		loadButtons();
		loadBackground();
		backgroundImgPink = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * GameCts.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * GameCts.SCALE);
		menuX = GameCts.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (25 * GameCts.SCALE);
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (120 * GameCts.SCALE), 0, GameState.PLAYING);
		buttons[1] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (177 * GameCts.SCALE), 1, GameState.OPTIONS);
		buttons[2] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (234 * GameCts.SCALE), 3, GameState.CREDITS);
		buttons[3] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (291 * GameCts.SCALE), 2, GameState.QUIT);
		buttons[4] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (348 * GameCts.SCALE), 4, GameState.LVLBUILDER);
	}

	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	public void draw(GameDrawer g) {
		g.drawImage(backgroundImgPink, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight);

		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				mb.setMousePressed(true);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				if (mb.isMousePressed())
					GameState.setState(mb.getState());
				if (mb.getState() == GameState.PLAYING)
					game.getPlaying().setLevelSong();
				if (mb.getState() == GameState.LVLBUILDER) {
					game.stop();
					game.getLvlBuilder().show();
				}

				break;
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(mb.isIn(e));
	}
}
