package com.mandarina.game.ui;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.constants.UICts;
import com.mandarina.game.main.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class AudioOptions {

	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;

	private Game game;

	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}

	private void createVolumeButton() {
		int vX = (int) (309 * GameCts.SCALE);
		int vY = (int) (278 * GameCts.SCALE);
		volumeButton = new VolumeButton(vX, vY, UICts.VolumeButtons.VOLUME_WIDTH,
				UICts.VolumeButtons.VOLUME_HEIGHT);
	}

	private void createSoundButtons() {
		int soundX = (int) (450 * GameCts.SCALE);
		int musicY = (int) (140 * GameCts.SCALE);
		int sfxY = (int) (186 * GameCts.SCALE);
		musicButton = new SoundButton(soundX, musicY, UICts.PauseButtons.SOUND_SIZE, UICts.PauseButtons.SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, UICts.PauseButtons.SOUND_SIZE, UICts.PauseButtons.SOUND_SIZE);
	}

	public void update() {
		musicButton.update();
		sfxButton.update();

		volumeButton.update();
	}

	public void draw(GraphicsContext g) {
		// Sound buttons
		musicButton.draw(g);
		sfxButton.draw(g);

		// Volume Button
		volumeButton.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getVolume();
			volumeButton.changeX((int) e.getX());
			float valueAfter = volumeButton.getVolume();
			if (valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}

		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggleEffectMute();
			}
		}

		musicButton.resetBools();
		sfxButton.resetBools();

		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);

		volumeButton.setMouseOver(false);

		if (isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public VolumeButton getVolumeButton() {
		return volumeButton;
	}
}
