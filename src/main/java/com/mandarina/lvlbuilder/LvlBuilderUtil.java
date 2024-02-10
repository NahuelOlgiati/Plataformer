package com.mandarina.lvlbuilder;

import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LvlBuilderUtil {

	private static final DropShadow dropShadow = new DropShadow();

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

	public static void debug(EventTarget target) {
		System.out.println(target);
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren() instanceof ObservableList);
		System.out.println(((Pane) target).getChildren() instanceof ImageView);
		System.out.println();
	}

	public static VBox newSelectableVBox(int size) {
		VBox vbox = new VBox();
		setSize(vbox, size, size);
		vbox.setOnMouseEntered(event -> {
			if (vbox.getChildren().size() == 1)
				((ImageView) vbox.getChildren().get(0)).setEffect(dropShadow);
		});
		vbox.setOnMouseExited(event -> {
			if (vbox.getChildren().size() == 1)
				((ImageView) vbox.getChildren().get(0)).setEffect(null);
		});
		return vbox;
	}
}
