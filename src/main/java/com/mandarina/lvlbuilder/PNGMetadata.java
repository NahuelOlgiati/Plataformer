package com.mandarina.lvlbuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;

public class PNGMetadata {

	private Map<String, Map<Integer, Integer>> metadata;

	public PNGMetadata() {
		this.metadata = new HashMap<String, Map<Integer, Integer>>();
	}

	public PNGMetadata(LvlBuilderImage img) {
		this.metadata = new HashMap<String, Map<Integer, Integer>>();
		LvlBuilderMetada.readFeatures(img, this);
	}

	public void add(RGB rgb, KeyCode keyCode, Map<Integer, Integer> coordMap) {
		metadata.put(getKey(rgb, keyCode), coordMap);
	}

	public static String getKey(RGB rgb, KeyCode keyCode) {
		return rgb.getValue() + "-" + keyCode.getName();
	}

	public void add(RGB rgb, KeyCode keyCode, Integer x, Integer y) {
		String key = getKey(rgb, keyCode);
		if (metadata.containsKey(key)) {
			Map<Integer, Integer> coordMap = metadata.get(key);
			coordMap.put(x, y);
		} else {
			Map<Integer, Integer> coordMap = new HashMap<>();
			coordMap.put(x, y);
			metadata.put(key, coordMap);
		}
	}

	public void remove(RGB rgb, KeyCode keyCode, Integer x, Integer y) {
		String key = getKey(rgb, keyCode);
		if (metadata.containsKey(key)) {
			Map<Integer, Integer> coordMap = metadata.get(key);
			if (coordMap.containsKey(x) && coordMap.get(x).equals(y)) {
				coordMap.remove(x);
				if (coordMap.isEmpty()) {
					metadata.remove(key);
				}
			}
		}
	}

	public Map<Integer, Integer> get(RGB rgb, KeyCode keyCode) {
		return metadata.get(getKey(rgb, keyCode));
	}

	public String toString(String key) {
		StringBuilder result = new StringBuilder();
		if (metadata.containsKey(key)) {
			Map<Integer, Integer> innerMap = metadata.get(key);
			int size = innerMap.size();
			int counter = 0;
			for (Map.Entry<Integer, Integer> entry : innerMap.entrySet()) {
				Integer x = entry.getKey();
				Integer y = entry.getValue();
				result.append(x).append("x").append(y);
				if (++counter < size) {
					result.append("-");
				}
			}
		}
		return result.toString();
	}

	public Map<Integer, Integer> getCoords(String valuesString) {
		Map<Integer, Integer> innerMap = new HashMap<>();
		if (valuesString != null) {
			String[] pairs = valuesString.split("-");
			for (String pair : pairs) {
				String[] values = pair.split("x");
				try {
					Integer x = Integer.parseInt(values[0]);
					Integer y = Integer.parseInt(values[1]);
					innerMap.put(x, y);
				} catch (NumberFormatException e) {
					System.err.println("Invalid format: " + pair);
				}
			}
		}
		return innerMap;
	}

	public void log() {
		for (Entry<String, Map<Integer, Integer>> e : metadata.entrySet()) {
			System.out.println(e.getKey());
			for (Entry<Integer, Integer> d : e.getValue().entrySet()) {
				System.out.println(d.getKey() + "x" + d.getValue());
			}
		}
	}

	public Map<String, Map<Integer, Integer>> getMetadata() {
		return metadata;
	}
}
