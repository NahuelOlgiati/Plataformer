package com.mandarina.lvlbuilder;

import javafx.scene.layout.VBox;
import javafx.util.Pair;

public interface FeatureManager {

	public TileFeature getTileFeature();

	public void applyTo(Pair<Integer, Integer> coord, PNGMetadata pm, RGB rgb, VBox pane);

	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane);

	public Object get(PNGMetadata pm, RGB rgb);

	public Object fromString(PNGMetadata pm, RGB rgb, String text);

	public String toString(PNGMetadata pm, RGB rgb);

	public void add(PNGMetadata pm, RGB rgb, SelectedTile selectedTile);
}