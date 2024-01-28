package com.mandarina.gamestates;

import com.mandarina.constants.GameCts;
import com.mandarina.main.Game;
import com.mandarina.ui.MenuButton;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[4];
	private Image backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		super(game);
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
		buttons[0] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (130 * GameCts.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (200 * GameCts.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (270 * GameCts.SCALE), 3, Gamestate.CREDITS);
		buttons[3] = new MenuButton(GameCts.GAME_WIDTH / 2, (int) (340 * GameCts.SCALE), 2, Gamestate.QUIT);
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	@Override
	public void draw(GraphicsContext g) {
		g.drawImage(backgroundImgPink, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight);

		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
				if (mb.getState() == Gamestate.PLAYING)
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
				break;
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(isIn(e, mb));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Handle key events if needed
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Handle mouse click events if needed
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Handle key release events if needed
	}
}
