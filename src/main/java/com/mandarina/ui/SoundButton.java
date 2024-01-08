package com.mandarina.ui;

import com.mandarina.constants.UICts;
import com.mandarina.utilz.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SoundButton extends PauseButton {

	private Image[][] soundImgs;
	private boolean mouseOver, mousePressed;
	private boolean muted;
	private int rowIndex, colIndex;

	public SoundButton(int x, int y, int width, int height) {
		super(x, y, width, height);

		loadSoundImgs();
	}

	private void loadSoundImgs() {
		Image temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
		soundImgs = new Image[2][3];
		for (int j = 0; j < soundImgs.length; j++)
			for (int i = 0; i < soundImgs[j].length; i++)
				soundImgs[j][i] = new WritableImage(temp.getPixelReader(), i * UICts.PauseButtons.SOUND_SIZE_DEFAULT,
						j * UICts.PauseButtons.SOUND_SIZE_DEFAULT, UICts.PauseButtons.SOUND_SIZE_DEFAULT,
						UICts.PauseButtons.SOUND_SIZE_DEFAULT);
	}

	public void update() {
		if (muted)
			rowIndex = 1;
		else
			rowIndex = 0;

		colIndex = 0;
		if (mouseOver)
			colIndex = 1;
		if (mousePressed)
			colIndex = 2;

	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public void draw(GraphicsContext g) {
		g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height);
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

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

}
