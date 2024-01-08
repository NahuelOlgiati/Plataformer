package com.mandarina.entities.player;

import com.mandarina.constants.EntityCts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

class PlayerAtlas {

	public static int GreenValue() {
		return EntityCts.PLAYER;
	}

	public static String Image() {
		return "player_sprites.png";
	}

	public static int XSize() {
		return 8;
	}

	public static int YSize() {
		return 7;
	}

	public static int GetSpriteAmount(PlayerState state) {
		switch (state) {
		case DEAD:
			return 8;
		case RUNNING:
			return 6;
		case IDLE:
			return 5;
		case HIT:
			return 4;
		case JUMP:
		case ATTACK:
			return 3;
		case FALLING:
		default:
			return 1;
		}
	}

	public static Image[][] getAnimations() {
		return LoadSave.getAnimations(XSize(), YSize(), PlayerSprite.WIDTH.val(), PlayerSprite.HEIGHT.val(),
				LoadSave.GetSpriteAtlas(Image()));
	}
}