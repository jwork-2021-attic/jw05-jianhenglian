package com.anish.mycreatures;
import com.anish.utils.Direction;

import java.awt.*;
import java.util.Random;

/**
 * 接下来要做的是给坦克增加方向属性，然后坦克会根据方向，突出子弹
 * 接下来，方向给定后，，向那里移动应该是坦克的事，判断是否碰墙也是，改一下
 * 接下来，把发射子弹的方法教给坦克，同时规定发射数量
 * 现在我们遇见一个问题，坦克爆了之后还会动，同时tank爆了谁应该知道，
 * @author ljh
 * @create 2021-11-21 19:24
 */
public class Tank extends Creature implements Runnable
{
    private Direction direction;
    private boolean isDead;
    private int numOfBu;
    public Tank(Color color, World world)
    {
        super(color, (char) 5, world);
        direction = Direction.RIGHT;
        isDead = false;
        numOfBu = 10;
    }

    public int getDirectionX()
    {
        return direction.getX();
    }

    public void setDirection(Direction d)
    {
        this.direction = d;
    }

    public int getDirectionY()
    {
        return direction.getY();
    }

    public void comMoveTo()
    {
        if(isCountWall() || isDead) return;
        this.world.put(new Floor(world), this.getX(), this.getY());
        this.world.put(this, this.getX() + getDirectionX(), this.getY() + getDirectionY());
    }

    public boolean isCountWall()
    {
        if(world.get(this.getX() + this.getDirectionX(), this.getY() + this.getDirectionY()) instanceof Wall)
        {
            return true;
        }
        return false;
    }
    public void fireBullet()
    {
        if(isCountWall()) return;
        Bullet bullet = new Bullet(world, this.direction);
        this.world.put(bullet, this.getX() + this.getDirectionX(), this.getY() + this.getDirectionY());
        new Thread(bullet).start();
        numOfBu--;
    }

    public synchronized boolean isDead()
    {
        return isDead;
    }

    public void boom()
    {
        world.put(new Sign(world), this.getX(), this.getY());
        isDead = true;
    }

    public void changeDirection()
    {
        direction = Direction.valueOf(Direction.names[(int) (Math.random() * 4)]);
    }
    @Override
    /**
     * 我们现在先写极为简单的方法，让坦克能动，碰到墙就拐弯。
     */
    public void run()
    {
        while(!isDead)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if(this.isCountWall())
            {
                changeDirection();
            }
            else
            {
                isCountTarget();
                this.comMoveTo();
            }
        }
    }

    /**
     * 这个方法，我们会查看坦克目标移动的这行有没有目标，有就发三枚子弹
     * 我们简化一下，只查看十行
     */
    public void isCountTarget()
    {
        int i = 0;
        int xAdd = 0;
        int yAdd = 0;
        while(i < 10)
        {
            xAdd += this.getDirectionX();
            yAdd += this.getDirectionY();
            if(world.get(this.getX() + xAdd, this.getY() + yAdd) instanceof Wall) return;
            if(world.get(this.getX() + xAdd, this.getY() + yAdd) instanceof Dragon)
            {
                this.fireBullet();
            }
            i++;
        }
    }
}
