package com.mandarina.main;

import com.mandarina.gamestates.Credits;
import com.mandarina.gamestates.GameOptions;
import com.mandarina.gamestates.Gamestate;
import com.mandarina.gamestates.Menu;
import com.mandarina.gamestates.Playing;

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

	@SuppressWarnings("incomplete-switch")
	private void onKeyPressed(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU -> this.menu.keyPressed(e);
		case PLAYING -> this.playing.keyPressed(e);
		case OPTIONS -> this.gameOptions.keyPressed(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onKeyReleased(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU -> this.menu.keyReleased(e);
		case PLAYING -> this.playing.keyReleased(e);
		case CREDITS -> this.credits.keyReleased(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onMouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING -> this.playing.mouseDragged(e);
		case OPTIONS -> this.gameOptions.mouseDragged(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onMouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU -> this.menu.mouseMoved(e);
		case PLAYING -> this.playing.mouseMoved(e);
		case OPTIONS -> this.gameOptions.mouseMoved(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onMouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING -> this.playing.mouseClicked(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onMousePressed(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU -> this.menu.mousePressed(e);
		case PLAYING -> this.playing.mousePressed(e);
		case OPTIONS -> this.gameOptions.mousePressed(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void onMouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
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
