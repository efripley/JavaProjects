package com.rip.micarl;

public class OakTree extends Tile
{
	public OakTree()
	{
		name = "Oak Tree";
		id = Definitions.OAKTREE;
		imageId = 263;
		colidable = true;
		strength = 9;
		shadowed = false;
		drop = Definitions.OAKLOG;
		numDrops = 1;
	}
};