package com.mandarina.lvlbuilder;

import java.util.List;

import com.mandarina.main.AppStage;

import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LvlBuilder {

	public static final int WINDOW_X = 1280;
	public static final int WINDOW_Y = 720;

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;

	private RGB rgb = RGB.RED;
	private int mainPaneX = 80;
	private int mainPaneY = 12;

	private ScrollPane redSidePane;
	private ScrollPane redMainPane;

	private ScrollPane greenSidePane;
	private ScrollPane greenMainPane;

	private ScrollPane blueSidePane;
	private ScrollPane blueMainPane;

	private Label mousePositionLabel;
	private HBox root;

	private ClipboardContent redClipboard = new ClipboardContent();
	private ClipboardContent greenClipboard = new ClipboardContent();
	private ClipboardContent blueClipboard = new ClipboardContent();

	public void show() {
		Scene scene = getScene();
		Stage stage = AppStage.get().getStage();
		stage.setScene(scene);
		stage.setTitle("Lvl Builder");
		stage.setResizable(false);
		stage.show();
	}

	private Scene getScene() {
		createSidePanes();
		createMainPanes();

		mousePositionLabel = new Label();

		root = new HBox(redSidePane, redMainPane);
		root.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();
			EventTarget target = event.getTarget();
			if (button == MouseButton.SECONDARY) {
				toClipboard(target);
			}
			event.consume();
		});

		LvlBuilderMenu lvlBuilderMenu = new LvlBuilderMenu(this);
		MenuBar menuBar = lvlBuilderMenu.getMenuBar();
		VBox vbox = new VBox(menuBar, root, mousePositionLabel);
		Scene scene = new Scene(vbox, WINDOW_X, WINDOW_Y);
		return scene;
	}

	private void updateMousePosition(MouseEvent event) {
		int x = (int) (event.getX() / TILE_WIDTH);
		int y = (int) (event.getY() / TILE_HEIGHT);
		mousePositionLabel.setText("X=" + x + ", Y=" + y);
	}

	private void createSidePanes() {
		redSidePane = createSidePane(LvlBuilderLoad.getItems(RGB.RED));
		greenSidePane = createSidePane(LvlBuilderLoad.getItems(RGB.GREEN));
		blueSidePane = createSidePane(LvlBuilderLoad.getItems(RGB.BLUE));
	}

	private void createMainPanes() {
		redMainPane = createMainPane();
		greenMainPane = createMainPane();
		blueMainPane = createMainPane();
	}

	private void switchPanes() {
		root.getChildren().clear();
		switch (rgb) {
		case RED:
			this.rgb = RGB.GREEN;
			LvlBuilderUtil.setScrollBarPosition(redMainPane, greenMainPane);
			root.getChildren().setAll(greenSidePane, greenMainPane);
			break;
		case GREEN:
			this.rgb = RGB.BLUE;
			LvlBuilderUtil.setScrollBarPosition(greenMainPane, blueMainPane);
			root.getChildren().setAll(blueSidePane, blueMainPane);
			break;
		case BLUE:
			this.rgb = RGB.RED;
			LvlBuilderUtil.setScrollBarPosition(blueMainPane, redMainPane);
			root.getChildren().setAll(redSidePane, redMainPane);
			break;
		}
	}

	private ScrollPane createSidePane(List<VBox> tiles) {
		FlowPane flowPane = new FlowPane();
		flowPane.setId("sidePane");

		int rows = 6;
		int gap = 2;
		int flowPaneWidth = TILE_WIDTH * rows + gap * 2;
		flowPane.setMinWidth(flowPaneWidth + gap * 2 + 1);
		flowPane.setStyle("-fx-background-color: lightgray;");
		flowPane.setHgap(gap);
		flowPane.setVgap(gap);
		flowPane.getChildren().addAll(tiles);

		// Wrap the panel in a ScrollPane
		ScrollPane scrollPane = new ScrollPane(flowPane);
		int scrollWidth = 22;
		scrollPane.setMinWidth(flowPaneWidth + scrollWidth);
		scrollPane.setPrefWidth(flowPaneWidth + scrollWidth);
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		return scrollPane;
	}

	private ScrollPane createMainPane() {
		VBox pane = new VBox();
		pane.setId("mainPane");
		LvlBuilderUtil.setSize(pane, TILE_WIDTH * mainPaneX, TILE_HEIGHT * mainPaneY);
		pane.setStyle("-fx-background-color: lightgray;");

		for (int i = 0; i < mainPaneY; i++) {
			HBox row = new HBox();
			LvlBuilderUtil.setSize(row, TILE_WIDTH, TILE_HEIGHT);

			// Add squares to the row
			for (int j = 0; j < mainPaneX; j++) {
				VBox square = LvlBuilderUtil.newSelectableVBox(TILE_WIDTH);
				row.getChildren().add(square);
			}

			pane.getChildren().add(row);
		}

		pane.setOnScroll(event -> {
			if (event.isShiftDown()) {
				switchPanes();
			}
		});

		pane.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();
			EventTarget target = event.getTarget();
			if (button == MouseButton.PRIMARY) {
				if (event.isShiftDown()) {
					removeTile(event);
				} else {
					fromClipboard(event);
				}
			} else if (button == MouseButton.SECONDARY) {
				toClipboard(target);
			}
			event.consume();
		});
		pane.setOnMouseDragged(event -> {
			MouseButton button = event.getButton();
			if (button == MouseButton.PRIMARY) {
				if (event.isShiftDown()) {
					removeTile(event);
				} else {
					fromClipboard(event);
				}
			}
			event.consume();
		});
		pane.setOnMouseMoved(this::updateMousePosition);

		ScrollPane scrollPane = new ScrollPane(pane);
		int scrollWidth = 18;
		int minWidth = TILE_WIDTH * 32;
		int minHeight = TILE_HEIGHT * 20;
		LvlBuilderUtil.setSize(scrollPane, minWidth + scrollWidth, minHeight + scrollWidth);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		return scrollPane;
	}

	private void removeTile(MouseEvent event) {
		int colIndex = (int) (event.getX() / TILE_WIDTH);
		int rowIndex = (int) (event.getY() / TILE_HEIGHT);

		VBox mainPane = (VBox) this.root.lookup("#mainPane");
		if (rowIndex >= 0 && rowIndex < mainPane.getChildren().size()) {
			HBox row = (HBox) mainPane.getChildren().get(rowIndex);
			if (colIndex >= 0 && colIndex < row.getChildren().size()) {
				VBox square = (VBox) row.getChildren().get(colIndex);
				square.getChildren().clear();
			}
		}
	}

	private ClipboardContent getCurrentClipboar() {
		switch (this.rgb) {
		case RED:
			return this.redClipboard;
		case GREEN:
			return this.greenClipboard;
		case BLUE:
			return this.blueClipboard;
		}
		return null;
	}

	private void fromClipboard(MouseEvent event) {
		ClipboardContent currentClipboar = getCurrentClipboar();
		if (currentClipboar.hasImage()) {
			LvlBuilderImage image = (LvlBuilderImage) currentClipboar.getImage();
			int colIndex = (int) (event.getX() / TILE_WIDTH);
			int rowIndex = (int) (event.getY() / TILE_HEIGHT);

			ImageView droppedImageView = new ImageView(image);
			LvlBuilderUtil.setFitSize(droppedImageView, TILE_WIDTH, TILE_HEIGHT);

			VBox mainPane = (VBox) this.root.lookup("#mainPane");
			if (rowIndex >= 0 && rowIndex < mainPane.getChildren().size()) {
				HBox row = (HBox) mainPane.getChildren().get(rowIndex);
				if (colIndex >= 0 && colIndex < row.getChildren().size()) {
					VBox square = (VBox) row.getChildren().get(colIndex);
					square.getChildren().clear();
					square.getChildren().add(droppedImageView);
				}
			}
		}
	}

	private void toClipboard(EventTarget target) {
		try {
			ClipboardContent currentClipboar = getCurrentClipboar();
			if (target instanceof ImageView imageView) {
				currentClipboar.putImage(imageView.getImage());
			} else if (((Pane) target).getChildren() instanceof ImageView imageView) {
				currentClipboar.putImage(imageView.getImage());
			} else if (((Pane) target).getChildren() instanceof ObservableList<?> && //
					((ObservableList<?>) ((Pane) target).getChildren()).size() == 1 && //
					((ObservableList<?>) ((Pane) target).getChildren()).get(0) instanceof ImageView imageView) {
				currentClipboar.putImage(imageView.getImage());
			} else {
				LvlBuilderUtil.debug(target);
			}
		} catch (Exception e) {
			System.out.println(e);
			LvlBuilderUtil.debug(target);
		}
	}

	public int getMainPaneX() {
		return mainPaneX;
	}

	public void setMainPaneX(int mainPaneX) {
		this.mainPaneX = mainPaneX;
	}

	public int getMainPaneY() {
		return mainPaneY;
	}

	public void setMainPaneY(int mainPaneY) {
		this.mainPaneY = mainPaneY;
	}

	public ScrollPane getRedMainPane() {
		return redMainPane;
	}

	public ScrollPane getGreenMainPane() {
		return greenMainPane;
	}

	public ScrollPane getBlueMainPane() {
		return blueMainPane;
	}
}
