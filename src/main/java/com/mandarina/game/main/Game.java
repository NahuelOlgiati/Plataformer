package com.mandarina.game.main;

import java.io.File;

import com.mandarina.game.gamestates.Credits;
import com.mandarina.game.gamestates.GameOptions;
import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Loading;
import com.mandarina.game.gamestates.Menu;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.ui.AudioOptions;
import com.mandarina.lvlbuilder.LvlBuilder;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.main.AppStage;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {

	private Scene scene;

	private Playing playing;
	private Menu menu;
	private Loading loading;
	private Credits credits;
	private LvlBuilder lvlBuilder;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private GameAudio gameAudio;

	private GameDrawer gameDrawer;
	private GameInputs gameInputs;
	private GameLoop gameLoop;

	public void show() {
		if (scene == null) {
			scene = getScene();
		}
		if (AppStage.get().getStage().getUserData() instanceof LvlBuilderImage image) {
			GameState.getGame().getPlaying().loadCustomLevel(image);
			GameState.setState(GameState.PLAYING);
			AppStage.get().getStage().setUserData(null);
		}
		
		if (AppStage.get().getStage().getUserData() instanceof File folder) {
			GameState.getGame().getPlaying().loadCustomFolder(folder);
			GameState.setState(GameState.PLAYING);
			AppStage.get().getStage().setUserData(null);
		}
		Stage stage = AppStage.get().getStage();
		stage.setTitle("Plataformer");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				windowFocusLost();
			}
		});
		stage.show();
	}

	public void clear() {
		this.gameAudio.dispose();
		scene = null;
	}

	private Scene getScene() {
		initClasses();

		this.gameDrawer = new GameDrawer(GameCts.GAME_WIDTH_DEFAULT, GameCts.GAME_HEIGHT_DEFAULT);
		Group root = new Group();
		this.gameDrawer.init(root);

		Scene scene = new Scene(root);
		gameInputs.init(scene);
		gameLoop = new GameLoop() {

			@Override
			public void update() {
				Game.this.update();
			}

			@Override
			public void repaint() {
				Game.this.repaint();
			}

		};
		return scene;
	}

	public void start() {
		gameLoop = new GameLoop() {

			@Override
			public void update() {
				Game.this.update();
			}

			@Override
			public void repaint() {
				Game.this.repaint();
			}

		};
		gameLoop.start();
	}

	public void stop() {
		gameLoop.stop();
	}

	private void initClasses() {
		audioOptions = new AudioOptions(this);
		gameAudio = new GameAudio(this);
		menu = new Menu(this);
		loading = new Loading();
		credits = new Credits();
		lvlBuilder = new LvlBuilder();
		gameOptions = new GameOptions(this);
		gameInputs = new GameInputs(this);
		playing = new Playing(this);
	}

	private void update() {
		switch (GameState.get()) {
		case MENU -> menu.update();
		case PLAYING -> playing.update();
		case OPTIONS -> gameOptions.update();
		case CREDITS -> credits.update();
		case QUIT -> System.exit(0);
		}
	}

	private void repaint() {
		switch (GameState.get()) {
		case MENU -> menu.draw(this.gameDrawer);
		case LOADING -> loading.draw(this.gameDrawer);
		case PLAYING -> playing.draw(this.gameDrawer);
		case OPTIONS -> gameOptions.draw(this.gameDrawer);
		case CREDITS -> credits.draw(this.gameDrawer);
		}
	}

	public void scale() {
		this.gameDrawer.scale();
		this.playing.scale();
		this.menu.scale();
		this.loading.scale();
		this.credits.scale();
		this.gameOptions.scale();
		this.audioOptions.scale();
	}

	public void windowFocusLost() {
		if (GameState.is(GameState.PLAYING))
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

	public LvlBuilder getLvlBuilder() {
		return lvlBuilder;
	}

	public AudioOptions getAudioOptions() {
		return audioOptions;
	}

	public GameAudio getAudioPlayer() {
		return gameAudio;
	}
}
