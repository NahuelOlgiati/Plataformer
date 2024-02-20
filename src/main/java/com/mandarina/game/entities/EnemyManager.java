package com.mandarina.game.entities;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.main.GameDrawer;

import javafx.geometry.Rectangle2D;

public class EnemyManager {

	private Playing playing;
	private Level currentLevel;

	public EnemyManager(Playing playing) {
		this.playing = playing;
	}

	public void loadEnemies(Level level) {
		this.currentLevel = level;
	}

	public void update() {
		boolean isAnyActive = currentLevel.getLevelEntities().update(playing);
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		currentLevel.getLevelEntities().draw(g, lvlOffsetX, lvlOffsetY);
	}

	public void checkEnemyHit(Rectangle2D attackBox, int playerDamage) {
		for (Enemy e : currentLevel.getLevelEntities().getEnemys())
			if (e.isActive())
				if (!EnemyState.DEAD.equals(e.getState()) && !EnemyState.HIT.equals(e.getState()))
					if (attackBox.intersects(e.getHitbox())) {
						e.hurt(playerDamage);
						return;
					}
	}

	public void resetAllEnemies() {
		for (Enemy e : currentLevel.getLevelEntities().getEnemys())
			e.resetEnemy();
	}

}
