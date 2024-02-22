package com.mandarina.lvlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mandarina.utilz.LoadSave;

import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public enum TileFeature {

	TRAVERSABLE(KeyCode.T, "traversable.png") {
		@Override
		public void apply(ImageView iw) {
			if (isApplicable(iw)) {
				apply(iw, getIcon());
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public void apply(PNGMetadata metadata, RGB rgb, ScrollPane mainPane) {
			List<Pair<Integer, Integer>> values = (List<Pair<Integer, Integer>>) get(metadata, rgb);
			if (values != null) {
				for (Pair<Integer, Integer> p : values) {
					Integer x = p.getKey();
					Integer y = p.getValue();
					VBox mainPaneBox = (VBox) mainPane.getContent();
					HBox row = (HBox) mainPaneBox.getChildren().get(y);
					VBox square = (VBox) row.getChildren().get(x);
					ImageView iv = (ImageView) square.getChildren().get(0);
					apply(iv);
				}
			}
		}

		@Override
		public boolean isApplicable(ImageView iw) {
			return true;
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
				inner.add(new Pair<Integer, Integer>(st.getX(), st.getY()));
			} else {
				ArrayList<Pair<Integer, Integer>> inner = new ArrayList<Pair<Integer, Integer>>();
				inner.add(new Pair<Integer, Integer>(st.getX(), st.getY()));
				metadata.put(key, inner);
			}
		}
	};

	private KeyCode keyCode;
	private String icon;

	public abstract void apply(ImageView iw);

	public abstract void apply(PNGMetadata metadata, RGB rgb, ScrollPane mainPane);

	public abstract boolean isApplicable(ImageView iw);

	public abstract Object get(PNGMetadata pm, RGB rgb);

	public abstract Object fromString(PNGMetadata pm, RGB rgb, String text);

	public abstract String toString(PNGMetadata pm, RGB rgb);

	public abstract void add(PNGMetadata pm, RGB rgb, SelectedTile selectedTile);

	TileFeature(KeyCode keyCode, String icon) {
		this.keyCode = keyCode;
		this.icon = icon;
	}

	private static void apply(ImageView iw, String icon) {
		Blend blend = new Blend();
		LvlBuilderImage image = (LvlBuilderImage) iw.getImage();
		Image featureImg = LoadSave.GetFeature(icon);
		ImageInput imageInput = new ImageInput(featureImg, iw.getX() + image.getWidth() - featureImg.getWidth() - 2,
				iw.getY() + image.getHeight() - featureImg.getHeight() - 2);
		blend.setTopInput(imageInput);
		blend.setMode(BlendMode.ADD);
		iw.setEffect(blend);
	}

	public KeyCode getKeyCode() {
		return keyCode;
	}

	public String getIcon() {
		return icon;
	}
}