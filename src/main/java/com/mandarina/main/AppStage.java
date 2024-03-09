package com.mandarina.main;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.main.GameCts;

import javafx.application.Platform;
import javafx.stage.Stage;

public class AppStage {

	private static AppStage appStage;
	private Stage stage;
	private static float gameScale = GameCts.SCALE_DEFAULT;
	private static float previusGameScale = gameScale;

	private static double gameHeight;
	private static double gameWidth;
	private static double tileSize;
	private static double previusTileSize;

	private static boolean widthSet = false;
	private static boolean heightSet = false;
	private static boolean isRunning = false;

	private static boolean refresh = true;

	private AppStage(Stage stage) {
		this.stage = stage;
	}

	public static AppStage getInstance(Stage stage) {
		if (appStage == null) {
			appStage = new AppStage(stage);
			stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
				if (newWidth != null) {
					widthSet = true;
					if (heightSet) {
						setScale(stage.getWidth(), stage.getHeight());
					}
				}
			});
			stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
				if (newHeight != null) {
					heightSet = true;
					if (widthSet) {
						setScale(stage.getWidth(), stage.getHeight());
					}
				}
			});
		}
		return appStage;
	}

	public void change() {
		if (stage.isMaximized()) {
			stage.setMaximized(false);
		} else {
			stage.setMaximized(true);
		}
	}

	public static void setScale(double screenWidth, double screenHeight) {
		if (!isRunning) {
			isRunning = true;
			System.out.println("setScale");
			GameState.getGame().stop();
//			GameState.setState(GameState.LOADING);
			Platform.runLater(() -> {
//				GameState currentState = GameState.get();
//				GameState.getGame().clear();
				float calculateScaleFactor = calculateScaleFactor(screenWidth, screenHeight);
				System.out.println(screenWidth);
				System.out.println(screenHeight);
				System.out.println(calculateScaleFactor);
				previusGameScale = gameScale;
				gameScale = calculateScaleFactor;

				refresh();

				GameState.getGame().scale();
//				GameState.setState(currentState);
				GameState.getGame().show();
				GameState.getGame().start();
				widthSet = false;
				heightSet = false;
				isRunning = false;
			});
		}
	}

	private static void refresh() {
		refresh = true;
		GetGameHeight();
		GetGameWidth();
		GetTileSize();
		GetPreviusTileSize();
		refresh = false;
	}

	private static float calculateScaleFactor(double newWidth, double newHeight) {
		double widthScale = newWidth / GameCts.GAME_WIDTH_DEFAULT;
		double heightScale = newHeight / GameCts.GAME_HEIGHT_DEFAULT;
		return (float) Math.min(widthScale, heightScale);
	}

	public static AppStage get() {
		return appStage;
	}

	public Stage getStage() {
		return stage;
	}

	public static double GetGameHeight() {
		if (refresh) {
			gameHeight = GetTileSize() * GameCts.TILES_IN_HEIGHT;
		}
		return gameHeight;
	}

	public static double GetGameWidth() {
		if (refresh) {
			gameWidth = GetTileSize() * GameCts.TILES_IN_WIDTH;
		}
		return gameWidth;
	}

	public static double GetTileSize() {
		if (refresh) {
			tileSize = GameCts.TILES_DEFAULT_SIZE * gameScale;
		}
		return tileSize;
	}

	public static double GetPreviusTileSize() {
		if (refresh) {
			previusTileSize = GameCts.TILES_DEFAULT_SIZE * previusGameScale;
		}
		return previusTileSize;
	}

	public static int GetTilesIn(double value) {
		return (int) (value / AppStage.GetTileSize());
	}

	public static int Scale(int value) {
		return (int) (value * gameScale);
	}

	public static double Scale(double value) {
		return value * gameScale;
	}

}
