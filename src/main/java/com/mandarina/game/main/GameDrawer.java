package com.mandarina.game.main;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameDrawer {

	private Canvas canvas;
	private GraphicsContext ctx;

	public GameDrawer(double width, double height) {
		this.canvas = new Canvas(GameCts.GAME_WIDTH, GameCts.GAME_HEIGHT);
		this.ctx = this.canvas.getGraphicsContext2D();
	}

	public void init(Group root) {
		root.getChildren().addAll(canvas);
	}

	public void drawImage(Image img, double x, double y, double w, double h) {
		ctx.drawImage(img, x, y, w, h);
	}

	public void setStroke(Color c) {
		ctx.setStroke(c);
	}

	public void strokeRect(double x, double y, double w, double h) {
		ctx.strokeRect(x, y, w, h);
	}

	public void setFill(Color c) {
		ctx.setFill(c);
	}

	public void fillRect(double x, double y, double w, double h) {
		ctx.fillRect(x, y, w, h);
	}
}
