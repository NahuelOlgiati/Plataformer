package com.mandarina.game.ui;

import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VolumeButton extends PauseButton {

	private Image[] imgs;
	private Image slider;
	private Rectangle sliderBounds;
	private int index = 0;
	private boolean mouseOver, mousePressed;
	private int buttonX, minX, maxX;
	private float volume;

	public VolumeButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		sliderBounds = new Rectangle(x - 5, y, VolumeButtonCts.SLIDER_WIDTH, height);
		minX = (int) (sliderBounds.getX() + VolumeButtonCts.VOLUME_WIDTH / 2);
		maxX = (int) (sliderBounds.getX() + VolumeButtonCts.SLIDER_WIDTH - VolumeButtonCts.VOLUME_WIDTH / 2);
		loadImgs();
		setVolume(0.75f);
		updateVolume();
	}

	private void loadImgs() {
		Image temp = LoadSave.GetSprite(LoadSave.VOLUME_BUTTONS);
		imgs = new Image[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = LoadSave.GetSubimage(temp, i, 0, VolumeButtonCts.VOLUME_DEFAULT_WIDTH,
					VolumeButtonCts.VOLUME_DEFAULT_HEIGHT);

		slider = new WritableImage(temp.getPixelReader(), 3 * VolumeButtonCts.VOLUME_DEFAULT_WIDTH, 0,
				VolumeButtonCts.SLIDER_DEFAULT_WIDTH, VolumeButtonCts.VOLUME_DEFAULT_HEIGHT);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	public void draw(GameDrawer g) {
		g.drawImage(slider, sliderBounds.getX(), sliderBounds.getY(), sliderBounds.getWidth(),
				sliderBounds.getHeight());
		g.drawImage(imgs[index], buttonX - VolumeButtonCts.VOLUME_WIDTH / 2, bounds.getY(),
				VolumeButtonCts.VOLUME_WIDTH, bounds.getHeight());
//		drawBoundBox(g);
//		drawSliderBoundBox(g);
	}

	protected void drawBoundBox(GameDrawer g) {
		g.setStroke(Color.BLUE);
		g.strokeRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	protected void drawSliderBoundBox(GameDrawer g) {
		g.setStroke(Color.RED);
		g.strokeRect(sliderBounds.getX(), sliderBounds.getY(), sliderBounds.getWidth(), sliderBounds.getHeight());
	}

	public void changeX(int x) {
		if (x < minX)
			buttonX = minX;
		else if (x > maxX)
			buttonX = maxX;
		else
			buttonX = x;
		bounds.setX(buttonX - VolumeButtonCts.VOLUME_WIDTH / 2);
		updateVolume();
	}

	private void updateVolume() {
		float range = maxX - minX;
		float value = buttonX - minX;
		volume = value / range;
	}

	private void setVolume(float targetVolume) {
		float range = maxX - minX;
		changeX((int) (minX + (range * targetVolume)));
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

	public float getVolume() {
		return volume;
	}

	public Rectangle getBounds() {
		return bounds;
	}
}
