package com.mandarina.game.leveldata;

import java.util.Random;

import com.mandarina.utilz.LoadSave;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
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

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud, i * BackgroundCloudCts.BIG_CLOUD_WIDTH - (int) (lvlOffsetX * 0.3), (int) (204 * GameCts.SCALE),
					BackgroundCloudCts.BIG_CLOUD_WIDTH, BackgroundCloudCts.BIG_CLOUD_HEIGHT);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, BackgroundCloudCts.SMALL_CLOUD_WIDTH * 4 * i - (int) (lvlOffsetX * 0.7), smallCloudsPos[i],
					BackgroundCloudCts.SMALL_CLOUD_WIDTH, BackgroundCloudCts.SMALL_CLOUD_HEIGHT);
	}

}
