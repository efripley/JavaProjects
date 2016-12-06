package com.rip.micarl;

import com.rip.framework.Tilex;
import com.rip.framework.Tilex.TXKey;

public class Chest extends Tile
{
	private Definitions.stcItemSlot[] inventory = new Definitions.stcItemSlot[9];
	private int index;

	public Chest()
	{
		name = "Chest";
		id = Definitions.CHEST;
		imageId = 289;
		colidable = true;
		strength = 7;
		shadowed = true;
		drop = Definitions.CHEST;
		numDrops = 1;
		for(int a = 0; a < 9; a++)
		{
			inventory[a] = new Definitions.stcItemSlot();
			inventory[a].id = Definitions.NONE;
			inventory[a].amount = 0;
		}
		index = 0;
	}
	
	public void incrementIndex()
	{
		index++;
		if(index >= 9)
			index = 0;
	}
	
	public void decrementIndex()
	{
		index--;
		if(index < 0)
			index = 8;
	}
	
	public void interact()
	{
		boolean playerInventory = true;
		boolean pickUp = true;
		Definitions.stcItemSlot chestSlot = new Definitions.stcItemSlot();
		while(true)
		{
			Tilex.clear();
			Player.drawInventory(12, 5);
			draw(32, 5);
			if(playerInventory)
			{
				for(int b = 12; b < 18; b++)
					Tilex.draw(219, b, index * 2 + 6, Main.UI1, Tilex.LIGHT_GRAY, 1);
			}
			else
			{
				for(int b = 32; b < 38; b++)
					Tilex.draw(219, b, index * 2 + 6, Main.UI1, Tilex.LIGHT_GRAY, 1);
			}

			for(int ay = 13; ay < 16; ay++)
				for(int ax = 21; ax < 28; ax++)
					Tilex.draw(219, ax, ay, Main.UI1, Tilex.GRAY, 1);
			if(chestSlot.id != Definitions.NONE)
			{
				Tilex.draw(Definitions.getImage(chestSlot.id), 22, 14, Main.UI2, Tilex.WHITE, 1);
				Tilex.print(chestSlot.amount, 24, 14, Main.UI2, Tilex.WHITE);
			}

			Main.upButton.draw();
			Main.downButton.draw();
			Main.leftButton.draw();
			Main.rightButton.draw();
			Main.goBack.draw();
			Main.oneItem.draw();
			Main.halfItem.draw();
			Main.allItem.draw();
			Tilex.flip();

			TXKey key = Tilex.getInput(0);
		
			if(Main.upButton.clicked(key))
			{
				decrementIndex();
				if(chestSlot.amount > 0)
					pickUp = false;
			}
			else if(Main.downButton.clicked(key))
			{
				incrementIndex();
				if(chestSlot.amount > 0)
					pickUp = false;
			}
			else if(Main.leftButton.clicked(key))
			{
				playerInventory = true;
				if(chestSlot.amount > 0)
					pickUp = false;
			}
			else if(Main.rightButton.clicked(key))
			{
				playerInventory = false;
				if(chestSlot.amount > 0)
					pickUp = false;
			}
			else if(Main.goBack.clicked(key))
			{
				Tilex.clear();
				return;
			}
			else if(pickUp && playerInventory)
			{
				if(Main.oneItem.clicked(key))
				{
					if(chestSlot.amount < 64)
					{
						chestSlot.id = Player.getItem(index);
						chestSlot.amount++;
					}
				}
				else if(Main.halfItem.clicked(key))
				{
					int half = Player.peekItem(index).amount / 2;
					for(int a = 0; a < half; a++)
					{
						if(chestSlot.amount < 64)
						{
							chestSlot.id = Player.getItem(index);
							chestSlot.amount++;
						}
					}
				}
				else if(Main.allItem.clicked(key))
				{
					int all = Player.peekItem(index).amount;
					for(int a = 0; a < all; a++)
					{
						if(chestSlot.amount < 64)
						{
							chestSlot.id = Player.getItem(index);
							chestSlot.amount++;
						}
					}
				}
				if(chestSlot.amount >= 64 || Player.peekItem(index).amount <= 0)
					pickUp = false;
			}
			else if(!pickUp && playerInventory)
			{
				if(Main.oneItem.clicked(key))
				{
					if(Player.peekItem(index).id == chestSlot.id || Player.peekItem(index).id == Definitions.NONE)
					{
						if(chestSlot.amount > 0 && Player.peekItem(index).amount < 64)
						{
							Player.addToInventory(index, chestSlot.id);
							chestSlot.amount--;
							if(chestSlot.amount <= 0)
								chestSlot.id = Definitions.NONE;
						}
					}
				}
				else if(Main.halfItem.clicked(key))
				{
					if(Player.peekItem(index).id == chestSlot.id || Player.peekItem(index).id == Definitions.NONE)
					{
						int half = chestSlot.amount / 2;
						for(int a = 0; a < half; a++)
						{
							if(Player.peekItem(index).amount < 64)
							{
								Player.addToInventory(index, chestSlot.id);
								chestSlot.amount--;
								if(chestSlot.amount <= 0)
									chestSlot.id = Definitions.NONE;
							}
						}
					}
				}
				else if(Main.allItem.clicked(key))
				{
					if(Player.peekItem(index).id == chestSlot.id || Player.peekItem(index).id == Definitions.NONE)
					{
						int all = chestSlot.amount;
						for(int a = 0; a < all; a++)
						{
							if(Player.peekItem(index).amount < 64)
							{
								Player.addToInventory(index, chestSlot.id);
								chestSlot.amount--;
								if(chestSlot.amount <= 0)
									chestSlot.id = Definitions.NONE;
							}
						}
					}
				}
				if(chestSlot.amount <= 0)
					pickUp = true;
			}
			else if(pickUp && !playerInventory)
			{
				if(Main.oneItem.clicked(key))
				{
					if(peekItem().amount < 64)
					{
						chestSlot.id = getItem();
						chestSlot.amount++;
					}
				}
				else if(Main.halfItem.clicked(key))
				{
					int half = peekItem().amount / 2;
					for(int a = 0; a < half; a++)
					{
						if(chestSlot.amount < 64)
						{
							chestSlot.id = getItem();
							chestSlot.amount++;
						}
					}
				}
				else if(Main.allItem.clicked(key))
				{
					int all = peekItem().amount;
					for(int a = 0; a < all; a++)
					{
						if(chestSlot.amount < 64)
						{
							chestSlot.id = getItem();
							chestSlot.amount++;
						}
					}
				}
				if(chestSlot.amount >= 64 || peekItem().amount <= 0)
					pickUp = false;
			}
			else if(!pickUp && !playerInventory)
			{
				if(Main.oneItem.clicked(key))
				{
					if(peekItem().id == chestSlot.id || peekItem().id == Definitions.NONE)
					{
						if(chestSlot.amount > 0 && peekItem().amount < 64)
						{
							addToInventory(chestSlot.id);
							chestSlot.amount--;
							if(chestSlot.amount <= 0)
								chestSlot.id = Definitions.NONE;
						}
					}
				}
				else if(Main.halfItem.clicked(key))
				{
					if(peekItem().id == chestSlot.id || peekItem().id == Definitions.NONE)
					{
						int half = chestSlot.amount / 2;
						for(int a = 0; a < half; a++)
						{
							if(peekItem().amount < 64)
							{
								addToInventory(chestSlot.id);
								chestSlot.amount--;
								if(chestSlot.amount <= 0)
									chestSlot.id = Definitions.NONE;
							}
						}
					}
				}
				else if(Main.allItem.clicked(key))
				{
					if(peekItem().id == chestSlot.id || peekItem().id == Definitions.NONE)
					{
						int all = chestSlot.amount;
						for(int a = 0; a < all; a++)
						{
							if(peekItem().amount < 64)
							{
								addToInventory(chestSlot.id);
								chestSlot.amount--;
								if(chestSlot.amount <= 0)
									chestSlot.id = Definitions.NONE;
							}
						}
					}
				}
				if(chestSlot.amount <= 0)
					pickUp = true;
			}
		}
	}
	
	public void addToInventory(int new_tile)
	{
		boolean found_slot = false;
		if((inventory[index].id == new_tile || inventory[index].id == Definitions.NONE) && inventory[index].amount < 64)
		{
			inventory[index].amount++;
			inventory[index].id = new_tile;
			found_slot = true;
		}
		if(new_tile != Definitions.AIR && new_tile != Definitions.NONE)
		{
			for(int a = 0; a < 9; a++)
			{
				if(inventory[a].id != Definitions.NONE)
				{
					if(new_tile == inventory[a].id && inventory[a].amount < 64 && !found_slot)
					{
						inventory[a].amount++;
						found_slot = true;
					}
				}
			}
			if(!found_slot)
			{
				for(int a = 0; a < 9; a++)
				{
					if(inventory[a].amount == 0 && !found_slot)
					{
						inventory[a].id = new_tile;
						inventory[a].amount++;
						found_slot = true;
					}
				}
			}
		}
	}
	
	public Definitions.stcItemSlot peekItem()
	{
		Definitions.stcItemSlot return_tile = new Definitions.stcItemSlot();
		return_tile.id = Definitions.NONE;
		return_tile.amount = 0;

		if(inventory[index].amount > 0)
		{
			return_tile = inventory[index];
		}
		
		return return_tile;
	}

	public int getItem()
	{
		int return_tile = Definitions.NONE;
		if(inventory[index].amount > 0)
		{
			return_tile = inventory[index].id;
			inventory[index].amount--;
			if(inventory[index].amount <= 0)
				inventory[index].id = Definitions.NONE;
		}
		
		return return_tile;
	}

	public void draw(int x, int y)
	{
		for(int ay = y; ay < y + 19; ay++)
			for(int ax = x; ax < x + 6; ax++)
				Tilex.draw(219, ax, ay, Main.UI1, Tilex.GRAY, 1);
		for(int a = 0; a < 9; a++)
		{
			if(inventory[a].id > Definitions.NONE)
			{
				
				Tilex.draw(Definitions.getImage(inventory[a].id), x + 1, (a * 2) + 6, Main.UI2, Tilex.WHITE, 1);
				Tilex.print(inventory[a].amount, x + 3, (a * 2) + 6, Main.UI2, Tilex.WHITE);
				
			}
			if(inventory[a].id == Definitions.NONE)
			{
				Tilex.print(inventory[a].amount, x + 3, (a * 2) + 6, Main.UI2, Tilex.WHITE);
			}
		}
	}

	public void load()
	{
		for(int a = 0; a < 9; a++)
		{
			inventory[a].id = Tilex.readInt();
			inventory[a].amount = Tilex.readInt();
		}
	}

	public void save()
	{
		for(int a = 0; a < 9; a++)
		{
			Tilex.write(inventory[a].id);
			Tilex.write(" ");
			Tilex.write(inventory[a].amount);
			Tilex.write(" ");
		}
	}
}