package com.rip.micarl;

public class OakPlankRecipie extends Recipie
{
	public OakPlankRecipie()
	{
		ingredient[0].id = Definitions.OAKLOG;
		ingredient[0].amount = 1;
		ingredient[1].id = Definitions.NONE; 
		ingredient[1].amount = 0;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.OAKPLANK;
		numProduct = 4;
	}
}