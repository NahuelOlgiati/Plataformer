package com.mandarina.lvlbuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mandarina.lvlbuilder.feature.PNGMetadata;
import com.mandarina.lvlbuilder.feature.TileFeature;
import com.mandarina.main.AppStage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class LvlBuilder {

	private RGB rgb = RGB.RED;
	private int mainPaneX = 80;
	private int mainPaneY = 12;

	private Stage stage;

	private ScrollPane redSidePane;
	private ScrollPane redMainPane;

	private ScrollPane greenSidePane;
	private ScrollPane greenMainPane;

	private ScrollPane blueSidePane;
	private ScrollPane blueMainPane;

	private PNGMetadata pm;

	private Label mousePositionLabel;
	private HBox root;

	private ClipboardContent redClipboard = new ClipboardContent();
	private ClipboardContent greenClipboard = new ClipboardContent();
	private ClipboardContent blueClipboard = new ClipboardContent();

	private Set<SelectedTile> selectedTiles;

	public void show() {
		if (stage == null) {
			stage = getStage();
		}
		stage.show();
	}

	public void close() {
		stage.close();
	}

	private Stage getStage() {
		createSidePanes();
		createMainPanes();
		this.pm = new PNGMetadata();
		this.mousePositionLabel = new Label();

		root = new HBox(redSidePane, redMainPane);
		root.setOnKeyReleased(event -> {
			applyFeature(event);
		});

		LvlBuilderMenu lvlBuilderMenu = new LvlBuilderMenu(this);
		MenuBar menuBar = lvlBuilderMenu.getMenuBar();
		VBox vbox = new VBox(menuBar, root, mousePositionLabel);
		Scene scene = new Scene(vbox, LvlBuilderCts.WINDOW_X, LvlBuilderCts.WINDOW_Y);
		scene.getStylesheets().add(LvlBuilderLoad.GetCSS().toExternalForm());

		Stage newStage = new Stage();
		newStage.setTitle("Lvl Builder");
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.initStyle(StageStyle.UNDECORATED);
		Stage primaryStage = AppStage.get().getStage();
		newStage.initOwner(primaryStage);
		newStage.setScene(scene);
		newStage.setResizable(false);
		newStage.setOnHidden(event -> primaryStage.requestFocus());
		return newStage;
	}

	private void updateMousePosition(MouseEvent event) {
		int x = (int) (event.getX() / LvlBuilderCts.TILE_WIDTH);
		int y = (int) (event.getY() / LvlBuilderCts.TILE_HEIGHT);
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

	private ScrollPane createSidePane(List<AnchorPane> tiles) {
		VBox pane = new VBox();
		int maxElementsPerRow = 6;
		int gap = 2;

		for (int i = 0; i < tiles.size(); i += maxElementsPerRow) {
			int endIndex = Math.min(i + maxElementsPerRow, tiles.size());
			List<AnchorPane> rowTiles = tiles.subList(i, endIndex);
			HBox row = new HBox(gap);
			row.setSpacing(gap); // Set hgap
			row.getChildren().addAll(rowTiles);
			pane.getChildren().add(row);
		}

		pane.setSpacing(gap); // Set vgap
		pane.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();
			if (button == MouseButton.SECONDARY) {
				toClipboard(event, pane, gap);
			}
			event.consume();
		});

		ScrollPane scrollPane = new ScrollPane(pane);
		int scrollWidth = 18;
		int minWidth = (LvlBuilderCts.TILE_WIDTH + gap) * maxElementsPerRow / 2 + scrollWidth;
		int minHeight = LvlBuilderCts.TILE_HEIGHT * ((tiles.size() + maxElementsPerRow - 1) / maxElementsPerRow)
				+ scrollWidth;
		scrollPane.setMinWidth(minWidth);
		scrollPane.setMinHeight(minHeight);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		return scrollPane;
	}

	private ScrollPane createMainPane() {
		VBox pane = new VBox();
		LvlBuilderUtil.setSize(pane, LvlBuilderCts.TILE_WIDTH * mainPaneX, LvlBuilderCts.TILE_HEIGHT * mainPaneY);
		pane.setStyle("-fx-background-color: lightgray;");

		for (int i = 0; i < mainPaneY; i++) {
			HBox row = new HBox();
			LvlBuilderUtil.setSize(row, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
			for (int j = 0; j < mainPaneX; j++) {
				AnchorPane square = LvlBuilderUtil.newSelectableVBox();
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
			if (button == MouseButton.PRIMARY) {
				if (event.isShiftDown()) {
					removeTile(event, pane);
				} else if (event.isAltDown()) {
					selectTile(event, pane);
				} else {
					fromClipboard(event, pane);
				}
			} else if (button == MouseButton.SECONDARY) {
				toClipboard(event, pane);
			}
			event.consume();
		});
		pane.setOnMouseDragged(event -> {
			MouseButton button = event.getButton();
			if (button == MouseButton.PRIMARY) {
				if (event.isShiftDown()) {
					removeTile(event, pane);
				} else if (event.isAltDown()) {
					selectTile(event, pane);
				} else {
					fromClipboard(event, pane);
				}
			}
			event.consume();
		});
		pane.setOnMouseMoved(this::updateMousePosition);

		ScrollPane scrollPane = new ScrollPane(pane);
		int scrollWidth = 18;
		int minWidth = LvlBuilderCts.TILE_WIDTH * 32;
		int minHeight = LvlBuilderCts.TILE_HEIGHT * 20;
		LvlBuilderUtil.setSize(scrollPane, minWidth + scrollWidth, minHeight + scrollWidth);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		return scrollPane;
	}

	private void removeTile(MouseEvent event, VBox pane) {
		AnchorPane square = LvlBuilderUtil.getSquare(event, pane);
		square.getChildren().clear();
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

	private VBox getCurrentMainPane() {
		switch (this.rgb) {
		case RED:
			return this.getRedMainPane();
		case GREEN:
			return this.getGreenMainPane();
		case BLUE:
			return this.getBlueMainPane();
		}
		return null;
	}

	private void selectTile(MouseEvent event, VBox pane) {
		ImageView iv = LvlBuilderUtil.getImageView(event, pane);
		if (iv != null) {
			LvlBuilderImage vi = (LvlBuilderImage) iv.getImage();
			if (vi != null) {
				this.selectedTiles = this.selectedTiles == null ? new HashSet<SelectedTile>() : this.selectedTiles;
				Pair<Integer, Integer> coords = LvlBuilderUtil.getCoords(event);
				SelectedTile st = new SelectedTile(iv, rgb, coords);
				this.selectedTiles.add(st);
				AnchorPane square = LvlBuilderUtil.getSquare(coords, pane);
				LvlBuilderUtil.setEffect(square, LvlBuilderUtil.selectedEffect);
			}
		}
	}

	private void fromClipboard(MouseEvent event, VBox pane) {
		ClipboardContent currentClipboar = getCurrentClipboar();
		if (currentClipboar.hasImage()) {
			AnchorPane square = LvlBuilderUtil.getSquare(event, pane);
			square.getChildren().clear();
			LvlBuilderImage image = (LvlBuilderImage) currentClipboar.getImage();
			ImageView droppedImageView = new ImageView(image);
			LvlBuilderUtil.setFitSize(droppedImageView, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
			square.getChildren().add(droppedImageView);
		}
	}

	private void toClipboard(MouseEvent event, VBox pane) {
		ImageView iv = LvlBuilderUtil.getImageView(event, pane);
		if (iv != null) {
			getCurrentClipboar().putImage(iv.getImage());
		}
	}

	private void toClipboard(MouseEvent event, VBox pane, Integer gap) {
		ImageView iv = LvlBuilderUtil.getImageView(event, pane, gap);
		if (iv != null) {
			getCurrentClipboar().putImage(iv.getImage());
		}
	}

	private void applyFeature(KeyEvent event) {
		if (selectedTiles != null && event.getCode() != KeyCode.ALT) {
			for (SelectedTile st : selectedTiles) {
				for (TileFeature e : TileFeature.values()) {
					if (event.getCode() == e.getKeyCode()) {
						e.getManager().applyTo(st.getCoords(), pm, rgb, getCurrentMainPane());
					}
				}
			}
			for (SelectedTile st : selectedTiles) {
				st.getImageView().setEffect(null);
			}
			this.selectedTiles = null;
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

	public VBox getRedMainPane() {
		return (VBox) redMainPane.getContent();
	}

	public VBox getGreenMainPane() {
		return (VBox) greenMainPane.getContent();
	}

	public VBox getBlueMainPane() {
		return (VBox) blueMainPane.getContent();
	}

	public PNGMetadata getPNGMetadata() {
		return pm;
	}

	public void setPNGMetadata(PNGMetadata pm) {
		this.pm = pm;
	}
}
