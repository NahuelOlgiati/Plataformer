package com.mandarina.objects;

import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Grass {

	private int x, y, type;

	public Grass(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;

	}

	public int getType() {
		return type;
	}
	
	public static Image[] load() {
		Image grassTemp = LoadSave.GetAtlas(LoadSave.GRASS);
		Image[] grassImgs = new Image[2];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = new WritableImage(grassTemp.getPixelReader(), 32 * i, 0, 32, 32);
		
		return grassImgs;
	}
}
