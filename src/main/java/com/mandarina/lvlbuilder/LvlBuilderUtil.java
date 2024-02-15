package com.mandarina.lvlbuilder;

import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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

	public static ImageView getImageView(EventTarget target) {
		try {
			if (target instanceof ImageView imageView) {
				return imageView;
			}
			if (((Pane) target).getChildren() instanceof ImageView imageView) {
				return imageView;
			}
			if (((Pane) target).getChildren() instanceof ObservableList<?> && //
					((ObservableList<?>) ((Pane) target).getChildren()).size() == 1 && //
					((ObservableList<?>) ((Pane) target).getChildren()).get(0) instanceof ImageView imageView) {
				return imageView;
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static void debug(EventTarget target) {
		System.out.println(target);
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren() instanceof ObservableList);
		System.out.println(((Pane) target).getChildren() instanceof ImageView);
		System.out.println();
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
