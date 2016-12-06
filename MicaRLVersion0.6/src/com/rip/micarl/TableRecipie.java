package com.rip.micarl;

public class TableRecipie extends Recipie
{
	public TableRecipie()
	{
		ingredient[0].id = Definitions.OAKPLANK;
		ingredient[0].amount = 4;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.TABLE;
		numProduct = 1;
	}
}