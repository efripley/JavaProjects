package com.rip.micarl;

public class Sand extends Tile
{
	public Sand()
	{
		name = "Sand";
		id = Definitions.SAND;
		imageId = 259;
		colidable = true;
		strength = 4;
		shadowed = true;
		drop = Definitions.SAND;
		numDrops = 1;
	}
}