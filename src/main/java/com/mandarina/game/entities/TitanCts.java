package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class TitanCts {
	public static final int HEALTH = 100;
	public static final int DAMAGE = 40;

	public static final int SPRITE_WIDTH = 96;
	public static final int SPRITE_HEIGHT = 112;
	public static final int DRAW_OFFSET_X = 32;
	public static final int DRAW_OFFSET_Y = 4;

	public static final int HITBOX_WIDTH = 38;
	public static final int HITBOX_HEIGHT = 100;
	public static final int HITBOX_HORIZONTAL_CHECKS = HITBOX_WIDTH / GameCts.TILES_DEFAULT_SIZE;
	public static final int HITBOX_VERTICAL_CHECKS = HITBOX_HEIGHT / GameCts.TILES_DEFAULT_SIZE;

	public static final int ATTACKBOX_WIDTH = 40;
	public static final int ATTACKBOX_HEIGHT = 60;
	public static final int ATTACKBOX_OFFSET_X = 50;
	public static final int ATTACKBOX_OFFSET_Y = -40;

	public static final String ATLAS_IMAGE = "titan.png";
	public static final int ATLAS_SIZE_X = 6;
	public static final int ATLAS_SIZE_Y = 5;

	public static final float WALK_SPEED = 0.35f;
	public static final int ATTACK_DISTANCE = 60;
	public static final int ATTACK_ANI_IND = 4;
}