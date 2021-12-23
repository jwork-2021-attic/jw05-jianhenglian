package com.anish.utils;

/**
 * @author ljh
 * @create 2021-12-20 15:03
 */
public enum Direction
{
    RIGHT(1, 0),
    LEFT(-1, 0),
    UP(0,-1),
    DOWN(0, 1);
    int x;
    int y;
    public static String[] names = {"RIGHT", "LEFT", "UP", "DOWN"};
    Direction(int i, int i1)
    {
        this.x = i;
        this.y = i1;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

}
