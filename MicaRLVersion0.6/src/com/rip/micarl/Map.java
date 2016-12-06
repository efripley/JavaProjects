package com.rip.micarl;

import android.util.Log;

import com.rip.framework.Tilex;

public class Map
{	
	public static final int MAP_SIZE = 300;

	private Tile[][][] map = new Tile[MAP_SIZE][MAP_SIZE][2];
	
	public Map()
	{		
		for(int ay = 0; ay < MAP_SIZE; ay++)
		{
			for(int ax = 0; ax < MAP_SIZE; ax++)
			{
				map[ax][ay][0] = Tiles.getTile(Definitions.GRASS);
				map[ax][ay][1] = Tiles.getTile(Definitions.AIR);
			}
		}
	}

	public void generate()
	{
		int num_elements = Definitions.rand.nextInt(6) + 5;
		int x = 0;
		int y = 0;
		for(int a = 0; a < num_elements; a++)
		{
			x = Definitions.rand.nextInt(MAP_SIZE);
			y = Definitions.rand.nextInt(MAP_SIZE);
			generateLake(x, y);
		}

		generateRiver();

		num_elements = Definitions.rand.nextInt(20) + 20;
		for(int a = 0; a < num_elements; a++)
		{
			x = Definitions.rand.nextInt(MAP_SIZE);
			y = Definitions.rand.nextInt(MAP_SIZE);
			generateMountain(x, y);
		}
		
		num_elements = Definitions.rand.nextInt(6) + 5;
		for(int a = 0; a < num_elements; a++)
		{
			x = Definitions.rand.nextInt(MAP_SIZE);
			y = Definitions.rand.nextInt(MAP_SIZE);
			generateRedMountain(x, y);
		}

		generateCoal();
		
		num_elements = Definitions.rand.nextInt(20) + 20;
		for(int a = 0; a < num_elements; a++)
		{
			x = Definitions.rand.nextInt(MAP_SIZE);
			y = Definitions.rand.nextInt(MAP_SIZE);
			generateForest(x, y);
		}
	}

	public void placeTile(int x, int y, int z, int newTile)
	{		
		if(newTile == Definitions.WOODDOOR)
			map[x][y][z] = new WoodDoor();
		else if(newTile == Definitions.CHEST)
			map[x][y][z] = new Chest();
		else
			map[x][y][z] = Tiles.getTile(newTile);
	}

	public void removeTile(int x, int y, int z)
	{
		if(z == 1)
		{
			map[x][y][z] = Tiles.getTile(Definitions.AIR);
		}
		else if(z == 0)
			map[x][y][z] = Tiles.getTile(Definitions.BEDROCK);
	}

	public void interact(int x, int y, int z)
	{
		map[x][y][z].interact();
	}

	public void load()
	{
		for(int az = 0; az < 2; az++)
		{
			for(int ay = 0; ay < MAP_SIZE; ay++)
			{
				for(int ax = 0; ax < MAP_SIZE; ax++)
				{
					int temp = Tilex.readInt();
					if(temp == Definitions.WOODDOOR)
					{
						boolean doorClosed = Tilex.readBoolean();
						map[ax][ay][az] = new WoodDoor();
						if(!doorClosed)
							map[ax][ay][az].interact();
					}
					else if(temp == Definitions.CHEST)
					{
						map[ax][ay][az] = new Chest();
						map[ax][ay][az].load();
					}
					else
					{
						map[ax][ay][az] = Tiles.getTile(temp);
					}
				}
			}
		}
	}

	public void save()
	{
		for(int az = 0; az < 2; az++)
		{
			for(int ay = 0; ay < MAP_SIZE; ay++)
			{
				for(int ax = 0; ax < MAP_SIZE; ax++)
				{
					int id = map[ax][ay][az].getId();
					Tilex.write(id);
					Tilex.write(" ");
					if(id == Definitions.WOODDOOR)
					{
						Tilex.write(map[ax][ay][az].isColidable());
						Tilex.write(" ");
					}
					else if(id == Definitions.CHEST)
					{
						map[ax][ay][az].save();
					}
				}
				Tilex.write("\n");
			}
		}
	}

	public boolean isColidable(int x, int y, int z)
	{
		if(x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE)
			return true;
		return map[x][y][z].isColidable();
	}

	public boolean isBedrock(int x, int y, int z)
	{
		return map[x][y][z] == Tiles.getTile(Definitions.BEDROCK);
	}

	public boolean isAir(int x, int y, int z)
	{
		return map[x][y][z] == Tiles.getTile(Definitions.AIR);
	}

	public boolean isTable(int x, int y, int z)
	{
		return map[x][y][z] == Tiles.getTile(Definitions.TABLE);
	}

	public boolean isLog(int x, int y, int z)
	{
		return map[x][y][z] == Tiles.getTile(Definitions.OAKLOG);
	}

	public boolean isWater(int x, int y, int z)
	{
		return map[x][y][z] == Tiles.getTile(Definitions.WATER);
	}

	public Tile getTile(int x, int y, int z)
	{
		return map[x][y][z];
	}

	public int getDrop(int x, int y, int z)
	{
		return map[x][y][z].getDrop();
	}

	public void draw(int centerX, int centerY)
	{
		int drawX = 0;
		int drawY = 0;
		int north = Definitions.NONE;
		int south = Definitions.NONE;
		int west = Definitions.NONE;
		int east = Definitions.NONE;
		for(int az = 0; az < 2; az++)
		{
			for(int ay = centerY - 8; ay <= centerY + 8; ay++)
			{
				for(int ax = centerX - 13; ax <= centerX + 13; ax++)
				{
					if(ax >= 0 && ax < MAP_SIZE && ay >= 0 && ay < MAP_SIZE)
					{
						if(ay - 1 > 0)
							north = map[ax][ay - 1][az].getId();
						if(ay + 1 < MAP_SIZE - 1)
							south = map[ax][ay + 1][az].getId();
						if(ax - 1 > 0)
							west = map[ax - 1][ay][az].getId();
						if(ax + 1 < MAP_SIZE - 1)
							east = map[ax + 1][ay][az].getId();
						
						map[ax][ay][az].draw(drawX, drawY, az, north, south, west, east);
						if(az == 1 && !map[ax][ay][az].isShadowed())
							drawShadow(ax, ay, drawX, drawY);
					}
					drawX++;
				}
				drawX = 0;
				drawY++;
			}
			drawX = 0;
			drawY = 0;
		}
		if(Main.clock >= 19 || Main.clock <= 7)
		{
			int tempId = 347;
			if(Main.clock >= 21 || Main.clock <= 5)
				tempId = 346;
			for(int ay = 0; ay < 15; ay++)
			{
				for(int ax = 0; ax < 25; ax++)
				{
					{
						Tilex.draw(tempId, ax, ay, Main.NIGHT, Tilex.WHITE, 1);
					}
				}
			}
		}
	}

	public void drawShadow(int x, int y, int dx, int dy)
	{
		boolean north = false;
		boolean south = false;
		boolean east = false;
		boolean west = false;
		if(y > 0)
			north = map[x][y - 1][1].isShadowed();
		if(y < MAP_SIZE - 1)
			south = map[x][y + 1][1].isShadowed();
		if(x < MAP_SIZE - 1)
			east = map[x + 1][y][1].isShadowed();
		if(x > 0)
			west = map[x - 1][y][1].isShadowed();
		int imageId = 256;
		if(north && south && east && west)
			imageId = 272;
		else if(north && west && east)
			imageId = 273;
		else if(north && south && west)
			imageId = 274;
		else if(south && east && west)
			imageId = 275;
		else if(north && south && east)
			imageId = 276;
		else if(north && east)
			imageId = 277;
		else if(east && south)
			imageId = 278;
		else if(south && west)
			imageId = 279;
		else if(west && north)
			imageId = 280;
		else if(north && south)
			imageId = 281;
		else if(west && east)
			imageId = 282;
		else if(north)
			imageId = 283;
		else if(east)
			imageId = 284;
		else if(south)
			imageId = 285;
		else if(west)
			imageId = 286; 
		Tilex.draw(imageId, dx, dy, Main.SHADOW, Tilex.WHITE, 1);
	}

	public void drawMiniMap(int x, int y)
	{	
		Tilex.setOffset(Main.MINIMAP, 40, .5f);
		int dx = 0;
		int dy = 0;	
		for(int ay = y - 60; ay <= y + 60; ay++)
		{
			for(int ax = x - 60; ax <= x + 60; ax++)
			{
				if(ax >= 0 && ax < MAP_SIZE && ay >= 0 && ay < MAP_SIZE)
				{
					if(ax == x && ay == y)
						Tilex.draw(219, dx, dy, Main.MINIMAP, Tilex.RED, 1);
					else if(map[ax][ay][1] == Tiles.getTile(Definitions.AIR))
					{
						int tileId = map[ax][ay][0].getImageId();
						Tilex.draw(tileId, dx, dy, Main.MINIMAP, Tilex.WHITE, 1);
					}
					else
					{
						int tileId = map[ax][ay][1].getImageId();
						Tilex.draw(tileId, dx, dy, Main.MINIMAP, Tilex.WHITE, 1);
					}
				}
				dx++;
			}
			dx =0;
			dy++;
		}
	}

	private void generateLake(int x, int y)
	{
		int wx = x;
		int wy = y;
		int sx = x;
		int sy = y;
		int numBlocks = Definitions.rand.nextInt(1000) + 1000;
		int sandCount = 0;
		int maxSand = 100;
		for(int a = 0; a < numBlocks; a++)
		{
			if(sx > 0 && sx < MAP_SIZE - 1 && sy > 0 && sy < MAP_SIZE - 1 && map[sx][sy][0] == Tiles.getTile(Definitions.WATER) && sandCount < maxSand)
			{
				map[sx][sy][0] = Tiles.getTile(Definitions.SAND);
				map[sx + 1][sy][0] = Tiles.getTile(Definitions.SAND);
				map[sx - 1][sy][0] = Tiles.getTile(Definitions.SAND);
				map[sx][sy + 1][0] = Tiles.getTile(Definitions.SAND);
				map[sx][sy - 1][0] = Tiles.getTile(Definitions.SAND);
				sandCount++;
			}
			if(wx > 0 && wx < MAP_SIZE - 1 && wy > 0 && wy < MAP_SIZE - 1)	
			{
				map[wx][wy][0] = Tiles.getTile(Definitions.WATER);
				map[wx + 1][wy][0] = Tiles.getTile(Definitions.WATER);
				map[wx - 1][wy][0] = Tiles.getTile(Definitions.WATER);
				map[wx][wy + 1][0] = Tiles.getTile(Definitions.WATER);
				map[wx][wy - 1][0] = Tiles.getTile(Definitions.WATER);
			}

			int direction = Definitions.rand.nextInt(4);
			switch(direction)
			{
			case 0:
				wy--;
				break;
			case 1:
				wy++;
				break;
			case 2:
				wx--;
				break;
			case 3:
				wx++;
				break;
			}

			direction = Definitions.rand.nextInt(4);
			switch(direction)
			{
			case 0:
				sy--;
				break;
			case 1:
				sy++;
				break;
			case 2:
				sx--;
				break;
			case 3:
				sx++;
				break;
			}
		}
	}
	
	private void generateRiver()
	{
	}
	
	private void generateMountain(int x, int y)
	{	
		int numBlocks = Definitions.rand.nextInt(1000) + 1000;
		for(int a = 0; a < numBlocks; a++)
		{
			if(x > 0 && x < MAP_SIZE - 1 && y > 0 && y < MAP_SIZE - 1)
				if((map[x][y][0] == Tiles.getTile(Definitions.GRASS) || 
				map[x][y][0] == Tiles.getTile(Definitions.DIRT) ||
				map[x][y][0] == Tiles.getTile(Definitions.STONE) ||
				map[x][y][0] == Tiles.getTile(Definitions.WATER)) && 
				(map[x][y][1] == Tiles.getTile(Definitions.AIR) || 
				map[x][y][1] == Tiles.getTile(Definitions.STONE)))
				{
					map[x][y][0] = Tiles.getTile(Definitions.STONE);
					map[x + 1][y][0] = Tiles.getTile(Definitions.STONE);
					map[x - 1][y][0] = Tiles.getTile(Definitions.STONE);
					map[x][y + 1][0] = Tiles.getTile(Definitions.STONE);
					map[x][y - 1][0] = Tiles.getTile(Definitions.STONE);
					map[x][y][1] = Tiles.getTile(Definitions.STONE);
					map[x + 1][y][1] = Tiles.getTile(Definitions.STONE);
					map[x - 1][y][1] = Tiles.getTile(Definitions.STONE);
					map[x][y + 1][1] = Tiles.getTile(Definitions.STONE);
					map[x][y - 1][1] = Tiles.getTile(Definitions.STONE);
				}	
			int direction = Definitions.rand.nextInt(4);
			switch(direction)
			{
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				x--;
				break;
			case 3:
				x++;
				break;
			}
		}
	}

	private void generateRedMountain(int x, int y)
	{
		int numBlocks = Definitions.rand.nextInt(1000) + 1000;
		for(int a = 0; a < numBlocks; a++)
		{
			if(x > 0 && x < MAP_SIZE - 1 && y > 0 && y < MAP_SIZE - 1)
				if((map[x][y][0] == Tiles.getTile(Definitions.GRASS) || 
				map[x][y][0] == Tiles.getTile(Definitions.DIRT) ||
				map[x][y][0] == Tiles.getTile(Definitions.REDROCK) ||
				map[x][y][0] == Tiles.getTile(Definitions.WATER)) && 
				(map[x][y][1] == Tiles.getTile(Definitions.AIR) || 
				map[x][y][1] == Tiles.getTile(Definitions.REDROCK)))
				{
					map[x][y][0] = Tiles.getTile(Definitions.REDROCK);
					map[x + 1][y][0] = Tiles.getTile(Definitions.REDROCK);
					map[x - 1][y][0] = Tiles.getTile(Definitions.REDROCK);
					map[x][y + 1][0] = Tiles.getTile(Definitions.REDROCK);
					map[x][y - 1][0] = Tiles.getTile(Definitions.REDROCK);
					map[x][y][1] = Tiles.getTile(Definitions.REDROCK);
					map[x + 1][y][1] = Tiles.getTile(Definitions.REDROCK);
					map[x - 1][y][1] = Tiles.getTile(Definitions.REDROCK);
					map[x][y + 1][1] = Tiles.getTile(Definitions.REDROCK);
					map[x][y - 1][1] = Tiles.getTile(Definitions.REDROCK);
				}	
			int direction = Definitions.rand.nextInt(4);
			switch(direction)
			{
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				x--;
				break;
			case 3:
				x++;
				break;
			}
		}
	}

	private void generateCoal()
	{
		int numBlocks = Definitions.rand.nextInt(1001) + 1000;
		int x = 0;
		int y = 0;
		boolean found = false;
		for(int a = 0; a < numBlocks; a++)
		{
			found = false;
			while(!found)
			{
				x = Definitions.rand.nextInt(MAP_SIZE);
				y = Definitions.rand.nextInt(MAP_SIZE);
				if(map[x][y][1] == Tiles.getTile(Definitions.STONE))
				{
					found = true;
					map[x][y][1] = Tiles.getTile(Definitions.COALORE);
					map[x][y][0] = Tiles.getTile(Definitions.COALORE);
				}
			}
		}
	}
	
	private void generateForest(int x, int y)
	{
		int numTrees = Definitions.rand.nextInt(1000) + 1000;
		for(int a = 0; a < numTrees; a++)
		{
			if(x >= 0 && x < MAP_SIZE && y >= 0 && y < MAP_SIZE)
			{
				if((map[x][y][0] == Tiles.getTile(Definitions.GRASS) ||
				map[x][y][0] == Tiles.getTile(Definitions.DIRT)) && 
				map[x][y][1] == Tiles.getTile(Definitions.AIR))
					map[x][y][1] = Tiles.getTile(Definitions.OAKTREE);
			}
			int direction = Definitions.rand.nextInt(4);
			int distance = Definitions.rand.nextInt(5) + 1;
			switch(direction)
			{
			case 0:
				y -= distance;
				break;
			case 1:
				y += distance;
				break;
			case 2:
				x -= distance;
				break;
			case 3:
				x += distance;
				break;
			}
		}
	}
}