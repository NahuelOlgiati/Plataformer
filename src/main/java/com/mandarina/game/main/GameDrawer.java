package com.mandarina.game.main;

import com.mandarina.main.AppStage;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GameDrawer {

	private float widthDefault, heightDefault;
	private Canvas canvas;
	private GraphicsContext ctx;

	public GameDrawer(float widthDefault, float heightDefault) {
		this.widthDefault = widthDefault;
		this.heightDefault = heightDefault;
		this.canvas = new Canvas(AppStage.Scale(widthDefault), AppStage.Scale(heightDefault));
		this.ctx = this.canvas.getGraphicsContext2D();
	}

	public void init(Group root) {
		root.getChildren().addAll(canvas);
	}

	public void drawImage(Image img, float x, float y, float w, float h) {
		ctx.drawImage(img, x, y, w, h);
	}

	public void setStroke(Color c) {
		ctx.setStroke(c);
	}

	public void strokeRect(float x, float y, float w, float h) {
		ctx.strokeRect(x, y, w, h);
	}

	public void setFill(Color c) {
		ctx.setFill(c);
	}

	public void fillRect(float x, float y, float w, float h) {
		ctx.fillRect(x, y, w, h);
	}

	public void setLineWidth(float w) {
		ctx.setLineWidth(w);
	}

	public WritableImage getSnapshot() {
		return canvas.snapshot(null, null);
	}

	public void scale() {
		this.canvas.setWidth(AppStage.Scale(widthDefault));
		this.canvas.setHeight(AppStage.Scale(heightDefault));
	}
}
