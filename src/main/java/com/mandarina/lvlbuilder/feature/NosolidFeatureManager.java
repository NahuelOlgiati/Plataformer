package com.mandarina.lvlbuilder.feature;

import java.util.List;

import com.mandarina.game.geometry.Point;
import com.mandarina.lvlbuilder.LvlBuilderUtil;
import com.mandarina.lvlbuilder.RGB;
import com.mandarina.utilz.LoadSave;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class NosolidFeatureManager implements FeatureManager<List<Point>> {

	@Override
	public TileFeature getTileFeature() {
		return TileFeature.NOSOLID;
	}

	@Override
	public void applyTo(Point coord, PNGMetadata pm, RGB rgb, VBox pane) {
		if (isApplicable()) {
			apply(pm, rgb, coord, pane);
		}
	}

	private boolean isApplicable() {
		return true;
	}

	@Override
	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane) {
		List<Point> values = get(pm, rgb);
		if (values != null) {
			for (Point coord : values) {
				ImageView iv = LvlBuilderUtil.getImageView(coord, pane);
				if (iv != null) {
					applyTo(coord, null, rgb, pane);
				}
			}
		}
	}

	@Override
	public List<Point> get(PNGMetadata pm, RGB rgb) {
		return PointListMetadata.get(pm, rgb, getTileFeature().getKeyCode());
	}

	@Override
	public List<Point> fromString(PNGMetadata pm, RGB rgb, String text) {
		return PointListMetadata.fromString(pm, rgb, text);
	}

	@Override
	public String toString(PNGMetadata pm, RGB rgb) {
		return PointListMetadata.toString(pm, rgb, getTileFeature().getKeyCode());
	}

	@Override
	public void add(PNGMetadata pm, RGB rgb, Point coord) {
		PointListMetadata.add(pm, rgb, coord, getTileFeature().getKeyCode());
	}

	private void apply(PNGMetadata pm, RGB rgb, Point coord, VBox pane) {
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
		for (Node node : fp.getChildren()) {
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
