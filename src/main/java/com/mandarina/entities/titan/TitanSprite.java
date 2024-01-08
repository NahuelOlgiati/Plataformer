package com.mandarina.entities.titan;

import com.mandarina.constants.GameCts;

enum TitanSprite {
	WIDTH(96), //
	HEIGHT(112), //

	DRAWOFFSET_X(32), //
	DRAWOFFSET_Y(90);

	private final int value;

	TitanSprite(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

	public int scaled() {
		return (int) (val() * GameCts.SCALE);
	}
}