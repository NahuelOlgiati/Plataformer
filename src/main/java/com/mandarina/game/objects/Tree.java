package com.mandarina.game.objects;

import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Tree {

	public static Image[][] load() {
		Image[][] treeImgs = new Image[2][4];
		Image treeOneImg = LoadSave.GetAtlas(LoadSave.TREE_ONE);
		for (int i = 0; i < 4; i++)
			treeImgs[0][i] = new WritableImage(treeOneImg.getPixelReader(), i * 39, 0, 39, 92);

		Image treeTwoImg = LoadSave.GetAtlas(LoadSave.TREE_TWO);
		for (int i = 0; i < 4; i++)
			treeImgs[1][i] = new WritableImage(treeTwoImg.getPixelReader(), i * 62, 0, 62, 54);

		return treeImgs;
	}
}
