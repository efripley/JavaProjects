package com.rip.micarl;

public class Ingredients
{
	private Ingredient[] ingredient = new Ingredient[Definitions.NUMINGREDIENTS];
	
	public Ingredients()
	{
		ingredient[0] = new Stick();
		ingredient[1] = new Coal();
	}

	public String getName(int item)
	{
		return ingredient[item - Definitions.STICK].getName();
	}

	public int getImageId(int item)
	{
		return ingredient[item - Definitions.STICK].getImageId();
	}
}