package com.mandarina.game.ui;

import com.mandarina.game.constants.UICts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

public class VolumeButton extends PauseButton {

	private Image[] imgs;
	private Image slider;
	private int index = 0;
	private boolean mouseOver, mousePressed;
	private int buttonX, minX, maxX;
	private float floatValue = 0f;

	public VolumeButton(int x, int y, int width, int height) {
		super(x + width / 2, y, UICts.VolumeButtons.VOLUME_WIDTH, height);
		bounds = new Rectangle(x - UICts.VolumeButtons.VOLUME_WIDTH / 2, y, UICts.VolumeButtons.VOLUME_WIDTH, height);
		buttonX = x + width / 2;
		this.x = x;
		this.width = width;
		minX = x + UICts.VolumeButtons.VOLUME_WIDTH / 2;
		maxX = x + width - UICts.VolumeButtons.VOLUME_WIDTH / 2;
		loadImgs();
	}

	private void loadImgs() {
		Image temp = LoadSave.GetSprite(LoadSave.VOLUME_BUTTONS);
		imgs = new Image[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = new WritableImage(temp.getPixelReader(), i * UICts.VolumeButtons.VOLUME_DEFAULT_WIDTH, 0,
					UICts.VolumeButtons.VOLUME_DEFAULT_WIDTH, UICts.VolumeButtons.VOLUME_DEFAULT_HEIGHT);

		slider = new WritableImage(temp.getPixelReader(), 3 * UICts.VolumeButtons.VOLUME_DEFAULT_WIDTH, 0,
				UICts.VolumeButtons.SLIDER_DEFAULT_WIDTH, UICts.VolumeButtons.VOLUME_DEFAULT_HEIGHT);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;

	}

	public void draw(GraphicsContext g) {
		g.drawImage(slider, x, y, width, height);
		g.drawImage(imgs[index], buttonX - UICts.VolumeButtons.VOLUME_WIDTH / 2, y, UICts.VolumeButtons.VOLUME_WIDTH,
				height);
	}

	public void changeX(int x) {
		if (x < minX)
			buttonX = minX;
		else if (x > maxX)
			buttonX = maxX;
		else
			buttonX = x;

		updateFloatValue();
		bounds.setX(buttonX - UICts.VolumeButtons.VOLUME_WIDTH / 2);
	}

	private void updateFloatValue() {
		float range = maxX - minX;
		float value = buttonX - minX;
		floatValue = value / range;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
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

	public float getFloatValue() {
		return floatValue;
	}
}
