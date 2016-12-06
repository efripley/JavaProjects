package com.rip.micarl;

public class ChestRecipie extends Recipie
{
	public ChestRecipie()
	{
		ingredient[0].id = Definitions.OAKPLANK;
		ingredient[0].amount = 8;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.CHEST;
		numProduct = 1;
	}
}