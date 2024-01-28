package com.mandarina.lvlbuilder;

import java.nio.file.Path;

import javafx.scene.image.Image;

public class TiledImage extends Image {

	private Path path;
	private RGB rgb;
	private int firstValue;
	private int lastValue;
	private String name;

	public TiledImage(Path path) {
		super(path.toString());
		this.path = path;
		var values = path.getFileName().toString().split("_");
		this.rgb = RGB.get(values[0]);
		this.firstValue = Integer.parseInt(values[1]);
		this.lastValue = Integer.parseInt(values[2]);
		this.name = values[3].split("\\.")[0];
	}

	public Path getPath() {
		return path;
	}

	public RGB getRGB() {
		return rgb;
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

}
