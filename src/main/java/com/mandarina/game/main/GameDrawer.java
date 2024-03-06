package com.mandarina.game.main;

import com.mandarina.main.AppStage;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameDrawer {

	private double widthDefault, heightDefault;
	private Canvas canvas;
	private GraphicsContext ctx;

	public GameDrawer(double widthDefault, double heightDefault) {
		this.widthDefault = widthDefault;
		this.heightDefault = heightDefault;
		this.canvas = new Canvas(AppStage.Scale(widthDefault), AppStage.Scale(heightDefault));
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

	public void setLineWidth(double w) {
		ctx.setLineWidth(w);
	}

	public void scale() {
		this.canvas.setWidth(AppStage.Scale(widthDefault));
		this.canvas.setHeight(AppStage.Scale(heightDefault));
	}
}
