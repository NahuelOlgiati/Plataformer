package com.mandarina.main;

import javafx.stage.Stage;

public class AppStage {

	private static AppStage appStage;
	private Stage stage;

	private AppStage(Stage stage) {
		this.stage = stage;
	}

	public static AppStage getInstance(Stage stage) {
		if (appStage == null) {
			appStage = new AppStage(stage);
		}
		return appStage;
	}

	public static AppStage get() {
		return appStage;
	}

	public Stage getStage() {
		return stage;
	}
}
