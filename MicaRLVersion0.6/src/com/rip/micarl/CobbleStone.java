package com.rip.micarl;

public class CobbleStone extends Tile
{
	public CobbleStone()
	{
		name = "Cobble Stone";
		id = Definitions.COBBLESTONE;
		imageId = 262;
		colidable = true;
		strength = 15;
		shadowed = true;
		drop = Definitions.COBBLESTONE;
		numDrops = 1;
	}
}