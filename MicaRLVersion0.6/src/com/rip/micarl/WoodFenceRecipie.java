package com.rip.micarl;

public class WoodFenceRecipie extends Recipie
{
	public WoodFenceRecipie()
	{
		ingredient[0].id = Definitions.STICK;
		ingredient[0].amount = 6;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.WOODFENCE;
		numProduct = 2;
	}
}