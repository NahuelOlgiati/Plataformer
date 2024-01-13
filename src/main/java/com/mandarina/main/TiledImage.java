package com.mandarina.main;

import java.nio.file.Path;

import javafx.scene.image.Image;

public class TiledImage extends Image {

	private final int EMTPY_VALUE = 128;
	
	private Path path;
	private TiledImageColor color;
	private int firstValue;
	private int lastValue;
	private String name;

	public TiledImage(Path path) {
		super(path.toString());
		this.path = path;
		var values = path.getFileName().toString().split("_");
		this.color = TiledImageColor.get(values[0]);
		this.firstValue = Integer.parseInt(values[1]);
		this.lastValue = Integer.parseInt(values[2]);
		this.name = values[3].split("\\.")[0];
	}

	public Path getPath() {
		return path;
	}

	public TiledImageColor getColor() {
		return color;
	}

	public int getFirstValue() {
		return firstValue;
	}

	public int getLastValue() {
		return lastValue;
	}

	public String getName() {
		return name;
	}

	private enum TiledImageColor {
		RED, GREEN, BLUE;

		public static TiledImageColor get(String s) {
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
}
