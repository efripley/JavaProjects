package com.rip.micarl;

public class Water extends Tile
{
	public Water()
	{
		name = "Water";
		id = Definitions.WATER;
		imageId = 260;
		colidable = false;
		strength = 0;
		shadowed = false;
		drop = Definitions.NONE;
		numDrops = 0;
	}
}