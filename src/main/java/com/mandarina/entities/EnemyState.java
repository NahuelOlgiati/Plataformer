package com.mandarina.entities;

public enum EnemyState {
	IDLE(0), RUNNING(1), ATTACK(2), HIT(3), DEAD(4);

	private int value;

	EnemyState(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}

}