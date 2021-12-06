package com.anish.mycreatures;

import com.anish.mycreatures.Floor;
import com.anish.mycreatures.Thing;
import com.anish.mycreatures.World;

import java.awt.*;

/**
 * 接下来我们扩展一下子弹的功能，让它能够自由的移动
 * @author ljh
 * @create 2021-11-21 19:32
 */
public class Bullet extends Thing implements Runnable
{
    public Bullet(World world)
    {
        super(Color.magenta, (char)249, world);
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            world.put(new Floor(world), this.getX(),this.getY());
            world.put(this, this.getX()+1,this.getY()+1);
        }
    }
}
