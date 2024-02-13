package com.mandarina.lvlbuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mandarina.game.constants.GameCts;
import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.main.Game;
import com.mandarina.main.AppStage;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LvlBuilderMenu {

	private LvlBuilder lvlBuilder;

	public LvlBuilderMenu(LvlBuilder lvlBuilder) {
		this.lvlBuilder = lvlBuilder;
	}

	public MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem saveMenuItem = new MenuItem("Save");
		saveMenuItem.setOnAction(this::saveMainPaneAsImage);
		fileMenu.getItems().add(saveMenuItem);

		MenuItem inputMenuItem = new MenuItem("Resize");
		inputMenuItem.setOnAction(this::showInputDialog);
		fileMenu.getItems().add(inputMenuItem);

		MenuItem openMenuItem = new MenuItem("Load");
		openMenuItem.setOnAction(this::openFile);
		fileMenu.getItems().add(openMenuItem);

		MenuItem runMenuItem = new MenuItem("Run");
		runMenuItem.setOnAction(this::run);
		fileMenu.getItems().add(runMenuItem);

		MenuItem backMenuItem = new MenuItem("Back");
		backMenuItem.setOnAction(this::back);
		fileMenu.getItems().add(backMenuItem);

		menuBar.getMenus().add(fileMenu);
		return menuBar;
	}

	private void openFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			Image image = new Image(selectedFile.toURI().toString());
			resizeMainPane((int) image.getWidth(), (int) image.getHeight());
			loadLevel(image);
		}
	}

	private void run(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			Image lvl = new Image(selectedFile.toURI().toString());
			AppStage.get().getStage().setUserData(lvl);
			GameState.setState(GameState.PLAYING);
			Game game = GameState.getGame();
			game.show();
			game.start();
		}
	}

	private void back(ActionEvent event) {
		GameState.setState(GameState.MENU);
		Game game = GameState.getGame();
		game.show();
		game.start();
	}

	private void loadLevel(Image img) {
		PixelReader pixelReader = img.getPixelReader();
		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color c = pixelReader.getColor(i, j);
				int red = (int) (c.getRed() * 255);
				int green = (int) (c.getGreen() * 255);
				int blue = (int) (c.getBlue() * 255);

				loadManePane(lvlBuilder.getRedMainPane(), j, i, RGB.RED, red);
				loadManePane(lvlBuilder.getGreenMainPane(), j, i, RGB.GREEN, green);
				loadManePane(lvlBuilder.getBlueMainPane(), j, i, RGB.BLUE, blue);
			}
		}
	}

	private void loadManePane(ScrollPane mainPane, int x, int y, RGB rgb, int value) {
		VBox mainPaneBox = (VBox) mainPane.getContent();
		HBox row = (HBox) mainPaneBox.getChildren().get(x);
		VBox square = (VBox) row.getChildren().get(y);
		square.getChildren().clear();
		if (value != GameCts.EMPTY_TILE_VALUE) {
			LvlBuilderImage vi = LvlBuilderLoad.getImage(rgb, value);
			if (vi != null) {
				ImageView droppedImageView = new ImageView(vi);
				LvlBuilderUtil.setFitSize(droppedImageView, LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);
				square.getChildren().add(droppedImageView);
			}
		}
	}

	public static void setScrollBarPosition(ScrollPane from, ScrollPane to) {
		to.setHvalue(from.getHvalue());
		to.setVvalue(from.getVvalue());
	}

	private void saveMainPaneAsImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
		File file = fileChooser.showSaveDialog(new Stage());

		if (file != null) {
			Canvas canvas = getCanvas();
			writeCanvasImage(canvas, file);
		}
	}

	private void writeCanvasImage(Canvas canvas, File file) {
		try {
			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(Color.TRANSPARENT);
			WritableImage writableImage = canvas.snapshot(parameters, null);
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Canvas getCanvas() {
		Canvas canvas = new Canvas(lvlBuilder.getMainPaneX(), lvlBuilder.getMainPaneY());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (int j = 0; j < lvlBuilder.getMainPaneY(); j++) {
			for (int i = 0; i < lvlBuilder.getMainPaneX(); i++) {
				int redValue = getRGBValue(lvlBuilder.getRedMainPane(), j, i);
				int greenValue = getRGBValue(lvlBuilder.getGreenMainPane(), j, i);
				int blueValue = getRGBValue(lvlBuilder.getBlueMainPane(), j, i);
				Color color = Color.rgb(redValue, greenValue, blueValue);
				gc.setFill(color);
				gc.fillRect(i, j, 1, 1);
			}
		}
		return canvas;
	}

	private int getRGBValue(ScrollPane mainPane, int rowIndex, int colIndex) {
		int rgbValue = GameCts.EMPTY_TILE_VALUE;
		VBox mainBox = (VBox) mainPane.getContent();
		HBox row = (HBox) mainBox.getChildren().get(rowIndex);
		VBox col = (VBox) row.getChildren().get(colIndex);
		if (col.getChildren().size() == 1) {
			ImageView iv = (ImageView) col.getChildren().get(0);
			LvlBuilderImage vi = (LvlBuilderImage) iv.getImage();
			if (vi == null) {
				System.out.println(rowIndex + "," + colIndex);
			}
			rgbValue = vi.getValue();
		}
		return rgbValue;
	}

	private void showInputDialog(ActionEvent event) {
		// Create the grid pane for the input fields
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		// Create the text fields for X and Y values
		TextField xField = new TextField("80");
		TextField yField = new TextField("12");

		// Add labels and text fields to the grid
		grid.add(new Label("X:"), 0, 0);
		grid.add(xField, 1, 0);
		grid.add(new Label("Y:"), 0, 1);
		grid.add(yField, 1, 1);

		// Create the dialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Enter Values");
		dialog.setHeaderText(null);
		dialog.getDialogPane().setContent(grid);

		// Add OK and Cancel buttons
		ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

		// Show the dialog and wait for user input
		dialog.showAndWait().ifPresent(result -> {
			if (result == okButton) {
				// Process the entered values
				try {
					int x = Integer.parseInt(xField.getText());
					int y = Integer.parseInt(yField.getText());
					resizeMainPane(x, y);
				} catch (NumberFormatException e) {
					System.err.println("Error parsing values.");
				}
			}
		});
	}

	private void resizeMainPane(int x, int y) {
		int currentCanvasX = lvlBuilder.getMainPaneX();
		int currentCanvasY = lvlBuilder.getMainPaneY();
		lvlBuilder.setMainPaneX(x);
		lvlBuilder.setMainPaneY(y);

		VBox redMainPaneBox = (VBox) lvlBuilder.getRedMainPane().getContent();
		resizeMainPane(redMainPaneBox, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(redMainPaneBox, LvlBuilder.TILE_WIDTH * x, LvlBuilder.TILE_HEIGHT * y);

		VBox greenMainPaneBox = (VBox) lvlBuilder.getGreenMainPane().getContent();
		resizeMainPane(greenMainPaneBox, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(greenMainPaneBox, LvlBuilder.TILE_WIDTH * x, LvlBuilder.TILE_HEIGHT * y);

		VBox blueMainPaneBox = (VBox) lvlBuilder.getBlueMainPane().getContent();
		resizeMainPane(blueMainPaneBox, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(blueMainPaneBox, LvlBuilder.TILE_WIDTH * x, LvlBuilder.TILE_HEIGHT * y);
	}

	private void resizeMainPane(VBox mainPane, int currentCanvasX, int currentCanvasY, int newCanvasX, int newCanvasY) {
		// Resize rows (HBox elements)
		if (newCanvasY > currentCanvasY) {
			// Add new rows
			for (int i = currentCanvasY; i < newCanvasY; i++) {
				HBox newRow = new HBox();
				LvlBuilderUtil.setSize(newRow, LvlBuilder.TILE_WIDTH, LvlBuilder.TILE_HEIGHT);

				// Add squares to the new row
				for (int j = 0; j < newCanvasX; j++) {
					VBox square = LvlBuilderUtil.newSelectableVBox(LvlBuilder.TILE_WIDTH);
					newRow.getChildren().add(square);
				}

				mainPane.getChildren().add(newRow);
			}
		} else if (newCanvasY < currentCanvasY) {
			// Remove excess rows
			mainPane.getChildren().remove(newCanvasY, currentCanvasY);
		}

		// Resize columns (VBox elements)
		for (Node row : mainPane.getChildren()) {
			HBox currentRow = (HBox) row;

			if (newCanvasX > currentCanvasX) {
				// Add new squares to the current row
				for (int i = currentCanvasX; i < newCanvasX; i++) {
					VBox square = LvlBuilderUtil.newSelectableVBox(LvlBuilder.TILE_WIDTH);
					currentRow.getChildren().add(square);
				}
			} else if (newCanvasX < currentCanvasX) {
				// Remove excess squares from the current row
				currentRow.getChildren().remove(newCanvasX, currentRow.getChildren().size());
			}
		}
	}
}
