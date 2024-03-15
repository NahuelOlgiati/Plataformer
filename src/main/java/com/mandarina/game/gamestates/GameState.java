package com.mandarina.game.gamestates;

import com.mandarina.game.main.GameAudio;
import com.mandarina.game.main.Game;

public enum GameState {

	PLAYING, LVLBUILDER, MENU, LOADING, OPTIONS, QUIT, CREDITS;

	private static GameState state;
	private static Game game;

	public static void getInstance(Game gameInstance) {
		state = MENU;
		game = gameInstance;
	}

	public static GameState get() {
		return state;
	}

	public static Game getGame() {
		return game;
	}

	public static void setState(GameState newState) {
		switch (newState) {
		case MENU -> game.getAudioPlayer().playSong(GameAudio.MENU_1);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		case LVLBUILDER -> game.getAudioPlayer().playSong(GameAudio.BUILD);
		}
		state = newState;
	}

	public static boolean is(GameState value) {
		return state == value;
	}
}
