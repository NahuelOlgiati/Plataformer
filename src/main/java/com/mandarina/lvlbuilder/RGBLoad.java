package com.mandarina.lvlbuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RGBLoad {

	public static List<VBox> getItems(String prefix) {
		List<VBox> items = new ArrayList<>();
		for (Path path : getRGBFiles(prefix)) {
			if (isTiledImage(path)) {
				ValuedImage[] valuedImages = getValuedImages(new TiledImage(path));
				items.addAll(getBoxes(valuedImages));
			} else {
				ValuedImage vi = new ValuedImage(path);
				items.addAll(getBoxes(vi));
			}
		}
		return items;
	}

	private static List<Path> getRGBFiles(String prefix) {
		try {
			return Files.walk(Paths.get("assets", "rgb"), 1).filter(Files::isRegularFile).filter(p -> {
				return p.getFileName().toString().startsWith(prefix);
			}).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ValuedImage getImage(String prefix, int value) {
		for (Path path : getRGBFiles(prefix)) {
			if (isTiledImage(path)) {
				TiledImage ti = new TiledImage(path);
				if (value >= ti.getFirstValue() && value <= ti.getLastValue()) {
					return getValuedImage(ti, value);
				}
			} else {
				ValuedImage vi = new ValuedImage(path);
				if (vi.getValue() == value) {
					return vi;
				}
			}
		}
		return null;
	}

	private static List<VBox> getBoxes(ValuedImage... images) {
		List<VBox> vboxCollection = new ArrayList<>();
		for (ValuedImage image : images) {
			ImageView imageView = new ImageView(image);
			LvlBuilderUtil.setFitSize(imageView, LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);
			VBox square = LvlBuilderUtil.newSelectableVBox(LvlBuilder.TILE_WIDTH);
			square.getChildren().add(imageView);
			vboxCollection.add(square);
		}
		return vboxCollection;
	}

	public static boolean isTiledImage(Path path) {
		String[] parts = path.getFileName().toString().split("_");
		try {
			Integer.parseInt(parts[1]);
			Integer.parseInt(parts[2]);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	private static ValuedImage[] getValuedImages(TiledImage timg) {
		ValuedImage[] levelSprite = new ValuedImage[timg.getLastValue()];
		for (int i = timg.getFirstValue(); i < timg.getLastValue(); i++) {
			levelSprite[i] = new ValuedImage(timg.getPixelReader(), i * LvlBuilder.TILE_WIDTH, 0, LvlBuilder.TILE_WIDTH,
					LvlBuilder.TILE_HEIGHT, timg.getFirstValue() + i);
		}
		return levelSprite;
	}

	private static ValuedImage getValuedImage(TiledImage timg, int value) {
		ValuedImage vi = null;
		for (int i = timg.getFirstValue(); i < timg.getLastValue(); i++) {
			if (i == value) {
				return new ValuedImage(timg.getPixelReader(), i * LvlBuilder.TILE_WIDTH, 0, LvlBuilder.TILE_WIDTH,
						LvlBuilder.TILE_HEIGHT, timg.getFirstValue() + i);
			}
		}
		return vi;
	}
}
