package com.rip.micarl;

public class Grass extends Tile
{
	public Grass()
	{
		name = "Grass";
		id = Definitions.GRASS;
		imageId = 257;
		colidable = true;
		strength = 5;
		shadowed = true;
		drop = Definitions.DIRT;
		numDrops = 1;
	}
};