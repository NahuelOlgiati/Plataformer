package com.mandarina.game.leveldata;

import com.mandarina.utilz.LoadSave;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Background {

	Image backgroundSprite;

	public Background() {
		this.backgroundSprite = LoadSave.GetSprite(LoadSave.PLAYING_BG_IMG);
		this.backgroundSprite = darkerBackground(backgroundSprite, 1);
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		g.drawImage(backgroundSprite, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
	}

	public static Image darkerBackground(Image originalImage, double factor) {
		int width = (int) originalImage.getWidth();
		int height = (int) originalImage.getHeight();

		WritableImage darkenedImage = new WritableImage(width, height);
		PixelReader pixelReader = originalImage.getPixelReader();
		PixelWriter pixelWriter = darkenedImage.getPixelWriter();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = pixelReader.getColor(x, y);
				double red = color.getRed() * factor;
				double green = color.getGreen() * factor;
				double blue = color.getBlue() * factor;

				// Clamp values to stay within the [0, 1] range
				red = Math.min(Math.max(red, 0.0), 1.0);
				green = Math.min(Math.max(green, 0.0), 1.0);
				blue = Math.min(Math.max(blue, 0.0), 1.0);

				Color darkenedColor = new Color(red, green, blue, color.getOpacity());
				pixelWriter.setColor(x, y, darkenedColor);
			}
		}

		return darkenedImage;
	}

}
