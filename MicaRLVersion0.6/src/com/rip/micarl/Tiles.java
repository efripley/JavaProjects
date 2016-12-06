package com.rip.micarl;

public class Tiles
{
	private Tile[] tile = new Tile[Definitions.LASTTILE];

	private static Tiles tiles = new Tiles();

	private Tiles()
	{
		tile[Definitions.AIR] = new Air();
		tile[Definitions.GRASS] = new Grass();
		tile[Definitions.DIRT] = new Dirt();
		tile[Definitions.SAND] = new Sand();
		tile[Definitions.WATER] = new Water();
		tile[Definitions.STONE] = new Stone();
		tile[Definitions.COBBLESTONE] = new CobbleStone();
		tile[Definitions.OAKTREE] = new OakTree();
		tile[Definitions.OAKLOG] = new OakLog();
		tile[Definitions.BEDROCK] = new Bedrock();
		tile[Definitions.TABLE] = new Table();
		tile[Definitions.OAKPLANK] = new OakPlank();
		tile[Definitions.WOODDOOR] = new WoodDoor();
		tile[Definitions.CHEST] = new Chest();
		tile[Definitions.REDROCK] = new RedRock();
		tile[Definitions.FLAGSTONE] = new FlagStone();
		tile[Definitions.WOODFENCE] = new WoodFence();
		tile[Definitions.COALORE] = new CoalOre();
		tile[Definitions.GLASS] = new Glass();
	}

	private Tile tilesGetTile(int myTile)
	{
		return tile[myTile];
	}

	public static Tile getTile(int myTile)
	{
		return tiles.tilesGetTile(myTile);
	}
}