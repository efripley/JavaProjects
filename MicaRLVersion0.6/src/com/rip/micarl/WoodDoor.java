package com.rip.micarl;

import com.rip.framework.Tilex;

public class WoodDoor extends Tile
{
	public WoodDoor()
	{
		name = "Wood Door";
		id = Definitions.WOODDOOR;
		imageId = 287;
		colidable = true;
		strength = 7;
		shadowed = true;
		drop = Definitions.WOODDOOR;
		numDrops = 1;
	}

	public void interact()
	{
		colidable = !colidable;
		shadowed = !shadowed;
	}

	public void draw(int x, int y, int z, int north, int south, int west, int east)
	{
		if(colidable == false)
			imageId = 287;
		else
			imageId = 288;
		
		Tilex.draw(imageId, x, y, z, Tilex.WHITE, 1);
	}
}