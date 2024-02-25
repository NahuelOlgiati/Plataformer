package com.mandarina.lvlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mandarina.utilz.LoadSave;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public enum TileFeature {

	TRAVERSABLE(KeyCode.T, "traversable.png") {
		@Override
		public void apply(Pair<Integer, Integer> coord, RGB rgb, VBox pane) {
			if (isApplicable()) {
				apply(coord, pane);
			}
		}

		private boolean isApplicable() {
			return true;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void apply(PNGMetadata metadata, RGB rgb, VBox pane) {
			List<Pair<Integer, Integer>> values = (List<Pair<Integer, Integer>>) get(metadata, rgb);
			if (values != null) {
				for (Pair<Integer, Integer> coord : values) {
					ImageView iv = LvlBuilderUtil.getImageView(coord, pane);
					if (iv != null) {
						apply(coord, rgb, pane);
					}
				}
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public Object get(PNGMetadata pm, RGB rgb) {
			String key = PNGMetadata.getKey(rgb, this.getKeyCode());
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
			String key = PNGMetadata.getKey(rgb, this.getKeyCode());
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
			String key = PNGMetadata.getKey(rgb, this.getKeyCode());
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
	};

	private KeyCode keyCode;
	private String icon;

	public abstract void apply(Pair<Integer, Integer> coord, RGB rgb, VBox pane);

	public abstract void apply(PNGMetadata metadata, RGB rgb, VBox pane);

	public abstract Object get(PNGMetadata pm, RGB rgb);

	public abstract Object fromString(PNGMetadata pm, RGB rgb, String text);

	public abstract String toString(PNGMetadata pm, RGB rgb);

	public abstract void add(PNGMetadata pm, RGB rgb, SelectedTile selectedTile);

	TileFeature(KeyCode keyCode, String icon) {
		this.keyCode = keyCode;
		this.icon = icon;
	}

	private static void apply(Pair<Integer, Integer> coord, VBox pane) {
		AnchorPane ap = LvlBuilderUtil.getSquare(coord, pane);
		ImageView i2 = new ImageView(LoadSave.GetFeature("0.png"));
		ImageView i3 = new ImageView(LoadSave.GetFeature("1.png"));
		ImageView i4 = new ImageView(LoadSave.GetFeature("2.png"));
		ImageView i5 = new ImageView(LoadSave.GetFeature("3.png"));
		ImageView i6 = new ImageView(LoadSave.GetFeature("4.png"));
		ImageView i7 = new ImageView(LoadSave.GetFeature("nosolid.png"));
		FlowPane fp = new FlowPane();
		fp.setMaxWidth(LvlBuilderCts.TILE_WIDTH);
		fp.getChildren().addAll(i2, i3, i4, i5, i6, i7);
		ap.getChildren().add(fp);
	}

	public KeyCode getKeyCode() {
		return keyCode;
	}

	public String getIcon() {
		return icon;
	}
}