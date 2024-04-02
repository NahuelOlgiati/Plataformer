package com.mandarina.lvlbuilder;

import java.util.Objects;

import com.mandarina.game.geometry.Point;

import javafx.scene.image.ImageView;

public class SelectedTile {

	private ImageView imageView;
	private RGB rgb;
	private Point coords;

	public SelectedTile(ImageView imageView, RGB rgb, Point coords) {
		this.imageView = imageView;
		this.rgb = rgb;
		this.coords = coords;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public RGB getRgb() {
		return rgb;
	}

	public Point getCoords() {
		return coords;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SelectedTile that))
			return false;
		return Objects.equals(coords, that.coords);
	}

	@Override
	public int hashCode() {
		return Objects.hash(coords);
	}
}
