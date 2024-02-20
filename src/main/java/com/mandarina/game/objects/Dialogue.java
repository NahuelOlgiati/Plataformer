package com.mandarina.game.objects;

import com.mandarina.game.main.GameCts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Dialogue {

	private int x, y, type;
	private int aniIndex, aniTick;
	private boolean active = true;

	public Dialogue(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void update() {
		aniTick++;
		if (aniTick >= GameCts.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 5) {
				active = false;
				aniIndex = 0;
			}
		}
	}

	public void deactive() {
		active = false;
	}

	public void reset(int x, int y) {
		this.x = x;
		this.y = y;
		active = true;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getType() {
		return type;
	}

	public boolean isActive() {
		return active;
	}

	public static Image[] loadQuestions() {
		return LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(LoadSave.QUESTION));
	}

	public static Image[] loadExclamations() {
		return LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(LoadSave.EXCLAMATION));
	}
}
