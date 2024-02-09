package com.mandarina.game.entities.crabby;

import com.mandarina.game.constants.EntityCts;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class CrabbyAtlas {

	public static int GreenValue() {
		return EntityCts.CRABBY;
	}

	public static String Image() {
		return "crabby.png";
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
		return LoadSave.GetAnimations(XSize(), YSize(), CrabbySprite.WIDTH.val(), CrabbySprite.HEIGHT.val(),
				LoadSave.GetAtlas(Image()));
	}
}