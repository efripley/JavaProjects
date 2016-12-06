package com.rip.micarl;

public class GlassRecipie extends Recipie
{
	public GlassRecipie()
	{
		ingredient[0].id = Definitions.SAND;
		ingredient[0].amount = 8;
		ingredient[1].id = Definitions.COAL; 
		ingredient[1].amount = 1;
		ingredient[2].id = Definitions.NONE;
		ingredient[2].amount = 0;
		product = Definitions.GLASS;
		numProduct = 8;
	}
}