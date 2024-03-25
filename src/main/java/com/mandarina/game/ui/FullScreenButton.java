package com.mandarina.game.ui;

import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.Box;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class FullScreenButton {

	private float x, y, s;
	private int diff, offset, border, rectangleSize;
	private boolean mouseOver, mousePressed;
	private Box bounds;
	private boolean maximize;

	public FullScreenButton() {
		this.rectangleSize = AppStage.Scale(10);
		this.s = AppStage.Scale(20);
		this.x = AppStage.GetGameWidth() - AppStage.Scale(40);
		this.y = rectangleSize + AppStage.Scale(10);
		this.diff = AppStage.Scale(4);
		this.offset = AppStage.Scale(7);
		this.border = AppStage.Scale(2);
		this.maximize = AppStage.get().getStage().isMaximized();
		initBounds();
	}

	private void initBounds() {
		bounds = new Box(x, y, s, s);
	}

	public void draw(GameDrawer g) {
		if (maximize) {
			drawMinimize(g);
		} else {
			drawMaximize(g);
		}
//		drawBounds(g);
	}

	public void change() {
		AppStage.get().change();
	}

	protected void drawBounds(GameDrawer g) {
		g.setStroke(Color.PINK);
		g.strokeRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}

	private void drawMinimize(GameDrawer g) {
		if (mouseOver) {
			drawRectangle(g, x + offset, y + offset, Color.WHITE);
			drawRectangle(g, x + offset + diff, y + offset - diff, Color.WHITE);
		} else {
			drawRectangle(g, x + offset, y + offset, Color.BLACK);
			drawRectangle(g, x + offset + diff, y + offset - diff, Color.BLACK);
		}
	}

	private void drawMaximize(GameDrawer g) {
		if (mouseOver) {
			drawRectangle(g, x + offset, y + offset, Color.WHITE);
		} else {
			drawRectangle(g, x + offset, y + offset, Color.BLACK);
		}
	}

	private void drawRectangle(GameDrawer g, float x, float y, Color color) {
		g.setStroke(color);
		g.setLineWidth(border);
		g.strokeRect(x, y, rectangleSize, rectangleSize);
		g.setFill(Color.TRANSPARENT);
		g.fillRect(x + border, y + border, rectangleSize * border, rectangleSize * border);
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Box getBounds() {
		return bounds;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public void setMouseOver(MouseEvent e) {
		setMouseOver(isIn(e));
	}

	public void isMousePressed(MouseEvent e) {
		if (isIn(e)) {
			setMousePressed(true);
		}
	}

	public boolean isIn(MouseEvent e) {
		return this.getBounds().contains(e.getX(), e.getY());
	}
}
