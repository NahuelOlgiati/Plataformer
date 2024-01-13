package com.mandarina.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageBuilderApp extends Application {

	private ScrollPane sidePanel;
	private VBox mainPanel;

	private ScrollPane sidePanel2;
	private VBox mainPanel2;

	HBox root;

	private Color[][] colors = new Color[10][10];

	private int currentPanels = 1;

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

		panel.getChildren().addAll(getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView(),
				getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView(),
				getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView(), getNewImageView(),
				getNewImageView());

		// Drag and drop functionality for the entire panel
		panel.setOnDragDetected(event -> {
			Dragboard dragboard = panel.startDragAndDrop(TransferMode.COPY);

			// Add an image to the dragboard
			Image image = new Image("assets/tree_one_atlas.png");
			ClipboardContent content = new ClipboardContent();
			content.putImage(image);
			dragboard.setContent(content);

			event.consume();
		});

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

	private ImageView getNewImageView() {
		// Add an image to the side panel
		ImageView imageView = new ImageView(new Image("assets/tree_one_atlas.png"));
		setFitSize(imageView, 50);

		// Allow the image to be dragged
		imageView.setOnDragDetected(event -> {
			Dragboard dragboard = imageView.startDragAndDrop(TransferMode.COPY);

			// Add the image to the dragboard
			ClipboardContent content = new ClipboardContent();
			content.putImage(imageView.getImage());
			dragboard.setContent(content);

			event.consume();
		});
		return imageView;
	}

	private void setFitSize(ImageView imageView, int size) {
		imageView.setFitWidth(size);
		imageView.setFitHeight(size);
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
		setSize(panel, 50 * xTiles, 50 * yTiles);
		panel.setStyle("-fx-background-color: white;");

		int squareSize = 50; // Size of each square in pixels

		// Create a grid of squares using an HBox for each row
		for (int i = 0; i < panel.getMinWidth() / squareSize; i++) {
			HBox row = new HBox();
			setSize(row, squareSize, squareSize);

			// Add squares to the row
			for (int j = 0; j < panel.getMinWidth() / squareSize; j++) {
				VBox square = createSquare(squareSize);
				row.getChildren().add(square);
			}

			panel.getChildren().add(row);
		}

		// Set up drag over and drag dropped event handlers
		panel.setOnDragOver(event -> {
			if (event.getGestureSource() != panel && event.getDragboard().hasImage()) {
				event.acceptTransferModes(TransferMode.COPY);
			}
			event.consume();
		});

		panel.setOnScroll(event -> {
			if (event.getDeltaY() < 0) {
				switchPanels(2);
			} else {
				switchPanels(1);
			}
		});

		panel.setOnDragDropped(event -> {
			Dragboard dragboard = event.getDragboard();
			boolean success = false;

			if (dragboard.hasImage()) {
				Image image = dragboard.getImage();
				int colIndex = (int) (event.getX() / squareSize);
				int rowIndex = (int) (event.getY() / squareSize);

				this.colors[rowIndex][colIndex] = null;

				ImageView droppedImageView = new ImageView(image);
				setFitSize(droppedImageView, squareSize);

				// Add the image to the specific square in the grid
				if (rowIndex < panel.getChildren().size()) {
					HBox row = (HBox) panel.getChildren().get(rowIndex);
					if (colIndex < row.getChildren().size()) {
						VBox square = (VBox) row.getChildren().get(colIndex);
						square.getChildren().add(droppedImageView);
						success = true;
					}
				}
			}

			event.setDropCompleted(success);
			event.consume();
		});

		return panel;
	}

	private VBox createSquare(int size) {
		VBox square = new VBox();
		setSize(square, size, size);
		square.setStyle("-fx-border-color: black; -fx-border-width: 0px;");
		return square;
	}

	private void setSize(Region r, int width, int height) {
		r.setMinWidth(width);
		r.setMaxWidth(width);
		r.setMinHeight(height);
		r.setMaxHeight(height);
		r.setPrefSize(width, height);
	}
}
