package com.mandarina.lvlbuilder.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mandarina.lvlbuilder.RGB;
import com.mandarina.utilz.Point;

import javafx.scene.input.KeyCode;

public class PointListMetadata {

	@SuppressWarnings("unchecked")
	public static List<Point> get(PNGMetadata pm, RGB rgb, KeyCode keycode) {
		String key = PNGMetadata.getKey(rgb, keycode);
		return (List<Point>) pm.getMetadata().get(key);
	}

	public static List<Point> fromString(PNGMetadata pm, RGB rgb, String text) {
		List<Point> fs = new ArrayList<Point>();
		if (text != null) {
			String[] pairs = text.split("-");
			for (String pair : pairs) {
				String[] values = pair.split("x");
				try {
					Integer x = Integer.parseInt(values[0]);
					Integer y = Integer.parseInt(values[1]);
					fs.add(new Point(x, y));
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
		List<Point> values = get(pm, rgb, keycode);
		if (values != null) {
			int size = values.size();
			for (Point p : values) {
				Integer x = p.getX();
				Integer y = p.getY();
				result.append(x).append("x").append(y);
				if (++counter < size) {
					result.append("-");
				}
			}
		}
		return result.toString();
	}

	@SuppressWarnings("unchecked")
	public static void add(PNGMetadata pm, RGB rgb, Point coords, KeyCode keycode) {
		String key = PNGMetadata.getKey(rgb, keycode);
		Map<String, Object> metadata = pm.getMetadata();
		if (pm.getMetadata().containsKey(key)) {
			List<Point> inner = (List<Point>) metadata.get(key);
			inner.add(coords);
		} else {
			ArrayList<Point> inner = new ArrayList<Point>();
			inner.add(coords);
			metadata.put(key, inner);
		}
	}

	public static void remove(PNGMetadata pm, RGB rgb, KeyCode keyCode, Point coord) {
		if (pm != null) {
			List<Point> list = get(pm, rgb, keyCode);
			list.remove(coord);
		}
	}
}
