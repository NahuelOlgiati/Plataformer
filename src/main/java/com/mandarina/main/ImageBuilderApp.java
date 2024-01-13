package com.mandarina.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageBuilderApp extends Application {

	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;

	private ScrollPane sidePanel;
	private VBox mainPanel;

	private ScrollPane sidePanel2;
	private VBox mainPanel2;

	HBox root;

	private Color[][] colors = new Color[10][10];

	private int currentPanels = 1;

	private ClipboardContent clipboard = new ClipboardContent();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Initialize panels
		sidePanel = createSidePanel();
		mainPanel = createMainPanel(10, 10);

		sidePanel2 = createSidePanel();
		mainPanel2 = createMainPanel(10, 10);

		// Add panels to the root layout
		root = new HBox(sidePanel, mainPanel);
		root.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();
			EventTarget target = event.getTarget();
			if (button == MouseButton.SECONDARY) {
				toClipboard(target);
			}
			event.consume();
		});

		// Create scene and set it on the stage
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false); // Make the stage not resizable

		// Set up the stage
		primaryStage.setTitle("Image Drag and Drop App");
		addMenuBar(primaryStage);
		primaryStage.show();
	}

	private void addMenuBar(Stage primaryStage) {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem downloadPngItem = new MenuItem("Download PNG");

		downloadPngItem.setOnAction(event -> {
			try {
				saveMainPanelAsImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		fileMenu.getItems().add(downloadPngItem);
		menuBar.getMenus().add(fileMenu);

		VBox vbox = new VBox(menuBar, root);
		Scene scene = new Scene(vbox, 800, 400);
		primaryStage.setScene(scene);
	}

	private void switchPanels(int panelNumber) {
		if (panelNumber != currentPanels) {
			if (panelNumber == 1) {
				root.getChildren().setAll(sidePanel, mainPanel);
				currentPanels = 1;
			} else if (panelNumber == 2) {
				root.getChildren().setAll(sidePanel2, mainPanel2);
				currentPanels = 2;
			}
		}
	}

	private ScrollPane createSidePanel() {
		VBox panel = new VBox();
		panel.setMinWidth(200);
		panel.setStyle("-fx-background-color: lightgray;");

		panel.getChildren().addAll(getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView2(),
				getNewImageView2());

		// Wrap the panel in a ScrollPane
		ScrollPane scrollPane = new ScrollPane(panel);
		scrollPane.setPrefWidth(100); // Set the preferred width of the scroll pane

		// Set up scroll pane properties
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		// Add a border to the scroll pane for better visibility
		scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		return scrollPane;
	}

	private VBox getNewImageView() {
		VBox square = createSquare(TILE_WIDTH);
		Image image = new Image("assets/tree_one_atlas.png");
		ImageView imageView = new ImageView(image);
		setFitSize(imageView, TILE_WIDTH, TILE_HEIGHT);
		square.getChildren().add(imageView);
		return square;
	}

	private VBox getNewImageView2() {
		VBox square = createSquare(TILE_WIDTH);
		Image image = new Image("assets/ball.png");
		ImageView imageView = new ImageView(image);
		setFitSize(imageView, TILE_WIDTH, TILE_HEIGHT);
		square.getChildren().add(imageView);
		return square;
	}

	private void saveMainPanelAsImage() throws IOException {
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT); // Set background to transparent

		WritableImage writableImage = mainPanel.snapshot(parameters, null);

		// Choose a file to save the image
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
		File file = fileChooser.showSaveDialog(new Stage());

		if (file != null) {
			// Convert the snapshot to a BufferedImage
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

			// Save the BufferedImage to the chosen file
			ImageIO.write(bufferedImage, "png", file);
		}
	}

	private VBox createMainPanel(int xTiles, int yTiles) {
		VBox panel = new VBox();
		setSize(panel, TILE_WIDTH * xTiles, TILE_HEIGHT * yTiles);
		panel.setStyle("-fx-background-color: white;");

		for (int i = 0; i < panel.getMinWidth() / TILE_WIDTH; i++) {
			HBox row = new HBox();
			setSize(row, TILE_WIDTH, TILE_HEIGHT);

			// Add squares to the row
			for (int j = 0; j < panel.getMinWidth() / TILE_WIDTH; j++) {
				VBox square = createSquare(TILE_WIDTH);
				row.getChildren().add(square);
			}

			panel.getChildren().add(row);
		}

		panel.setOnScroll(event -> {
			if (event.getDeltaY() < 0) {
				switchPanels(2);
			} else {
				switchPanels(1);
			}
		});

		panel.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();
			EventTarget target = event.getTarget();
			if (button == MouseButton.PRIMARY) {
				fromClipboard(event);
			} else if (button == MouseButton.SECONDARY) {
				toClipboard(target);
			}
			event.consume();
		});

		return panel;
	}

	private void fromClipboard(MouseEvent event) {
		if (clipboard.hasImage()) {
			Image image = clipboard.getImage();
			int colIndex = (int) (event.getX() / TILE_WIDTH);
			int rowIndex = (int) (event.getY() / TILE_HEIGHT);

			this.colors[rowIndex][colIndex] = null;

			ImageView droppedImageView = new ImageView(image);
			setFitSize(droppedImageView, TILE_WIDTH, TILE_HEIGHT);

			// Add the image to the specific square in the grid
			if (rowIndex < mainPanel.getChildren().size()) {
				HBox row = (HBox) mainPanel.getChildren().get(rowIndex);
				if (colIndex < row.getChildren().size()) {
					VBox square = (VBox) row.getChildren().get(colIndex);
					square.getChildren().clear();
					square.getChildren().add(droppedImageView);
				}
			}
		}
	}

	private void toClipboard(EventTarget target) {
		try {
			if (target instanceof ImageView imageView) {
				clipboard.putImage(imageView.getImage());
			} else if (((Pane) target).getChildren() instanceof ImageView imageView) {
				clipboard.putImage(imageView.getImage());
			} else if (((Pane) target).getChildren() instanceof ObservableList<?> && //
					((ObservableList<?>) ((Pane) target).getChildren()).size() == 1 && //
					((ObservableList<?>) ((Pane) target).getChildren()).get(0) instanceof ImageView imageView) {
				clipboard.putImage(imageView.getImage());
			} else {
				debug(target);
			}
		} catch (Exception e) {
			System.out.println(e);
			debug(target);
		}
	}

	private void debug(EventTarget target) {
		System.out.println(target);
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren());
		System.out.println(((Pane) target).getChildren() instanceof ObservableList);
		System.out.println(((Pane) target).getChildren() instanceof ImageView);
		System.out.println();
	}

	private VBox createSquare(int size) {
		VBox vbox = new VBox();
		setSize(vbox, size, size);

		makeSelectable(vbox);

		return vbox;
	}

	private void makeSelectable(VBox vbox) {
		vbox.setOnMouseEntered(event -> {
			vbox.setStyle("-fx-border-color: blue; -fx-border-width: 1;");
		});

		vbox.setOnMouseExited(event -> {
			vbox.setStyle("-fx-border-color: transparent; -fx-border-width: 1;");
		});
	}

	private void setSize(Region r, int width, int height) {
		r.setMinWidth(width);
		r.setMaxWidth(width);
		r.setMinHeight(height);
		r.setMaxHeight(height);
		r.setPrefSize(width, height);
	}

	private void setFitSize(ImageView imageView, int width, int height) {
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
	}
}
