package com.rip.micarl;

import android.util.Log;

import com.rip.framework.Tilex;

public class Zombie
{
	private float x, y;
	private float speed;
	private float comitment;
	private int dx, dy;
	private float bounds;
	private int id;
	private int health;
	private int attack;
	private boolean beingAttacked;

	public Zombie(int id)
	{
		x = 0;
		y = 0;
		speed = 1.5f;
		dx = 0;
		dy = -1;
		bounds = .5f;
		this.id = id;
		comitment = 0;
		health = 0;
		attack = 0;
		beingAttacked = false;
	}

	public void generate()
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
		attack = 2;
		beingAttacked = false;
		comitment = 0;
	}

	public void update(float deltaTime)
	{
		if(health > 0)
		{
			if((int)x - (int)Player.getX() >= -8 && (int)x - (int)Player.getX() <= 8 && (int)y - (int)Player.getY() >= -8 && (int)y - (int)Player.getY() <= 8)
			{
				boolean foundPath =  false;
				int px = (int)x - (int)Player.getX() + 8;
				int py = (int)y - (int)Player.getY() + 8;
				int lowest = Player.path[px][py];
				for(int ay = -1; ay <= 1; ay++)
				{
					for(int ax = -1; ax <= 1; ax++)
					{
						if(ax + px >= 0 && ax + px < 17 && ay + py >= 0 && ay + py < 17)
						{
							if(Player.path[ax + px][ay + py] < lowest)
							{
								foundPath = true;
								lowest = Player.path[ax + px][ay + py];
								dx = ax;
								dy = ay;
							}
						}
					}
				}
				if(!foundPath)
				{
					if(comitment <= 0)
					{
						int direction = Definitions.rand.nextInt(8);
						switch(direction)
						{
						case 0:
							faceUp();
							break;
						case 1:
							faceUpRight();
							break;
						case 2:
							faceRight();
							break;
						case 3:
							faceDownRight();
							break;
						case 4:
							faceDown();
							break;
						case 5:
							faceDownLeft();
							break;
						case 6:
							faceLeft();
							break;
						case 7:
							faceUpLeft();
							break;
						}
						comitment = Definitions.rand.nextInt(5) + 5;
					}
					else
					{
						move(deltaTime);
						if(Main.gameMap.isColidable((int)x, (int)y, 1) || Main.zombies.isColidable(id, x, y, bounds))
						{
							moveBack(deltaTime);
							comitment = 0;
						}
						else
						{
							comitment -= deltaTime;
						}
					}
				}
				else
				{
					move(deltaTime);
					comitment -= deltaTime;
					if(Player.isColidable(x, y, bounds))
					{
						if(comitment <= 0)
						{
							Player.recieveAttack(attack);
							comitment = 1;
						}
						moveBack(deltaTime);
					}
					else if(Main.zombies.isColidable(id, x, y, bounds))
					{
						moveBack(deltaTime);
					}
				}
			}
			else if(comitment <= 0)
			{
				int direction = Definitions.rand.nextInt(8);
				switch(direction)
				{
				case 0:
					faceUp();
					break;
				case 1:
					faceUpRight();
					break;
				case 2:
					faceRight();
					break;
				case 3:
					faceDownRight();
					break;
				case 4:
					faceDown();
					break;
				case 5:
					faceDownLeft();
					break;
				case 6:
					faceLeft();
					break;
				case 7:
					faceUpLeft();
					break;
				}
				comitment = Definitions.rand.nextInt(5) + 5;
			}
			else
			{
				move(deltaTime);
				if(Main.gameMap.isColidable((int)x, (int)y, 1) || Main.zombies.isColidable(id, x, y, bounds))
				{
					moveBack(deltaTime);
					comitment = 0;
				}
				else
				{
					comitment -= deltaTime;
				}
			}
		}
	}

	public void faceUp()
	{
		dx = 0;
		dy = -1;
		speed = 1.5f;
	}

	public void faceDown()
	{
		dx = 0;
		dy = 1;
		speed = 1.5f;
	}

	public void faceUpLeft()
	{
		dx = -1;
		dy = -1;
		speed = 1.5f * .7f;
	}

	public void faceUpRight()
	{
		dx = 1;
		dy = -1;
		speed = 1.5f * .7f;
	}

	private void faceLeft()
	{
		dx = -1;
		dy = 0;
		speed = 1.5f;
	}

	public void faceRight()
	{
		dx = 1;
		dy = 0;
		speed = 1.5f;
	}

	public void faceDownLeft()
	{
		dx = -1;
		dy = 1;
		speed = 1.5f * .7f;
	}

	public void faceDownRight()
	{
		dx = 1;
		dy = 1;
		speed = 1.5f * .7f;
	}

	public void move(float deltaTime)
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

	public void moveBack(float deltaTime)
	{
		x -= dx * speed * deltaTime;
		y -= dy * speed * deltaTime;
	}

	public void load()
	{
		x = Tilex.readFloat();
		y = Tilex.readFloat();
		dx = Tilex.readInt();
		dy = Tilex.readInt();
		comitment = Tilex.readFloat();
		id = Tilex.readInt();
		health = Tilex.readInt();
		attack = Tilex.readInt();
	}

	public void save()
	{
		Tilex.write(x);
		Tilex.write(" ");
		Tilex.write(y);
		Tilex.write(" ");
		Tilex.write(dx);
		Tilex.write(" ");
		Tilex.write(dy);
		Tilex.write(" ");
		Tilex.write(comitment);
		Tilex.write(" ");
		Tilex.write(id);
		Tilex.write(" ");
		Tilex.write(health);
		Tilex.write(" ");
		Tilex.write(attack);
		Tilex.write("\n");
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public int getDX()
	{
		return dx;
	}

	public int getDY()
	{
		return dy;
	}

	public int getHealth()
	{
		return health;
	}

	public void recieveAttack(int attack)
	{
		health -= attack;
		beingAttacked = true;
	}

	public int getAttack()
	{
		return attack;
	}

	public int getId()
	{
		return id;
	}

	public boolean isColidable(int id, float x, float y, float bounds)
	{
		if(this.id == id)
			return false;
		else if((x - bounds < this.x + this.bounds && x - bounds > this.x - this.bounds) || (x + bounds < this.x + this.bounds && x + bounds > this.x - this.bounds))
			if((y - bounds < this.y + this.bounds && y - bounds > this.y - this.bounds) || (y + bounds < this.y + this.bounds && y + bounds > this.y - this.bounds))
				return true;
		return false;
	}

	public void draw()
	{
		if(x >= (int)Player.getX() - 13 && x <= (int)Player.getX() + 13 && y >= (int)Player.getY() - 8 && y <= (int)Player.getY() + 8)
		{
			int imageId = 0;
			if(beingAttacked)
				imageId = 219;
			else if(dy == -1 && dx == 1)
				imageId = 342;
			else if(dy == -1 && dx == -1)
				imageId = 343;
			else if(dy == 1 && dx == 1)
				imageId = 344;
			else if(dy == 1 && dx == -1)
				imageId = 345;
			else if(dy == -1)
				imageId = 338;
			else if(dy == 1)
				imageId = 339;
			else if(dx == -1)
				imageId = 340;
			else if(dx == 1)
				imageId = 341;
			int drawX = 13 - ((int)Player.getX() - (int)x);
			int drawY = 8 - ((int)Player.getY() - (int)y);
			float offsetX = x - (int)x;
			float offsetY = y - (int)y;
			Tilex.setOffset(drawX, drawY, Main.NPC, offsetX, offsetY);
			Tilex.draw(imageId, drawX, drawY, Main.NPC , Tilex.WHITE, 1);
			beingAttacked = false;
		}
	}

	public void drawMiniMap()
	{
		if(x >= Player.getX() - 60 && x <= Player.getX() + 60 && y >= Player.getY() - 60 && y <= Player.getY() + 60)
		{
			int drawX = 60 - ((int)Player.getX() - (int)x);
			int drawY = 60 - ((int)Player.getY() - (int)y);
			Tilex.draw(219, drawX, drawY, Main.MINIMAP , Tilex.WHITE, 1);
		}
	}
}