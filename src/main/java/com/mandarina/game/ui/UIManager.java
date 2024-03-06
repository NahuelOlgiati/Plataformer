package com.mandarina.game.ui;

import com.mandarina.game.gamestates.Playing;

public class UIManager {

	private Playing playing;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private GameCompletedOverlay gameCompletedOverlay;

	public UIManager(Playing playing) {
		this.playing = playing;
		this.pauseOverlay = new PauseOverlay(this.playing);
		this.gameOverOverlay = new GameOverOverlay(this.playing);
		this.levelCompletedOverlay = new LevelCompletedOverlay(this.playing);
		this.gameCompletedOverlay = new GameCompletedOverlay(this.playing);
	}

	public PauseOverlay getPauseOverlay() {
		return pauseOverlay;
	}

	public GameOverOverlay getGameOverOverlay() {
		return gameOverOverlay;
	}

	public LevelCompletedOverlay getLevelCompletedOverlay() {
		return levelCompletedOverlay;
	}

	public GameCompletedOverlay getGameCompletedOverlay() {
		return gameCompletedOverlay;
	}

	public void scale() {
		pauseOverlay.scale();
		gameOverOverlay.scale();
		levelCompletedOverlay.scale();
		gameCompletedOverlay.scale();
	}
}
