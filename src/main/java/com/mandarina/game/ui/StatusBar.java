package com.mandarina.game.ui;

import com.mandarina.game.entities.Player;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.AppStage;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.Catalog;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class StatusBar {

	private Image statusBarImg;

	private int statusBarWidth;
	private int statusBarHeight;
	private int statusBarX;
	private int statusBarY;

	private int healthBarWidth;
	private int healthBarHeight;
	private int healthBarXStart;
	private int healthBarYStart;
	private int healthWidth;

	private int powerBarWidth;
	private int powerBarHeight;
	private int powerBarXStart;
	private int powerBarYStart;
	private int powerWidth;

	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;

	private Playing playing;

	public StatusBar(Playing playing) {
		this.playing = playing;
		this.statusBarImg = LoadSave.GetSprite(Catalog.STATUS_BAR);

		this.statusBarWidth = AppStage.Scale(192);
		this.statusBarHeight = AppStage.Scale(58);
		this.statusBarX = AppStage.Scale(10);
		this.statusBarY = AppStage.Scale(10);

		this.healthBarWidth = AppStage.Scale(150);
		this.healthBarHeight = AppStage.Scale(4);
		this.healthBarXStart = AppStage.Scale(34);
		this.healthBarYStart = AppStage.Scale(14);

		this.powerBarWidth = AppStage.Scale(104);
		this.powerBarHeight = AppStage.Scale(2);
		this.powerBarXStart = AppStage.Scale(44);
		this.powerBarYStart = AppStage.Scale(34);

		this.healthWidth = healthBarWidth;
		this.powerWidth = powerBarWidth;
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();
	}

	public void updateHealthBar() {
		Player player = playing.getPlayer();
		healthWidth = (int) ((player.getCurrentHealth() / player.getMaxHealth()) * healthBarWidth);
	}

	public void updatePowerBar() {
		powerWidth = (int) ((powerValue / (double) powerMaxValue) * powerBarWidth);

		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	public void draw(GameDrawer g) {
		// Background ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight);

		// Health bar
		g.setFill(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// Power Bar
		g.setFill(Color.YELLOW);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
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

	public void scale() {
		this.statusBarWidth = AppStage.Scale(192);
		this.statusBarHeight = AppStage.Scale(58);
		this.statusBarX = AppStage.Scale(10);
		this.statusBarY = AppStage.Scale(10);

		this.healthBarWidth = AppStage.Scale(150);
		this.healthBarHeight = AppStage.Scale(4);
		this.healthBarXStart = AppStage.Scale(34);
		this.healthBarYStart = AppStage.Scale(14);

		this.powerBarWidth = AppStage.Scale(104);
		this.powerBarHeight = AppStage.Scale(2);
		this.powerBarXStart = AppStage.Scale(44);
		this.powerBarYStart = AppStage.Scale(34);
	}
}
