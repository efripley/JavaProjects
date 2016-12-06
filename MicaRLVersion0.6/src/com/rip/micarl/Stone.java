package com.rip.micarl;

public class Stone extends Tile
{
	public Stone()
	{
		name = "Stone";
		id = Definitions.STONE;
		imageId = 261;
		colidable = true;
		strength = 15;
		shadowed = true;
		drop = Definitions.COBBLESTONE;
		numDrops = 1;
	}
}