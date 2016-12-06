package com.rip.micarl;

import android.util.Log;

import com.rip.framework.Tilex;
import com.rip.framework.Tilex.TXKey;

public class Player
{
	private static Player player = new Player();

	public static int[][] path = new int[17][17];

	private float x, y;
	private float speed;
	private int dx, dy;
	private float bounds;
	private float timer;
	private int health;
	private int attack;
	private boolean beingAttacked;
	private Definitions.stcItemSlot[] inventory = new Definitions.stcItemSlot[9];
	private int inventoryIndex;

	private Player()
	{
		x = 0;
		y = 0;
		speed = 4.0f;
		dx = 0;
		dy = -1;
		bounds = .5f;
		timer = 0;
		health = 0;
		attack = 0;
		beingAttacked = false;
		for(int a = 0; a < 9; a++)
		{
			inventory[a] = new Definitions.stcItemSlot();
			inventory[a].id = Definitions.NONE;
			inventory[a].amount = 0;
		}
		inventoryIndex = 0;
	}

	private void playerGenerate()
	{
		boolean spawned = false;
		while(!spawned)
		{
			x = Definitions.rand.nextInt(300);
			y = Definitions.rand.nextInt(300);
			if(!Main.gameMap.isColidable((int)x, (int)y, 1))
				spawned = true;
		}
		dx = 0;
		dy = -1;
		health = 10;
		attack = 3;
		beingAttacked = false;
		for(int a = 0; a < 9; a++)
		{
			inventory[a] = new Definitions.stcItemSlot();
			inventory[a].id = Definitions.NONE;
			inventory[a].amount = 0;
		}
		inventoryIndex = 0;
	}

	public static void generate()
	{
		player.playerGenerate();
	}

	private void playerFaceUp()
	{
		dx = 0;
		dy = -1;
		speed = 4;
	}

	public static void faceUp()
	{
		player.playerFaceUp();
	}

	private void playerFaceDown()
	{
		dx = 0;
		dy = 1;
		speed = 4;
	}

	public static void faceDown()
	{
		player.playerFaceDown();
	}

	private void playerFaceUpLeft()
	{
		dx = -1;
		dy = -1;
		speed = 4 * .7f;
	}

	public static void faceUpLeft()
	{
		player.playerFaceUpLeft();
	}

	private void playerFaceUpRight()
	{
		dx = 1;
		dy = -1;
		speed = 4 * .7f;
	}

	public static void faceUpRight()
	{
		player.playerFaceUpRight();
	}

	private void playerFaceLeft()
	{
		dx = -1;
		dy = 0;
		speed = 4;
	}

	public static void faceLeft()
	{
		player.playerFaceLeft();
	}

	private void playerFaceRight()
	{
		dx = 1;
		dy = 0;
		speed = 4;
	}

	public static void faceRight()
	{
		player.playerFaceRight();
	}

	private void playerFaceDownLeft()
	{
		dx = -1;
		dy = 1;
		speed = 4 * .7f;
	}

	public static void faceDownLeft()
	{
		player.playerFaceDownLeft();
	}

	private void playerFaceDownRight()
	{
		dx = 1;
		dy = 1;
		speed = 4 * .7f;
	}

	public static void faceDownRight()
	{
		player.playerFaceDownRight();
	}

	private void playerMove(float deltaTime)
	{
		x += dx * speed * deltaTime;
		y += dy * speed * deltaTime;
		if(Main.gameMap.isColidable((int)x, (int)y, 1) || Main.gameMap.isBedrock((int)x, (int)y, 0))
		{
			x -= dx * speed * deltaTime;
			y -= dy * speed * deltaTime;

			x += dx * speed * deltaTime;
			if(Main.gameMap.isColidable((int)x, (int)y, 1) || Main.gameMap.isBedrock((int)x, (int)y, 0))
				x -= dx * speed * deltaTime;
			
			y += dy * speed * deltaTime;
			if(Main.gameMap.isColidable((int)x, (int)y, 1) || Main.gameMap.isBedrock((int)x, (int)y, 0))
				y -= dy * speed * deltaTime;
		}
	}

	public static void move(float deltaTime)
	{
		player.playerMove(deltaTime);
	}

	private void playerUpdate(float deltaTime)
	{

		for(int ay = 0; ay < 17; ay++)
			for(int ax = 0; ax < 17; ax++)
				path[ax][ay] = -1;
		findPath((int)player.x, (int)player.y, 8, 8, 0);
		timer -= deltaTime;
		if(timer <= 0)
		{
			health++;
			if(health >= 10)
				health = 10;
			timer = 5.0f;
		}
	}

	public static void update(float deltaTime)
	{
		player.playerUpdate(deltaTime);
	}

	public static void findPath(int mx, int my, int px, int py, int distance)
	{
		if(Main.gameMap.isColidable(mx, my, 1))
			distance = 128;
		if(distance > 128)
			distance = 128;
		path[px][py] = distance;
		if(px - 1 >= 0 && (path[px - 1][py] == -1 || distance + 1 < path[px - 1][py]))
			findPath(mx - 1, my, px - 1, py, distance + 1);
		if(px + 1 < 17 && (path[px + 1][py] == -1 || distance + 1 < path[px + 1][py]))
			findPath(mx + 1, my, px + 1, py, distance + 1);
		if(py - 1 >= 0 && (path[px][py - 1] == -1 || distance + 1 < path[px][py - 1]))
			findPath(mx, my - 1, px, py - 1, distance + 1);
		if(py + 1 < 17 && (path[px][py + 1] == -1 || distance + 1 < path[px][py + 1]))
			findPath(mx, my + 1, px, py + 1, distance + 1);
	}

	private void playerAddToInventory(int index, int newTile)
	{
		if(index == -1)
			index = inventoryIndex;
		boolean foundSlot = false;
		if((newTile == inventory[index].id || inventory[index].id == Definitions.NONE) && inventory[index].amount < 64 && index != inventoryIndex)
		{
			inventory[index].id = newTile;
			inventory[index].amount++;
			foundSlot = true;
		}
		else if(newTile != Definitions.AIR && newTile != Definitions.NONE)
		{
			for(int a = 0; a < 9; a++)
			{
				if(inventory[a].id != Definitions.NONE)
				{
					if(newTile == inventory[a].id && inventory[a].amount < 64 && !foundSlot)
					{
						inventory[a].amount++;
						foundSlot = true;
					}
				}
			}
			if(!foundSlot)
			{
				for(int a = 0; a < 9; a++)
				{
					if(inventory[a].amount == 0 && !foundSlot)
					{
						inventory[a].id = newTile;
						inventory[a].amount++;
						foundSlot = true;
					}
				}
			}
		}
	}

	public static void addToInventory(int index, int newTile)
	{
		player.playerAddToInventory(index, newTile);
	}
	
	private void playerBrowseInventory()
	{
		while(true)
		{
			Tilex.clear();
			playerDrawInventory(19, 5);
			for(int b = 19; b < 25; b++)
				Tilex.draw(219, b, (inventoryIndex * 2) + 6, Main.UI1, Tilex.LIGHT_GRAY, 1);
			Main.menuUp.draw();
			Main.menuDown.draw();
			Main.exit.draw();
			Main.accept.draw();
			Tilex.flip();

			TXKey key = Tilex.getInput(0);
		
			if(Main.menuUp.clicked(key))
			{
				decrementIndex();
			}
			else if(Main.menuDown.clicked(key))
			{
				incrementIndex();
			}
			else if(Main.accept.clicked(key))
			{
				Tilex.clear();
				return;
			}
			else if(Main.exit.clicked(key))
			{
				getItem(-1);
			}
		}
	}

	public static void browseInventory()
	{
		player.playerBrowseInventory();
	}

	private void playerIncrementIndex()
	{
		inventoryIndex++;
		if(inventoryIndex >= 9)
			inventoryIndex = 0;
	}

	public static void incrementIndex()
	{
		player.playerIncrementIndex();
	}

	private void playerDecrementIndex()
	{
		inventoryIndex--;
		if(inventoryIndex < 0)
			inventoryIndex = 8;
	}

	public static void decrementIndex()
	{
		player.playerDecrementIndex();
	}

	private void playerLoad()
	{
		x = Tilex.readFloat();
		y = Tilex.readFloat();
		dx = Tilex.readInt();
		dy = Tilex.readInt();
		health = Tilex.readInt();
		attack = Tilex.readInt();
		for(int a = 0; a < 9; a++)
		{
			inventory[a].id = Tilex.readInt();
			inventory[a].amount = Tilex.readInt();
		}
	}

	public static void load()
	{
		player.playerLoad();
	}

	private void playerSave()
	{
		Tilex.write(x);
		Tilex.write(" ");
		Tilex.write(y);
		Tilex.write(" ");
		Tilex.write(dx);
		Tilex.write(" ");
		Tilex.write(dy);
		Tilex.write(" ");
		Tilex.write(health);
		Tilex.write(" ");
		Tilex.write(attack);
		Tilex.write("\n");
		for(int a = 0; a < 9; a++)
		{
			Tilex.write(inventory[a].id);
			Tilex.write(" ");
			Tilex.write(inventory[a].amount);
			Tilex.write("\n");
		}
	}

	public static void save()
	{
		player.playerSave();
	}

	private int playerGetItem(int index)
	{
		if(index == -1)
			index = inventoryIndex;
		int returnTile = Definitions.NONE;
		if(inventory[index].amount > 0)
		{
			returnTile = inventory[index].id;
			inventory[index].amount--;
			if(inventory[index].amount <= 0)
				inventory[index].id = Definitions.NONE;
		}
		
		return returnTile;
	}

	public static int getItem(int index)
	{
		return player.playerGetItem(index);
	}

	private void playerUseItem(Definitions.stcItemSlot item)
	{
		int remaining = item.amount;
		for(int a = 0; a < 9; a++)
		{
			if(inventory[a].id == item.id)
			{
				if(inventory[a].amount >= remaining)
				{
					inventory[a].amount -= remaining;
					break;
				}
				else
				{
					remaining -= inventory[a].amount;
				}
			}
			if(inventory[a].amount <= 0)
				inventory[a].id = Definitions.NONE;
		}
	}

	public static void useItem(Definitions.stcItemSlot item)
	{
		player.playerUseItem(item);
	}

	private Definitions.stcItemSlot playerPeekItem(int index)
	{
		if(index == -1)
			index = inventoryIndex;
		return inventory[index];
	}

	public static Definitions.stcItemSlot peekItem(int index)
	{
		return player.playerPeekItem(index);
	}

	private float playerGetX()
	{
		return x;
	}

	public static float getX()
	{
		return player.playerGetX();
	}

	private float playerGetY()
	{
		return y;
	}

	public static float getY()
	{
		return player.playerGetY();
	}

	private int playerGetDX()
	{
		return dx;
	}

	public static int getDX()
	{
		return player.playerGetDX();
	}

	private int playerGetDY()
	{
		return dy;
	}

	public static int getDY()
	{
		return player.playerGetDY();
	}

	private int playerGetHealth()
	{
		return health;
	}

	public static int getHealth()
	{
		return player.playerGetHealth();
	}

	private int playerGetAttack()
	{
		return attack;
	}

	public static int getAttack()
	{
		return player.playerGetAttack();
	}

	private void playerRecieveAttack(int attack)
	{
		health -= attack;
		if(health < 0)
			health = 0;
		else
			beingAttacked = true;
	}

	public static void recieveAttack(int attack)
	{
		player.playerRecieveAttack(attack);
	}

	private boolean playerHasItem(Definitions.stcItemSlot item)
	{
		int remaining = item.amount;
		for(int a = 0; a < 9; a++)
		{
			if(inventory[a].id == item.id)
			{
				if(remaining <= inventory[a].amount)
					return true;
				else
					remaining -= inventory[a].amount;
			}
		}
		return false;
	}

	public static boolean hasItem(Definitions.stcItemSlot item)
	{
		return player.playerHasItem(item);
	}

	private boolean playerIsColidable(float x, float y, float bounds)
	{
		if((x - bounds < this.x + this.bounds && x - bounds > this.x - this.bounds) || (x + bounds < this.x + this.bounds && x + bounds > this.x - this.bounds))
			if((y - bounds < this.y + this.bounds && y - bounds > this.y - this.bounds) || (y + bounds < this.y + this.bounds && y + bounds > this.y - this.bounds))
				return true;
		return false;
	}

	public static boolean isColidable(float x, float y, float bounds)
	{
		return player.playerIsColidable(x, y, bounds);
	}

	private void playerDraw()
	{
		float drawX = (int)x - x;
	 	float drawY = (int)y - y;
	 	drawX -= .5;
	 	drawY -= .5; 
		Tilex.setOffset(Main.GROUND, drawX, drawY);
		Tilex.setOffset(Main.SURFACE, drawX, drawY);
		Tilex.setOffset(Main.SHADOW, drawX, drawY);
		Tilex.setOffset(Main.NPC, drawX - .5f, drawY - .5f);

		int imageId = 0;
		if(beingAttacked)
			imageId = 219;
		else if(dy == -1 && dx == 1)
			imageId = 322;
		else if(dy == -1 && dx == -1)
			imageId = 323;
		else if(dy == 1 && dx == 1)
			imageId = 324;
		else if(dy == 1 && dx == -1)
			imageId = 325;
		else if(dy == -1)
			imageId = 265;
		else if(dy == 1)
			imageId = 266;
		else if(dx == -1)
			imageId = 267;
		else if(dx == 1)
			imageId = 268;
		Tilex.setOffset(13, 8, Main.NPC, x - (int)x, y - (int)y);
		Tilex.draw(imageId, 13, 8, Main.NPC, Tilex.WHITE, 1);
		for(int a = 0; a < health; a++)
		{
			if(!beingAttacked)
				Tilex.draw(3, a, 0, Main.UI2, Tilex.RED, 1);
			else
				Tilex.draw(3, a, 0, Main.UI2, Tilex.WHITE, 1);
		}
		for(int ay = 0; ay < 2; ay++)
				for(int ax = 23; ax < 29; ax++)
					Tilex.draw(219, ax, ay, Main.UI1, Tilex.GRAY, 1);
		if(inventory[inventoryIndex].id != Definitions.NONE)
		{
			int itemImage = Definitions.getImage(inventory[inventoryIndex].id);
			Tilex.draw(itemImage, 24, 0, Main.UI2, Tilex.WHITE, 1);
			Tilex.print(inventory[inventoryIndex].amount, 26, 0, Main.UI2, Tilex.WHITE);
		}
		beingAttacked = false;
	}

	public static void draw()
	{
		player.playerDraw();
	}

	private void playerDrawWater()
	{
		float drawX = (int)x - x;
	 	float drawY = (int)y - y;
	 	drawX -= .5;
	 	drawY -= .5; 
		Tilex.setOffset(Main.GROUND, drawX, drawY);
		Tilex.setOffset(Main.SURFACE, drawX, drawY);
		Tilex.setOffset(Main.SHADOW, drawX, drawY);
		Tilex.setOffset(Main.NPC, drawX - .5f, drawY - .5f);
		
		int imageId = 0;
		if(beingAttacked)
			imageId = 219;
		else if(dy == -1 && dx == 1)
			imageId = 326;
		else if(dy == -1 && dx == -1)
			imageId = 327;
		else if(dy == 1 && dx == 1)
			imageId = 328;
		else if(dy == 1 && dx == -1)
			imageId = 329;
		else if(dy == -1)
			imageId = 290;
		if(dy == 1)
			imageId = 291;
		else if(dx == -1)
			imageId = 292;
		else if(dx == 1)
			imageId = 293;
		Tilex.setOffset(13, 8, Main.NPC, x - (int)x, y - (int)y);
		Tilex.draw(imageId, 13, 8, Main.NPC, Tilex.WHITE, 1);
		for(int a = 0; a < health; a++)
		{
			if(!beingAttacked)
				Tilex.draw(3, a, 0, Main.UI2, Tilex.RED, 1);
			else
				Tilex.draw(3, a, 0, Main.UI2, Tilex.WHITE, 1);
		}
		for(int ay = 0; ay < 2; ay++)
				for(int ax = 23; ax < 29; ax++)
					Tilex.draw(219, ax, ay, Main.UI1, Tilex.GRAY, 1);
		if(inventory[inventoryIndex].id != Definitions.NONE)
		{
			int itemImage = Definitions.getImage(inventory[inventoryIndex].id);
			Tilex.draw(itemImage, 24, 0, Main.UI2, Tilex.WHITE, 1);
			Tilex.print(inventory[inventoryIndex].amount, 26, 0, Main.UI2, Tilex.WHITE);
		}
		beingAttacked = false;
	}

	public static void drawWater()
	{
		player.playerDrawWater();
	}

	private void playerDrawInventory(int x, int y)
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
			else if(inventory[a].id == Definitions.NONE)
			{
				Tilex.print(inventory[a].amount, x + 3, (a * 2) + 6, Main.UI2, Tilex.WHITE);
			}
		}
	}

	public static void drawInventory(int x, int y)
	{
		player.playerDrawInventory(x, y);
	}
}