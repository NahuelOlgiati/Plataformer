package com.mandarina.utilz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.RGB;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LoadSave {

	private static ClassLoader cl = Thread.currentThread().getContextClassLoader();

	public static Image GetAtlas(String fileName) {
		return GetImage(Paths.get("assets", "atlas", fileName));
	}

	public static Image GetFeature(String fileName) {
		return GetImage(Paths.get("assets", "feature", fileName));
	}

	public static Image GetSprite(String fileName) {
		return GetImage(Paths.get("assets", fileName));
	}

	private static Image GetImage(Path path) {
		return new Image(cl.getResourceAsStream(pathNormalization(path)));
	}

	public static Image[] GetAnimations(int size, int spriteW, int spriteH, Image img) {
		Image[] tempArr = new Image[size];
		for (int i = 0; i < size; i++) {
			tempArr[i] = new WritableImage(img.getPixelReader(), i * spriteW, 0, spriteW, spriteH);
		}
		return tempArr;
	}

	public static Image[][] GetAnimations(int xSize, int ySize, int spriteW, int spriteH, Image img) {
		Image[][] tempArr = new Image[ySize][xSize];
		for (int j = 0; j < ySize; j++) {
			for (int i = 0; i < xSize; i++) {
				tempArr[j][i] = new WritableImage(img.getPixelReader(), i * spriteW, j * spriteH, spriteW, spriteH);
			}
		}
		return tempArr;
	}

	public static LvlBuilderImage GetLevel(int num) {
		return GetLvlImage(Paths.get("assets", "lvls", num + ".png"));
	}

	public static LvlBuilderImage GetLevel(File folder, int num) {
		try {
			return new LvlBuilderImage(new File(folder, num + ".png"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static LvlBuilderImage GetLvlImage(Path path) {
		try {
			return Stream.of(new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path)))
					.findFirst().map(r -> {
						try (InputStream is = r.getInputStream()) {
							return new LvlBuilderImage(is, r);
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
					}).orElse(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int GetNumOfLevels() {
		return GetCount(Paths.get("assets", "lvls"));
	}

	private static int GetCount(Path path) {
		try {
			return new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path) + "/*").length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Integer GetNumOfLevels(File directory) {
		int count = 0;
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					count++;
				}
			}
		}
		return count;
	}

	private static String pathNormalization(Path path) {
		return path.toString().replace('\\', '/');
	}

	public static WritableImage GetSubimage(Image img, int x, int y, int width, int height) {
		return new WritableImage(img.getPixelReader(), x * width, y * height, width, height);
	}

	public static URL GetCSS(String cssFileName) {
		return cl.getResource(pathNormalization(Paths.get("assets", "css")) + "/" + cssFileName);
	}

	public static LvlBuilderImage[] GetAllRGB() {
		return GetRGB(Paths.get("assets", "rgb"), "*");
	}

	public static LvlBuilderImage[] GetAllRGB(RGB rgb) {
		return GetRGB(Paths.get("assets", "rgb"), rgb.getValue() + "_*");
	}

	private static LvlBuilderImage[] GetRGB(Path path, String regex) {
		try {
			return Stream
					.of(new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path) + "/" + regex))
					.map(r -> {
						try (InputStream is = r.getInputStream()) {
							return new LvlBuilderImage(is, r);
						} catch (Throwable e) {
							e.printStackTrace();
							return null;
						}
					}).toArray(LvlBuilderImage[]::new);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}