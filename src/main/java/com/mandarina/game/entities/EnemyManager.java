package com.mandarina.game.entities;

import java.util.List;

import com.mandarina.game.entities.crabby.Crabby;
import com.mandarina.game.entities.crabby.CrabbyAtlas;
import com.mandarina.game.entities.pinkstar.Pinkstar;
import com.mandarina.game.entities.pinkstar.PinkstarAtlas;
import com.mandarina.game.entities.shark.Shark;
import com.mandarina.game.entities.shark.SharkAtlas;
import com.mandarina.game.entities.titan.Titan;
import com.mandarina.game.entities.titan.TitanAtlas;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.Level;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnemyManager {

	private Playing playing;
	private Image[][] crabbyArr, pinkstarArr, sharkArr, titanArr;
	private Level currentLevel;

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		this.currentLevel = level;
	}

	public void update(int[][] lvlData) {
		boolean isAnyActive = false;
		for (Crabby c : currentLevel.getCrabs())
			if (c.isActive()) {
				c.update(lvlData, playing);
				isAnyActive = true;
			}

		for (Pinkstar p : currentLevel.getPinkstars())
			if (p.isActive()) {
				p.update(lvlData, playing);
				isAnyActive = true;
			}

		for (Shark s : currentLevel.getSharks())
			if (s.isActive()) {
				s.update(lvlData, playing);
				isAnyActive = true;
			}

		for (Titan t : currentLevel.getTitans())
			if (t.isActive()) {
				t.update(lvlData, playing);
				isAnyActive = true;
			}

		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY) {
		draw(g, lvlOffsetX, lvlOffsetY, currentLevel.getCrabs(), crabbyArr);
		draw(g, lvlOffsetX, lvlOffsetY, currentLevel.getPinkstars(), pinkstarArr);
		draw(g, lvlOffsetX, lvlOffsetY, currentLevel.getSharks(), sharkArr);
		draw(g, lvlOffsetX, lvlOffsetY, currentLevel.getTitans(), titanArr);
	}

	private void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, List<? extends Enemy> enemies,
			Image[][] animations) {
		for (Enemy e : enemies)
			if (e.isActive()) {
				e.draw(g, lvlOffsetX, lvlOffsetY, animations);
			}
	}

	public void checkEnemyHit(Rectangle2D attackBox) {
		for (Crabby c : currentLevel.getCrabs())
			if (c.isActive())
				if (!EnemyState.DEAD.equals(c.getState()) && !EnemyState.HIT.equals(c.getState()))
					if (attackBox.intersects(c.getHitbox())) {
						c.hurt(20);
						return;
					}

		for (Pinkstar p : currentLevel.getPinkstars())
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

		for (Shark s : currentLevel.getSharks())
			if (s.isActive()) {
				if (!EnemyState.DEAD.equals(s.getState()) && !EnemyState.HIT.equals(s.getState()))
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(20);
						return;
					}
			}

		for (Titan t : currentLevel.getTitans())
			if (t.isActive()) {
				if (!EnemyState.DEAD.equals(t.getState()) && !EnemyState.HIT.equals(t.getState()))
					if (attackBox.intersects(t.getHitbox())) {
						t.hurt(20);
						return;
					}
			}
	}

	private void loadEnemyImgs() {
		crabbyArr = CrabbyAtlas.getAnimations();
		pinkstarArr = PinkstarAtlas.getAnimations();
		sharkArr = SharkAtlas.getAnimations();
		titanArr = TitanAtlas.getAnimations();
	}

	public void resetAllEnemies() {
		for (Crabby c : currentLevel.getCrabs())
			c.resetEnemy();
		for (Pinkstar p : currentLevel.getPinkstars())
			p.resetEnemy();
		for (Shark s : currentLevel.getSharks())
			s.resetEnemy();
		for (Titan t : currentLevel.getTitans())
			t.resetEnemy();
	}

}
