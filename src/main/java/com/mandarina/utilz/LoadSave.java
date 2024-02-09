package com.mandarina.utilz;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class LoadSave {

	private static ClassLoader cl = Thread.currentThread().getContextClassLoader();

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

	public static Image GetAtlas(String fileName) {
		return GetImage(Paths.get("assets", "atlas", fileName));
	}

	public static Image GetSprite(String fileName) {
		return GetImage(Paths.get("assets", fileName));
	}

	public static Image GetLvl(String fileName) {
		return GetImage(Paths.get("assets", "lvls", fileName));
	}

	private static Image GetImage(Path path) {
		return new Image(cl.getResourceAsStream(path.toString().replace('\\', '/')));
	}

	public static Image[] GetAnimations(int size, int spriteW, int spriteH, Image img) {
		Image[] tempArr = new Image[size];
		PixelReader pixelReader = img.getPixelReader();
		for (int i = 0; i < size; i++) {
			tempArr[i] = new WritableImage(pixelReader, i * spriteW, 0, spriteW, spriteH);
		}
		return tempArr;
	}

	public static Image[][] GetAnimations(int xSize, int ySize, int spriteW, int spriteH, Image img) {
		Image[][] tempArr = new Image[ySize][xSize];
		PixelReader pixelReader = img.getPixelReader();
		for (int j = 0; j < ySize; j++) {
			for (int i = 0; i < xSize; i++) {
				tempArr[j][i] = new WritableImage(pixelReader, i * spriteW, j * spriteH, spriteW, spriteH);
			}
		}
		return tempArr;
	}

	public static Image[] GetAllLevels() {
		List<Image> images = new ArrayList<>();
		try {
			images.add(GetLvl("1.png"));
			images.add(GetLvl("2.png"));
			images.add(GetLvl("3.png"));
			images.add(GetLvl("4.png"));
			images.add(GetLvl("5.png"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return images.toArray(new Image[0]);
	}
}