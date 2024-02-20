package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class PinkstarCts {
	public static final int HEALTH = 25;
	public static final int DAMAGE = 20;

	public static final int SPRITE_WIDTH_DEFAULT = 34;
	public static final int SPRITE_WIDTH = (int) (SPRITE_WIDTH_DEFAULT * GameCts.SCALE);

	public static final int SPRITE_HEIGHT_DEFAULT = 30;
	public static final int SPRITE_HEIGHT = (int) (SPRITE_HEIGHT_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_X_DEFAULT = 9;
	public static final int DRAW_OFFSET_X = (int) (DRAW_OFFSET_X_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_Y_DEFAULT = 7;
	public static final int DRAW_OFFSET_Y = (int) (DRAW_OFFSET_Y_DEFAULT * GameCts.SCALE);

	public static final int HITBOX_WIDTH = 17;
	public static final int HITBOX_HEIGHT = 21;

	public static final int ATTACK_HITBOX_WIDTH = 17;
	public static final int ATTACK_HITBOX_HEIGHT = 21;
	public static final int ATTACK_HITBOX_OFFSET_X = 0;

	public static final String ATLAS_IMAGE = "pinkstar.png";
	public static final int ATLAS_SIZE_X = 8;
	public static final int ATLAS_SIZE_Y = 5;
}