package com.mandarina.lvlbuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.core.io.FileUrlResource;

import com.mandarina.game.gamestates.GameState;
import com.mandarina.game.main.Game;
import com.mandarina.game.main.GameCts;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

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
			try {
				LvlBuilderImage lvl = new LvlBuilderImage(new FileInputStream(selectedFile),
						new FileUrlResource(selectedFile.toURI().toURL()));
				resizeMainPane((int) lvl.getWidth(), (int) lvl.getHeight());
				loadLevel(lvl);
				loadMetadata(lvl);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private void run(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				LvlBuilderImage lvl = new LvlBuilderImage(new FileInputStream(selectedFile),
						new FileUrlResource(selectedFile.toURI().toURL()));
				AppStage.get().getStage().setUserData(lvl);
				GameState.setState(GameState.PLAYING);
				Game game = GameState.getGame();
				game.show();
				game.start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
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
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				int green = (int) (c.getGreen() * 255);
				int blue = (int) (c.getBlue() * 255);

				Pair<Integer, Integer> coords = new Pair<Integer, Integer>(x, y);
				loadManePane(coords, lvlBuilder.getRedMainPane(), RGB.RED, red);
				loadManePane(coords, lvlBuilder.getGreenMainPane(), RGB.GREEN, green);
				loadManePane(coords, lvlBuilder.getBlueMainPane(), RGB.BLUE, blue);
			}
		}
	}

	private void loadMetadata(LvlBuilderImage image) {
		PNGMetadata pm = new PNGMetadata(image);
		LvlBuilderMetada.log(pm);
		lvlBuilder.setPNGMetadata(pm);
		loadManePaneMetadata(lvlBuilder.getRedMainPane(), RGB.RED, pm);
		loadManePaneMetadata(lvlBuilder.getGreenMainPane(), RGB.GREEN, pm);
		loadManePaneMetadata(lvlBuilder.getBlueMainPane(), RGB.BLUE, pm);
	}

	private void loadManePane(Pair<Integer, Integer> coords, VBox mainPane, RGB rgb, int value) {
		AnchorPane square = LvlBuilderUtil.getSquare(coords, mainPane);
		square.getChildren().clear();
		if (value != GameCts.EMPTY_TILE_VALUE) {
			LvlBuilderImage vi = LvlBuilderLoad.getImage(rgb, value);
			if (vi != null) {
				ImageView droppedImageView = new ImageView(vi);
				LvlBuilderUtil.setFitSize(droppedImageView, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
				square.getChildren().add(droppedImageView);
			}
		}
	}

	private void loadManePaneMetadata(VBox mainPane, RGB rgb, PNGMetadata pm) {
		for (TileFeature tf : TileFeature.values()) {
			tf.apply(pm, rgb, mainPane);
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
			PNGMetadata pm = lvlBuilder.getPNGMetadata();
			LvlBuilderMetada.writeMetadata(file, pm);
			LvlBuilderMetada.log(pm);
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
		for (int y = 0; y < lvlBuilder.getMainPaneY(); y++) {
			for (int x = 0; x < lvlBuilder.getMainPaneX(); x++) {
				Pair<Integer, Integer> coords = new Pair<>(x, y);
				int redValue = getRGBValue(coords, lvlBuilder.getRedMainPane());
				int greenValue = getRGBValue(coords, lvlBuilder.getGreenMainPane());
				int blueValue = getRGBValue(coords, lvlBuilder.getBlueMainPane());
				Color color = Color.rgb(redValue, greenValue, blueValue);
				gc.setFill(color);
				gc.fillRect(x, y, 1, 1);
			}
		}
		return canvas;
	}

	private int getRGBValue(Pair<Integer, Integer> coords, VBox mainPane) {
		int rgbValue = GameCts.EMPTY_TILE_VALUE;
		ImageView iv = LvlBuilderUtil.getImageView(coords, mainPane);
		if (iv != null) {
			LvlBuilderImage vi = (LvlBuilderImage) iv.getImage();
			if (vi == null) {
				System.out.println(coords);
			}
			rgbValue = vi.getValue();
		}
		return rgbValue;
	}

	private void showInputDialog(ActionEvent event) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField xField = new TextField("80");
		TextField yField = new TextField("12");

		grid.add(new Label("X:"), 0, 0);
		grid.add(xField, 1, 0);
		grid.add(new Label("Y:"), 0, 1);
		grid.add(yField, 1, 1);

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Enter Values");
		dialog.setHeaderText(null);
		dialog.getDialogPane().setContent(grid);

		ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

		dialog.showAndWait().ifPresent(result -> {
			if (result == okButton) {
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

		VBox redMainPane = lvlBuilder.getRedMainPane();
		resizeMainPane(redMainPane, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(redMainPane, LvlBuilderCts.TILE_WIDTH * x, LvlBuilderCts.TILE_HEIGHT * y);

		VBox greenMainPane = lvlBuilder.getGreenMainPane();
		resizeMainPane(greenMainPane, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(greenMainPane, LvlBuilderCts.TILE_WIDTH * x, LvlBuilderCts.TILE_HEIGHT * y);

		VBox blueMainPane = lvlBuilder.getBlueMainPane();
		resizeMainPane(blueMainPane, currentCanvasX, currentCanvasY, x, y);
		LvlBuilderUtil.setSize(blueMainPane, LvlBuilderCts.TILE_WIDTH * x, LvlBuilderCts.TILE_HEIGHT * y);
	}

	private void resizeMainPane(VBox mainPane, int currentCanvasX, int currentCanvasY, int newCanvasX, int newCanvasY) {
		if (newCanvasY > currentCanvasY) {
			for (int i = currentCanvasY; i < newCanvasY; i++) {
				HBox newRow = new HBox();
				LvlBuilderUtil.setSize(newRow, LvlBuilderCts.TILE_WIDTH, LvlBuilderCts.TILE_HEIGHT);
				for (int j = 0; j < newCanvasX; j++) {
					AnchorPane square = LvlBuilderUtil.newSelectableVBox();
					newRow.getChildren().add(square);
				}
				mainPane.getChildren().add(newRow);
			}
		} else if (newCanvasY < currentCanvasY) {
			mainPane.getChildren().remove(newCanvasY, currentCanvasY);
		}

		for (Node row : mainPane.getChildren()) {
			HBox currentRow = (HBox) row;
			if (newCanvasX > currentCanvasX) {
				for (int i = currentCanvasX; i < newCanvasX; i++) {
					AnchorPane square = LvlBuilderUtil.newSelectableVBox();
					currentRow.getChildren().add(square);
				}
			} else if (newCanvasX < currentCanvasX) {
				currentRow.getChildren().remove(newCanvasX, currentRow.getChildren().size());
			}
		}
	}
}
