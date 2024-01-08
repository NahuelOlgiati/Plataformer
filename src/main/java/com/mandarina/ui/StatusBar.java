package com.mandarina.ui;

import com.mandarina.constants.GameCts;
import com.mandarina.entities.player.Player;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class StatusBar {

	private Image statusBarImg;

	private int statusBarWidth = (int) (192 * GameCts.SCALE);
	private int statusBarHeight = (int) (58 * GameCts.SCALE);
	private int statusBarX = (int) (10 * GameCts.SCALE);
	private int statusBarY = (int) (10 * GameCts.SCALE);

	private int healthBarWidth = (int) (150 * GameCts.SCALE);
	private int healthBarHeight = (int) (4 * GameCts.SCALE);
	private int healthBarXStart = (int) (34 * GameCts.SCALE);
	private int healthBarYStart = (int) (14 * GameCts.SCALE);
	private int healthWidth = healthBarWidth;

	private int powerBarWidth = (int) (104 * GameCts.SCALE);
	private int powerBarHeight = (int) (2 * GameCts.SCALE);
	private int powerBarXStart = (int) (44 * GameCts.SCALE);
	private int powerBarYStart = (int) (34 * GameCts.SCALE);
	private int powerWidth = powerBarWidth;

	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;

	private Player player;

	public StatusBar(Player player) {
		this.player = player;
		loadAnimations();
	}
	
	public void update() {
		updateHealthBar();
		updatePowerBar();
	}


	public void updateHealthBar() {
		healthWidth = (int) ((player.getCurrentHealth() / (float) player.getMaxHealth()) * healthBarWidth);
	}

	public void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	public void draw(GraphicsContext g) {
		// Background ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight);

		// Health bar
		g.setFill(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// Power Bar
		g.setFill(Color.YELLOW);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	public void loadAnimations() {
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	public void changePower(int value) {
		powerValue += value;
		powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
	}

	public void resetPower() {
		powerValue = powerMaxValue;
	}

	public int getPowerValue() {
		return powerValue;
	}
}
