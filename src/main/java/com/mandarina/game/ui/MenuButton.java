package com.mandarina.game.ui;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class MenuButton {
	private float xPos, yPos;
	private int rowIndex, index;
	private int xOffsetCenter;
	private GameState state;
	private Image[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public MenuButton(float xPos, float yPos, int rowIndex, GameState state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		this.xOffsetCenter = AppStage.Scale(MenuButtonCts.WIDTH_DEFAULT) / 2;
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, AppStage.Scale(MenuButtonCts.WIDTH_DEFAULT),
				AppStage.Scale(MenuButtonCts.HEIGHT_DEFAULT));
	}

	private void loadImgs() {
		imgs = new Image[3];
		Image temp = LoadSave.GetSprite(Catalog.MENU_BUTTONS);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = LoadSave.GetSubimage(temp, i, rowIndex, MenuButtonCts.WIDTH_DEFAULT,
					MenuButtonCts.HEIGHT_DEFAULT);
		}
	}

	public void draw(GameDrawer g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, AppStage.Scale(MenuButtonCts.WIDTH_DEFAULT),
				AppStage.Scale(MenuButtonCts.HEIGHT_DEFAULT));
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public GameState getState() {
		return state;
	}

	public boolean isIn(MouseEvent e) {
		return this.getBounds().contains(e.getX(), e.getY());
	}
}
