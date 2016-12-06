package com.rip.micarl;

import com.rip.framework.Tilex;

public class Recipies
{
	public int NUMRECIPIES = 9;

	private static Recipies recipies = new Recipies();
 
	private Recipie[] recipie = new Recipie[NUMRECIPIES];
	private int index;

	private Recipies()
	{
		recipie[0] = new OakPlankRecipie();
		recipie[1] = new WoodDoorRecipie();
		recipie[2] = new ChestRecipie();
		recipie[3] = new TableRecipie();
		recipie[4] = new StickRecipie();
		recipie[5] = new WoodFenceRecipie();
		recipie[6] = new StoneRecipie();
		recipie[7] = new GlassRecipie();
		recipie[8] = new RedRockRecipie();

		index = 0;
	}

	private void recipiesIncrementIndex()
	{
		index++;
		if(index >= NUMRECIPIES)
			index = 0;
	}

	public static void incrementIndex()
	{
		recipies.recipiesIncrementIndex();
	}

	private void recipiesDecrementIndex()
	{
		index--;
		if(index < 0)
			index = NUMRECIPIES - 1;
	}

	public static void decrementIndex()
	{
		recipies.recipiesDecrementIndex();
	}

	private Definitions.stcItemSlot recipiesGetIngredient(int slot)
	{
		return recipie[index].getIngredient(slot);
	}

	public static Definitions.stcItemSlot getIngredient(int slot)
	{
		return recipies.recipiesGetIngredient(slot);
	}

	private int recipiesGetNumProduct()
	{
		return recipie[index].getNumProduct();
	}

	public static int getNumProduct()
	{
		return recipies.recipiesGetNumProduct();
	}

	private int recipiesGetProduct()
	{
		return recipie[index].getProduct();
	}

	public static int getProduct()
	{
		return recipies.recipiesGetProduct();
	}

	private void recipiesDraw(int x, int y)
	{
		for(int ay = y; ay < y + 19; ay++)
			for(int ax = x; ax < x + 6; ax++)
				Tilex.draw(219, ax, ay, Main.UI1, Tilex.GRAY, 1);
		for(int a = index - 2; a <= index + 3; a++)
		{
			if(a >= 0 && a < NUMRECIPIES)
			{
				Tilex.draw(Definitions.getImage(recipie[a].getProduct()), x + 1, y + 1 + ((a - (index - 2)) * 3), Main.UI2, Tilex.WHITE, 1);
				Tilex.print(recipie[a].getNumProduct(), x + 3, y + 1 + ((a - (index - 2)) * 3), Main.UI2, Tilex.WHITE);
				if(index == a)
					for(int b = x; b < x + 6; b++)
					Tilex.draw(219, b, y + 1 + ((a - (index - 2)) * 3), Main.UI1, Tilex.LIGHT_GRAY, 1);
			}
		}
	}

	public static void draw(int x, int y)
	{
		recipies.recipiesDraw(x, y);
	}
}