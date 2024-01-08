package com.mandarina.entities.player;

enum PlayerState {
	IDLE(0), RUNNING(1), JUMP(2), FALLING(3), ATTACK(4), HIT(5), DEAD(6);

	private int value;

	PlayerState(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}
}