package com.rip.micarl;

public class FlagStone extends Tile
{
	public FlagStone()
	{
		name = "Flag Stone";
		id = Definitions.FLAGSTONE;
		imageId = 295;
		colidable = true;
		strength = 15;
		shadowed = true;
		drop = Definitions.FLAGSTONE;
		numDrops = 1;
	}
}