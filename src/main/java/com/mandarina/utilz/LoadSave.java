package com.mandarina.utilz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LoadSave {

	private static ClassLoader cl = Thread.currentThread().getContextClassLoader();

	public static final String APP_ICON = "rayman.png";
	public static final String OUTSIDE = "outside.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String POTION = "potions.png";
	public static final String CONTAINER = "objects.png";
	public static final String TRAP = "trap.png";
	public static final String CANNON = "cannon.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "options_background.png";
	public static final String QUESTION = "question.png";
	public static final String EXCLAMATION = "exclamation.png";
	public static final String CREDITS = "credits_list.png";
	public static final String GRASS = "grass.png";
	public static final String TREE_ONE = "tree_one.png";
	public static final String TREE_TWO = "tree_two.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String RAIN_PARTICLE = "rain_particle.png";
	public static final String WATER = "water.png";
	public static final String WATER_BOTTOM = "water.png";
	public static final String SHIP = "ship.png";

	public static Image GetAppIcon() {
		return GetImage(Paths.get("assets", APP_ICON));
	}

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

	private static LvlBuilderImage GetLvlImage(Path path) {
		try {
			return Stream.of(new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path)))
					.findFirst().map(r -> {
						try (InputStream is = r.getInputStream()) {
							return new LvlBuilderImage(is, r);
						} catch (IOException e) {
							System.out.println(e);
							return null;
						}
					}).orElse(null);
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

	public static int GetNumOfLevels() {
		return getCount(Paths.get("assets", "lvls"));
	}

	private static int getCount(Path path) {
		try {
			return (int) Stream
					.of(new PathMatchingResourcePatternResolver(cl).getResources(pathNormalization(path) + "/*"))
					.count();
		} catch (IOException e) {
			System.out.println(e);
		}
		return 0;
	}

	private static String pathNormalization(Path path) {
		return path.toString().replace('\\', '/');
	}

	public static WritableImage GetSubimage(Image img, int x, int y, int width, int height) {
		return new WritableImage(img.getPixelReader(), x * width, y * height, width, height);
	}
}