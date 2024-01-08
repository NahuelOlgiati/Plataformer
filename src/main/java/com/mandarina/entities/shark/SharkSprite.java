package com.mandarina.entities.shark;

import com.mandarina.constants.GameCts;

enum SharkSprite {
	WIDTH(34), //
	HEIGHT(30), //

	DRAWOFFSET_X(8), //
	DRAWOFFSET_Y(6);

	private final int value;

	SharkSprite(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

	public int scaled() {
		return (int) (val() * GameCts.SCALE);
	}
}