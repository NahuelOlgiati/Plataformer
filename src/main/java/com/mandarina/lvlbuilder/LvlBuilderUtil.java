package com.mandarina.lvlbuilder;

import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class LvlBuilderUtil {

	private static final Effect overEffect = new DropShadow();
	public static final Effect selectedEffect = new SepiaTone();

	public static void setSize(Region r, int width, int height) {
		r.setMinWidth(width);
		r.setMaxWidth(width);
		r.setMinHeight(height);
		r.setMaxHeight(height);
		r.setPrefSize(width, height);
	}

	public static void setFitSize(ImageView imageView, int width, int height) {
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
	}

	public static void setScrollBarPosition(ScrollPane from, ScrollPane to) {
		to.setHvalue(from.getHvalue());
		to.setVvalue(from.getVvalue());
	}

	public static ImageView getImageView(MouseEvent event, VBox pane) {
		ImageView iv = null;
		VBox square = getSquare(event, pane);
		if (square.getChildren().size() == 1) {
			iv = (ImageView) square.getChildren().get(0);
		}
		return iv;
	}

	public static ImageView getImageView(MouseEvent event, VBox pane, Integer gap) {
		ImageView iv = null;
		VBox square = getSquare(event, pane, gap);
		if (square.getChildren().size() == 1) {
			iv = (ImageView) square.getChildren().get(0);
		}
		return iv;
	}

	public static VBox getSquare(MouseEvent event, VBox pane) {
		Pair<Integer, Integer> coords = getCoords(event);
		HBox row = (HBox) pane.getChildren().get(coords.getValue());
		return (VBox) row.getChildren().get(coords.getKey());
	}

	public static VBox getSquare(MouseEvent event, VBox pane, Integer gap) {
		Pair<Integer, Integer> coords = getCoords(event, gap);
		HBox row = (HBox) pane.getChildren().get(coords.getValue());
		return (VBox) row.getChildren().get(coords.getKey());
	}

	public static Pair<Integer, Integer> getCoords(MouseEvent event) {
		int colIndex = (int) (event.getX() / LvlBuilderCts.TILE_WIDTH);
		int rowIndex = (int) (event.getY() / LvlBuilderCts.TILE_HEIGHT);
		return new Pair<Integer, Integer>(colIndex, rowIndex);
	}

	public static Pair<Integer, Integer> getCoords(MouseEvent event, Integer gap) {
		int colIndex = (int) (event.getX() / (LvlBuilderCts.TILE_WIDTH + gap));
		int rowIndex = (int) (event.getY() / (LvlBuilderCts.TILE_HEIGHT + gap));
		return new Pair<Integer, Integer>(colIndex, rowIndex);
	}

	public static VBox newSelectableVBox(ImageView imageView) {
		setFitSize(imageView, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
		VBox square = LvlBuilderUtil.newSelectableVBox();
		square.getChildren().add(imageView);
		return square;
	}

	public static VBox newSelectableVBox() {
		VBox vbox = new VBox();
		setSize(vbox, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
		vbox.setOnMouseEntered(event -> {
			if (isEffect(vbox, null)) {
				setEffect(vbox, overEffect);
			}
		});
		vbox.setOnMouseExited(event -> {
			if (isEffect(vbox, overEffect)) {
				setEffect(vbox, null);
			}
		});
		return vbox;
	}

	private static ImageView getImageView(VBox vbox) {
		if (vbox.getChildren().size() == 1)
			return ((ImageView) vbox.getChildren().get(0));
		return null;
	}

	private static void setEffect(VBox vbox, Effect effect) {
		ImageView iv = getImageView(vbox);
		if (iv != null) {
			iv.setEffect(effect);
		}
	}

	private static Effect getEffect(VBox vbox) {
		ImageView iv = getImageView(vbox);
		if (iv != null) {
			return iv.getEffect();
		}
		return null;
	}

	private static boolean isEffect(VBox vbox, Effect effect) {
		return effect == null ? getEffect(vbox) == null : effect.equals(getEffect(vbox));
	}
}
