package com.mandarina.game.gamestates;

import com.mandarina.game.entities.EnemyManager;
import com.mandarina.game.entities.Player;
import com.mandarina.game.entities.PlayerCts;
import com.mandarina.game.leveldata.LevelManager;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameCts;
import com.mandarina.game.objects.ObjectManager;
import com.mandarina.game.ui.GameCompletedOverlay;
import com.mandarina.game.ui.GameOverOverlay;
import com.mandarina.game.ui.LevelCompletedOverlay;
import com.mandarina.game.ui.PauseOverlay;
import com.mandarina.game.ui.StatusBar;
import com.mandarina.lvlbuilder.LvlBuilderImage;

import javafx.geometry.Rectangle2D;
import com.mandarina.game.main.GameDrawer;
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
	private Level currentLevel;

	private int lvlOffsetX;
	private int maxLvlOffsetX;
	private int leftBorder = (int) (0.25 * GameCts.GAME_WIDTH);
	private int rightBorder = (int) (0.75 * GameCts.GAME_WIDTH);

	private int lvlOffsetY;
	private int maxLvlOffsetY;
	private int bottomBorder = (int) (0.25 * GameCts.GAME_HEIGHT);
	private int topBorder = (int) (0.75 * GameCts.GAME_HEIGHT);

	private boolean paused = false;
	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean gameCompleted;
	private boolean playerDying;

	public Playing(Game game) {
		this.game = game;
		initClasses();
	}

	public void loadNextLevel() {
		levelManager.loadNextLevel();
		Level level = levelManager.getCurrentLevel();
		loadLevel(level);
	}

	public void loadCustomLevel(LvlBuilderImage image) {
		Level level = new Level(image);
		levelManager.loadCustomLevel(level);
		loadLevel(level);
	}

	private void loadLevel(Level level) {
		player.setSpawn(level.getLevelEntities().getPlayerSpawn());
		this.currentLevel = level;
		resetAll();
	}

	private void initClasses() {
		levelManager = new LevelManager(this);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, this, game.getAudioPlayer());

		pauseOverlay = new PauseOverlay(this, game.getAudioOptions());
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

		statusBar = new StatusBar(player);
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
			levelManager.update();
			player.update();
			enemyManager.update();
			objectManager.update();
			statusBar.update();
			checkCloseToBorderX();
			checkCloseToBorderY();
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

	public void draw(GameDrawer g) {
		levelManager.draw(g, lvlOffsetX, lvlOffsetY);
		player.draw(g, lvlOffsetX, lvlOffsetY);
		objectManager.draw(g, lvlOffsetX, lvlOffsetY);
		enemyManager.draw(g, lvlOffsetX, lvlOffsetY);
		
		statusBar.draw(g);

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

		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D attackBox) {
		enemyManager.checkEnemyHit(attackBox, PlayerCts.DAMAGE);
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
		if (levelManager.getLevelIndex() + 1 >= levelManager.getNumOfLevels()) {
			gameCompleted = true;
			levelManager.reset();
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

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public LevelData getLevelData() {
		return currentLevel.getLevelData();
	}

	public int getLvlOffsetX() {
		return lvlOffsetX;
	}

	public int getLvlOffsetY() {
		return lvlOffsetY;
	}
}