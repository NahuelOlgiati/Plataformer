package com.mandarina.entities.player;

import com.mandarina.constants.GameCts;

enum PlayerSprite {
	WIDTH(64), //
	HEIGHT(40), //

	DRAWOFFSET_X(21), //
	DRAWOFFSET_Y(4);

	private final int value;

	PlayerSprite(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

	public int scaled() {
		return (int) (val() * GameCts.SCALE);
	}
}