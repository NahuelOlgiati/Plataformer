package com.mandarina.game.main;

import com.mandarina.game.gamestates.Offset;

public interface LayerDrawer {
	public void drawL1(GameDrawer g, Offset offset);

	public void drawL2(GameDrawer g, Offset offset);

	public void drawL3(GameDrawer g, Offset offset);

	public void drawL4(GameDrawer g, Offset offset);
}
