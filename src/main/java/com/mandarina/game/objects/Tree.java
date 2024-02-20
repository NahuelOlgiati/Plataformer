package com.mandarina.game.objects;

import java.util.Random;

import com.mandarina.game.main.GameCts;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.utilz.LoadSave;

import javafx.scene.image.Image;

public class Tree {

	private int x, y, type, aniIndex, aniTick;

	public Tree(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;

		// Sets the aniIndex to a random value, to get some variations for the trees so
		// they all don't move in synch.
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

	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY, Image[][] treeImgs) {
		int type = getType();
		if (type == 9)
			type = 8;
		int index = type - 7;
		g.drawImage(treeImgs[index][getAniIndex()], getX() - lvlOffsetX + GetTreeOffsetX(getType()),
				getY() - lvlOffsetY + GetTreeOffsetY(getType()), GetTreeWidth(getType()), GetTreeHeight(getType()));
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

	public static int GetTreeOffsetX(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return (GameCts.TILES_SIZE / 2) - (GetTreeWidth(treeType) / 2);
		case ObjectCts.TREE_RIGHT:
			return (int) (GameCts.TILES_SIZE / 2.5f);
		case ObjectCts.TREE_LEFT:
			return (int) (GameCts.TILES_SIZE / 1.65f);
		}

		return 0;
	}

	public static int GetTreeOffsetY(int treeType) {

		switch (treeType) {
		case ObjectCts.TREE_UP:
			return -GetTreeHeight(treeType) + GameCts.TILES_SIZE * 2;
		case ObjectCts.TREE_RIGHT, ObjectCts.TREE_LEFT:
			return -GetTreeHeight(treeType) + (int) (GameCts.TILES_SIZE / 1.25f);
		}
		return 0;

	}

	public static int GetTreeWidth(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return (int) (39 * GameCts.SCALE);
		case ObjectCts.TREE_RIGHT:
			return (int) (62 * GameCts.SCALE);
		case ObjectCts.TREE_LEFT:
			return -(int) (62 * GameCts.SCALE);

		}
		return 0;
	}

	public static int GetTreeHeight(int treeType) {
		switch (treeType) {
		case ObjectCts.TREE_UP:
			return (int) (92 * GameCts.SCALE);
		case ObjectCts.TREE_RIGHT, ObjectCts.TREE_LEFT:
			return (int) (54 * GameCts.SCALE);

		}
		return 0;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public void setAniIndex(int aniIndex) {
		this.aniIndex = aniIndex;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
