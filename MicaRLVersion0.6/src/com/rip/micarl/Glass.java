package com.rip.micarl;

public class Glass extends Tile
{
	public Glass()
	{
		name = "Glass";
		id = Definitions.GLASS;
		imageId = 300;
		colidable = true;
		strength = 5;
		shadowed = true;
		drop = Definitions.NONE;
		numDrops = 0;
	}
}