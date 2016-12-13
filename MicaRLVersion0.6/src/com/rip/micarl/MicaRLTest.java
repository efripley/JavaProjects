package com.rip.micarl;

public class MicaRLTest{
	void recipies(){
		assert Recipies.getProduct() == Definitions.OAKPLANK;
		Recipies.incrementIndex();
		assert Recipies.getProduct() == Definitions.WOODDOOR;
		Recipies.incrementIndex();
		Recipies.incrementIndex();
		Recipies.incrementIndex();
		assert Recipies.getProduct() == Definitions.STICK;
	}

	void tiles(){
		assert Tiles.getTile(0).name.equals("Air");
		assert Tiles.getTile(5).name.equals("Stone");
		assert Tiles.getTile(19).name.equals("Glass");
	}
}