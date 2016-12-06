package com.rip.micarl;

import android.util.Log;

import com.rip.framework.TXRoutine;
import com.rip.framework.Tilex;
import com.rip.framework.Tilex.TXKey;
import com.rip.framework.Tilex.TXButton;

public class Main implements TXRoutine
{
    public boolean title = true;
    public boolean load = false;
    public boolean newGame = false;
    public boolean game = false;
    public boolean firstRun = true;
    public boolean initialize = true;
    public int moveTouchId = 0;
    public int tileStrength = 0;
	public int index = 0;
    public StringBuilder buildName = new StringBuilder();
    public char currentChar = 0;
    public String[] files = new String[5];

    public long ticks;
    public float deltaTime;
    
    public long deltaClock;
    public static int clock;

    public static Map gameMap = new Map();
    public Ingredients gameIngredients = new Ingredients();
    public static Zombies zombies = new Zombies();

    public boolean attack = false;
    public boolean turn = false;
    public boolean hit = false;
    public boolean interact = false;
    public int hitX = 0;
    public int hitY = 0;
    public int hitProgress = 0;

    public static TXButton menuUp;
    public static TXButton menuDown;
    public static TXButton upButton;
    public static TXButton ghostUp;
    public static TXButton upRightButton;
    public static TXButton ghostUpRight;
    public static TXButton upLeftButton;
    public static TXButton ghostUpLeft;
    public static TXButton downButton;
    public static TXButton ghost
    public static TXButton downRightButton;
    public static TXButton downLeftButton;
    public static TXButton leftButton;
    public static TXButton rightButton;
    public static TXButton button1;
    public static TXButton button2;
    public static TXButton button3;
    public static TXButton button4;
    public static TXButton accept;
    public static TXButton exit;
    public static TXButton saveExit;
    public static TXButton goBack;
    public static TXButton oneItem;
    public static TXButton halfItem;
    public static TXButton allItem;

    public static int GROUND = 0;
    public static int SURFACE = 1;
    public static int SHADOW = 2;
    public static int NPC = 3;
    public static int NIGHT = 4;
    public static int UI1 = 5;
    public static int UI2 = 6;
    public static int MINIMAP = 7;
	
    public Main()
    {

    }

    private int getDirection(TXKey key)
    {
        boolean processKey = false;
        if(key.id == moveTouchId)
        {
            processKey = true;
        }   
        else if(moveButton.held(key))
        {
            moveTouchId = key.id;
            processKey = true;
        }
        else
        {
            moveTouchId = -1;
        }

        if(processKey)
        {
            if(key.y - moveButton.centerY >= 0)
            {
                if(key.x == 0)
                    key.x = 1;
                float ratio = key.y / key.x;
                if(ratio >= 2 || ratio <= -2)
                    return 3;
                else if(ratio >= 1 / 2.0f && ratio < 2)
                {
                    return ;
                }
                else if(ratio < 1 / 2.0f && ratio > 0)
                {
                    return 2;
                }
                else if(ratio <=  -1 / 2.0f && ratio > -2)
                {
                    return 7;
                }
                else if(ratio > -1 / 2.0f && ratio < 0)
                {
                    return 6;
                }
            }
            else if(key.y - moveButton.centerY < 0)
            {
                if(key.x == 0)
                    key.x = 1;
                float ratio = key.y / key.x;
                if(ratio >= 2 || ratio <= -2)
                    return 0;
                else if(ratio >= 1 / 2.0f && ratio < 2)
                {
                    return 1;
                }
                else if(ratio < 1 / 2.0f && ratio > 0)
                {
                    return 2;
                }
                else if(ratio <=  -1 / 2.0f && ratio > -2)
                {
                    return 7;
                }
                else if(ratio > -1 / 2.0f && ratio < 0)
                {
                    return 6;
                }
            }
        }
    }

    @Override
	public void update()
	{
        if(initialize)
        {
            Tilex.newLayer(GROUND, 27, 17, 32);
            Tilex.newLayer(SURFACE, 27, 17, 32);
            Tilex.newLayer(SHADOW, 27, 17, 32);
            Tilex.newLayer(NPC, 27, 17, 32);
            Tilex.newLayer(NIGHT, 25, 15, 32);
            Tilex.newLayer(UI1, 50, 30, 16);
            Tilex.newLayer(UI2, 50, 30, 16);
            Tilex.newLayer(MINIMAP, 121, 121, 4);
            
            Tilex.newSheet("Font16.png", 0);
            Tilex.newSheet("MicaRLSheet1.png", 1);

            for(int a = 0; a < 5; a++)
                files[a] = "Empty";
            
            menuUp = new TXButton(304, 2, 16, UI2, 4);
            menuDown = new TXButton(305, 2, 24, UI2, 4);
            upButton = new TXButton(304, 6, 16, UI2, 4);
            upRightButton = new TXButton(320, 10, 16, UI2, 4);
            upLeftButton = new TXButton(321, 2, 16, UI2, 4);
            downButton = new TXButton(305, 6, 24, UI2, 4);
            downRightButton = new TXButton(321, 10, 24, UI2, 4);
            downLeftButton = new TXButton(320, 2, 24, UI2, 4);
            leftButton = new TXButton(306, 2, 20, UI2, 4);
            rightButton = new TXButton(307, 10, 20, UI2, 4);
            button1 = new TXButton(308, 44, 2, UI2, 4);
            button2 = new TXButton(309, 44, 9, UI2, 4);
            button3 = new TXButton(311, 44, 20, UI2, 4);
            button4 = new TXButton(310, 36, 20, UI2, 4);
            accept = new TXButton(330, 44, 24, UI2, 4);
            exit = new TXButton(331, 44, 16, UI2, 4);
            saveExit = new TXButton(332, 44, 24, UI2, 4);
            goBack = new TXButton(333, 44, 2, UI2, 4);
            oneItem = new TXButton(334, 44, 24, UI2, 4);
            halfItem = new TXButton(335, 44, 16, UI2, 4);
            allItem = new TXButton(336, 44, 9, UI2, 4);

            ticks = System.currentTimeMillis();
            deltaClock = ticks;
            clock = 4;

            initialize = false;
            
            for(int a = Definitions.AIR; a < Definitions.LASTTILE; a++)
                Definitions.loadItem(a, Tiles.getTile(a).getName(), Tiles.getTile(a).getImageId());

            for(int a = Definitions.STICK; a < Definitions.LASTINGREDIENT; a++)
                Definitions.loadItem(a, gameIngredients.getName(a), gameIngredients.getImageId(a));
        }
        if(game)
        {
            deltaTime = (System.currentTimeMillis() - ticks) / 1000.0f;
            ticks = System.currentTimeMillis();

            if(ticks - deltaClock > 30000)
            {
                deltaClock = ticks;
                clock++;
                if(clock > 23)
                    clock = 0;
            }
            if(deltaTime > .1f)
            {
                deltaTime = .1f;
            }

            TXKey key1 = Tilex.getInput(0);

            if(upButton.held(key1))
            {
                Player.faceUp();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(upRightButton.held(key1))
            {
                Player.faceUpRight();
                turn = true;
                attack = false;
                interact = false;
                hit = false; 
            }
            else if(upLeftButton.held(key1))
            {
                Player.faceUpLeft();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(downButton.held(key1))
            {
                Player.faceDown();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(downRightButton.held(key1))
            {
                Player.faceDownRight();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(downLeftButton.held(key1))
            {
                Player.faceDownLeft();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(leftButton.held(key1))
            {
                Player.faceLeft();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(rightButton.held(key1))
            {
                Player.faceRight();
                turn = true;
                attack = false;
                interact = false;
                hit = false;
            }
            else if(button4.clicked(key1))
            {
                attack = !attack;
                if(attack)
                {
                    interact = false;
                    turn = true;
                }
                else
                    hit = false;
            }
            else if(button3.clicked(key1))
            {
                interact = !interact;
                if(interact)
                {
                    attack = false;
                    turn = true;
                    hit = false;
                }
            }
            else if(button2.clicked(key1))
            {
                Player.browseInventory();
                hit = false;
                attack = false;
                interact = false;
            }
            else if(button1.clicked(key1))
            {
                hit = false;
                attack = false;
                interact = false;
                if(pauseScreen() == 0)
                {
                    Tilex.openWriter(buildName.toString() + ".dat");
                    Tilex.write(clock);
                    Tilex.write("\n");
                    Player.save();
                    zombies.save();
                    gameMap.save();
                    Tilex.closeWriter();      
                    game = false;
                    title = true;
                    firstRun = true;
                }
            }

            if(turn)
            {
                if(attack)
                {
                    int actionX = (int)Player.getX() + Player.getDX();
                    int actionY = (int)Player.getY() + Player.getDY();
                    if(zombies.attackZombie())
                    {
                       
                    }
                    else if(!gameMap.isAir(actionX, actionY, 1))
                    {
                        Tile myTile = gameMap.getTile(actionX, actionY, 1);
                        if(tileStrength == 0)
                            tileStrength = myTile.getStrength();
                        tileStrength -= Player.getAttack();
                        hitProgress = (int)((1 - (float)tileStrength / myTile.getStrength()) * 8);
                        hit = true;
                        hitX = actionX;
                        hitY = actionY;
                        hitX -= (int)Player.getX();
                        hitY -= (int)Player.getY();
                        hitX += 13;
                        hitY += 8;
                        if(tileStrength <= 0)
                        {
                            for(int a = 0; a < myTile.getNumDrops(); a++)
                                Player.addToInventory(-1, myTile.getDrop());
                            gameMap.removeTile(actionX, actionY, 1);
                            tileStrength = 0;
                        }
                    }
                    else if(!gameMap.isAir(actionX, actionY, 0))
                    {
                        if(Player.peekItem(-1).id == Definitions.NONE && Player.peekItem(-1).id < Definitions.LASTTILE)
                        {
                            Tile myTile = gameMap.getTile(actionX, actionY, 0);
                            if(tileStrength == 0)
                                tileStrength = myTile.getStrength();
                            tileStrength -= Player.getAttack();
                            hitProgress = (int)((1 - (float)tileStrength / myTile.getStrength()) * 8);
                            hit = true;
                            hitX = actionX;
                            hitY = actionY;
                            hitX -= (int)Player.getX();
                            hitY -= (int)Player.getY();
                            hitX += 13;
                            hitY += 8;
                            if(tileStrength <= 0)
                            {
                                for(int a = 0; a < myTile.getNumDrops(); a++)
                                    Player.addToInventory(-1, myTile.getDrop());
                                gameMap.removeTile(actionX, actionY, 0);
                                tileStrength = 0;
                            }
                        }
                    }
                    attack = false;
                }
                else if(interact)
                {
                    int interactX = (int)Player.getX() + Player.getDX();
                    int interactY = (int)Player.getY() + Player.getDY();
                    if(gameMap.isBedrock(interactX, interactY, 0))
                    {
                        if(Player.peekItem(-1).id > Definitions.NONE && Player.peekItem(-1).id < Definitions.LASTTILE)
                        {
                            gameMap.placeTile(interactX, interactY, 0, Player.getItem(-1));
                        }
                    }
                    else if(gameMap.isAir(interactX, interactY, 1))
                    {
                        if(Player.peekItem(-1).id > Definitions.NONE && Player.peekItem(-1).id < Definitions.LASTTILE)
                        {
                            gameMap.placeTile(interactX, interactY, 1, Player.getItem(-1));
                        }
                    }
                    else if(gameMap.isLog(interactX, interactY, 1))
                    {
                        gameMap.placeTile(interactX, interactY, 1, Definitions.TABLE);
                    }
                    else
                        gameMap.interact(interactX, interactY, 1);
                    interact = false;
                    tileStrength = 0;
                }
                else
                {
                    Player.move(deltaTime);
                    tileStrength = 0;
                }
                turn = false;
            }
            Player.update(deltaTime);
            zombies.update(deltaTime);

            if(Player.getHealth() <= 0)
                Tilex.print("GAME OVER", 20, 14, UI2, Tilex.WHITE);
            else if(zombies.numZombies == 0)
                Tilex.print("YOU WIN", 21, 14, UI2, Tilex.WHITE);

            gameMap.draw((int)Player.getX(), (int)Player.getY());
            zombies.draw();
            int boxX = (int)Player.getX() + Player.getDX();
            int boxY = (int)Player.getY() + Player.getDY();
            boxX -= (int)Player.getX();
            boxY -= (int)Player.getY();
            boxX += 13;
            boxY += 8;
            Tilex.draw(337, boxX, boxY, SHADOW, Tilex.WHITE, 1);     
            if(hit)
            {
                Tilex.draw(311 + hitProgress , hitX, hitY, SHADOW, Tilex.WHITE, 1);
                if(hitProgress >= 8)
                    hit = false;
            }
            if(!gameMap.isWater((int)Player.getX(), (int)Player.getY(), 0))
            {
                Player.draw();
            }
            else
            {
                Player.drawWater();
            }
            if(clock == 0)
            {
                Tilex.print(12, 0, 1, UI2, Tilex.WHITE);
            }
            else if(clock < 13)
            {
                Tilex.print(clock, 0, 1, UI2, Tilex.WHITE);
            }
            else
                Tilex.print(clock - 12, 0, 1, UI2, Tilex.WHITE);
            if(clock < 12)
                Tilex.print("AM", 3, 1, UI2, Tilex.WHITE);
            else
                Tilex.print("PM", 3, 1, UI2, Tilex.WHITE);
            upButton.draw();
            upRightButton.draw();
            upLeftButton.draw();
            downButton.draw();
            downRightButton.draw();
            downLeftButton.draw();
            leftButton.draw();
            rightButton.draw();
            button1.draw();
            button2.draw();
            button3.draw();
            button4.draw();
            Tilex.flip();
            Tilex.clear();

            if(game)
                firstRun = false;
            if(Player.getHealth() <= 0 || zombies.numZombies == 0)
            {
                TXKey key = Tilex.getInput(0);
                while(key.type != TXKey.UP)
                {
                    key = Tilex.getInput(0);
                }
                game = false;
                title = true;
                firstRun = true;
            }
        }
        else if(load)
        {
            if(firstRun)
            {
                currentChar = 'A';
                firstRun = false;
                index = 0;
                if(Tilex.openReader("MicaRL.opt"))
                {
                    for(int a = 0; a < 5; a++)
                        files[a] = Tilex.readString();
                    Tilex.closeReader();
                }
                else
                {
                    load = false;
                    title = true;
                    firstRun = true;
                }
            }

            Tilex.clear();
            Tilex.print("Saved Games", 10, 0, UI1, Tilex.WHITE);
            for(int a = 0; a < 5; a++)
            {
                if(a == index)
                {
                    for(int b = -2; b < files[a].length() + 2; b++)
                        Tilex.draw(219, 10 + b, 6 + a, UI1, Tilex.LIGHT_GRAY, 1);
                }
                Tilex.print(files[a], 10, 6 + a, UI2, Tilex.WHITE);
            }
            menuUp.draw();
            menuDown.draw();
            goBack.draw();
            exit.draw();
            accept.draw();
            Tilex.flip();
            
            TXKey key = Tilex.getInput(0);
            
            if(menuUp.clicked(key))
            {
                index--;
                if(index < 0)
                    index = 0;
            }
            else if(menuDown.clicked(key))
            {
                index++;
                if(index > 4)
                    index = 4;
            }
            else if(goBack.clicked(key))
            {
                firstRun = true;
                load = false;
                title = true;
            }
            else if(exit.clicked(key))
            {
                Log.d("Delete", files[index] + ".dat");
                Tilex.deleteFile(files[index] + ".dat");
                files[index] = "Empty";
                Tilex.openWriter("MicaRL.opt");
                for(int a = 0; a < 5; a++)
                {
                    Tilex.write(files[a]);
                    Tilex.write("\n");
                }
                Tilex.closeWriter();
            }
            else if(accept.clicked(key))
            {
                buildName = new StringBuilder(files[index]);
                if(Tilex.openReader(files[index] + ".dat"))
                {
                    clock = Tilex.readInt();
                    Player.load();
                    zombies.load();
                    gameMap.load();
                    Tilex.closeReader();
                    load = false;
                    game = true;
                }
                else
                {
                    load = false;
                    title = true;
                }
            }
        }
        else if(newGame)
        {
            if(firstRun)
            {
                currentChar = 'A';
                buildName.setLength(0);
                firstRun = false;
            }

            Tilex.clear();
            Tilex.print("Name your world:", 0, 0, UI1, Tilex.WHITE);
            Tilex.print(buildName.toString(), 16, 0, UI1, Tilex.WHITE);
            Tilex.print(String.valueOf(currentChar), 16 + buildName.length(), 0, UI1, Tilex.WHITE);
            upButton.draw();
            downButton.draw();
            leftButton.draw();
            rightButton.draw();
            accept.draw();
            Tilex.flip();
            
            TXKey key = Tilex.getInput(0);
            
            if(upButton.clicked(key))
            {
                currentChar++;
                if(currentChar > 'Z')
                    currentChar = 'A';
            }
            else if(downButton.clicked(key))
            {
                currentChar--;
                if(currentChar < 'A')
                    currentChar = 'Z';
            }
            else if(rightButton.clicked(key))
            {
                buildName.append(currentChar);
                currentChar = 'A';
            }
            else if(leftButton.clicked(key))
            {
                if(buildName.length() > 0)
                {
                    currentChar = buildName.charAt(buildName.length() - 1);
                    buildName.deleteCharAt(buildName.length() - 1);
                }
                else
                {
                    firstRun = true;
                    newGame = false;
                    title = true;
                }
            }
            else if(accept.clicked(key))
            {
                buildName.append(currentChar);
                for(int a = 0; a < 5; a++)
                {
                    if(files[a] == "Empty" || a == 4 || buildName.toString() == files[a])
                    {
                        files[a] = buildName.toString();
                        break;
                    }
                }
                clock = 7;
                gameMap.generate();
                Player.generate();
                zombies.generate();
                Tilex.openWriter(buildName.toString() + ".dat");
                Tilex.write(clock);
                Tilex.write("\n");
                Player.save();
                zombies.save();
                gameMap.save();
                Tilex.closeWriter();
                Tilex.openWriter("MicaRL.opt");
                for(int a = 0; a < 5; a++)
                {
                    Tilex.write(files[a]);
                    Tilex.write("\n");
                }
                Tilex.closeWriter();
                newGame = false;
                game = true;
                firstRun = true;
            }
        }
        else if(title)
        {
            if(firstRun)
            {
                firstRun = false;
                index = 0;
            }
            //Drawing M
            for(int a = 0; a < 8; a++)
            {
                Tilex.draw(219, 10, 6 + a, UI2, Tilex.WHITE, 1);
                Tilex.draw(219, 14, 6 + a, UI2, Tilex.WHITE, 1);
                if(a < 3)
                {
                    Tilex.draw(219, 11, 7 + a, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 13, 7 + a, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 12, 9 + a, UI1, Tilex.WHITE, 1);
                }
            }
            //End drawing M

            //Drawing I
            for(int a = 0; a < 4; a++)
            {
                Tilex.draw(219, 16, 10 + a, UI1, Tilex.WHITE, 1);
            }
            //End drawing I

            //Drawing C
            for(int a = 0; a < 4; a++)
            {
                Tilex.draw(219, 18, 10 + a, UI1, Tilex.WHITE, 1);
                if(a < 2)
                {
                    Tilex.draw(219, 19 + a, 10, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 19 + a, 13, UI1, Tilex.WHITE, 1);
                }
            }
            //End drawing C

            //Drawing A
            for(int a = 0; a < 4; a++)
            {
                Tilex.draw(219, 22, 10 + a, UI1, Tilex.WHITE, 1);
                Tilex.draw(219, 25, 10 + a, UI1, Tilex.WHITE, 1);
                if(a < 2)
                {
                    Tilex.draw(219, 23 + a, 10, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 23 + a, 13, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 26, 12 + a, UI1, Tilex.WHITE, 1);
                }
            }
            //End drawing A

            //Drawing R
            for(int a = 0; a < 8; a++)
            {
                Tilex.draw(219, 31, 6 + a, UI1, Tilex.WHITE, 1);
                if(a < 4)
                    Tilex.draw(219, 34, 6 + a, UI1, Tilex.WHITE, 1);
                if(a < 3)
                    Tilex.draw(219, 34, 11 + a, UI1, Tilex.WHITE, 1);
                if(a < 2)
                {
                    Tilex.draw(219, 33, 10 + a, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 32 + a, 6, UI1, Tilex.WHITE, 1);
                    Tilex.draw(219, 32 + a, 9, UI1, Tilex.WHITE, 1);
                }
            }
            //End drawing R

            //Drawing L
            for(int a = 0; a < 8; a++)
            {
                Tilex.draw(219, 36, 6 + a, UI1, Tilex.WHITE, 1);
                if(a < 3)
                    Tilex.draw(219, 37 + a, 13, UI1, Tilex.WHITE, 1);
            }
            //End drawing L

            Tilex.print("Version 0.6", 10, 15, UI1, Tilex.GRAY);

            for(int a = 0; a < 10; a++)
                Tilex.draw(219, 10 + a, 20 + index, UI1, Tilex.GRAY, 1);
            Tilex.print("Continue", 10, 20, UI2, Tilex.WHITE);
            Tilex.print("New", 10, 21, UI2, Tilex.WHITE);

            menuUp.draw();
            menuDown.draw();
            exit.draw();
            accept.draw();

            Tilex.flip();
            Tilex.clear();

            TXKey key = Tilex.getInput(0);

            if(menuUp.clicked(key))
            {
                index--;
                if(index < 0)
                    index = 0;
            }
            else if(menuDown.clicked(key))
            {
                index++;
                if(index > 1)
                    index = 1;
            }
            else if(exit.clicked(key))
                Tilex.finish();
            else if(accept.clicked(key))
            {
                if(index == 0)
                {
                   load = true;
                   title = false;
                   firstRun = true;
                }
                else if(index == 1)
                {
                    newGame = true;
                    title = false;
                    firstRun = true;
                }
            }
        }
	}

    @Override
    public void pause()
    {
        if(game)
        {
            Tilex.openWriter(buildName.toString() + ".dat");
            Tilex.write(clock);
            Tilex.write("\n");
            Player.save();
            zombies.save();
            gameMap.save();
            Tilex.closeWriter();
        }
    }

    public void resume()
    {

    }

    private int pauseScreen()
    {
        Tilex.clear();
        gameMap.drawMiniMap((int)Player.getX(), (int)Player.getY());
        zombies.drawMiniMap();
        goBack.draw();
        saveExit.draw();
        Tilex.flip();
        while(true)
        {
            TXKey key = Tilex.getInput(0);
        
            if(goBack.clicked(key))
            {
                Tilex.clear();
                return 1;
            }
            else if(saveExit.clicked(key))
            {
                Tilex.clear();
                return 0;
            }
        }
    }
}