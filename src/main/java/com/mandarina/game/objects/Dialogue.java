package com.mandarina.game.objects;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Dialogue extends GameObject {

	private int aniIndex, aniTick;
	private boolean active = true;

	public Dialogue(Point2D spawn, int type) {
		super(spawn, type);
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

	public void draw(GameDrawer g, double lvlOffsetX, double lvlOffsetY, Image[] dialogueQuestionSprite,
			Image[] dialogueExclamationSprite) {
		if (getObjType() == DialogueCts.QUESTION)
			g.drawImage(dialogueQuestionSprite[getAniIndex()], x - lvlOffsetX, y - lvlOffsetY,
					AppStage.Scale(DialogueCts.DIALOGUE_WIDTH_DEFAULT),
					AppStage.Scale(DialogueCts.DIALOGUE_HEIGHT_DEFAULT));
		else
			g.drawImage(dialogueExclamationSprite[getAniIndex()], x - lvlOffsetX, y - lvlOffsetY,
					AppStage.Scale(DialogueCts.DIALOGUE_WIDTH_DEFAULT),
					AppStage.Scale(DialogueCts.DIALOGUE_HEIGHT_DEFAULT));
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
