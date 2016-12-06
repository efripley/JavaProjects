package com.rip.micarl;

public class Recipie
{
	protected Definitions.stcItemSlot[] ingredient = new Definitions.stcItemSlot[3];
	protected int product;
	protected int numProduct;
	
	public Recipie()
	{
		for(int a = 0; a < 3; a++)
			ingredient[a] = new Definitions.stcItemSlot();
	}

	public Definitions.stcItemSlot getIngredient(int index)
	{
		return ingredient[index];
	}

	public int getProduct()
	{
		return product;
	}

	public int getNumProduct()
	{
		return numProduct;
	}
}