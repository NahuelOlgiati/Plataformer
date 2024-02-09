package com.mandarina.game.ui;

import com.mandarina.game.constants.UICts;
import com.mandarina.game.gamestates.GameState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class MenuButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = UICts.Buttons.B_WIDTH / 2;
	private GameState state;
	private Image[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, UICts.Buttons.B_WIDTH, UICts.Buttons.B_HEIGHT);
	}

	private void loadImgs() {
		imgs = new Image[3];
		Image temp = LoadSave.GetSprite(LoadSave.MENU_BUTTONS);
		PixelReader pixelReader = temp.getPixelReader();
		for (int i = 0; i < imgs.length; i++) {
			WritableImage subImage = new WritableImage(pixelReader, i * UICts.Buttons.B_WIDTH_DEFAULT,
					rowIndex * UICts.Buttons.B_HEIGHT_DEFAULT, UICts.Buttons.B_WIDTH_DEFAULT,
					UICts.Buttons.B_HEIGHT_DEFAULT);
			imgs[i] = subImage;
		}
	}

	public void draw(GraphicsContext g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, UICts.Buttons.B_WIDTH, UICts.Buttons.B_HEIGHT);
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
