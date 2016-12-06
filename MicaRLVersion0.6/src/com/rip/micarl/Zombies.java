package com.rip.micarl;

import com.rip.framework.Tilex;

public class Zombies
{
	public Zombie[] zombie = new Zombie[100];
	public int numZombies;

	public Zombies()
	{
		for(int a = 0; a < 100; a++)
			zombie[a] = new Zombie(a);
		numZombies = 0;
	}

	public void generate()
	{
		for(int a = 0; a < 100; a++)
		{
			zombie[a].generate();
		}
		numZombies = 100;
	}

	public void update(float deltaTime)
	{
		numZombies = 0;
		for(int a = 0; a < 100; a++)
		{
			if(zombie[a].getHealth() > 0)
			{
				zombie[a].update(deltaTime);
				numZombies++;
			}
		}
	}

	public boolean isColidable(int id, float x, float y, float bounds)
	{
		for(int a = 0; a < 100; a++)
		{
			if(zombie[a].isColidable(id, x, y, bounds))
				return true;
		}
		return false;
	}

	public int isZombie(float x, float y)
	{
		for(int a = 0; a < 100; a++)
		{
			if(zombie[a].isColidable(-1, x, y, 0))
				return a;
		}
		return -1;
	}

	public boolean attackZombie()
	{
		float aX = Player.getX();
		float aY = Player.getY();
		for(int a = 0; a < 12; a++)
		{
			int index = isZombie(aX, aY);
			if(index >= 0)
			{
				zombie[index].recieveAttack(Player.getAttack());
				return true;
			}
			aX += Player.getDX() * .25f;
			aY += Player.getDY() * .25f;
		}
		return false;
	}

	public void load()
	{
		numZombies = Tilex.readInt();
		for(int a = 0; a < 100; a++)
			zombie[a].load();
	}

	public void save()
	{
		Tilex.write(numZombies);
		Tilex.write("\n");
		for(int a = 0; a < 100; a++)
			zombie[a].save();
	}

	public void draw()
	{
		for(int a = 0; a < 100; a++)
		{
			if(zombie[a].getHealth() > 0)
				zombie[a].draw();
		}
	}

	public void drawMiniMap()
	{
		for(int a = 0; a < 100; a++)
		{
			if(zombie[a].getHealth() > 0)
				zombie[a].drawMiniMap();
		}
	}
}