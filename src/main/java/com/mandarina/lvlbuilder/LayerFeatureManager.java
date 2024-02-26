package com.mandarina.lvlbuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mandarina.utilz.LoadSave;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class LayerFeatureManager implements FeatureManager {

	private Integer layerNum;

	public LayerFeatureManager(int layerNum) {
		this.layerNum = layerNum;
	}

	@Override
	public TileFeature getTileFeature() {
		return switch (this.layerNum) {
		case 2 -> TileFeature.LAYER2;
		case 3 -> TileFeature.LAYER3;
		case 4 -> TileFeature.LAYER4;
		default -> TileFeature.LAYER1;
		};
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
	@SuppressWarnings("unchecked")
	public void applyFrom(PNGMetadata pm, RGB rgb, VBox pane) {
		List<Pair<Integer, Integer>> values = (List<Pair<Integer, Integer>>) get(pm, rgb);
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
	@SuppressWarnings("unchecked")
	public Object get(PNGMetadata pm, RGB rgb) {
		String key = PNGMetadata.getKey(rgb, getTileFeature().getKeyCode());
		return (List<Pair<Integer, Integer>>) pm.getMetadata().get(key);
	}

	@Override
	public Object fromString(PNGMetadata pm, RGB rgb, String text) {
		List<Pair<Integer, Integer>> fs = new ArrayList<Pair<Integer, Integer>>();
		if (text != null) {
			String[] pairs = text.split("-");
			for (String pair : pairs) {
				String[] values = pair.split("x");
				try {
					Integer x = Integer.parseInt(values[0]);
					Integer y = Integer.parseInt(values[1]);
					fs.add(new Pair<Integer, Integer>(x, y));
				} catch (NumberFormatException e) {
					System.err.println("Invalid format: " + pair);
				}
			}
		}
		return fs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString(PNGMetadata pm, RGB rgb) {
		StringBuilder result = new StringBuilder();
		int counter = 0;
		String key = PNGMetadata.getKey(rgb, getTileFeature().getKeyCode());
		List<Pair<Integer, Integer>> values = (List<Pair<Integer, Integer>>) pm.getMetadata().get(key);
		if (values != null) {
			int size = values.size();
			for (Pair<Integer, Integer> p : values) {
				Integer x = p.getKey();
				Integer y = p.getValue();
				result.append(x).append("x").append(y);
				if (++counter < size) {
					result.append("-");
				}
			}
		}
		return result.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void add(PNGMetadata pm, RGB rgb, SelectedTile st) {
		String key = PNGMetadata.getKey(rgb, getTileFeature().getKeyCode());
		Map<String, Object> metadata = pm.getMetadata();
		if (metadata.containsKey(key)) {
			List<Pair<Integer, Integer>> inner = (List<Pair<Integer, Integer>>) metadata.get(key);
			inner.add(st.getCoords());
		} else {
			ArrayList<Pair<Integer, Integer>> inner = new ArrayList<Pair<Integer, Integer>>();
			inner.add(st.getCoords());
			metadata.put(key, inner);
		}
	}

	private void apply(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coord, VBox pane) {
		AnchorPane ap = LvlBuilderUtil.getSquare(coord, pane);
		FlowPane fp = LvlBuilderUtil.getOrCreateFlowPane(ap);
		Iterator<Node> iterator = fp.getChildren().iterator();
		while (iterator.hasNext()) {
			String id = iterator.next().getId();
			if (id.startsWith("LAYER")) {
				iterator.remove();
				remove(pm, rgb, TileFeature.valueOf(id).getKeyCode(), coord);
			}
		}
		ImageView iv = new ImageView(LoadSave.GetFeature(getTileFeature().getIcon()));
		iv.setId(getID());
		fp.getChildren().add(iv);
		add(pm, rgb, coord);
	}

	@SuppressWarnings("unchecked")
	private void remove(PNGMetadata pm, RGB rgb, KeyCode keyCode, Pair<Integer, Integer> coord) {
		if (pm != null) {
			List<Pair<Integer, Integer>> list = (List<Pair<Integer, Integer>>) get(pm, rgb);
			list.remove(coord);
		}
	}

	@SuppressWarnings("unchecked")
	private void add(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coord) {
		if (pm != null) {
			Object object = get(pm, rgb);
			if (object == null) {
				List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
				list.add(coord);
				pm.add(rgb, getTileFeature().getKeyCode(), list);
			} else {
				List<Pair<Integer, Integer>> list = (List<Pair<Integer, Integer>>) object;
				list.add(coord);
				pm.add(rgb, getTileFeature().getKeyCode(), list);
			}
		}
	}

	private String getID() {
		return getTileFeature().name();
	}
}
