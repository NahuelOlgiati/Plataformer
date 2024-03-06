package com.mandarina.game.main;

import com.mandarina.game.gamestates.GameState;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameInputs {

	private Game game;

	public GameInputs(Game game) {
		this.game = game;
	}

	private void onKeyPressed(KeyEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.game.getPlaying().keyPressed(e);
		}
	}

	private void onKeyReleased(KeyEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.game.getPlaying().keyReleased(e);
		case CREDITS -> this.game.getCredits().keyReleased(e);
		}
	}

	private void onMouseDragged(MouseEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.game.getPlaying().mouseDragged(e);
		case OPTIONS -> this.game.getGameOptions().mouseDragged(e);
		}
	}

	private void onMouseMoved(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.game.getMenu().mouseMoved(e);
		case PLAYING -> this.game.getPlaying().mouseMoved(e);
		case OPTIONS -> this.game.getGameOptions().mouseMoved(e);
		}
	}

	private void onMouseClicked(MouseEvent e) {
		switch (GameState.get()) {
		case PLAYING -> this.game.getPlaying().mouseClicked(e);
		}
	}

	private void onMousePressed(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.game.getMenu().mousePressed(e);
		case PLAYING -> this.game.getPlaying().mousePressed(e);
		case OPTIONS -> this.game.getGameOptions().mousePressed(e);
		}
	}

	private void onMouseReleased(MouseEvent e) {
		switch (GameState.get()) {
		case MENU -> this.game.getMenu().mouseReleased(e);
		case PLAYING -> this.game.getPlaying().mouseReleased(e);
		case OPTIONS -> this.game.getGameOptions().mouseReleased(e);
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
