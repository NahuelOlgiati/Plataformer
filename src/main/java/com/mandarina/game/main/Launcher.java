package com.mandarina.game.main;

import com.mandarina.game.gamestates.GameState;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.initStyle(StageStyle.UNDECORATED);
		AppStage.getInstance(stage);
		if (noParameters()) {
			Game game = new Game();
			GameState.getInstance(game);
			game.show();
			game.start();
		}
	}

	private boolean noParameters() {
		return getParameters().getRaw().isEmpty();
	}
}
