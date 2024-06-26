package com.mandarina.game.gamestates;

import java.io.File;

import com.mandarina.game.entities.EnemyManager;
import com.mandarina.game.entities.Player;
import com.mandarina.game.entities.PlayerCts;
import com.mandarina.game.geometry.Box;
import com.mandarina.game.leveldata.LevelManager;
import com.mandarina.game.leveldata.Slide;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.ObjectManager;
import com.mandarina.game.ui.StatusBar;
import com.mandarina.game.ui.UIManager;
import com.mandarina.lvlbuilder.LvlBuilderImage;

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
	private UIManager uiManager;
	private StatusBar statusBar;
	private Offset offset;
	private Level currentLevel;

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
		levelManager.loadCustomLevel(image);
		Level level = levelManager.getCurrentLevel();
		loadLevel(level);
	}

	public void loadCustomFolder(File folder) {
		levelManager.loadCustomFolder(folder);
		Level level = levelManager.getCurrentLevel();
		loadLevel(level);
	}

	private void loadLevel(Level level) {
		player = level.getLevelEntities().getPlayer();
		player.setPlaying(this);
		this.currentLevel = level;
		resetAll();
	}

	private void initClasses() {
		levelManager = new LevelManager(this);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);
		uiManager = new UIManager(this);
		statusBar = new StatusBar(this);
		offset = new Offset(this);
	}

	public void update() {
		if (paused)
			uiManager.getPauseOverlay().update();
		else if (lvlCompleted)
			uiManager.getLevelCompletedOverlay().update();
		else if (gameCompleted)
			uiManager.getGameCompletedOverlay().update();
		else if (gameOver)
			uiManager.getGameOverOverlay().update();
		else if (playerDying)
			player.update();
		else {
			levelManager.update(offset);
			player.update();
			enemyManager.update(offset);
			objectManager.update(offset);
			statusBar.update();
			offset.update();
		}
	}

	public void draw(GameDrawer g) {
		levelManager.drawL1(g, offset);
		objectManager.drawL1(g, offset);
		enemyManager.drawL1(g, offset);

		player.draw(g, offset);

		levelManager.drawL2(g, offset);
		objectManager.drawL2(g, offset);
		enemyManager.drawL2(g, offset);

		levelManager.drawL3(g, offset);
		objectManager.drawL3(g, offset);
		enemyManager.drawL3(g, offset);

		levelManager.drawL4(g, offset);
		objectManager.drawL4(g, offset);
		enemyManager.drawL4(g, offset);

		statusBar.draw(g);

		if (paused) {
			g.setFill(new Color(0, 0, 0, 0.6));
			g.fillRect(0, 0, AppStage.GetGameWidth(), AppStage.GetGameHeight());
			uiManager.getPauseOverlay().draw(g);
		} else if (gameOver)
			uiManager.getGameOverOverlay().draw(g);
		else if (lvlCompleted)
			uiManager.getLevelCompletedOverlay().draw(g);
		else if (gameCompleted)
			uiManager.getGameCompletedOverlay().draw(g);
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
		offset.reset();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public Slide checkOverSlider(Box attackBox) {
		return levelManager.checkOverSlider(attackBox);
	}

	public void checkObjectHit(Box attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Box attackBox) {
		enemyManager.checkEnemyHit(attackBox, PlayerCts.DAMAGE);
	}

	public void checkPotionTouched(Box hitbox) {
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
			case C:
				player.setDuck(true);
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
			case C:
				player.setDuck(false);
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
				uiManager.getPauseOverlay().mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (gameOver)
			uiManager.getGameOverOverlay().mousePressed(e);
		else if (paused)
			uiManager.getPauseOverlay().mousePressed(e);
		else if (lvlCompleted)
			uiManager.getLevelCompletedOverlay().mousePressed(e);
		else if (gameCompleted)
			uiManager.getGameCompletedOverlay().mousePressed(e);

	}

	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			uiManager.getGameOverOverlay().mouseReleased(e);
		else if (paused)
			uiManager.getPauseOverlay().mouseReleased(e);
		else if (lvlCompleted)
			uiManager.getLevelCompletedOverlay().mouseReleased(e);
		else if (gameCompleted)
			uiManager.getGameCompletedOverlay().mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			uiManager.getGameOverOverlay().mouseMoved(e);
		else if (paused)
			uiManager.getPauseOverlay().mouseMoved(e);
		else if (lvlCompleted)
			uiManager.getLevelCompletedOverlay().mouseMoved(e);
		else if (gameCompleted)
			uiManager.getGameCompletedOverlay().mouseMoved(e);
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

	public Game getGame() {
		return game;
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

	public void scale() {
		this.levelManager.scale();
		this.objectManager.scale();
		this.enemyManager.scale();
		this.uiManager.scale();
		this.statusBar.scale();
		if (currentLevel != null) {
			this.player.scale();
			this.currentLevel.scale();
		}
	}
}