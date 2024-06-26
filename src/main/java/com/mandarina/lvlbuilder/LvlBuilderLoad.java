package com.mandarina.lvlbuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mandarina.utilz.LoadSave;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

public class LvlBuilderLoad {

	public static URL GetCSS() {
		return LoadSave.GetCSS("lvlbuilder.css");
	}

	public static List<AnchorPane> getItems(RGB rgb) {
		List<AnchorPane> items = new ArrayList<AnchorPane>();
		LvlBuilderImage[] getAllRGB = LoadSave.GetAllRGB(rgb);
		for (LvlBuilderImage image : getAllRGB) {
			if (image.isTiled()) {
				LvlBuilderImage[] valuedImages = getTiledImages(image);
				items.addAll(getBoxes(valuedImages));
			} else {
				items.addAll(getBoxes(image));
			}
		}
		return items;
	}

	public static LvlBuilderImage getImage(RGB rgb, int value) {
		for (LvlBuilderImage image : LoadSave.GetAllRGB(rgb)) {
			if (image.isTiled()) {
				if (value >= image.getFirstValue() && value <= image.getLastValue()) {
					return getTile(image, value);
				}
			} else {
				if (image.getValue() == value) {
					return image;
				}
			}
		}
		return null;
	}

	private static List<AnchorPane> getBoxes(LvlBuilderImage... images) {
		List<AnchorPane> vboxCollection = new ArrayList<AnchorPane>();
		for (LvlBuilderImage image : images) {
			ImageView imageView = new ImageView(image);
			AnchorPane square = LvlBuilderUtil.newSelectableVBox(imageView);
			vboxCollection.add(square);
		}
		return vboxCollection;
	}

	private static LvlBuilderImage[] getTiledImages(LvlBuilderImage img) {
		LvlBuilderImage[] levelSprite = new LvlBuilderImage[img.getLastValue()];
		for (int i = img.getFirstValue(); i < img.getLastValue(); i++) {
			levelSprite[i] = getTile(img, i);
		}
		return levelSprite;
	}

	private static LvlBuilderImage getTile(LvlBuilderImage img, int value) {
		LvlBuilderImage vi = null;
		for (int i = img.getFirstValue(); i < img.getLastValue(); i++) {
			if (i == value) {
				return writeTileImage(img, i);
			}
		}
		return vi;
	}

	private static LvlBuilderImage writeTileImage(LvlBuilderImage img, int i) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i * LvlBuilderCts.TILE_WIDTH, 0,
					LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(wi, null);
			ImageIO.write(bufferedImage, "png", outputStream);
			try (InputStream is = new ByteArrayInputStream(outputStream.toByteArray())) {
				LvlBuilderImage image = new LvlBuilderImage(is, img.getResource());
				image.setValue(i);
				return image;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
