package com.mandarina.utilz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class LoadSave {

	public static final String LEVEL_ATLAS = "outside_sprites.png";
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
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	public static final String OPTIONS_MENU = "options_background.png";
	public static final String QUESTION_ATLAS = "question_atlas.png";
	public static final String EXCLAMATION_ATLAS = "exclamation_atlas.png";
	public static final String CREDITS = "credits_list.png";
	public static final String GRASS_ATLAS = "grass_atlas.png";
	public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
	public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String RAIN_PARTICLE = "rain_particle.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";
	public static final String SHIP = "ship.png";

	public static Image GetSpriteAtlas(String fileName) {
		Image img = null;
		Path path = Paths.get("assets", fileName); // Adjust the path based on your project structure

		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			img = new Image(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}
	
	public static Image[][] getAnimations(int xSize, int ySize, int spriteW, int spriteH, Image img) {
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
		Path folderPath = Paths.get("assets/lvls"); // Adjust the path based on your project structure
		List<Image> images = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
			stream.forEach(path -> {
				try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
					images.add(new Image(is));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return images.toArray(new Image[0]);
	}
}