package com.mandarina.game.objects;

import java.util.Random;

import com.mandarina.game.gamestates.Offset;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.main.AppStage;
import com.mandarina.utilz.LoadSave;

import com.mandarina.utilz.Point;
import javafx.scene.image.Image;

public class Tree extends GameObject {

	private int aniIndex, aniTick;

	public Tree(Point spawn, int type) {
		super(spawn, type);
		Random r = new Random();
		aniIndex = r.nextInt(4);
	}

	public void update() {
		aniTick++;
		if (aniTick >= 35) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public void draw(GameDrawer g, Offset offset, Image[][] treeImgs) {
		int type = getObjType();
		if (type == 9)
			type = 8;
		int index = type - 7;
		g.drawImage(treeImgs[index][getAniIndex()], x - offset.getX() + GetTreeOffsetX(getObjType()),
				y - offset.getY() + GetTreeOffsetY(getObjType()), GetTreeWidth(getObjType()),
				GetTreeHeight(getObjType()));
	}

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

	public static float GetTreeOffsetX(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return (AppStage.GetTileSize() / 2) - (GetTreeWidth(treeType) / 2);
		case ObjectCts.TREE_RIGHT:
			return (int) (AppStage.GetTileSize() / 2.5);
		case ObjectCts.TREE_LEFT:
			return (int) (AppStage.GetTileSize() / 1.65);
		}

		return 0;
	}

	public static float GetTreeOffsetY(int treeType) {

		switch (treeType) {
		case ObjectCts.TREE_UP:
			return -GetTreeHeight(treeType) + AppStage.GetTileSize() * 2;
		case ObjectCts.TREE_RIGHT, ObjectCts.TREE_LEFT:
			return -GetTreeHeight(treeType) + (int) (AppStage.GetTileSize() / 1.25);
		}
		return 0;

	}

	public static int GetTreeWidth(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return AppStage.Scale(39);
		case ObjectCts.TREE_RIGHT:
			return AppStage.Scale(62);
		case ObjectCts.TREE_LEFT:
			return AppStage.Scale(-62);

		}
		return 0;
	}

	public static int GetTreeHeight(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return AppStage.Scale(92);
		case ObjectCts.TREE_RIGHT, ObjectCts.TREE_LEFT:
			return AppStage.Scale(54);

		}
		return 0;
	}

	@Override
	public int getAniIndex() {
		return aniIndex;
	}

	public void setAniIndex(int aniIndex) {
		this.aniIndex = aniIndex;
	}

}
