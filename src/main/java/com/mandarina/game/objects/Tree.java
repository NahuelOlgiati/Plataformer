package com.mandarina.game.objects;

import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Tree {

	public static Image[][] load() {
		Image[][] treeImgs = new Image[2][4];
		Image treeOneImg = LoadSave.GetAtlas(LoadSave.TREE_ONE);
		for (int i = 0; i < 4; i++)
			treeImgs[0][i] = LoadSave.GetSubimage(treeOneImg, i, 0, 39, 92);

		Image treeTwoImg = LoadSave.GetAtlas(LoadSave.TREE_TWO);
		for (int i = 0; i < 4; i++)
			treeImgs[1][i] = LoadSave.GetSubimage(treeTwoImg, i, 0, 62, 54);

		return treeImgs;
	}
}
