package com.rip.micarl;

import com.rip.framework.Tilex;
import com.rip.framework.Tilex.TXKey;

public class Table extends Tile
{
	public Table()
	{
		name = "Table";
		id = Definitions.TABLE;
		imageId = 270;
		colidable = true;
		strength = 15;
		shadowed = true;
		drop = Definitions.NONE;
		numDrops = 0;
	}

	public void interact()
	{
		while(true)
		{
			Tilex.clear();
			Player.drawInventory(9, 5);
			Recipies.draw(29, 5);
			Main.menuUp.draw();
			Main.menuDown.draw();
			Main.goBack.draw();
			Main.accept.draw();
			Tilex.flip();

			TXKey key = Tilex.getInput(0);

			if(Main.menuUp.clicked(key))
			{
				Recipies.decrementIndex();
			}
			else if(Main.menuDown.clicked(key))
			{
				Recipies.incrementIndex();
			}
			else if(Main.accept.clicked(key))
			{
				Tilex.clear();
				if(Player.hasItem(Recipies.getIngredient(0)))
				{
					if(Recipies.getIngredient(1).amount == 0 || Player.hasItem(Recipies.getIngredient(1)))
					{
						if(Recipies.getIngredient(2).amount == 0 || Player.hasItem(Recipies.getIngredient(2)))
						{
							for(int a = 0; a < 3; a++)
								Player.useItem(Recipies.getIngredient(a));
							for(int a = 0; a < Recipies.getNumProduct(); a++)
								Player.addToInventory(-1, Recipies.getProduct());
						}
					}
				}
			}
			else if(Main.goBack.clicked(key))
			{
				Tilex.clear();
				return;
			}
		}
	}
}