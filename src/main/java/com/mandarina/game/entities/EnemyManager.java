package com.mandarina.game.entities;

import com.mandarina.game.entities.crabby.Crabby;
import com.mandarina.game.entities.pinkstar.Pinkstar;
import com.mandarina.game.entities.shark.Shark;
import com.mandarina.game.entities.titan.Titan;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;
import com.mandarina.game.levels.LevelData;
import com.mandarina.game.levels.LevelEntities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

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
		LevelData lvlData = currentLevel.getLevelData();
		boolean isAnyActive = currentLevel.getLevelEntities().update(lvlData, playing);
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		currentLevel.getLevelEntities().draw(g, lvlOffsetX, lvlOffsetY);
	}

	public void checkEnemyHit(Rectangle2D attackBox) {
		LevelEntities le = currentLevel.getLevelEntities();
		for (Crabby c : le.getCrabs())
			if (c.isActive())
				if (!EnemyState.DEAD.equals(c.getState()) && !EnemyState.HIT.equals(c.getState()))
					if (attackBox.intersects(c.getHitbox())) {
						c.hurt(20);
						return;
					}

		for (Pinkstar p : le.getPinkstars())
			if (p.isActive()) {
				if (EnemyState.ATTACK.equals(p.getState()) && p.getAniIndex() >= 3)
					return;
				else {
					if (!EnemyState.DEAD.equals(p.getState()) && !EnemyState.HIT.equals(p.getState()))
						if (attackBox.intersects(p.getHitbox())) {
							p.hurt(20);
							return;
						}
				}
			}

		for (Shark s : le.getSharks())
			if (s.isActive()) {
				if (!EnemyState.DEAD.equals(s.getState()) && !EnemyState.HIT.equals(s.getState()))
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(20);
						return;
					}
			}

		for (Titan t : le.getTitans())
			if (t.isActive()) {
				if (!EnemyState.DEAD.equals(t.getState()) && !EnemyState.HIT.equals(t.getState()))
					if (attackBox.intersects(t.getHitbox())) {
						t.hurt(20);
						return;
					}
			}
	}

	public void resetAllEnemies() {
		for (Enemy e : currentLevel.getLevelEntities().getEnemys())
			e.resetEnemy();
	}

}
