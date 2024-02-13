package com.mandarina.game.main;

import com.mandarina.game.gamestates.Credits;
import com.mandarina.game.gamestates.GameOptions;
import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.gamestates.Menu;
import com.mandarina.game.gamestates.Playing;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameInputs {

	private Playing playing;
	private Menu menu;
	private GameOptions gameOptions;
	private Credits credits;

	public GameInputs(Playing playing, Menu menu, GameOptions gameOptions, Credits credits) {
		this.playing = playing;
		this.menu = menu;
		this.gameOptions = gameOptions;
		this.credits = credits;
	}

	private void onKeyPressed(KeyEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.playing.keyPressed(e);
		}
	}

	private void onKeyReleased(KeyEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.playing.keyReleased(e);
		case CREDITS -> this.credits.keyReleased(e);
		}
	}

	private void onMouseDragged(MouseEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.playing.mouseDragged(e);
		case OPTIONS -> this.gameOptions.mouseDragged(e);
		}
	}

	private void onMouseMoved(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.menu.mouseMoved(e);
		case PLAYING -> this.playing.mouseMoved(e);
		case OPTIONS -> this.gameOptions.mouseMoved(e);
		}
	}

	private void onMouseClicked(MouseEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.playing.mouseClicked(e);
		}
	}

	private void onMousePressed(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.menu.mousePressed(e);
		case PLAYING -> this.playing.mousePressed(e);
		case OPTIONS -> this.gameOptions.mousePressed(e);
		}
	}

	private void onMouseReleased(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.menu.mouseReleased(e);
		case PLAYING -> this.playing.mouseReleased(e);
		case OPTIONS -> this.gameOptions.mouseReleased(e);
		}
	}

	public void init(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> this.onKeyPressed(e));
		scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> this.onKeyReleased(e));
		scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> this.onMouseDragged(e));
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> this.onMouseMoved(e));
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> this.onMouseClicked(e));
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> this.onMousePressed(e));
		scene.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> this.onMouseReleased(e));
	}
}
