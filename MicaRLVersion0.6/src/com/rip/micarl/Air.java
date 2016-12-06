package com.rip.micarl;

public class Air extends Tile
{
	public Air()
	{
		name = "Air";
		id = Definitions.AIR;
		imageId = 256;
		colidable = false;
		strength = 0;
		shadowed = false;
		drop = Definitions.NONE;
		numDrops = 0;
	}
}