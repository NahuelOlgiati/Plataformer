package com.mandarina.lvlbuilder;

import java.nio.file.Path;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ValuedImage extends WritableImage {

	private int value;

	public ValuedImage(PixelReader pixelReader, int x, int y, int width, int height, int value) {
		super(pixelReader, x, y, width, height);
		this.value = value;
	}

	public ValuedImage(Path path) {
		super(new Image(path.toString()).getPixelReader(), 0, 0, LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);
		var values = path.getFileName().toString().split("_");
//		this.rgb = RGB.get(values[0]);
		this.value = Integer.parseInt(values[1]);
//		this.lastValue = Integer.parseInt(values[2]);
//		this.name = values[3].split("\\.")[0];
	}

	public int getValue() {
		return value;
	}
}
