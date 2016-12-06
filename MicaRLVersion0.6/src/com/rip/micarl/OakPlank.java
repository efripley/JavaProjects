package com.rip.micarl;

public class OakPlank extends Tile
{
	public OakPlank()
	{
		name = "Oak Plank";
		id = Definitions.OAKPLANK;
		imageId = 271;
		colidable = true;
		strength = 7;
		shadowed = true;
		drop = Definitions.OAKPLANK;
		numDrops = 1;
	}	
}