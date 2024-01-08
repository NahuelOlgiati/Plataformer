package com.mandarina.entities;

import javafx.geometry.Rectangle2D;
import java.util.List;

import com.mandarina.entities.crabby.Crabby;
import com.mandarina.entities.crabby.CrabbyAtlas;
import com.mandarina.entities.pinkstar.Pinkstar;
import com.mandarina.entities.pinkstar.PinkstarAtlas;
import com.mandarina.entities.shark.Shark;
import com.mandarina.entities.shark.SharkAtlas;
import com.mandarina.entities.titan.Titan;
import com.mandarina.entities.titan.TitanAtlas;
import com.mandarina.gamestates.Playing;
import com.mandarina.levels.Level;

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

	public void draw(GraphicsContext g, int xLvlOffset) {
		draw(g, xLvlOffset, currentLevel.getCrabs(), crabbyArr);
		draw(g, xLvlOffset, currentLevel.getPinkstars(), pinkstarArr);
		draw(g, xLvlOffset, currentLevel.getSharks(), sharkArr);
		draw(g, xLvlOffset, currentLevel.getTitans(), titanArr);
	}

	private void draw(GraphicsContext g, int xLvlOffset, List<? extends Enemy> enemies, Image[][] animations) {
		for (Enemy e : enemies)
			if (e.isActive()) {
				e.draw(g, xLvlOffset, animations);
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
