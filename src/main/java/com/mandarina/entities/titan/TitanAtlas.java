package com.mandarina.entities.titan;

import com.mandarina.constants.EntityCts;
import com.mandarina.entities.EnemyState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class TitanAtlas {

	public static int GreenValue() {
		return EntityCts.TITAN;
	}

	public static String Image() {
		return "titan_atlas.png";
	}

	public static int XSize() {
		return 6;
	}

	public static int YSize() {
		return 5;
	}

	public static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 6;
		case RUNNING:
			return 6;
		case ATTACK:
			return 6;
		case HIT:
			return 6;
		case DEAD:
			return 6;
		}
		return 0;
	}

	public static Image[][] getAnimations() {
		return LoadSave.getAnimations(XSize(), YSize(), TitanSprite.WIDTH.val(), TitanSprite.HEIGHT.val(),
				LoadSave.GetSpriteAtlas(Image()));
	}
}