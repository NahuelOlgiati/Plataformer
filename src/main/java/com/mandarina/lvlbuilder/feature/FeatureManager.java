package com.mandarina.lvlbuilder.feature;

import com.mandarina.lvlbuilder.RGB;
import com.mandarina.utilz.Point;

import javafx.scene.layout.VBox;

public interface FeatureManager<T> {

	public TileFeature getTileFeature();

	public void applyTo(Point coord, PNGMetadata pm, RGB rgb, VBox pane);

	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane);

	public T get(PNGMetadata pm, RGB rgb);

	public T fromString(PNGMetadata pm, RGB rgb, String text);

	public String toString(PNGMetadata pm, RGB rgb);

	public void add(PNGMetadata pm, RGB rgb, Point coord);
}