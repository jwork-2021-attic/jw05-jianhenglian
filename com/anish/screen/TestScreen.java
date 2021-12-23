package com.anish.screen;

import asciiPanel.AsciiPanel;
import com.anish.mycreatures.Floor;
import com.anish.mycreatures.Tank;
import com.anish.mycreatures.World;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author ljh
 * @create 2021-12-16 8:58
 */
public class TestScreen implements Screen
{
    private World world;
    int i = 0;
    int j = 0;
    public TestScreen()
    {
        world = new World();
        for (int i = 0; i < World.HEIGHT; i++)
        {
            for (int j = 0; j < World.WIDTH; j++)
            {
                world.put(new Floor(world), i, j);
            }
        }
        Tank tank = new Tank(Color.red, world);
        world.put(tank, 0, 0);
        new Thread(tank).start();
        Tank tank1 = new Tank(Color.red, world);
        world.put(tank1, 10, 10);
        new Thread(tank1).start();
        Tank tank2 = new Tank(Color.red, world);
        world.put(tank2, 20, 20);
        new Thread(tank2).start();
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
        return this;
    }
    public void go()
    {
        this.world.put(new Tank(Color.magenta, world), ++i, ++j);
    }
}
