package com.mandarina.objects;

import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class CannonBall {

	public static Image load() {
		return LoadSave.GetSprite(LoadSave.CANNON_BALL);
	}
}
