package com.rip.micarl;

import com.rip.framework.Tilex;

public class WoodFence extends Tile
{
	public WoodFence()
	{
		name = "Wood Fence";
		id = Definitions.WOODFENCE;
		imageId = 296;
		colidable = true;
		strength = 0;
		shadowed = false;
		drop = Definitions.WOODFENCE;
		numDrops = 1;
	}
	public void draw(int x, int y, int z, int north, int south, int east, int west)
	{
		if(north == Definitions.WOODFENCE && south == Definitions.WOODFENCE)
			imageId = 298;
		else if(west == Definitions.WOODFENCE && east == Definitions.WOODFENCE)
			imageId = 297;
		else
			imageId = 296;
		Tilex.draw(imageId, x, y, z, Tilex.WHITE, 1);
	}
}