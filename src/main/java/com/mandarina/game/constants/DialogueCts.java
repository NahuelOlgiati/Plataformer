package com.mandarina.game.constants;

public class DialogueCts {
	public static final int QUESTION = 0;
	public static final int EXCLAMATION = 1;

	public static final int DIALOGUE_WIDTH = (int) (14 * GameCts.SCALE);
	public static final int DIALOGUE_HEIGHT = (int) (12 * GameCts.SCALE);

	public static int GetSpriteAmount(int type) {
		switch (type) {
		case QUESTION, EXCLAMATION:
			return 5;
		}
		return 0;
	}
}