package com.mandarina.lvlbuilder;

import com.mandarina.utilz.LoadSave;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public enum TileFeature {

	TRAVERSABLE(KeyCode.T, "traversable.png") {
		@Override
		public void apply(ImageView iw) {
			apply(iw, getIcon());
		}
	};

	private KeyCode keyCode;
	private String icon;

	public abstract void apply(ImageView iw);

	TileFeature(KeyCode keyCode, String icon) {
		this.keyCode = keyCode;
		this.icon = icon;
	}

	public static void apply(ImageView iw, String icon) {
		Blend blend = new Blend();
		LvlBuilderImage image = (LvlBuilderImage) iw.getImage();
		Image featureImg = LoadSave.GetFeature(icon);
		ImageInput imageInput = new ImageInput(featureImg, iw.getX() + image.getWidth() - featureImg.getWidth() - 2,
				iw.getY() + image.getHeight() - featureImg.getHeight() - 2);
		blend.setTopInput(imageInput);
		blend.setMode(BlendMode.ADD);
		iw.setEffect(blend);
	}

	public KeyCode getKeyCode() {
		return keyCode;
	}

	public String getIcon() {
		return icon;
	}
}