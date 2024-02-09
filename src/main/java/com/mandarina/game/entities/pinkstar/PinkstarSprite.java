package com.mandarina.game.entities.pinkstar;

import com.mandarina.game.constants.GameCts;

enum PinkstarSprite {
	WIDTH(34), //
	HEIGHT(30), //

	DRAWOFFSET_X(9), //
	DRAWOFFSET_Y(7);

	private final int value;

	PinkstarSprite(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

	public int scaled() {
		return (int) (val() * GameCts.SCALE);
	}
}