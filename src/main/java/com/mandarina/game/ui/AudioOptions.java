package com.mandarina.game.ui;

import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;

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
		int vX = AppStage.Scale(309);
		int vY = AppStage.Scale(278);
		volumeButton = new VolumeButton(vX, vY, AppStage.Scale(VolumeButtonCts.VOLUME_WIDTH_DEFAULT),
				AppStage.Scale(VolumeButtonCts.VOLUME_HEIGHT_DEFAULT));
	}

	private void createSoundButtons() {
		int soundX = AppStage.Scale(450);
		int musicY = AppStage.Scale(140);
		int sfxY = AppStage.Scale(186);
		musicButton = new SoundButton(soundX, musicY, AppStage.Scale(SoundButtonCts.SIZE_DEFAULT),
				AppStage.Scale(SoundButtonCts.SIZE_DEFAULT));
		sfxButton = new SoundButton(soundX, sfxY, AppStage.Scale(SoundButtonCts.SIZE_DEFAULT),
				AppStage.Scale(SoundButtonCts.SIZE_DEFAULT));
	}

	public void update() {
		musicButton.update();
		sfxButton.update();

		volumeButton.update();
	}

	public void draw(GameDrawer g) {
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

	public void scale() {
		createVolumeButton();
		createSoundButtons();
	}
}
