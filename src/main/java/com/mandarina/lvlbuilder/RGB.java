package com.mandarina.lvlbuilder;

public enum RGB {
	RED, GREEN, BLUE;

	public static RGB get(String s) {
		switch (s) {
		case "R":
			return RED;
		case "G":
			return GREEN;
		case "B":
			return BLUE;
		default:
			throw new IllegalArgumentException("Invalid prefix: " + s);
		}
	}
}