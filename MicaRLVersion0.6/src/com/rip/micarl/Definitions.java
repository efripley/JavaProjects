package com.rip.micarl;

import java.util.Random;

public class Definitions
{
	public static final int NONE = 0;
	public static final int AIR = 1;
	public static final int GRASS = 2;
	public static final int DIRT = 3;
	public static final int SAND = 4;
	public static final int WATER = 5;
	public static final int STONE = 6;
	public static final int COBBLESTONE = 7;
	public static final int OAKTREE = 8;
	public static final int OAKLOG = 9;
	public static final int BEDROCK = 10;
	public static final int TABLE = 11;
	public static final int OAKPLANK = 12;
	public static final int WOODDOOR = 13;
	public static final int CHEST = 14;
	public static final int REDROCK = 15;
	public static final int FLAGSTONE = 16;
	public static final int WOODFENCE = 17;
	public static final int COALORE = 18;
	public static final int GLASS = 19;
	public static final int LASTTILE = 20;

	public static final int NUMTILES = LASTTILE - AIR;

	public static final int STICK = 1001;
	public static final int COAL = 1002;
	public static final int LASTINGREDIENT = 1003;

	public static final int NUMINGREDIENTS = LASTINGREDIENT - STICK;

	public static Random rand = new Random();

	public static class stcItemSlot
	{
		public int id;
		public int amount;
	}

	private String[] tileName = new String[NUMTILES];
	private String[] ingredientName = new String[NUMINGREDIENTS];
	private int[] tileImage = new int[NUMTILES];
	private int[] ingredientImage = new int[NUMINGREDIENTS];

	private static Definitions defs = new Definitions();

	private Definitions()
	{
		for(int a = 0; a < NUMTILES; a++)
		{
			tileName[a] = "";
			tileImage[a] = 0;
		}
		for(int a = 0; a < NUMINGREDIENTS; a++)
		{
			ingredientName[a] = "";
			ingredientImage[a] = 0;
		}
	}

	private void defsLoadItem(int item, String name, int imageId)
	{
		if(item > NONE && item < LASTTILE)
		{
			tileName[item - AIR] = name;
			tileImage[item - AIR] = imageId;
		}
		else
		{
			ingredientName[item - STICK] = name;
			ingredientImage[item - STICK] = imageId;
		}
	}

	public static void loadItem(int item, String name, int imageId)
	{
		defs.defsLoadItem(item, name, imageId);
	}

	private String defsGetName(int item)
	{
		if(item > NONE && item < LASTTILE)
			return tileName[item - AIR];
		else
			return ingredientName[item - STICK];
	}

	public static String getName(int item)
	{
		return defs.defsGetName(item);
	}

	private int defsGetImage(int item)
	{
		if(item > NONE && item < LASTTILE)
			return tileImage[item - AIR];
		else
			return ingredientImage[item - STICK];
	}

	public static int getImage(int item)
	{
		return defs.defsGetImage(item);
	}
}