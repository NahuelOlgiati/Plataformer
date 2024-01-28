package com.mandarina.constants;

public class ObjectCts {

	public static final int RED_POTION = 0;
	public static final int BLUE_POTION = 1;
	public static final int BARREL = 2;
	public static final int BOX = 3;
	public static final int SPIKE = 4;
	public static final int CANNON_LEFT = 5;
	public static final int CANNON_RIGHT = 6;
	public static final int TREE_UP = 7;
	public static final int TREE_TWO = 8;
	public static final int TREE_THREE = 9;

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

	public static int GetSpriteAmount(int object_type) {
		switch (object_type) {
		case RED_POTION, BLUE_POTION:
			return 7;
		case BARREL, BOX:
			return 8;
		case CANNON_LEFT, CANNON_RIGHT:
			return 7;
		}
		return 1;
	}

	public static int GetTreeOffsetX(int treeType) {
		switch (treeType) {
		case TREE_UP:
			return (GameCts.TILES_SIZE / 2) - (GetTreeWidth(treeType) / 2);
		case TREE_TWO:
			return (int) (GameCts.TILES_SIZE / 2.5f);
		case TREE_THREE:
			return (int) (GameCts.TILES_SIZE / 1.65f);
		}

		return 0;
	}

	public static int GetTreeOffsetY(int treeType) {

		switch (treeType) {
		case TREE_UP:
			return -GetTreeHeight(treeType) + GameCts.TILES_SIZE * 2;
		case TREE_TWO, TREE_THREE:
			return -GetTreeHeight(treeType) + (int) (GameCts.TILES_SIZE / 1.25f);
		}
		return 0;

	}

	public static int GetTreeWidth(int treeType) {
		switch (treeType) {
		case TREE_UP:
			return (int) (39 * GameCts.SCALE);
		case TREE_TWO:
			return (int) (62 * GameCts.SCALE);
		case TREE_THREE:
			return -(int) (62 * GameCts.SCALE);

		}
		return 0;
	}

	public static int GetTreeHeight(int treeType) {
		switch (treeType) {
		case TREE_UP:
			return (int) (int) (92 * GameCts.SCALE);
		case TREE_TWO, TREE_THREE:
			return (int) (54 * GameCts.SCALE);

		}
		return 0;
	}
}