package com.mandarina.main;

import com.mandarina.audio.AudioPlayer;
import com.mandarina.constants.GameCts;
import com.mandarina.gamestates.Credits;
import com.mandarina.gamestates.GameOptions;
import com.mandarina.gamestates.Gamestate;
import com.mandarina.gamestates.Menu;
import com.mandarina.gamestates.Playing;
import com.mandarina.ui.AudioOptions;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game extends Application {

	private Canvas canvas;
	private GraphicsContext gc;

	private Playing playing;
	private Menu menu;
	private Credits credits;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;

	private GameInputs gameInputs;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		initClasses((Image)primaryStage.getUserData());
		
		canvas = new Canvas(GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		StackPane root = new StackPane();
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);
		gameInputs.init(scene);

		primaryStage.setTitle("Plataformer");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				windowFocusLost();
			}
		});
		primaryStage.show();
		startGameLoop();
	}

	private void initClasses(Image image) {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this, image);
		credits = new Credits(this);
		gameOptions = new GameOptions(this);
		gameInputs = new GameInputs(playing, menu, gameOptions, credits);
	}

	private void startGameLoop() {
		new GameLoop() {

			@Override
			public void update() {
				Game.this.update();
			}

			@Override
			public void repaint() {
				Game.this.repaint();
			}

		}.start();
	}

	private void update() {
		switch (Gamestate.state) {
		case MENU -> menu.update();
		case PLAYING -> playing.update();
		case OPTIONS -> gameOptions.update();
		case CREDITS -> credits.update();
		case QUIT -> System.exit(0);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void repaint() {
		switch (Gamestate.state) {
		case MENU -> menu.draw(gc);
		case PLAYING -> playing.draw(gc);
		case OPTIONS -> gameOptions.draw(gc);
		case CREDITS -> credits.draw(gc);
		}
	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}

	public Credits getCredits() {
		return credits;
	}

	public AudioOptions getAudioOptions() {
		return audioOptions;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
