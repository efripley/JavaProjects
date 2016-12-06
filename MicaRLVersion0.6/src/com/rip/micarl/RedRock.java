package com.rip.micarl;

public class RedRock extends Tile
{
	public RedRock()
	{
		name = "Red Rock";
		id = Definitions.REDROCK;
		imageId = 294;
		colidable = true;
		strength = 15;
		shadowed = true;
		drop = Definitions.FLAGSTONE;
		numDrops = 1;
	}
}