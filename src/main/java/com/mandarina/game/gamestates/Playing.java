package com.mandarina.game.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.mandarina.game.constants.DialogueCts;
import com.mandarina.game.constants.EnvCts;
import com.mandarina.game.constants.GameCts;
import com.mandarina.game.effects.DialogueEffect;
import com.mandarina.game.effects.Rain;
import com.mandarina.game.entities.EnemyManager;
import com.mandarina.game.entities.player.Player;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelManager;
import com.mandarina.game.main.Game;
import com.mandarina.game.objects.ObjectManager;
import com.mandarina.game.ui.GameCompletedOverlay;
import com.mandarina.game.ui.GameOverOverlay;
import com.mandarina.game.ui.LevelCompletedOverlay;
import com.mandarina.game.ui.PauseOverlay;
import com.mandarina.game.ui.StatusBar;
import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Playing {

	private Game game;

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private GameCompletedOverlay gameCompletedOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private StatusBar statusBar;
	private Rain rain;

	private boolean paused = false;

	private int lvlOffsetX;
	private int maxLvlOffsetX;
	private int leftBorder = (int) (0.25 * GameCts.GAME_WIDTH);
	private int rightBorder = (int) (0.75 * GameCts.GAME_WIDTH);

	private int lvlOffsetY;
	private int maxLvlOffsetY;
	private int bottomBorder = (int) (0.25 * GameCts.GAME_HEIGHT);
	private int topBorder = (int) (0.75 * GameCts.GAME_HEIGHT);

	private Image backgroundImg, bigCloud, smallCloud, shipImgs[];
	private Image[] questionImgs, exclamationImgs;
	private ArrayList<DialogueEffect> dialogEffects = new ArrayList<>();

	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean gameCompleted;
	private boolean playerDying;
	private boolean drawRain;

	// Ship will be decided to drawn here. It's just a cool addition to the game
	// for the first level. Hinting on that the player arrived with the boat.

	// If you would like to have it on more levels, add a value for objects when
	// creating the level from lvlImgs. Just like any other object.

	// Then play around with position values so it looks correct depending on where
	// you want
	// it.

	private boolean drawShip = false;
	private int shipAni, shipTick, shipDir = 1;
	private float shipHeightDelta, shipHeightChange = 0.05f * GameCts.SCALE;

	public Playing(Game game) {
		this.game = game;
		initClasses();

		backgroundImg = LoadSave.GetSprite(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSprite(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSprite(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * GameCts.SCALE) + rnd.nextInt((int) (100 * GameCts.SCALE));

		shipImgs = LoadSave.GetAnimations(4, 78, 72, LoadSave.GetAtlas(LoadSave.SHIP));

		loadDialogue();
		calcLvlOffset();
		loadStartLevel();
		setDrawRainBoolean();
	}

	private void loadDialogue() {
		loadDialogueImgs();

		// Load dialogue array with premade objects, that gets activated when needed.
		// This is a simple
		// way of avoiding ConcurrentModificationException error. (Adding to a list that
		// is being looped through.

		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, DialogueCts.EXCLAMATION));
		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, DialogueCts.QUESTION));

		for (DialogueEffect de : dialogEffects)
			de.deactive();
	}

	private void loadDialogueImgs() {
		questionImgs = LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(LoadSave.QUESTION));
		exclamationImgs = LoadSave.GetAnimations(5, 14, 12, LoadSave.GetAtlas(LoadSave.EXCLAMATION));
	}

	public void loadNextLevel() {
		levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getLevelEntities().getPlayerSpawn());
		resetAll();
		drawShip = false;
	}

	public void loadCustomLevel(LvlBuilderImage image) {
		Level level = new Level(image);
		levelManager.loadCustomLevel(level);
		player.setSpawn(level.getLevelEntities().getPlayerSpawn());
		resetAll();
		drawShip = false;
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX();
		maxLvlOffsetY = levelManager.getCurrentLevel().getLvlOffsetY();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, this, game.getAudioPlayer());
		player.setSpawn(levelManager.getCurrentLevel().getLevelEntities().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this, game.getAudioOptions());
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

		statusBar = new StatusBar(player);
		rain = new Rain();
	}

	public void update() {
		if (paused)
			pauseOverlay.update();
		else if (lvlCompleted)
			levelCompletedOverlay.update();
		else if (gameCompleted)
			gameCompletedOverlay.update();
		else if (gameOver)
			gameOverOverlay.update();
		else if (playerDying)
			player.update();
		else {
			updateDialogue();
			if (drawRain)
				rain.update(lvlOffsetX, lvlOffsetY);
			levelManager.update();
			player.update();
			enemyManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			statusBar.update();
			checkCloseToBorderX();
			checkCloseToBorderY();
			if (drawShip)
				updateShipAni();
		}
	}

	private void updateShipAni() {
		shipTick++;
		if (shipTick >= 35) {
			shipTick = 0;
			shipAni++;
			if (shipAni >= 4)
				shipAni = 0;
		}

		shipHeightDelta += shipHeightChange * shipDir;
		shipHeightDelta = Math.max(Math.min(10 * GameCts.SCALE, shipHeightDelta), 0);

		if (shipHeightDelta == 0)
			shipDir = 1;
		else if (shipHeightDelta == 10 * GameCts.SCALE)
			shipDir = -1;

	}

	private void updateDialogue() {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive())
				de.update();
	}

	private void drawDialogue(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive()) {
				if (de.getType() == DialogueCts.QUESTION)
					g.drawImage(questionImgs[de.getAniIndex()], de.getX() - lvlOffsetX, de.getY() - lvlOffsetY,
							DialogueCts.DIALOGUE_WIDTH, DialogueCts.DIALOGUE_HEIGHT);
				else
					g.drawImage(exclamationImgs[de.getAniIndex()], de.getX() - lvlOffsetX, de.getY() - lvlOffsetY,
							DialogueCts.DIALOGUE_WIDTH, DialogueCts.DIALOGUE_HEIGHT);
			}
	}

	public void addDialogue(int x, int y, int type) {
		// Not adding a new one, we are recycling. #ThinkGreen lol
		dialogEffects.add(new DialogueEffect(x, y - (int) (GameCts.SCALE * 15), type));
		for (DialogueEffect de : dialogEffects)
			if (!de.isActive())
				if (de.getType() == type) {
					de.reset(x, -(int) (GameCts.SCALE * 15));
					return;
				}
	}

	private void checkCloseToBorderX() {
		int playerX = (int) player.getHitbox().getMinX();
		int diff = playerX - lvlOffsetX;

		if (diff > rightBorder)
			lvlOffsetX += diff - rightBorder;
		else if (diff < leftBorder)
			lvlOffsetX += diff - leftBorder;

		lvlOffsetX = Math.max(Math.min(lvlOffsetX, maxLvlOffsetX), 0);
	}

	private void checkCloseToBorderY() {
		int playerY = (int) player.getHitbox().getMinY();
		int diff = playerY - lvlOffsetY;

		if (diff > topBorder)
			lvlOffsetY += diff - topBorder;
		else if (diff < bottomBorder)
			lvlOffsetY += diff - bottomBorder;

		lvlOffsetY = Math.max(Math.min(lvlOffsetY, maxLvlOffsetY), 0);
	}

	public void draw(GraphicsContext g) {
		g.drawImage(backgroundImg, 0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);

		drawClouds(g);
		if (drawRain)
			rain.draw(g, lvlOffsetX, lvlOffsetY);

		if (drawShip)
			g.drawImage(shipImgs[shipAni], (int) (100 * GameCts.SCALE) - lvlOffsetX,
					(int) ((288 * GameCts.SCALE) + shipHeightDelta - lvlOffsetY), (int) (78 * GameCts.SCALE),
					(int) (72 * GameCts.SCALE));

		levelManager.draw(g, lvlOffsetX, lvlOffsetY);
		objectManager.draw(g, lvlOffsetX, lvlOffsetY);
		enemyManager.draw(g, lvlOffsetX, lvlOffsetY);
		statusBar.draw(g);
		player.draw(g, lvlOffsetX, lvlOffsetY);
		drawDialogue(g, lvlOffsetX, lvlOffsetY);

		if (paused) {
			g.setFill(new Color(0, 0, 0, 0.6));
			g.fillRect(0, 0, GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
		else if (gameCompleted)
			gameCompletedOverlay.draw(g);

	}

	private void drawClouds(GraphicsContext g) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud, i * EnvCts.BIG_CLOUD_WIDTH - (int) (lvlOffsetX * 0.3), (int) (204 * GameCts.SCALE),
					EnvCts.BIG_CLOUD_WIDTH, EnvCts.BIG_CLOUD_HEIGHT);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, EnvCts.SMALL_CLOUD_WIDTH * 4 * i - (int) (lvlOffsetX * 0.7), smallCloudsPos[i],
					EnvCts.SMALL_CLOUD_WIDTH, EnvCts.SMALL_CLOUD_HEIGHT);
	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		drawRain = false;

		setDrawRainBoolean();

		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		dialogEffects.clear();
	}

	private void setDrawRainBoolean() {
		// This method makes it rain 20% of the time you load a level.
//		if (rnd.nextFloat() >= 0.8f)
		drawRain = true;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseButton.PRIMARY)
				player.setAttacking(true);
			else if (e.getButton() == MouseButton.SECONDARY)
				player.powerAttack();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted) {
			switch (e.getCode()) {
			case A:
				player.setLeft(true);
				break;
			case D:
				player.setRight(true);
				break;
			case SPACE:
				player.setJump(true);
				break;
			case ESCAPE:
				paused = !paused;
				break;
			default:
				break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted) {
			switch (e.getCode()) {
			case A:
				player.setLeft(false);
				break;
			case D:
				player.setRight(false);
				break;
			case SPACE:
				player.setJump(false);
				break;
			default:
				break;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mousePressed(e);
		else if (paused)
			pauseOverlay.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mousePressed(e);
		else if (gameCompleted)
			gameCompletedOverlay.mousePressed(e);

	}

	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseReleased(e);
		else if (paused)
			pauseOverlay.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseMoved(e);
		else if (paused)
			pauseOverlay.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseMoved(e);
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.game.getAudioPlayer().lvlCompleted();
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// No more levels
			gameCompleted = true;
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
	}

	public void setLevelSong() {
		this.game.getAudioPlayer().setLevelSong(this.getLevelManager().getLevelIndex());
	}

	public void setMaxLvlOffsetX(int lvlOffsetX) {
		this.maxLvlOffsetX = lvlOffsetX;
	}

	public void setMaxLvlOffsetY(int lvlOffsetY) {
		this.maxLvlOffsetY = lvlOffsetY;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
}