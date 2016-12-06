package com.rip.micarl;

public class RedRockRecipie extends Recipie
{
	public RedRockRecipie()
	{
		ingredient[0].id = Definitions.FLAGSTONE;
		ingredient[0].amount = 8;
		ingredient[1].id = Definitions.COAL; 
		ingredient[1].amount = 1;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.REDROCK;
		numProduct = 8;
	}
};