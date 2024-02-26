package com.mandarina.lvlbuilder.feature;

import java.util.Iterator;
import java.util.List;

import com.mandarina.lvlbuilder.LvlBuilderUtil;
import com.mandarina.lvlbuilder.RGB;
import com.mandarina.utilz.LoadSave;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class NosolidFeatureManager implements FeatureManager<List<Pair<Integer, Integer>>> {

	@Override
	public TileFeature getTileFeature() {
		return TileFeature.NOSOLID;
	}

	@Override
	public void applyTo(Pair<Integer, Integer> coord, PNGMetadata pm, RGB rgb, VBox pane) {
		if (isApplicable()) {
			apply(pm, rgb, coord, pane);
		}
	}

	private boolean isApplicable() {
		return true;
	}

	@Override
	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane) {
		List<Pair<Integer, Integer>> values = get(pm, rgb);
		if (values != null) {
			for (Pair<Integer, Integer> coord : values) {
				ImageView iv = LvlBuilderUtil.getImageView(coord, pane);
				if (iv != null) {
					applyTo(coord, null, rgb, pane);
				}
			}
		}
	}

	@Override
	public List<Pair<Integer, Integer>> get(PNGMetadata pm, RGB rgb) {
		return PairListMetadata.get(pm, rgb, getTileFeature().getKeyCode());
	}

	@Override
	public List<Pair<Integer, Integer>> fromString(PNGMetadata pm, RGB rgb, String text) {
		return PairListMetadata.fromString(pm, rgb, text);
	}

	@Override
	public String toString(PNGMetadata pm, RGB rgb) {
		return PairListMetadata.toString(pm, rgb, getTileFeature().getKeyCode());
	}

	@Override
	public void add(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coord) {
		PairListMetadata.add(pm, rgb, coord, getTileFeature().getKeyCode());
	}

	private void apply(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coord, VBox pane) {
		AnchorPane ap = LvlBuilderUtil.getSquare(coord, pane);
		FlowPane fp = LvlBuilderUtil.getOrCreateFlowPane(ap);
		if (!exist(fp)) {
			ImageView iv = new ImageView(LoadSave.GetFeature(getTileFeature().getIcon()));
			iv.setId(getID());
			fp.getChildren().add(iv);
			if (pm != null) {
				add(pm, rgb, coord);
			}
		}
	}

	private boolean exist(FlowPane fp) {
		boolean existsNodeWithIdHola = false;
		Iterator<Node> iterator = fp.getChildren().iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (getID().equals(node.getId())) {
				existsNodeWithIdHola = true;
				break;
			}
		}
		return existsNodeWithIdHola;
	}

	private String getID() {
		return getTileFeature().name();
	}
}
