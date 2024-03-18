package com.mandarina.game.leveldata;

import java.util.Random;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class BackgroundCloud {

	private Image bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	public BackgroundCloud() {
		bigCloud = LoadSave.GetSprite(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSprite(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		initSmallCloudsPos();
	}

	private void initSmallCloudsPos() {
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = AppStage.Scale(90) + rnd.nextInt(AppStage.Scale(100));
	}

	public void draw(GameDrawer g, Offset offset) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud,
					i * AppStage.Scale(BackgroundCloudCts.BIG_CLOUD_WIDTH_DEFAULT) - (int) (offset.getX() * 0.3),
					AppStage.Scale(204), AppStage.Scale(BackgroundCloudCts.BIG_CLOUD_WIDTH_DEFAULT),
					AppStage.Scale(BackgroundCloudCts.BIG_CLOUD_HEIGHT_DEFAULT));

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud,
					AppStage.Scale(BackgroundCloudCts.SMALL_CLOUD_WIDTH_DEFAULT) * 4 * i - (int) (offset.getX() * 0.7),
					smallCloudsPos[i], AppStage.Scale(BackgroundCloudCts.SMALL_CLOUD_WIDTH_DEFAULT),
					AppStage.Scale(BackgroundCloudCts.SMALL_CLOUD_HEIGHT_DEFAULT));
	}

	public void scale() {
		initSmallCloudsPos();
	}
}
