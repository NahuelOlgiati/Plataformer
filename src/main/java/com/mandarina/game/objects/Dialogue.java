package com.mandarina.game.objects;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.geometry.Point;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Dialogue extends GameObject {

	private int aniIndex, aniTick;
	private boolean active = true;

	public Dialogue(Point spawn, int type) {
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

	public void draw(GameDrawer g, Offset offset, Image[] dialogueQuestionSprite, Image[] dialogueExclamationSprite) {
		if (getObjType() == DialogueCts.QUESTION)
			g.drawImage(dialogueQuestionSprite[getAniIndex()], x - offset.getX(), y - offset.getY(),
					AppStage.Scale(DialogueCts.DIALOGUE_WIDTH_DEFAULT),
					AppStage.Scale(DialogueCts.DIALOGUE_HEIGHT_DEFAULT));
		else
			g.drawImage(dialogueExclamationSprite[getAniIndex()], x - offset.getX(), y - offset.getY(),
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

	@Override
	public int getAniIndex() {
		return aniIndex;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public static Image[] loadQuestions() {
		return LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(Catalog.QUESTION));
	}

	public static Image[] loadExclamations() {
		return LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(Catalog.EXCLAMATION));
	}
}
