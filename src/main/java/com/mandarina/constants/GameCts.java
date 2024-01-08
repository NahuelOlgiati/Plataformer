package com.mandarina.constants;

public class GameCts {
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.7f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	public static final float GRAVITY = 0.04f * SCALE;
	public static final int ANI_SPEED = 25;
}