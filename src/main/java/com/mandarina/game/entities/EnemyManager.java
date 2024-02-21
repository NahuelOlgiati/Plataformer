package com.mandarina.game.entities;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.main.LayerDrawer;

import javafx.geometry.Rectangle2D;

public class EnemyManager implements LayerDrawer {

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

	@Override
	public void drawL1(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		currentLevel.getLevelEntities().drawL1(g, lvlOffsetX, lvlOffsetY);
	}

	@Override
	public void drawL2(GameDrawer g, int lvlOffsetX, int lvlOffsetY) {
		currentLevel.getLevelEntities().drawL2(g, lvlOffsetX, lvlOffsetY);
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
