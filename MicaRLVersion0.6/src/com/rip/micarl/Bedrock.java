package com.rip.micarl;

public class Bedrock extends Tile
{
	public Bedrock()
	{
		name = "Bedrock";
		id = Definitions.BEDROCK;
		imageId = 269;
		colidable = true;
		strength = 1000;
		shadowed = true;
		drop = Definitions.BEDROCK;
		numDrops = 1;
	}
}