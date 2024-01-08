package com.mandarina.main;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public abstract class GameLoopLike {

	private static final int UPS_SET = 160;
	private static final Duration KEYFRAME_UPDATE_DURATION = Duration.seconds(1.0 / UPS_SET);
	private static final boolean SHOW_FPS_UPS = true;

	private static final long ONE_SECOND_IN_NANOSECONDS = 1_000_000_000L;

	private long lastTime = System.nanoTime();
	private int frames = 0;
	private int updates = 0;

	private Timeline updateTimeline;
	private AnimationTimer animationTimer;

	public GameLoopLike() {
		updateTimeline = new Timeline(new KeyFrame(KEYFRAME_UPDATE_DURATION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				update();
				updates++;
			}
		}));
		updateTimeline.setCycleCount(Timeline.INDEFINITE);

		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				Platform.runLater(() -> {
					repaint();
					frames++;
				});

				if (SHOW_FPS_UPS && now - lastTime >= ONE_SECOND_IN_NANOSECONDS) {
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					lastTime = now;
					frames = 0;
					updates = 0;
				}
			}
		};
	}

	public void start() {
		updateTimeline.play();
		animationTimer.start();
	}

	public abstract void update();

	public abstract void repaint();
}
