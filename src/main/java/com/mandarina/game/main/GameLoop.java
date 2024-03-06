package com.mandarina.game.main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public abstract class GameLoop {

	private static final boolean SHOW_FPS_UPS = false;

	private static final int UPS_SET = 100;
	private static final int FPS_SET = 60;

	private static final int DELTA_SET = UPS_SET / FPS_SET;
	private static final int MAX_DELTA_SET = DELTA_SET;

	private static final Duration KEYFRAME_UPDATE_DURATION = Duration.seconds(1.0 / UPS_SET);
	private static final Duration KEYFRAME_REPAINT_DURATION = Duration.seconds(1.0 / FPS_SET);

	private int frames;
	private int updates;

	private Timeline updateTimeline;
	private Timeline repaintTimer;

	private int delta;

	public GameLoop() {
		init();
	}

	private void init() {
		this.frames = 0;
		this.updates = 0;
		this.delta = 0;
		updateTimeline = new Timeline(new KeyFrame(KEYFRAME_UPDATE_DURATION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				update();
				updates++;
				delta++;

				if (delta > MAX_DELTA_SET) {
					repaint();
					delta = 0;
				}
			}
		}));
		updateTimeline.setCycleCount(Animation.INDEFINITE);

		repaintTimer = new Timeline(new KeyFrame(KEYFRAME_REPAINT_DURATION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				Platform.runLater(() -> {
					repaint();
//				});
				frames++;
				delta -= DELTA_SET;

				if (delta < -MAX_DELTA_SET) {
					update();
					delta = 0;
				}

				if (SHOW_FPS_UPS && frames == 60) {
					System.out.println("UPSx60f: " + updates);
					frames = 0;
					updates = 0;
				}
			}
		}));
		repaintTimer.setCycleCount(Animation.INDEFINITE);
	}

	public void start() {
		init();
		updateTimeline.play();
		repaintTimer.play();
	}

	public void stop() {
		updateTimeline.stop();
		repaintTimer.stop();
		updateTimeline = null;
		repaintTimer = null;
	}

	public abstract void update();

	public abstract void repaint();
}
