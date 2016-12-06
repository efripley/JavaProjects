package com.rip.micarl;

import com.rip.framework.Tilex;

public class Tile
{
	protected String name;
	protected boolean colidable;
	protected int id;
	protected int imageId;
	protected int strength;
	protected boolean shadowed;
	protected int drop;
	protected int numDrops;

	public void interact(){}

	public String getName()
	{
		return name;
	}

	public boolean isColidable()
	{
		return colidable;
	}

	public int getId()
	{
		return id;
	}

	public int getImageId()
	{
		return imageId;
	}
	
	public int getStrength()
	{
		return strength;
	}

	public boolean isShadowed()
	{
		return shadowed;
	}

	public int getDrop()
	{
		return drop;
	}

	public int getNumDrops()
	{
		return numDrops;
	}

	public void draw(int x, int y, int z, int north, int south, int west, int east)
	{
		Tilex.draw(imageId, x, y, z, Tilex.WHITE, 1);
	}

	public void load(){}

	public void save(){}
}