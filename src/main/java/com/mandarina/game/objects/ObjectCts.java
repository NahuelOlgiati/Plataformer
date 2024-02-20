package com.mandarina.game.objects;

import com.mandarina.game.main.GameCts;

public class ObjectCts {

	public static final int RED_POTION = 0;
	public static final int BLUE_POTION = 1;
	public static final int BARREL = 2;
	public static final int BOX = 3;
	public static final int SPIKE = 4;
	public static final int CANNON_LEFT = 5;
	public static final int CANNON_RIGHT = 6;
	public static final int TREE_UP = 7;
	public static final int TREE_RIGHT = 8;
	public static final int TREE_LEFT = 9;

	public static final int RED_POTION_VALUE = 15;
	public static final int BLUE_POTION_VALUE = 10;

	public static final int CONTAINER_WIDTH_DEFAULT = 40;
	public static final int CONTAINER_HEIGHT_DEFAULT = 30;
	public static final int CONTAINER_WIDTH = (int) (GameCts.SCALE * CONTAINER_WIDTH_DEFAULT);
	public static final int CONTAINER_HEIGHT = (int) (GameCts.SCALE * CONTAINER_HEIGHT_DEFAULT);

	public static final int POTION_WIDTH_DEFAULT = 12;
	public static final int POTION_HEIGHT_DEFAULT = 16;
	public static final int POTION_WIDTH = (int) (GameCts.SCALE * POTION_WIDTH_DEFAULT);
	public static final int POTION_HEIGHT = (int) (GameCts.SCALE * POTION_HEIGHT_DEFAULT);

	public static final int SPIKE_WIDTH_DEFAULT = 32;
	public static final int SPIKE_HEIGHT_DEFAULT = 32;
	public static final int SPIKE_WIDTH = (int) (GameCts.SCALE * SPIKE_WIDTH_DEFAULT);
	public static final int SPIKE_HEIGHT = (int) (GameCts.SCALE * SPIKE_HEIGHT_DEFAULT);

	public static final int CANNON_WIDTH_DEFAULT = 40;
	public static final int CANNON_HEIGHT_DEFAULT = 26;
	public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * GameCts.SCALE);
	public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * GameCts.SCALE);
}