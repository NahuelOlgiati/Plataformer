package com.mandarina.lvlbuilder.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mandarina.lvlbuilder.RGB;

import javafx.scene.input.KeyCode;
import javafx.util.Pair;

public class PairListMetadata {

	@SuppressWarnings("unchecked")
	public static List<Pair<Integer, Integer>> get(PNGMetadata pm, RGB rgb, KeyCode keycode) {
		String key = PNGMetadata.getKey(rgb, keycode);
		return (List<Pair<Integer, Integer>>) pm.getMetadata().get(key);
	}

	public static List<Pair<Integer, Integer>> fromString(PNGMetadata pm, RGB rgb, String text) {
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

	public static String toString(PNGMetadata pm, RGB rgb, KeyCode keycode) {
		StringBuilder result = new StringBuilder();
		int counter = 0;
		List<Pair<Integer, Integer>> values = get(pm, rgb, keycode);
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

	@SuppressWarnings("unchecked")
	public static void add(PNGMetadata pm, RGB rgb, Pair<Integer, Integer> coords, KeyCode keycode) {
		String key = PNGMetadata.getKey(rgb, keycode);
		Map<String, Object> metadata = pm.getMetadata();
		if (pm.getMetadata().containsKey(key)) {
			List<Pair<Integer, Integer>> inner = (List<Pair<Integer, Integer>>) metadata.get(key);
			inner.add(coords);
		} else {
			ArrayList<Pair<Integer, Integer>> inner = new ArrayList<Pair<Integer, Integer>>();
			inner.add(coords);
			metadata.put(key, inner);
		}
	}

	public static void remove(PNGMetadata pm, RGB rgb, KeyCode keyCode, Pair<Integer, Integer> coord) {
		if (pm != null) {
			List<Pair<Integer, Integer>> list = get(pm, rgb, keyCode);
			list.remove(coord);
		}
	}
}
