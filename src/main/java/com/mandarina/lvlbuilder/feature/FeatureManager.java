package com.mandarina.lvlbuilder.feature;

import com.mandarina.lvlbuilder.RGB;

import javafx.scene.layout.VBox;
import javafx.util.Pair;

public interface FeatureManager<T> {

	public TileFeature getTileFeature();

	public void applyTo(Pair<Integer, Integer> coord, PNGMetadata pm, RGB rgb, VBox pane);

	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane);

	public T get(PNGMetadata pm, RGB rgb);

	public T fromString(PNGMetadata pm, RGB rgb, String text);

	public String toString(PNGMetadata pm, RGB rgb);

	public void add(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coord);
}