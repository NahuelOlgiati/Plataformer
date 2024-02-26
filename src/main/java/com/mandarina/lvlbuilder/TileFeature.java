package com.mandarina.lvlbuilder;

import javafx.scene.input.KeyCode;

public enum TileFeature {

	NOSOLID(KeyCode.N, "nosolid.png", new NosolidFeatureManager()),

	LAYER1(KeyCode.DIGIT1, "1.png", new LayerFeatureManager(1)),
	LAYER2(KeyCode.DIGIT2, "2.png", new LayerFeatureManager(2)),
	LAYER3(KeyCode.DIGIT3, "3.png", new LayerFeatureManager(3)),
	LAYER4(KeyCode.DIGIT4, "4.png", new LayerFeatureManager(4));

	private KeyCode keyCode;
	private String icon;
	protected FeatureManager manager;

	TileFeature(KeyCode keyCode, String icon, FeatureManager manager) {
		this.keyCode = keyCode;
		this.icon = icon;
		this.manager = manager;
	}

	public KeyCode getKeyCode() {
		return keyCode;
	}

	public String getIcon() {
		return icon;
	}

	public FeatureManager getManager() {
		return manager;
	}
}