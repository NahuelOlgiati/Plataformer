package com.mandarina.lvlbuilder;

public enum RGB {
	RED("R"), GREEN("G"), BLUE("B");

	private String value;

	RGB(String value) {
		this.value = value;
	}

	public static RGB get(String s) {
		for (RGB rgb : values()) {
			if (rgb.getValue().equals(s)) {
				return rgb;
			}
		}
		throw new IllegalArgumentException("Invalid value: " + s);
	}

	public String getValue() {
		return value;
	}
}