package com.mandarina.main;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.main.Game;
import com.mandarina.utilz.LoadSave;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.getIcons().add(LoadSave.GetAppIcon());
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
