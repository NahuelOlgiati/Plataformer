package com.mandarina.ui;

import com.mandarina.constants.UICts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class UrmButton extends PauseButton {
	private Image[] imgs;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;

	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImgs();
	}

	private void loadImgs() {
		Image temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new Image[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = new WritableImage(temp.getPixelReader(), i * UICts.URMButtons.URM_DEFAULT_SIZE,
					rowIndex * UICts.URMButtons.URM_DEFAULT_SIZE, UICts.URMButtons.URM_DEFAULT_SIZE,
					UICts.URMButtons.URM_DEFAULT_SIZE);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;

	}

	public void draw(GraphicsContext g) {
		g.drawImage(imgs[index], x, y, UICts.URMButtons.URM_SIZE, UICts.URMButtons.URM_SIZE);
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
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

}