package com.mandarina.entities.crabby;

import com.mandarina.constants.EntityCts;
import com.mandarina.entities.EnemyState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class CrabbyAtlas {

	public static int GreenValue() {
		return EntityCts.CRABBY;
	}

	public static String Image() {
		return "crabby_sprite.png";
	}

	public static int XSize() {
		return 9;
	}

	public static int YSize() {
		return 5;
	}

	public static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 9;
		case RUNNING:
			return 6;
		case ATTACK:
			return 7;
		case HIT:
			return 4;
		case DEAD:
			return 5;
		}
		return 0;
	}

	public static Image[][] getAnimations() {
		return LoadSave.getAnimations(XSize(), YSize(), CrabbySprite.WIDTH.val(), CrabbySprite.HEIGHT.val(),
				LoadSave.GetSpriteAtlas(Image()));
	}
}