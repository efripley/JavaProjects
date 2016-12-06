package com.rip.micarl;

import com.rip.framework.Tilex;

public class CoalOre extends Tile
{
	public CoalOre()
	{
		name = "CoalOre";
		id = Definitions.COALORE;
		imageId = 299;
		colidable = true;
		strength = 21;
		shadowed = true;
		drop = Definitions.COAL;
		numDrops = 1;
	}

	@Override
	public void draw(int x, int y, int z, int north, int south, int west, int east)
	{
		if(north == Definitions.AIR || south == Definitions.AIR || west == Definitions.AIR || east == Definitions.AIR || z == 0)
			imageId = 299;
		else
			imageId = 261;
		Tilex.draw(imageId, x, y, z, Tilex.WHITE, 1);
	}
};