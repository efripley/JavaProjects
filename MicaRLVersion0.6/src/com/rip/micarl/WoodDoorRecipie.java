package com.rip.micarl;

public class WoodDoorRecipie extends Recipie
{
	public WoodDoorRecipie()
	{
		ingredient[0].id = Definitions.OAKPLANK;
		ingredient[0].amount = 6;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.WOODDOOR;
		numProduct = 1;
	}
}