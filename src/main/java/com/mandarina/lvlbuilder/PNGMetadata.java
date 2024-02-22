package com.mandarina.lvlbuilder;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

public class PNGMetadata {

	private Map<String, Object> metadata;

	public PNGMetadata() {
		this.metadata = new HashMap<String, Object>();
	}

	public PNGMetadata(LvlBuilderImage img) {
		this.metadata = new HashMap<String, Object>();
		LvlBuilderMetada.readFeatures(img, this);
	}

	public void add(RGB rgb, KeyCode keyCode, Object object) {
		metadata.put(getKey(rgb, keyCode), object);
	}

	public static String getKey(RGB rgb, KeyCode keyCode) {
		return rgb.getValue() + "-" + keyCode.getName();
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}
}
