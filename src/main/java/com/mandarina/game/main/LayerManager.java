package com.mandarina.game.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mandarina.game.gamestates.Offset;

public abstract class LayerManager<T> {

	private int layerDefault;

	private List<T> layer1;
	private List<T> layer2;
	private List<T> layer3;
	private List<T> layer4;

	private T[] l1;
	private T[] l2;
	private T[] l3;
	private T[] l4;

	private T[] items;

	public LayerManager() {
		this(1);
	}

	public LayerManager(int layerDefault) {
		this.layerDefault = layerDefault;
	}

	public void add(T element, int num) {
		getLayer(num).add(element);
	}

	public void add(T element) {
		getLayer(layerDefault).add(element);
	}

	public List<T> getLayer(int num) {
		return switch (num) {
		case 1 -> layer1 = getOrCreate(layer1);
		case 2 -> layer2 = getOrCreate(layer2);
		case 3 -> layer3 = getOrCreate(layer3);
		case 4 -> layer4 = getOrCreate(layer4);
		default -> getLayer(layerDefault);
		};
	}

	private List<T> getOrCreate(List<T> layer) {
		if (layer == null) {
			layer = new ArrayList<>();
		}
		return layer;
	}

	public List<T> getLayer() {
		return getLayer(layerDefault);
	}

	@SuppressWarnings("unchecked")
	public void consolidate() {
		Class<T> clazz = getClazz();
		this.l1 = layer1 == null ? null : layer1.toArray((T[]) Array.newInstance(clazz, layer1.size()));
		this.l2 = layer2 == null ? null : layer2.toArray((T[]) Array.newInstance(clazz, layer2.size()));
		this.l3 = layer3 == null ? null : layer3.toArray((T[]) Array.newInstance(clazz, layer3.size()));
		this.l4 = layer4 == null ? null : layer4.toArray((T[]) Array.newInstance(clazz, layer4.size()));
		this.items = Stream.of(l1, l2, l3, l4).filter(arr -> arr != null).flatMap(Stream::of)
				.toArray(size -> (T[]) Array.newInstance(clazz, size));
		this.layer1 = null;
		this.layer2 = null;
		this.layer3 = null;
		this.layer4 = null;
	}

	public T[] getL(int num) {
		return switch (num) {
		case 1 -> l1;
		case 2 -> l2;
		case 3 -> l3;
		case 4 -> l4;
		default -> getL(layerDefault);
		};
	}

	public abstract Class<T> getClazz();

	protected abstract void draw(T t, GameDrawer g, Offset offset);

	private void drawLayer(int num, GameDrawer g, Offset offset) {
		T[] l = getL(num);
		if (l != null) {
			for (T t : l) {
				draw(t, g, offset);
			}
		}
	}

	public void drawL1(GameDrawer g, Offset offset) {
		drawLayer(1, g, offset);
	}

	public void drawL2(GameDrawer g, Offset offset) {
		drawLayer(2, g, offset);
	}

	public void drawL3(GameDrawer g, Offset offset) {
		drawLayer(3, g, offset);
	}

	public void drawL4(GameDrawer g, Offset offset) {
		drawLayer(4, g, offset);
	}

	public T[] getItems() {
		return items;
	}
}
