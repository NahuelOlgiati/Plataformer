package com.mandarina.game.ui;

import javafx.scene.shape.Rectangle;

public class PauseButton {

	protected Rectangle bounds;

	public PauseButton(int x, int y, int width, int height) {
		bounds = new Rectangle(x, y, width, height);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

}
