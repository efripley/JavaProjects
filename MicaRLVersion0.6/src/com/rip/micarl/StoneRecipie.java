package com.rip.micarl;

public class StoneRecipie extends Recipie
{
	public StoneRecipie()
	{
		ingredient[0].id = Definitions.COBBLESTONE;
		ingredient[0].amount = 8;
		ingredient[1].id = Definitions.COAL; 
		ingredient[1].amount = 1;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.STONE;
		numProduct = 8;
	}
}