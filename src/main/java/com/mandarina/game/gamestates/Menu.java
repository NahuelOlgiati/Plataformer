package com.mandarina.game.gamestates;

import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.ui.FullScreenButton;
import com.mandarina.game.ui.MenuButton;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Menu {

	private Game game;
	private MenuButton[] buttons = new MenuButton[5];
	private Image backgroundImg, backgroundImgPink;
	private float menuX, menuY, menuWidth, menuHeight;
	private FullScreenButton fullScreenButton;

	public Menu(Game game) {
		this.game = game;
		backgroundImgPink = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND_IMG);
		backgroundImg = LoadSave.GetSprite(LoadSave.MENU_BACKGROUND);
		loadButtons();
		loadBackground();
		loadFullScreenButton();

	}

	private void loadButtons() {
		buttons[0] = new MenuButton(AppStage.GetGameWidth() / 2, AppStage.Scale(120), 0, GameState.PLAYING);
		buttons[1] = new MenuButton(AppStage.GetGameWidth() / 2, AppStage.Scale(177), 1, GameState.OPTIONS);
		buttons[2] = new MenuButton(AppStage.GetGameWidth() / 2, AppStage.Scale(234), 3, GameState.CREDITS);
		buttons[3] = new MenuButton(AppStage.GetGameWidth() / 2, AppStage.Scale(291), 4, GameState.LVLBUILDER);
		buttons[4] = new MenuButton(AppStage.GetGameWidth() / 2, AppStage.Scale(348), 2, GameState.QUIT);
	}

	private void loadBackground() {
		menuWidth = AppStage.Scale(backgroundImg.getWidth());
		menuHeight = AppStage.Scale(backgroundImg.getHeight());
		menuX = AppStage.GetGameWidth() / 2 - menuWidth / 2;
		menuY = AppStage.Scale(25);
	}

	private void loadFullScreenButton() {
		fullScreenButton = new FullScreenButton();
	}

	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	public void draw(GameDrawer g) {
		g.drawImage(backgroundImgPink, 0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight);

		fullScreenButton.draw(g);

		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	public void mousePressed(MouseEvent e) {
		if (fullScreenButton.isIn(e))
			fullScreenButton.setMousePressed(true);

		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				mb.setMousePressed(true);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (fullScreenButton.isIn(e))
			if (fullScreenButton.isMousePressed())
				fullScreenButton.change();

		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				if (mb.isMousePressed()) {
					if (mb.getState() == GameState.PLAYING) {
						game.getPlaying().loadNextLevel();
					} else if (mb.getState() == GameState.LVLBUILDER) {
						game.stop();
						game.getLvlBuilder().show();
					}
					GameState.setState(mb.getState());
					break;
				}
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		fullScreenButton.resetBools();
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		fullScreenButton.setMouseOver(fullScreenButton.isIn(e));
		for (MenuButton mb : buttons)
			mb.setMouseOver(mb.isIn(e));
	}

	public void scale() {
		loadButtons();
		loadBackground();
		loadFullScreenButton();
	}
}
