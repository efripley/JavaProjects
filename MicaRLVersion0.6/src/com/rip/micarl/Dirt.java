package com.rip.micarl;

public class Dirt extends Tile
{
	public Dirt()
	{
		name = "Dirt";
		id = Definitions.DIRT;
		imageId = 258;
		colidable = true;
		strength = 5;
		shadowed = true;
		drop = Definitions.DIRT;
		numDrops = 1;
	}
}