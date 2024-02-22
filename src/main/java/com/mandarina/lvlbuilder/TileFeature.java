package com.mandarina.lvlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mandarina.utilz.LoadSave;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

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

	public List<Pair<Integer, Integer>> get(PNGMetadata pngMetadata, RGB rgb) {
		List<Pair<Integer, Integer>> r = new ArrayList<Pair<Integer, Integer>>();
		Map<Integer, Integer> values = pngMetadata.getMetadata().get(PNGMetadata.getKey(rgb, this.getKeyCode()));
		if (values != null) {
			r = toList(values);
		}
		return r;
	}

	public static List<Pair<Integer, Integer>> toList(Map<Integer, Integer> map) {
		List<Pair<Integer, Integer>> pairsList = new ArrayList<Pair<Integer, Integer>>();
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			pairsList.add(new Pair<Integer, Integer>(entry.getKey(), entry.getValue()));
		}
		return pairsList;
	}

	public KeyCode getKeyCode() {
		return keyCode;
	}

	public String getIcon() {
		return icon;
	}
}