package com.mandarina.game.ui;

import com.mandarina.utilz.Box;

public class PauseButton {

	protected Box bounds;

	public PauseButton(int x, int y, int width, int height) {
		bounds = new Box(x, y, width, height);
	}

	public Box getBounds() {
		return bounds;
	}

	public void setBounds(Box bounds) {
		this.bounds = bounds;
	}

}
