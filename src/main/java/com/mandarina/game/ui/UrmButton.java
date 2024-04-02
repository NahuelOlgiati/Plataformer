package com.mandarina.game.ui;

import com.mandarina.game.geometry.Box;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

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
		Image temp = LoadSave.GetSprite(Catalog.URM_BUTTONS);
		imgs = new Image[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = LoadSave.GetSubimage(temp, i, rowIndex, URMButtonCts.URM_SIZE_DEFAULT,
					URMButtonCts.URM_SIZE_DEFAULT);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;

	}

	public void draw(GameDrawer g) {
		Box b = getBounds();
		g.drawImage(imgs[index], b.getMinX(), b.getMinY(), AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT),
				AppStage.Scale(URMButtonCts.URM_SIZE_DEFAULT));
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
