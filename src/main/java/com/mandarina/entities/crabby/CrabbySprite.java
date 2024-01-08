package com.mandarina.entities.crabby;

import com.mandarina.constants.GameCts;

enum CrabbySprite {
	WIDTH(72), //
	HEIGHT(32), //

	DRAWOFFSET_X(26), //
	DRAWOFFSET_Y(9);

	private final int value;

	CrabbySprite(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

	public int scaled() {
		return (int) (val() * GameCts.SCALE);
	}
}