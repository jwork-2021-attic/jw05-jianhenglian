package com.anish.mycreatures;

import com.anish.utils.Direction;

import java.awt.*;

/**
 * @author ljh
 * @create 2021-12-22 8:56
 */
public class Dragon extends Creature
{
    private Direction direction;
    private boolean isDead;
    public Dragon(Color color, World world)
    {
        super(color, (char)21, world);
        direction = Direction.RIGHT;
        isDead = false;
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

    public void moveTo(Direction direction)
    {
        this.setDirection(direction);
        if(isCountWall()) return;
        this.world.put(new Floor(world), this.getX(), this.getY());
        this.world.put(this, this.getX() + getDirectionX(), this.getY() + getDirectionY());
    }

    public boolean isCountWall()
    {
        return world.get(this.getX() + this.getDirectionX(), this.getY() + this.getDirectionY()) instanceof Wall;
    }
    public void fireBullet()
    {
        if(isCountWall()) return;
        Bullet bullet = new Bullet(world, this.direction);
        this.world.put(bullet, this.getX() + this.getDirectionX(), this.getY() + this.getDirectionY());
        new Thread(bullet).start();
    }

    public boolean isDead()
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
}
