package com.rip.micarl;

public class StickRecipie extends Recipie
{
	public StickRecipie()
	{
		ingredient[0].id = Definitions.OAKPLANK;
		ingredient[0].amount = 2;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.STICK;
		numProduct = 4;
	}
}