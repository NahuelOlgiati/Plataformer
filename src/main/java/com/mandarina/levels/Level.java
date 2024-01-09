package com.mandarina.levels;

import java.util.ArrayList;

import com.mandarina.constants.EntityCts;
import com.mandarina.constants.GameCts;
import com.mandarina.constants.ObjectCts;
import com.mandarina.entities.crabby.Crabby;
import com.mandarina.entities.pinkstar.Pinkstar;
import com.mandarina.entities.shark.Shark;
import com.mandarina.entities.titan.Titan;
import com.mandarina.objects.BackgroundTree;
import com.mandarina.objects.Cannon;
import com.mandarina.objects.GameContainer;
import com.mandarina.objects.Grass;
import com.mandarina.objects.Potion;
import com.mandarina.objects.Spike;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Level {

	private Image img;
	private int[][] lvlData;

	private ArrayList<Crabby> crabs = new ArrayList<>();
	private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
	private ArrayList<Shark> sharks = new ArrayList<>();
	private ArrayList<Titan> titans = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<BackgroundTree> trees = new ArrayList<>();
	private ArrayList<Grass> grass = new ArrayList<>();

	private int lvlTilesWide;
	private int maxTilesOffsetX;
	private int maxLvlOffsetX;
	
	private int lvlTilesHeight;
	private int maxTilesOffsetY;
	private int maxLvlOffsetY;
	
	private Point2D playerSpawn;

	public Level(Image img) {
		this.img = img;
		lvlData = new int[(int) img.getHeight()][(int) img.getWidth()];
		loadLevel();
		calcLvlOffsets();
	}

	private void loadLevel() {

		// Looping through the image colors just once. Instead of one per
		// object/enemy/etc..
		// Removed many methods in HelpMethods class.

		PixelReader pixelReader = img.getPixelReader();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = pixelReader.getColor(x, y);
				int red = (int) (c.getRed() * 255);
				int green = (int) (c.getGreen() * 255);
				int blue = (int) (c.getBlue() * 255);

				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
		}
	}

	private void loadLevelData(int redValue, int x, int y) {
		if (redValue >= 50)
			lvlData[y][x] = 0;
		else
			lvlData[y][x] = redValue;
		switch (redValue) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> grass.add(new Grass((int) (x * GameCts.TILES_SIZE),
				(int) (y * GameCts.TILES_SIZE) - GameCts.TILES_SIZE, getRndGrassType(x)));
		}
	}

	private int getRndGrassType(int xPos) {
		return xPos % 2;
	}

	private void loadEntities(int greenValue, int x, int y) {
		switch (greenValue) {
		case EntityCts.CRABBY -> crabs.add(new Crabby(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE));
		case EntityCts.PINKSTAR -> pinkstars.add(new Pinkstar(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE));
		case EntityCts.SHARK -> sharks.add(new Shark(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE));
		case EntityCts.TITAN -> titans.add(new Titan(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE));
		case EntityCts.PLAYER -> playerSpawn = new Point2D(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE);
		}
	}

	private void loadObjects(int blueValue, int x, int y) {
		switch (blueValue) {
		case ObjectCts.RED_POTION, ObjectCts.BLUE_POTION ->
			potions.add(new Potion(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blueValue));
		case ObjectCts.BOX, ObjectCts.BARREL ->
			containers.add(new GameContainer(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blueValue));
		case ObjectCts.SPIKE -> spikes.add(new Spike(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, ObjectCts.SPIKE));
		case ObjectCts.CANNON_LEFT, ObjectCts.CANNON_RIGHT ->
			cannons.add(new Cannon(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blueValue));
		case ObjectCts.TREE_ONE, ObjectCts.TREE_TWO, ObjectCts.TREE_THREE ->
			trees.add(new BackgroundTree(x * GameCts.TILES_SIZE, y * GameCts.TILES_SIZE, blueValue));
		}
	}

	private void calcLvlOffsets() {
		lvlTilesWide = (int) img.getWidth();
		maxTilesOffsetX = lvlTilesWide - GameCts.TILES_IN_WIDTH;
		maxLvlOffsetX = GameCts.TILES_SIZE * maxTilesOffsetX;
		
		lvlTilesHeight = (int) img.getHeight();
		maxTilesOffsetY = lvlTilesHeight - GameCts.TILES_IN_HEIGHT;
		maxLvlOffsetY = GameCts.TILES_SIZE * maxTilesOffsetY;
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffsetX() {
		return maxLvlOffsetX;
	}
	
	public int getLvlOffsetY() {
		return maxLvlOffsetY;
	}

	public Point2D getPlayerSpawn() {
		return playerSpawn;
	}

	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	public ArrayList<Shark> getSharks() {
		return sharks;
	}

	public ArrayList<Titan> getTitans() {
		return titans;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Cannon> getCannons() {
		return cannons;
	}

	public ArrayList<Pinkstar> getPinkstars() {
		return pinkstars;
	}

	public ArrayList<BackgroundTree> getTrees() {
		return trees;
	}

	public ArrayList<Grass> getGrass() {
		return grass;
	}

}
