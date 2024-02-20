package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class CrabbyCts {
	public static final int HEALTH = 50;
	public static final int DAMAGE = 15;

	public static final int SPRITE_WIDTH_DEFAULT = 72;
	public static final int SPRITE_WIDTH = (int) (SPRITE_WIDTH_DEFAULT * GameCts.SCALE);

	public static final int SPRITE_HEIGHT_DEFAULT = 32;
	public static final int SPRITE_HEIGHT = (int) (SPRITE_HEIGHT_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_X_DEFAULT = 26;
	public static final int DRAW_OFFSET_X = (int) (DRAW_OFFSET_X_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_Y_DEFAULT = 9;
	public static final int DRAW_OFFSET_Y = (int) (DRAW_OFFSET_Y_DEFAULT * GameCts.SCALE);

	public static final int HITBOX_WIDTH = 22;
	public static final int HITBOX_HEIGHT = 19;

	public static final int ATTACK_HITBOX_WIDTH = 82;
	public static final int ATTACK_HITBOX_HEIGHT = 19;
	public static final int ATTACK_HITBOX_OFFSET_X = 30;

	public static final String ATLAS_IMAGE = "crabby.png";
	public static final int ATLAS_SIZE_X = 9;
	public static final int ATLAS_SIZE_Y = 5;
}