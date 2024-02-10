package com.mandarina.lvlbuilder;

import java.io.InputStream;

import javafx.scene.image.Image;

public class LvlBuilderImage extends Image {

	private String fileName;
	private RGB rgb;
	private boolean tiled;
	private int value;
	private int firstValue;
	private int lastValue;
	private String name;

	public LvlBuilderImage(InputStream is, String fileName) {
		super(is);
		this.fileName = fileName;
		var values = fileName.toString().split("_");
		this.rgb = RGB.get(values[0]);
		this.tiled = isTiledImage(fileName);
		if (this.tiled) {
			this.firstValue = Integer.parseInt(values[1]);
			this.lastValue = Integer.parseInt(values[2]);
			this.name = values[3].split("\\.")[0];
		} else {
			this.value = Integer.parseInt(values[1]);
			this.name = values[2].split("\\.")[0];
		}
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isTiled() {
		return tiled;
	}

	public RGB getRgb() {
		return rgb;
	}

	public int getValue() {
		return value;
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

	private static boolean isTiledImage(String fileName) {
		String[] parts = fileName.split("_");
		try {
			Integer.parseInt(parts[1]);
			Integer.parseInt(parts[2]);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public static String getFileName(String fileName, int value) {
		String[] parts = fileName.split("_");
		parts[1] = String.valueOf(value);
		return parts[0] + "_" + parts[1] + "_" + parts[parts.length - 1];
	}
}