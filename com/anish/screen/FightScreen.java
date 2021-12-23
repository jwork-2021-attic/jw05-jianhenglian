package com.anish.screen;

import asciiPanel.AsciiPanel;
import com.anish.mycreatures.*;
import com.anish.utils.Direction;
import mazegenerator.Main;
import mazegenerator.MazeGenerator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 现在我们再改进一下，让屏幕右边有一个框，显示得了几分了，
 * @author ljh
 * @create 2021-11-11 8:58
 */
public class FightScreen implements Screen,Runnable
{
    private World world;
    MazeGenerator mazeGenerator;
    private String[][] mazeLocation;
    Tank[] tanks;
    Dragon longge;
    Random rand;
    public FightScreen()
    {
        rand = new Random();
        this.world = new World();
        makeMaze();

        longge = new Dragon(Color.magenta, world);
        this.world.put(longge, 1, 1);

        makeTanks();
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

    public void makeMaze()
    {
        mazeGenerator = new MazeGenerator(World.HEIGHT - 1);
        mazeGenerator.generateMaze();
        //接下来处理原始数据
        // 现在我们做一个小小的改动，把迷宫的间隔扩大一点，只用前(25, 25)个数据
        // 这个很难行的通，想了一个简单的替换方式，找一个小精灵来打通这些路,还行，差强人意
        //我们改一下，将边框放上墙
        String rawMaze = mazeGenerator.getRawMaze();
        String[] lineMaze = rawMaze.split("\n");
        mazeLocation = new String[lineMaze.length][];
        int lengthOfMaze = mazeLocation.length;
        Random rand = new Random();
        for (int i = 0; i < lineMaze.length; i++)
        {
            mazeLocation[i] = lineMaze[i].substring(1, lineMaze[i].length() - 1).split(", ");
        }
        for (int i = 1; i < lengthOfMaze ; i++)
        {
            for (int j = 1; j < mazeLocation[1].length ; j++)
            {
                if (mazeLocation[i][j].equals("0") )
                {
                    world.put(new Wall(world), i, j);
                }
            }
        }
        for (int i = 0; i < lengthOfMaze * lengthOfMaze / 2; i++)
        {
            world.put(new Floor(world),rand.nextInt(lengthOfMaze), rand.nextInt(lengthOfMaze));
        }

        for(int i = 0;i<World.HEIGHT;i++)
        {
            world.put(new Wall(world), 0, i);
        }
        for(int i = 0;i<World.HEIGHT;i++)
        {
            world.put(new Wall(world), World.HEIGHT - 1, i);
        }
        for(int i = 0;i<World.HEIGHT;i++)
        {
            world.put(new Wall(world), i, 0);
        }
        for(int i = 0;i<World.HEIGHT;i++)
        {
            world.put(new Wall(world), i, World.HEIGHT - 1);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key)
    {
        switch(key.getKeyCode())
        {
            case 32:
                 if(!longge.isDead()) longge.fireBullet();
                break;
            case 37:
                longGo("LEFT");
                break;
            case 38:
                longGo("UP");
                break;
            case 39:
                longGo("RIGHT");
                break;
            case 40:
                longGo("DOWN");
                break;
            default:
                System.out.println(key.getKeyCode());
        }
        return this;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            outputScore();
            if(longge.isDead() || isDeadAll())
            {
                int score = scores();
                deadAll();
                for (int i = 0; i < World.WIDTH; i++)
                {
                    for (int j = 0; j < World.HEIGHT; j++)
                    {
                        world.put(new Floor(world), i, j);
                    }
                }
                outputWord("GAME OVER!", World.HEIGHT / 2, World.HEIGHT / 2);
                outputWord("You earn " + score + " scores", World.HEIGHT / 2, World.HEIGHT / 2 + 1);

                return;
            }
        }
    }
    public void longGo(String direction)
    {
        if(!longge.isDead()) longge.moveTo(Direction.valueOf(direction));
    }
    public void outputWord(String targetWord, int startX, int startY)
    {
        startX -= 5;
        int i = 0;
        for (char c : targetWord.toCharArray())
        {
            world.put(new MyChar(c, world), startX + i++, startY);
        }
    }
    public boolean isDeadAll()
    {
        for(Tank tank:tanks)
        {
            if(!tank.isDead()) return false;
        }
        return true;
    }
    public void deadAll()
    {
        for(Tank tank:tanks)
        {
            tank.boom();
        }
    }
    public int scores()
    {
        int result = 0;
        for(Tank tank:tanks)
        {
            if(tank.isDead()) result++;
        }
        return result;
    }
    public void makeTanks()
    {
        tanks = new Tank[4];
        tanks[1] = new Tank(Color.yellow, world);
        this.world.put(tanks[1], rand.nextInt(48) + 1, rand.nextInt(48) + 1);
        tanks[2] = new Tank(Color.red, world);
        this.world.put(tanks[2], rand.nextInt(48) + 1, rand.nextInt(48) + 1);
        tanks[3] = new Tank(Color.blue, world);
        this.world.put(tanks[3], rand.nextInt(48) + 1, rand.nextInt(48) + 1);
        tanks[0] = new Tank(Color.green, world);
        this.world.put(tanks[0], rand.nextInt(48) + 1, rand.nextInt(48) + 1 );
        new Thread(tanks[1]).start();
        new Thread(tanks[0]).start();
        new Thread(tanks[2]).start();
        new Thread(tanks[3]).start();
    }
    public void outputScore()
    {
        outputWord("Scores: " + scores(), 55, 0);
    }
}
