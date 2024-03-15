package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class LonglegCts {
	public static final int HEALTH = 100;
	public static final int DAMAGE = 40;

	public static final int SPRITE_WIDTH = 202;
	public static final int SPRITE_HEIGHT = 158;
	public static final int DRAW_OFFSET_X = 90;
	public static final int DRAW_OFFSET_Y = 52;

	public static final int HITBOX_WIDTH = 26;
	public static final int HITBOX_HEIGHT = 100;
	public static final int HITBOX_HORIZONTAL_CHECKS = HITBOX_WIDTH / GameCts.TILES_DEFAULT_SIZE;
	public static final int HITBOX_VERTICAL_CHECKS = HITBOX_HEIGHT / GameCts.TILES_DEFAULT_SIZE;

	public static final int ATTACKBOX_WIDTH = 40;
	public static final int ATTACKBOX_HEIGHT = 60;
	public static final int ATTACKBOX_OFFSET_X = 50;
	public static final int ATTACKBOX_OFFSET_Y = -40;

	public static final String ATLAS_IMAGE = "longleg.png";
	public static final int ATLAS_SIZE_X = 8;
	public static final int ATLAS_SIZE_Y = 5;

	public static final float WALK_SPEED = 0.35f;
	public static final int ATTACK_DISTANCE = 60;
	public static final int ATTACK_ANI_IND = 6;
}