package com.mandarina.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public abstract class GameLoop2 {

	private static final int FPS_SET = 140;
	private static final int UPS_SET = 160;
	private static final Duration KEYFRAME_DURATION = Duration.seconds(1.0 / FPS_SET);
	private static final boolean SHOW_FPS_UPS = true;

	private static final long ONE_SECOND_IN_NANOSECONDS = 1_000_000_000L;
	private long timePerFrame = ONE_SECOND_IN_NANOSECONDS / FPS_SET;
	private long timePerUpdate = ONE_SECOND_IN_NANOSECONDS / UPS_SET;

	private long lastTime = System.nanoTime();
	private int frames = 0;
	private int updates = 0;

	private long deltaU = 0;
	private long deltaF = 0;

	private Timeline timeline;

	public GameLoop2() {
		timeline = new Timeline(new KeyFrame(KEYFRAME_DURATION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				long now = System.nanoTime();
				deltaU = (now - lastTime) / timePerUpdate;
				deltaF = (now - lastTime) / timePerFrame;

				if (deltaU >= 1) {
					update();
					updates++;
					deltaU--;
				}

				if (deltaF >= 1) {
					Platform.runLater(() -> {
						repaint();
						frames++;
						deltaF--;
					});
				}

				if (SHOW_FPS_UPS && now - lastTime >= ONE_SECOND_IN_NANOSECONDS) {
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					lastTime = now;
					frames = 0;
					updates = 0;
				}
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
	}

	public void start() {
		timeline.play();
	}

	public abstract void update();

	public abstract void repaint();
}
