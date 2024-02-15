package com.mandarina.lvlbuilder;

import javafx.scene.image.ImageView;

public class SelectedTile {

	private ImageView imageView;
	private RGB rgb;
	private int x;
	private int y;

	public SelectedTile(ImageView imageView, RGB rgb, int x, int y) {
		this.imageView = imageView;
		this.rgb = rgb;
		this.x = x;
		this.y = y;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public RGB getRgb() {
		return rgb;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
