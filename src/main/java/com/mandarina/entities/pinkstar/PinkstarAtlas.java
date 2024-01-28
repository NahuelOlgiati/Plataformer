package com.mandarina.entities.pinkstar;

import com.mandarina.constants.EntityCts;
import com.mandarina.entities.EnemyState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class PinkstarAtlas {

	public static int GreenValue() {
		return EntityCts.PINKSTAR;
	}

	public static String Image() {
		return "pinkstar.png";
	}

	public static int XSize() {
		return 8;
	}

	public static int YSize() {
		return 5;
	}

	public static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 8;
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
		return LoadSave.GetAnimations(XSize(), YSize(), PinkstarSprite.WIDTH.val(), PinkstarSprite.HEIGHT.val(),
				LoadSave.GetAtlas(Image()));
	}
}