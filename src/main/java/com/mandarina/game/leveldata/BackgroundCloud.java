package com.mandarina.game.leveldata;

import java.util.Random;

import com.mandarina.game.constants.EnvCts;
import com.mandarina.game.constants.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BackgroundCloud {

	private Image bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	public BackgroundCloud() {
		bigCloud = LoadSave.GetSprite(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSprite(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * GameCts.SCALE) + rnd.nextInt((int) (100 * GameCts.SCALE));
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud, i * EnvCts.BIG_CLOUD_WIDTH - (int) (lvlOffsetX * 0.3), (int) (204 * GameCts.SCALE),
					EnvCts.BIG_CLOUD_WIDTH, EnvCts.BIG_CLOUD_HEIGHT);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, EnvCts.SMALL_CLOUD_WIDTH * 4 * i - (int) (lvlOffsetX * 0.7), smallCloudsPos[i],
					EnvCts.SMALL_CLOUD_WIDTH, EnvCts.SMALL_CLOUD_HEIGHT);
	}

}
