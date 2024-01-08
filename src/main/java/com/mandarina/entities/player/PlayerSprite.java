package com.mandarina.entities.player;

import com.mandarina.constants.GameCts;

enum PlayerSprite {
	WIDTH(44), //
	HEIGHT(62), //

	DRAWOFFSET_X(10), //
	DRAWOFFSET_Y(35);

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