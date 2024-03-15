package com.mandarina.game.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameAudio {

	private static ClassLoader cl = Thread.currentThread().getContextClassLoader();

	public static final int MENU_1 = 0;
	public static final int LEVEL_1 = 1;
	public static final int LEVEL_2 = 2;
	public static int BUILD = 3;

	public static final int DIE = 0;
	public static final int JUMP = 1;
	public static final int GAMEOVER = 2;
	public static final int LVL_COMPLETED = 3;
	public static final int ATTACK_ONE = 4;
	public static final int ATTACK_TWO = 5;
	public static final int ATTACK_THREE = 6;

	private MediaPlayer[] songs;
	private MediaPlayer[] effects;
	private int currentSongId;
	private boolean songMute, effectMute;
	private Random rand = new Random();

	private Game game;

	public GameAudio(Game game) {
		this.game = game;
		loadSongs();
		loadEffects();
		playSong(MENU_1);
	}

	private void loadSongs() {
		String[] names = { "menu", "level1", "level2", "build" };
		songs = new MediaPlayer[names.length];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getMediaPlayer(names[i]);
		}
	}

	private void loadEffects() {
		String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
		effects = new MediaPlayer[effectNames.length];
		for (int i = 0; i < effects.length; i++) {
			effects[i] = getMediaPlayer(effectNames[i]);
		}
	}

	public static Media GetAudio(String fileName) {
		return GetAudio(Paths.get("assets", "audio", fileName));
	}

	private static Media GetAudio(Path path) {
		return new Media(cl.getResource(pathNormalization(path)).toString());
	}

	private static String pathNormalization(Path path) {
		return path.toString().replace('\\', '/');
	}

	private MediaPlayer getMediaPlayer(String name) {
		return new MediaPlayer(GetAudio(name + ".wav"));
	}

	public void setVolume(double volume) {
		game.getAudioOptions().getVolumeButton().setVolume(volume);
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		MediaPlayer mp = songs[currentSongId];
		if (mp.getStatus() == MediaPlayer.Status.PLAYING)
			mp.stop();
	}

	public void dispose() {
		for (MediaPlayer mp : songs) {
			mp.dispose();
		}
	}

	public void setLevelSong(int lvlIndex) {
		if (lvlIndex % 2 == 0)
			playSong(LEVEL_1);
		else
			playSong(LEVEL_2);
	}

	public void lvlCompleted() {
		stopSong();
		playEffect(LVL_COMPLETED);
	}

	public void playAttackSound() {
		int start = 4;
		start += rand.nextInt(3);
		playEffect(start);
	}

	public void playEffect(int effect) {
		MediaPlayer mp = effects[effect];
		mp.stop();
		mp.seek(mp.getStartTime());
//		mp.play();
	}

	public void playSong(int song) {
		stopSong();
		currentSongId = song;
		MediaPlayer mp = songs[currentSongId];
		mp.seek(mp.getStartTime());
//		mp.play();
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		for (MediaPlayer song : songs) {
			song.setMute(songMute);
		}
	}

	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for (MediaPlayer effect : effects) {
			effect.setMute(effectMute);
		}
		if (!effectMute)
			playEffect(JUMP);
	}

	private void updateSongVolume() {
		double volume = game.getAudioOptions().getVolumeButton().getVolume();
		for (MediaPlayer song : songs) {
			song.setVolume(volume);
		}
	}

	private void updateEffectsVolume() {
		double volume = game.getAudioOptions().getVolumeButton().getVolume();
		for (MediaPlayer effect : effects) {
			effect.setVolume(volume);
		}
	}
}
