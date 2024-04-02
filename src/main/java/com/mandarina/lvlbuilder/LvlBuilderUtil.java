package com.mandarina.lvlbuilder;

import com.mandarina.game.geometry.Point;

import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LvlBuilderUtil {

	public static final Effect overEffect = new DropShadow();
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

	public static ImageView getImageView(Point coords, VBox pane) {
		AnchorPane square = getSquare(coords, pane);
		return getImageView(square);
	}

	public static ImageView getImageView(MouseEvent event, VBox pane) {
		AnchorPane square = getSquare(event, pane);
		return getImageView(square);
	}

	public static ImageView getImageView(MouseEvent event, VBox pane, Integer gap) {
		AnchorPane square = getSquare(event, pane, gap);
		return getImageView(square);
	}

	public static AnchorPane getSquare(Point coords, VBox pane) {
		HBox row = (HBox) pane.getChildren().get(coords.getY());
		return (AnchorPane) row.getChildren().get(coords.getX());
	}

	public static AnchorPane getSquare(MouseEvent event, VBox pane) {
		Point coords = getCoords(event);
		HBox row = (HBox) pane.getChildren().get(coords.getY());
		return (AnchorPane) row.getChildren().get(coords.getX());
	}

	public static AnchorPane getSquare(MouseEvent event, VBox pane, Integer gap) {
		Point coords = getCoords(event, gap);
		HBox row = (HBox) pane.getChildren().get(coords.getY());
		return (AnchorPane) row.getChildren().get(coords.getX());
	}

	public static Point getCoords(MouseEvent event) {
		int colIndex = (int) (event.getX() / LvlBuilderCts.TILE_WIDTH);
		int rowIndex = (int) (event.getY() / LvlBuilderCts.TILE_HEIGHT);
		return new Point(colIndex, rowIndex);
	}

	public static Point getCoords(MouseEvent event, Integer gap) {
		int colIndex = (int) (event.getX() / (LvlBuilderCts.TILE_WIDTH + gap));
		int rowIndex = (int) (event.getY() / (LvlBuilderCts.TILE_HEIGHT + gap));
		return new Point(colIndex, rowIndex);
	}

	public static AnchorPane newSelectableVBox(ImageView imageView) {
		setFitSize(imageView, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
		AnchorPane square = newSelectableVBox();
		square.getChildren().add(imageView);
		return square;
	}

	public static AnchorPane newSelectableVBox() {
		AnchorPane ap = new AnchorPane();
		setSize(ap, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
		ap.setOnMouseEntered(event -> {
			if (isEffect(ap, null)) {
				setEffect(ap, overEffect);
			}
		});
		ap.setOnMouseExited(event -> {
			if (isEffect(ap, overEffect)) {
				setEffect(ap, null);
			}
		});
		return ap;
	}

	public static ImageView getImageView(AnchorPane ap) {
		if (ap.getChildren().size() >= 1)
			return ((ImageView) ap.getChildren().get(0));
		return null;
	}

	public static FlowPane getOrCreateFlowPane(AnchorPane ap) {
		FlowPane fp;
		if (ap.getChildren().size() > 1) {
			fp = (FlowPane) ap.getChildren().get(1);
		} else {
			fp = new FlowPane();
			fp.setMaxWidth(LvlBuilderCts.TILE_WIDTH);
			ap.getChildren().add(fp);
		}
		return fp;
	}

	public static void setEffect(AnchorPane ap, Effect effect) {
		ImageView iv = getImageView(ap);
		if (iv != null) {
			iv.setEffect(effect);
		}
	}

	public static Effect getEffect(AnchorPane ap) {
		ImageView iv = getImageView(ap);
		if (iv != null) {
			return iv.getEffect();
		}
		return null;
	}

	public static boolean isEffect(AnchorPane ap, Effect effect) {
		return effect == null ? getEffect(ap) == null : effect.equals(getEffect(ap));
	}
}
