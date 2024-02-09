package com.mandarina.game.entities.titan;

import com.mandarina.game.constants.EntityCts;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class TitanAtlas {

	public static int GreenValue() {
		return EntityCts.TITAN;
	}

	public static String Image() {
		return "titan.png";
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
		return LoadSave.GetAnimations(XSize(), YSize(), TitanSprite.WIDTH.val(), TitanSprite.HEIGHT.val(),
				LoadSave.GetAtlas(Image()));
	}
}