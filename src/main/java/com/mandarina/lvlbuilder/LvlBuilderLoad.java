package com.mandarina.lvlbuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

public class LvlBuilderLoad {

	private static ClassLoader cl = Thread.currentThread().getContextClassLoader();

	private static String pathNormalization(Path path) {
		return path.toString().replace('\\', '/');
	}

	public static LvlBuilderImage[] GetAllRGB() {
		return getImages(Paths.get("assets", "rgb"), "*");
	}

	public static LvlBuilderImage[] GetAllRGB(RGB rgb) {
		return getImages(Paths.get("assets", "rgb"), rgb.getValue() + "_*");
	}

	private static LvlBuilderImage[] getImages(Path path, String regex) {
		try {
			return Stream
					.of(new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path) + "/" + regex))
					.map(r -> {
						try (InputStream inputStream = r.getInputStream()) {
							return new LvlBuilderImage(inputStream, r.getFilename());
						} catch (Throwable e) {
							System.out.println(e);
							return null;
						}
					}).toArray(LvlBuilderImage[]::new);
		} catch (Throwable e) {
			System.out.println(e);
		}
		return null;
	}

	public static List<VBox> getItems(RGB rgb) {
		List<VBox> items = new ArrayList<>();
		LvlBuilderImage[] getAllRGB = GetAllRGB(rgb);
		for (LvlBuilderImage image : getAllRGB) {
			if (image.isTiled()) {
				LvlBuilderImage[] valuedImages = getValuedImages(image);
				items.addAll(getBoxes(valuedImages));
			} else {
				items.addAll(getBoxes(image));
			}
		}
		return items;
	}

	public static LvlBuilderImage getImage(RGB rgb, int value) {
		for (LvlBuilderImage image : GetAllRGB(rgb)) {
			if (image.isTiled()) {
				if (value >= image.getFirstValue() && value <= image.getLastValue()) {
					return getValuedImage(image, value);
				}
			} else {
				if (image.getValue() == value) {
					return image;
				}
			}
		}
		return null;
	}

	private static List<VBox> getBoxes(LvlBuilderImage... images) {
		List<VBox> vboxCollection = new ArrayList<>();
		for (LvlBuilderImage image : images) {
			ImageView imageView = new ImageView(image);
			LvlBuilderUtil.setFitSize(imageView, LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);
			VBox square = LvlBuilderUtil.newSelectableVBox(LvlBuilder.TILE_WIDTH);
			square.getChildren().add(imageView);
			vboxCollection.add(square);
		}
		return vboxCollection;
	}

	private static LvlBuilderImage[] getValuedImages(LvlBuilderImage img) {
		LvlBuilderImage[] levelSprite = new LvlBuilderImage[img.getLastValue()];
		for (int i = img.getFirstValue(); i < img.getLastValue(); i++) {
			levelSprite[i] = getTileImage(img, i);
		}
		return levelSprite;
	}

	private static LvlBuilderImage getTileImage(LvlBuilderImage img, int i) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i * LvlBuilder.TILE_WIDTH, 0,
					LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(wi, null);
			ImageIO.write(bufferedImage, "png", outputStream);
			try (InputStream is = new ByteArrayInputStream(outputStream.toByteArray())) {
				return new LvlBuilderImage(is, LvlBuilderImage.getFileName(img.getFileName(), i));
			}
		} catch (Throwable e) {
			System.out.println(e);
		}
		return null;
	}

	private static LvlBuilderImage getValuedImage(LvlBuilderImage img, int value) {
		LvlBuilderImage vi = null;
		for (int i = img.getFirstValue(); i < img.getLastValue(); i++) {
			if (i == value) {
				return getTileImage(img, i);
			}
		}
		return vi;
	}
}
