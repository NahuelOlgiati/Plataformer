package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class TitanCts {
	public static final int HEALTH = 100;
	public static final int DAMAGE = 40;

	public static final int SPRITE_WIDTH_DEFAULT = 96;
	public static final int SPRITE_WIDTH = (int) (SPRITE_WIDTH_DEFAULT * GameCts.SCALE);

	public static final int SPRITE_HEIGHT_DEFAULT = 112;
	public static final int SPRITE_HEIGHT = (int) (SPRITE_HEIGHT_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_X_DEFAULT = 32;
	public static final int DRAW_OFFSET_X = (int) (DRAW_OFFSET_X_DEFAULT * GameCts.SCALE);

	public static final int DRAW_OFFSET_Y_DEFAULT = 90;
	public static final int DRAW_OFFSET_Y = (int) (DRAW_OFFSET_Y_DEFAULT * GameCts.SCALE);

	public static final int HITBOX_WIDTH = 18;
	public static final int HITBOX_HEIGHT = 12;

	public static final int ATTACK_HITBOX_WIDTH = 20;
	public static final int ATTACK_HITBOX_HEIGHT = 20;
	public static final int ATTACK_HITBOX_OFFSET_X = 30;

	public static final String ATLAS_IMAGE = "titan.png";
	public static final int ATLAS_SIZE_X = 6;
	public static final int ATLAS_SIZE_Y = 5;
}