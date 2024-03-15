package com.mandarina.game.entities;

import com.mandarina.game.main.GameCts;

public class PlayerCts {
	public static final int HEALTH = 100;
	public static final int DAMAGE = 20;

	public static final int SPRITE_WIDTH = 44;
	public static final int SPRITE_HEIGHT = 62;
	public static final int DRAW_OFFSET_X = 10;
	public static final int DRAW_OFFSET_Y = 22;

	public static final int HITBOX_WIDTH = 25;
	public static final int HITBOX_HEIGHT = 38;
	public static final int HITBOX_HORIZONTAL_CHECKS = HITBOX_WIDTH / GameCts.TILES_DEFAULT_SIZE;
	public static final int HITBOX_VERTICAL_CHECKS = HITBOX_HEIGHT / GameCts.TILES_DEFAULT_SIZE;

	public static final int ATTACKBOX_WIDTH = 35;
	public static final int ATTACKBOX_HEIGHT = 20;
	public static final int ATTACKBOX_OFFSET_X = -5;
	public static final int ATTACKBOX_OFFSET_Y = -10;

	public static final String ATLAS_IMAGE = "player.png";
	public static final int ATLAS_SIZE_X = 8;
	public static final int ATLAS_SIZE_Y = 6;

	public static final float WALK_SPEED = 1f;
	public static final float JUMP_SPEED = 2.25f;
	public static final float FALL_SPEED_AFTER_COLLISION = 0.5f;
	public static final int ATTACK_DISTANCE = 32;
	public static final int ATTACK_ANI_IND = 3;
}