package com.rip.micarl;

public class OakLog extends Tile
{
	public OakLog()
	{
		name = "Oak Log";
		id = Definitions.OAKLOG;
		imageId = 264;
		colidable = true;
		strength = 10;
		shadowed = true;
		drop = Definitions.OAKLOG;
		numDrops = 1;
	}
}