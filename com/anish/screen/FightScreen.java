package com.anish.fivescreens;

import asciiPanel.AsciiPanel;
import com.anish.mycreatures.Bullet;
import com.anish.mycreatures.Tank;
import com.anish.mycreatures.Floor;
import com.anish.mycreatures.Wall;
import com.anish.mycreatures.World;
import mazegenerator.MazeGenerator;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author ljh
 * @create 2021-11-11 8:58
 */
public class FightScreen implements FightScreen.Screen,Runnable
{
    private World world;
    MazeGenerator mazeGenerator;
    private String[][] mazeLocation;
    Tank tank;
    public FightScreen()
    {
        this.world = new World();
        mazeGenerator = new MazeGenerator(50);
        mazeGenerator.generateMaze();
        //接下来处理原始数据
        String rawMaze = mazeGenerator.getRawMaze();
        String[] lineMaze = rawMaze.split("\n");
        mazeLocation = new String[lineMaze.length][];
        for (int i = 0; i < lineMaze.length; i++)
        {
            mazeLocation[i] = lineMaze[i].substring(1, lineMaze[i].length() - 1).split(", ");
        }
        for (int i = 0; i < mazeLocation.length; i++)
        {
            for (int j = 0; j < mazeLocation[1].length; j++)
            {
                if (mazeLocation[i][j].equals("0"))
                {
                    world.put(new Wall(world), i, j);
                }
            }
        }
        tank = new Tank(Color.red, world);
        this.world.put(tank, 0, 0);
    }
    @Override
    public void displayOutput(AsciiPanel terminal)
    {
        for (int x = 0; x < World.WIDTH; x++)
        {
            for (int y = 0; y < World.HEIGHT; y++)
            {
                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key)
    {
        switch(key.getKeyCode())
        {
            case 32:
                Bullet bull = new Bullet(world);
                this.world.put(bull, tank.getX() + 1, tank.getY());
                bull.run();
                break;
            case 37:
                this.world.put(new Floor(world), tank.getX(), tank.getY());
                this.world.put(tank, tank.getX() - 1, tank.getY());
                break;
            case 38:
                this.world.put(new Floor(world), tank.getX(), tank.getY());
                this.world.put(tank, tank.getX(), tank.getY() - 1);
                break;
            case 39:
                this.world.put(new Floor(world), tank.getX(), tank.getY());
                this.world.put(tank, tank.getX() + 1, tank.getY());
                break;
            case 40:
                this.world.put(new Floor(world), tank.getX(), tank.getY());
                this.world.put(tank, tank.getX(), tank.getY() + 1);
                break;
            default:
                System.out.println(key.getKeyCode());
;
        }
        return this;
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static interface Screen {

        public void displayOutput(AsciiPanel terminal);

        public Screen respondToUserInput(KeyEvent key);
    }
}
