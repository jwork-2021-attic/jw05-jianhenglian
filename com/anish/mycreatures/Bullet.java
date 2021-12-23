package com.anish.mycreatures;

import com.anish.utils.Direction;

import java.awt.*;

/**
 * 接下来我们扩展一下子弹的功能，让它能够自由的移动
 * 现在子弹能朝着一个方向固定的移动，不过对于一颗子弹来说已经足够了
 * 首先给他添加一个功能，只能走十次
 * 再添加一个功能，撞墙后改变方向，改变方式呢，简单一点，原路返回就ok了。
 * 设定子弹的攻击功能，我们现在先简单一点，子弹在路上碰见了坦克，坦克就会消失，同时显示gameover
 * @author ljh
 * @create 2021-11-21 19:32
 */
public class Bullet extends Thing implements Runnable
{
    private int life = 20;
    private Direction direction;
    public Bullet(World world)
    {
        super(Color.magenta, (char)249, world);
        direction = Direction.RIGHT;
    }
    public Bullet(World world, Direction direction)
    {
        super(Color.magenta, (char)249, world);
        this.direction = direction;
    }

    @Override
    public void run()
    {
        while(life > 0)
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if(isCountCreature()) return;
            isCountWall();
            world.put(new Floor(world), this.getX(),this.getY());
            world.put(this, this.getX()+ direction.getX(),this.getY()+direction.getY());
            life--;
        }
        world.put(new Floor(world), this.getX(), this.getY());
    }
    private void isCountWall()
    {
        if(world.get(this.getX() + direction.getX(), this.getY() + direction.getY()) instanceof Wall)
        {
            switch (direction.name())
            {
                case "LEFT":
                    direction = Direction.RIGHT;
                    break;
                case "RIGHT":
                    direction = Direction.LEFT;
                    break;
                case "UP":
                    direction = Direction.DOWN;
                    break;
                case "DOWN":
                    direction = Direction.UP;
                    break;
            }
        }
    }
    private boolean isCountCreature()
    {
        if(world.get(this.getX() + direction.getX(), this.getY() + direction.getY()) instanceof Tank)
        {
            ((Tank)world.get(this.getX() + direction.getX(), this.getY() + direction.getY())).boom();
            world.put(new Floor(world), this.getX(), this.getY());
            return true;
        }
        if(world.get(this.getX() + direction.getX(), this.getY() + direction.getY()) instanceof Dragon)
        {
            ((Dragon)world.get(this.getX() + direction.getX(), this.getY() + direction.getY())).boom();
            world.put(new Floor(world), this.getX(), this.getY());
            return true;
        }
        return false;
    }
}
